#include <Romi32U4Buttons.h>
#include <Arduino.h>
#include <Wire.h> // I2C library
#include <LSM6.h>
#include <Chassis.h>

// to control printout; you'll need the Romi-32u4-utilities library
Romi32U4ButtonB buttonB;
Romi32U4ButtonC buttonC;

Chassis chassis;

// the IMU
//LSM6 imu;

void setup()
{
  Serial.begin(115200);
  
  Wire.begin();

  chassis.init();

  imu.setAccDataOutputRate(imu.ODR26);
  imu.setGyroDataOutputRate(imu.ODR52);

  delay(1000);

  if (!imu.init())
  {
    // Failed to detect the LSM6.
    while(1)
    {
      Serial.println(F("Failed to detect the LSM6. Just smash that reset button."));
      delay(100);
    }
  }
}

bool showAcc = false;
bool showGyro = false;

void loop()
{

  chassis.updatePitch();

  // while you can print both, it will be messy because the order is not guaranteed
  if(buttonB.getSingleDebouncedPress()) showAcc = !showAcc;
  if(buttonC.getSingleDebouncedPress()) showGyro = !showGyro;

  if(showAcc)
  {
    if(imu.getStatus() & 0x01)
    {
      imu.readAcc();

      long time = millis();
      Serial.print("time");
      Serial.println(time);
      Serial.print(' ');
      Serial.print("imu.a.x");
      Serial.print(imu.a.x);
      Serial.print(' ');
      Serial.print("imu.a.y");
      Serial.print(imu.a.y);
      Serial.print(' ');
      Serial.print("imu.a.z");
      Serial.print(imu.a.z);
      Serial.print('\n');
    }
  }

  if(showGyro)
  {
    if(imu.getStatus() & 0x02)
    {
      imu.readGyro();
  
      Serial.print("imu.g.x");
      Serial.print(imu.g.x);
      Serial.print(' ');
      Serial.print("imu.g.y");
      Serial.print(imu.g.y);
      Serial.print(' ');
      Serial.print("imu.g.z");
      Serial.print(imu.g.z);
      Serial.print('\n');
    }
  }
}

