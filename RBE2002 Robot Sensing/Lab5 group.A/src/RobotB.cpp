/**
 * This code adds the ability to tune the gains and change the targets
 * */
//DONE!
#include <Chassis.h>
#include <Romi32U4Buttons.h>
#include <IRdecoder.h>
#include "params.h"
//#include "serial_comm.h"
#include <RemoteConstants.h>
#include <Rangefinder.h>
#include <HC-SR04.h>
#include <Sharp-IR.h>
#include <LSM6.h>
#include <IRDirectionFinder.h>

IRDirectionFinder irFinder;
int irRemotePin = 14;
IRDecoder decoder(irRemotePin);
Romi32U4ButtonA buttonA;
Chassis chassis;
float error = 0;
float prevError = 0;
int sharpPin = A0;
float wallKp = 0.8;
float wallKd = 0.1;
SharpIR stopSonar(sharpPin);

float baseSpeed = 8;  //base linefollow speed
float WALL_DISTANCE = 15; //distance from wall in CM
float STOP_DISTANCE = 10; //distance from wall to stop forward SONAR
enum stateChoices{ROBOT_WALL_FOLLOWING, ROBOT_IDLE, ROBOT_TURN, ROBOT_DRIVE_FOR, DRIVE_TO_POINT, ROBOT_CLIMB, ROBOT_IR, PUSH_BUTTON} robotState;

void idle(void)
{
  //Serial.println("idle()"); 
  chassis.setMotorEfforts(0, 0);

  //set state to idle
  robotState = ROBOT_IDLE;
}

void handleWallFollowing(float currDistance, float targetDistance, float speed = baseSpeed) 
{ 
  if(robotState == ROBOT_WALL_FOLLOWING) 
  { 
    error = currDistance - targetDistance;
  
    float leftSpeed = baseSpeed - (wallKp*error + wallKd*(error - prevError)); 
    float rightSpeed = baseSpeed + (wallKp*error + wallKd*(error - prevError));  //currError - prevError * Kd

// #ifdef __WALL_DEBUG__ 
//     Serial.print(currDistance); 
//     Serial.print('\t'); 
//     Serial.print(leftSpeed); 
//     Serial.print('\t'); 
//     Serial.print(rightSpeed); 
//     Serial.print('\n'); 
//#endif 
    chassis.setMotorTargetSpeeds(leftSpeed, rightSpeed); 
    prevError = error;
  } 
}

bool first = true;
void handleApproach(){
  chassis.stop();
  first = true;
  robotState = ROBOT_TURN;
}

bool checkApproachEvent(float distance){
  stopSonar.getDistance(distance);
  stopSonar.getDistance(distance);
  //Serial.println(distance);
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

bool checkClimb(){
  float currAngle = chassis.filteredAngle * 180.0 / M_PI;
  if(currAngle < 3.0)
    return true;
  return false;
}

void handleClimb(){
  digitalWrite(30, HIGH);
  idle();
  return;
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
      if(keyPress == remoteSetup) robotState = ROBOT_WALL_FOLLOWING; //drive(50, 10);
      //if(keyPress == remoteUp){timer = 6000; speed = 10; robotState = ROBOT_DRIVE_FOR; initialCalc = true;} //straight line
      //if(keyPress == remote9){timer = 7850; robotState = ROBOT_DRIVE_FOR; initialCalc = true;}  //50cm arc of 90 degrees
      //if(keyPress == remoteLeft){timer = 12000; speed = 30; robotState = ROBOT_DRIVE_FOR; initialCalc = true;} //360 more like 404
      // if(keyPress == remote1){chassis.xTo = 30; chassis.yTo = 30; robotState = DRIVE_TO_POINT;}  //lab3
      // if(keyPress == remote2){chassis.xTo = 60; chassis.yTo = 0; robotState = DRIVE_TO_POINT;}   //lab3
      // if(keyPress == remote3){chassis.xTo = 30; chassis.yTo = -30; robotState = DRIVE_TO_POINT;} //lab3
      // if(keyPress == remote0){chassis.xTo = 0; chassis.yTo = 0; robotState = DRIVE_TO_POINT;}    //lab3
      //if(keyPress == remote6){robotState = ROBOT_CLIMB;}
      break;
      
    case ROBOT_WALL_FOLLOWING:
      if(keyPress == remoteVolPlus) baseSpeed++;
      if(keyPress == remoteVolMinus) baseSpeed--;
      break;
 
     default:
      break;
  }
}

void sendMessage(const String& topic, const String& message)
{
    Serial1.println(topic + String(':') + message);
}

float checkVal = 1023;
void checkIR(){
  irFinder.requestPosition();
  if(irFinder.available())
  {
    Point point = irFinder.ReadPoint(0);
    if(point.x != checkVal && point.y != checkVal)
      robotState = ROBOT_IR;      
  }
}

float prev;
float currentLoc;
void pushBut()
{
  if(currentLoc == prev)
    idle();
  else
    prev = currentLoc;
}

