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
