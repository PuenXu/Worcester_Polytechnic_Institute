#include <Arduino.h>
#include <Chassis.h>
#include <Romi32U4Motors.h>
#include <Math.h>
// We'll declare motors as global to make the ISRs happier, but we'll put them in Chassis.cpp
// to keep things organized

LeftMotor leftMotor;
RightMotor rightMotor;
Chassis::Chassis(void) {}

void Chassis::init(void)
{  
    noInterrupts(); //disable interupts while we mess with the Timer4 registers
  
    //sets up timer 4
    TCCR4A = 0x00; //disable some functionality -- no need to worry about this
    TCCR4B = 0x0B; //sets the prescaler -- look in the handout for values
    TCCR4C = 0x04; //toggles pin 6 at the timer frequency
    TCCR4D = 0x00; //normal mode

    /*
    * EDIT THE LINE BELOW WITH YOUR VALUE FOR TOP
    */

    OCR4C = 249;   //TOP goes in OCR4C 

    TIMSK4 = 0x04; //enable overflow interrupt

    interrupts(); //re-enable interrupts

    // init the motors
    Romi32U4Motor::init();

    //pinMode(6, OUTPUT); //COMMENT THIS OUT TO SHUT UP THE PIEZO!!!
}

void Chassis::loop(void)
{
    checker = false;
    if(readyToPID)
    {
        if(readyToPID > 1) Serial.println("Missed update in Chassis::loop()");
        
        update();
        updatePose();
        readyToPID = 0;
    }

}

void  Chassis::updatePose()
{
    Serial.print(millis());
  Serial.print("\t");
  Serial.print(chassis.xLoc);
  Serial.print("\t");
  Serial.print(chassis.yLoc);
  Serial.print("\t");
  Serial.println(chassis.theta);
    checker = true;
    float dr = rightMotor.speed * cmPerEncoderTick;
    float dl = leftMotor.speed * cmPerEncoderTick;
    float dTheta = (dr - dl) / wheel_track;
    float dO = (dr + dl) / 2.0;
    if(dr == dl)
    {xLoc += dO * cos(theta);
     yLoc += dO * sin(theta);
     theta = theta;
    }
    else{
        float R = (wheel_track / 2.0) * (dr + dl)/(dr-dl);
        xLoc += R * (sin(theta + dTheta) - sin(theta));
        yLoc += R * (cos(theta) - cos(theta + dTheta));
        theta += dTheta;
    }
    if(theta > 2.0 * M_PI)
    {theta -= 2.0 * M_PI;}
    if(theta < -2.0 * M_PI)
    {theta += 2.0 * M_PI;}
}

////////////////////////////////////////  updatePitch in lab 4
void Chassis::updatePitch(void){

    if(imu.getStatus() & 0x01)
    {
        imu.readAcc();
        imu.readGyro();

        long time = millis();
        Serial.print("time: ");
        Serial.print(time);
        Serial.print(' ');

        double angle = - atan2( - imu.a.x, imu.a.z) * 180 / 3.1415;

        Serial.print("angle: ");
        Serial.println(angle);
        Serial.print(' ');
    }
}
///////////////////////////////////////////

void Chassis::stop(void)
{
    leftMotor.setMotorEffort(0);
    rightMotor.setMotorEffort(0);
}

void Chassis::update(void)
{
    leftMotor.update();
    rightMotor.update();

    #ifdef __MOTOR_DEBUG__
        
        Serial.print('\n');
#endif
}

void Chassis::setMotorTargetSpeeds(float leftTicksPerInterval, float rightTicksPerInterval)
{
    leftMotor.setTargetSpeed(leftTicksPerInterval);
    rightMotor.setTargetSpeed(rightTicksPerInterval);
}

float interval = 0.0162; 
void Chassis::setWheelTargetSpeeds(float leftCMPerSec, float rightCMPerSec)
{
    leftMotor.setTargetSpeed(leftCMPerSec / cmPerEncoderTick * interval);
    rightMotor.setTargetSpeed(rightCMPerSec / cmPerEncoderTick * interval);
}

void Chassis::setTwist(float forwardSpeed, float turningSpeed)
{
    int16_t ticksPerIntervalFwd = (forwardSpeed * (16 / 1000.0)) / cmPerEncoderTick;
    int16_t ticksPerIntervalTurn = (robotRadius * 3.14 / 180.0) * 
                        (turningSpeed * (16 / 1000.0)) / cmPerEncoderTick;

    leftMotor.setTargetSpeed(ticksPerIntervalFwd - ticksPerIntervalTurn);
    rightMotor.setTargetSpeed(ticksPerIntervalFwd + ticksPerIntervalTurn);
}


float currTime;
bool turnR = true;
void Chassis::turn()
{
    float speed = 10;
    float turnTime = 1171;
    if(turnR){
    currTime = millis() + turnTime;
    turnR = false;
    }
    if(!turnR)
    setWheelTargetSpeeds(speed, -speed);

    if(millis() > currTime){
    stop();
        if(millis() > currTime + 500){
            turnR = true;
            globalTurn = true;
        }
    }
}

float xBuffer = 3;
float yBuffer = 3;

bool Chassis::reachPoint()
{ //do eucleadian
    if(sqrt(pow((xTo - xLoc), 2) + pow((yTo - yLoc), 2)) < 4){
        return true;
    }
    
    return false;
}

float KpDist = 0.8;
float KpTheta = 12;
void Chassis::driveToPoint()
{
   float thetaTarget = atan2((yTo - yLoc) , (xTo - xLoc));
   float sigmaTheta = thetaTarget - theta;
   float sigmaDist = sqrt(pow((xTo - xLoc), 2) + pow((yTo - yLoc), 2));
   if(sigmaTheta > M_PI)
    {sigmaTheta -= 2.0 * M_PI;}
   if(sigmaTheta < -M_PI)
    {sigmaTheta += 2.0 * M_PI;}
   if(sigmaDist > 10)
    {sigmaDist = 10;}

   float leftTarget = KpDist * sigmaDist - KpTheta * sigmaTheta;
   float rightTarget = KpDist * sigmaDist + KpTheta * sigmaTheta;
    setWheelTargetSpeeds(leftTarget, rightTarget);

}

/*
 * ISR for timing. On overflow, it takes a 'snapshot' of the encoder counts and raises a flag to let
 * the main program it is time to execute the PID calculations.
 */
ISR(TIMER4_OVF_vect)
{
  //Capture a "snapshot" of the encoder counts for later processing
  leftMotor.calcEncoderDelta();
  rightMotor.calcEncoderDelta();

  chassis.readyToPID++;
}