float IRerror;
float lineUp = 185;
float irKp = 0.1;
float amuSpike = -2000;
float irBase = 10;
bool initial = true;
long afterACC;
void handleIR(){
  //Serial.println("IR FOUND");
  if(initial)
  {
   afterACC = millis() + 2000;  //500ms more than current time.
   initial = false;
  }
  irFinder.requestPosition();
  if(irFinder.available())
   {
     Point point = irFinder.ReadPoint(0);
     if(point.y < 1023 && point.y != 0)
     {
     IRerror = (lineUp - point.y);
     if(IRerror > 10)
      IRerror = irKp * 10.0;
    if(IRerror < -10)
      IRerror = irKp * -10.0;
    // Serial.println(IRerror);
    baseSpeed = 15;
     chassis.setWheelTargetSpeeds(baseSpeed + IRerror, baseSpeed-IRerror);
     }
     if(point.y >= 1023)
      chassis.setWheelTargetSpeeds(baseSpeed, baseSpeed);
   }
  Serial.println(chassis.imu.a.x);
  if(afterACC < millis())
   if(chassis.imu.a.x < amuSpike)
    {
      initial = true;
      idle();
    }
}

void setup()
{
  Serial.begin(115200);
  Serial.begin(9600);
  delay(500);  //give it a moment to bring up the Serial

  Serial.println("setup()");

  Serial1.begin(9600);

  Serial.println("/setup()");  //setup wifi communication
  /*!
   *  @brief initailize the module.
   */
  irFinder.begin();
  Wire.begin();
  //while(!Serial) {}  //IF YOU UNCOMMENT THIS LINE, THEN YOU _MUST_ OPEN THE SERIAL MONITOR TO GET PAST IT!
  Serial.println("setup()");
  chassis.init();
  hc_sr04.init();
  decoder.init();
  Serial.println("/setup()");
  robotState = ROBOT_IDLE;
  timer = 0;
  chassis.globalTurn = false;
  pinMode(30,OUTPUT);
  delay(500);
}

bool robotInit = true;
unsigned long waitTime;
unsigned long currRobotTime;
bool robotIsClimbing;
float test;
float wallDistance = 0; float targetDistance = WALL_DISTANCE; 
void loop() 
{ 
  
  //Serial.println("test");
  //Must be called regularly!!! 
  chassis.loop();   
  chassis.updatePose();
  chassis.updatePitch();
  
  // Check for a key press on the remote 
  int16_t keyPress = decoder.getKeyCode(); 
  if(keyPress >= 0) handleKeyPress(keyPress);
  
  switch(robotState)
  {

    case ROBOT_TURN:
      {
      handleTurn();
      break;
      }
    
    case ROBOT_IR:
    {
      handleIR();
      break;
    }

    case PUSH_BUTTON:
    {
      chassis.setWheelTargetSpeeds(10, 10);
      pushBut();
      break;
    }

    case ROBOT_CLIMB:
    {
      if(robotInit){
        chassis.setWheelTargetSpeeds(10, 10);
        currRobotTime = millis();
        waitTime = 200.0;
        robotInit = false;
        Serial.print("test");
      }
      if(millis() > currRobotTime + waitTime){
        if (chassis.filteredAngle * 180 / M_PI > 8) {robotIsClimbing = true; digitalWrite(30, LOW);}
        if(checkClimb() && robotIsClimbing){handleClimb();}
      }
      break;
    }
    case ROBOT_WALL_FOLLOWING:
    {  
      float wallDistance = 0;
      float targetDistance = WALL_DISTANCE;
      if(hc_sr04.getDistance(wallDistance)) { if(wallDistance > 0) {handleWallFollowing(wallDistance, targetDistance);}}
      if(checkApproachEvent(STOP_DISTANCE)) {handleApproach();}
      checkIR();
      break;
    }

    default:
    {
      break;
    }

  }

  //Serial.println(robotState);

  // irFinder.requestPosition();
  // if(irFinder.available())
  // {
  //   Point point = irFinder.ReadPoint(0);
  //   Serial.println(point.y);
  // }
  
  // hc_sr04.getDistance(test);
  // if(test > 0)
  // Serial.println(test);
 
  
  // Everything below is in event checker/handler format 
 
  // Check for a new distance reading 
 //
  // Check to see if we've approached a wall. TODO: implement the functions! 
  
  //if(robotState == DRIVE_TO_POINT) {if(checkApproachEvent(STOP_DISTANCE)) handleApproach();} //lab 3
 
 // if(checkRomiTune()) handleTune();
  //if(chassis.checker) {if(robotState == DRIVE_TO_POINT) chassis.driveToPoint();}  //lab 3
  //if(chassis.reachPoint()){ if(robotState == DRIVE_TO_POINT) { idle(); Serial.println("idle");}}  //lab 3
  //if(robotState == DRIVE_TO_POINT){
 
  //}



}

