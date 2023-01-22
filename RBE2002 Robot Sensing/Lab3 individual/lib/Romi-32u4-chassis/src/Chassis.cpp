#include <Arduino.h>
#include <Chassis.h>
#include <Romi32U4Motors.h>

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
    if(readyToPID)
    {
        if(readyToPID > 1) Serial.println("Missed update in Chassis::loop()");
        
        update();
        updatePose();
        readyToPID = 0;
    }
}

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

float wheel_track = 14.9; //cm
float wheel_diam = 7.0; //cm
float ticks_per_rotationLeft = 1350; // from the datasheet
float ticks_per_rotationRight = 1450; // from the datasheet
float cmPerEncoderTickLeft = 3.1416 * wheel_diam / ticks_per_rotationLeft;
float cmPerEncoderTickRight = 3.1416 * wheel_diam / ticks_per_rotationRight;

float X = 0;
float Y = 0;
float Theta = 0;

void Chassis::updatePose(void)
{

        long printTime = millis();
        // long leftEncoderCountTotal = leftMotor.getCount();
        // long rightEncoderCountTotal = rightMotor.getCount();
        long leftEncoderCount = leftMotor.getAndResetCount();
        long rightEncoderCount = rightMotor.getAndResetCount();
        float rightWheelDistance = rightEncoderCount * cmPerEncoderTickRight;
        float leftWheelDistance = leftEncoderCount * cmPerEncoderTickLeft;
        float deltaTheta = (rightWheelDistance - leftWheelDistance) / wheel_track;
        float deltaDistance = (rightWheelDistance + leftWheelDistance) / 2;

        if(leftWheelDistance == rightWheelDistance){
            float deltaX = deltaDistance * cos(Theta);
            float deltaY = deltaDistance * sin(Theta);
            X += deltaX;
            Y += deltaY;
        }

        else{
            float radius = wheel_track * (rightWheelDistance + leftWheelDistance) / 2.0 / (rightWheelDistance - leftWheelDistance);
            X += radius * (sin(Theta + deltaTheta) - sin(Theta));
            Y += radius * (cos(Theta) - cos(Theta + deltaTheta));
            Theta += deltaTheta;
        }

    #ifdef __MOTOR_DEBUG__
        
        Serial.print(printTime);
        Serial.print('\t');
        // Serial.print(leftEncoderCountTotal);
        // Serial.print('\t');
        // Serial.print(rightEncoderCountTotal);
        // Serial.print('\t');

        Serial.print(X);
        Serial.print('\t');
        Serial.print(Y);
        Serial.print('\t');
        Serial.print(Theta);
        Serial.print('\t');
        Serial.print(deltaDistance);
        Serial.print('\t');
        Serial.print('\t');
        Serial.print(Theta);
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


long currTime;
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

void Chassis::drive60cm(bool& drive60cmParam, bool& arrivedParam){

    long sampleTime = 6000;
    long printTime = 200;
    long lastPrintTime = 0;
    long currTime = millis();
    long endTime;

    if(drive60cmParam){
        // long sampleTime = 6000;
        // long printTime = 200;
        // long lastPrintTime = 0;
        // long currTime = millis();
        endTime = millis() + sampleTime;
        Serial.println(endTime);
        drive60cmParam = false;
    }

    if(!drive60cmParam){
        chassis.setWheelTargetSpeeds(10,10);
    }
  
    // if (currTime - lastPrintTime > printTime){
    //     Serial.println(currTime);
    //     // Serial.println(endTime);
    //     Serial.println(drive60cmParam);
    //     lastPrintTime = currTime;
    // }

    if (millis() > endTime){
        chassis.setMotorEfforts(0,0);
        arrivedParam = true;
        //drive60cmParam = false;
        Serial.println("arrived");
        //lastTime = currTime;
    }
    
}


void Chassis::turn360deg(bool& turn360degParam, bool& turnedParam){

    long sampleTime = 12000;
    long printTime = 200;
    long lastPrintTime = 0;
    long currTime = millis();
    long endTime;

    if(turn360degParam){
        // long sampleTime = 6000;
        // long printTime = 200;
        // long lastPrintTime = 0;
        // long currTime = millis();
        endTime = millis() + sampleTime;
        Serial.println(endTime);
        turn360degParam = false;
    }

    if(!turn360degParam){
        chassis.setWheelTargetSpeeds(-3.75,3.75);
    }
  
    // if (currTime - lastPrintTime > printTime){
    //     Serial.println(currTime);
    //     // Serial.println(endTime);
    //     Serial.println(turn360degParam);
    //     lastPrintTime = currTime;
    // }

    if (millis() > endTime){
        chassis.setMotorEfforts(0,0);
        turnedParam = true;
        //drive60cmParam = false;
        Serial.println("turned");
        //lastTime = currTime;
    }
    
}

void Chassis::drawArc(bool& drawArcParam, bool& drewParam){

    long sampleTime = 7853;
    long printTime = 200;
    long lastPrintTime = 0;
    long currTime = millis();
    long endTime;

    if(drawArcParam){
        // long sampleTime = 6000;
        // long printTime = 200;
        // long lastPrintTime = 0;
        // long currTime = millis();
        endTime = millis() + sampleTime;
        Serial.println(endTime);
        drawArcParam = false;
    }

    if(!drawArcParam){
        chassis.setWheelTargetSpeeds(11.2,8.9);
    }
  
    // if (currTime - lastPrintTime > printTime){
    //     Serial.println(currTime);
    //     // Serial.println(endTime);
    //     Serial.println(turn360degParam);
    //     lastPrintTime = currTime;
    // }

    if (millis() > endTime){
        chassis.setMotorEfforts(0,0);
        drewParam = true;
        //drive60cmParam = false;
        Serial.println("drew");
        //lastTime = currTime;
    }
    
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