/*
#include <Chassis.h>
#include <Romi32U4Buttons.h>
#include <IRdecoder.h>
// #include "params.h"
//#include "serial_comm.h"
#include <RemoteConstants.h>
#include <Rangefinder.h>
#include <HC-SR04.h>
#include <Sharp-IR.h>
 
int irRemotePin = 14;
IRDecoder decoder(irRemotePin);
Romi32U4ButtonA buttonA;
Chassis chassis;
float error = 0;
float prevError = 0;
int sharpPin = 18;
float Kp = 0.8;
float Kd = 0.1;
SharpIR stopSonar(sharpPin);


float baseSpeed = 8;  //base linefollow speed
float WALL_DISTANCE = 15; //distance from wall in CM
float STOP_DISTANCE = 20; //distance from wall to stop forward SONAR
enum stateChoices{ROBOT_WALL_FOLLOWING, ROBOT_IDLE, ROBOT_TURN, ROBOT_DRIVE_FOR, DRIVE_TO_POINT} robotState, nextRobotState;


void handleWallFollowing(float currDistance, float targetDistance, float speed = baseSpeed) 
{ 
  if(robotState == ROBOT_WALL_FOLLOWING) 
  { 
    error = currDistance - targetDistance;
  
    float leftSpeed = baseSpeed - (Kp*error + Kd*(error - prevError)); 
    float rightSpeed = baseSpeed + (Kp*error + Kd*(error - prevError));  //currError - prevError * Kd

#ifdef __WALL_DEBUG__ 
    Serial.print(currDistance); 
    Serial.print('\t'); 
    Serial.print(leftSpeed); 
    Serial.print('\t'); 
    Serial.print(rightSpeed); 
    Serial.print('\n'); 
#endif 
    chassis.setMotorTargetSpeeds(leftSpeed, rightSpeed); 
    prevError = error;
  } 
}

void idle(void)
{
  //Serial.println("idle()"); 
  chassis.setMotorEfforts(0, 0);

  //set state to idle
  robotState = ROBOT_IDLE;
}

bool first = true;
void handleApproach(){
  idle();
  //chassis.stop();
  //first = true;
  //robotState = ROBOT_TURN;
}

bool checkApproachEvent(float distance){
  stopSonar.getDistance(distance);
  if(distance < STOP_DISTANCE && distance > 0)
  {
    return true;
  }
  stopSonar.getDistance(distance);
  return false;
}

bool checkTurnEvent(){
  if(robotState == ROBOT_TURN)
    return true;
  return false; 
}

void handleTurn()
{
  chassis.turn();
  if(chassis.globalTurn){chassis.globalTurn = false;
    robotState = ROBOT_WALL_FOLLOWING;}
}

bool checkRomiTune(){
  if(robotState == ROBOT_DRIVE_FOR)
    return true;
return false;
}

float timer;
float speed;
float wheelRadius = 6.8 / 2.0;
bool initialCalc = false;
float currTimer;
float degrees30 =  chassis.romiRadius * 2.0 * M_PI / 12.0;
void handleTune(){
  //Serial.print(degrees30);
  if(initialCalc)
  {currTimer = millis() + timer;
  initialCalc = false;}
  if(timer == 6000)
    chassis.setWheelTargetSpeeds(10, 10);
  if(timer == 12000)
    chassis.setWheelTargetSpeeds(-degrees30, degrees30);
  if(timer == 7850)
    chassis.setWheelTargetSpeeds(8.5, 11.5);
  if(millis() > currTimer){
    chassis.setMotorTargetSpeeds(0,0);
    robotState = ROBOT_IDLE;}
}

// bool checkRomiToPoint(){
//   if (robotState == DRIVE_TO_POINT)
//     return true;
//   return false;
// }

// void driveToPoint(){
//   if(chassis.checker)
//     chassis.driveToPoint();
//   if(chassis.reachPoint())
//     idle();
// }


void handleKeyPress(int keyPress){
  if(keyPress == remote8) //exception to the rule 
    { 
        idle();
        return; 
    }
  switch(robotState)
  {
    case ROBOT_IDLE:
      //if(keyPress == remoteSetup) robotState = ROBOT_WALL_FOLLOWING; //drive(50, 10);
      //if(keyPress == remoteUp){timer = 6000; speed = 10; robotState = ROBOT_DRIVE_FOR; initialCalc = true;} //straight line
      //if(keyPress == remote9){timer = 7850; robotState = ROBOT_DRIVE_FOR; initialCalc = true;}  //50cm arc of 90 degrees
      //if(keyPress == remoteLeft){timer = 12000; speed = 30; robotState = ROBOT_DRIVE_FOR; initialCalc = true;} //360 more like 404
      if(keyPress == remote1){chassis.xTo = 30; chassis.yTo = 30; robotState = DRIVE_TO_POINT;}
      if(keyPress == remote2){chassis.xTo = 60; chassis.yTo = 0; robotState = DRIVE_TO_POINT;}
      if(keyPress == remote3){chassis.xTo = 30; chassis.yTo = -30; robotState = DRIVE_TO_POINT;}
      if(keyPress == remote0){chassis.xTo = 0; chassis.yTo = 0; robotState = DRIVE_TO_POINT;}
      break;
      
    case ROBOT_WALL_FOLLOWING:
      if(keyPress == remoteVolPlus) baseSpeed++;
      if(keyPress == remoteVolMinus) baseSpeed--;
      break;
 
     default:
      break;
  }
}


void setup()
{
  Serial.begin(115200);
  //while(!Serial) {}  //IF YOU UNCOMMENT THIS LINE, THEN YOU _MUST_ OPEN THE SERIAL MONITOR TO GET PAST IT!
  Serial.println("setup()");
  chassis.init();
 // hc_sr04.init();
  decoder.init();
  Serial.println("/setup()");
  robotState = ROBOT_IDLE;
  timer = 0;
}

bool print = true;
void loop() 
{ 
  //Must be called regularly!!! 
  chassis.loop(); 
  // Everything below is in event checker/handler format 
  // Check for a key press on the remote 
  int16_t keyPress = decoder.getKeyCode(); 
  if(keyPress >= 0) handleKeyPress(keyPress); 
  // Check for a new distance reading 
    //float wallDistance = 0; float targetDistance = WALL_DISTANCE; 
    //if(hc_sr04.getDistance(wallDistance)) handleWallFollowing(wallDistance, targetDistance); 
  // Check to see if we've approached a wall. TODO: implement the functions! 
  
  if(robotState == DRIVE_TO_POINT) {if(checkApproachEvent(STOP_DISTANCE)) handleApproach();}
 // if(checkTurnEvent()) handleTurn();
 // if(checkRomiTune()) handleTune();
  if(chassis.checker) {if(robotState == DRIVE_TO_POINT) chassis.driveToPoint();}
  if(chassis.reachPoint()){ if(robotState == DRIVE_TO_POINT) { idle(); Serial.println("idle");}}
  //if(robotState == DRIVE_TO_POINT){
 
  //}
  
}
*/
