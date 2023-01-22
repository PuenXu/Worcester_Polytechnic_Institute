/**
 * This code is the IR Position Finder / Button Pusher robot's code
 * */

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
#include <String.h>
#include <HC-SR04.cpp>

//IR Position Finder Setup
IRDirectionFinder irFinder; 

//IR Reciever Setup
int irRemotePin = 14;           //IR Reciever (Remote) pin assignment
IRDecoder decoder(irRemotePin); //IR Reciever setup       
               
//Sharp IR Setup
int sharpPin = A0;              
SharpIR stopSonar(sharpPin);

//Chassis setup
Chassis chassis; 
float baseSpeed = 10;  //base speed
float WALL_DISTANCE = 15; //distance from wall in CM
float STOP_DISTANCE = 10; //distance from wall to stop forward SONAR

//Robot State
enum stateChoices{ROBOT_WALL_FOLLOWING, ROBOT_IDLE, ROBOT_TURN, ROBOT_IR, SEND_CODE} robotState;

/**
 * @brief This function turns the robot into a waiting state, waiting for an update of any sort (keypress)
 * 
 */
void idle(void)
{
  //Serial.println("idle()"); 
  chassis.setMotorEfforts(0, 0);

  //set state to idle
  robotState = ROBOT_IDLE;
}

float prevError = 0;  //keeps track of previous error
/**
 * @brief to wall follow using proportional and derivative control
 * @param currDistance current distance to the side wall
 * @param targetDistance target distance to the side wall
 * @param speed speed of the robot
*/
void handleWallFollowing(float currDistance, float targetDistance, float speed = baseSpeed) 
{ 
  float error = 0;
  float wallKp = 0.8;
  float wallKd = 0.1;
  if(robotState == ROBOT_WALL_FOLLOWING) 
  { 
    error = currDistance - targetDistance;
  
    float leftSpeed = baseSpeed - (wallKp*error + wallKd*(error - prevError)); 
    float rightSpeed = baseSpeed + (wallKp*error + wallKd*(error - prevError));  //currError - prevError * Kd

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

/**
 * @brief to handle the case when the robot approaches the front wall, which is to stop and switch to turn state
*/
bool first = true;
void handleApproach(){
  chassis.stop();
  first = true;
  robotState = ROBOT_TURN;
}

/**
 * @brief to check whether a robot is within certain distance to the front wall
 * @param distance current distance to the front wall
 * @return true if the robot is within certain distance to the front wall, false otherwise
*/
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

/**
 * @brief to check whether a robot is in the turn state
 * @return true if the robot is in the turn state, false otherwise
*/
bool checkTurnEvent(){
  if(robotState == ROBOT_TURN)
    return true;
  return false; 
}

/**
 * @brief to handle the case when the robot is in the turn case, which is to turn and to switch to wall following state
 */
void handleTurn()
{
  chassis.turn(true);
  if(chassis.globalTurn){chassis.globalTurn = false;
    robotState = ROBOT_WALL_FOLLOWING;}
}

/**
 * @brief to handle key presses by the remote control
 * @param keyPress a integer that represents key press by the remote control
 */
void handleKeyPress(int keyPress){
  //Serial.println(keyPress);
  if(keyPress == remote8) //exception to the rule 
    { 
        idle();
        return; 
    }
  switch(robotState)
  {
    case ROBOT_IDLE:
      if(keyPress == remoteSetup) robotState = ROBOT_WALL_FOLLOWING;
      if(keyPress == remote1){robotState = SEND_CODE;}
      break;
      
    case ROBOT_WALL_FOLLOWING:
      if(keyPress == remoteVolPlus) baseSpeed++;
      if(keyPress == remoteVolMinus) baseSpeed--;
      break;
 
     default:
      break;
  }
}

/**
 * @brief send message to the MQTT
 * @param topic the topic of the message
 * @param message the message itself
*/
void sendMessage(const String& topic, const String& message)
{
    Serial1.println(topic + String(':') + message);
    //Serial.println(topic + String(':') + message);
}

/**
 * @brief This function is using the IR position finder, and seeing if their is an IR beacon in its seeable range
 * if it is, switched the robot state to ROBOT_IR otherwise keep wall following
 */
void checkIR(){
  float checkVal = 1023;
  irFinder.requestPosition();
  if(irFinder.available())
  {
    Point point = irFinder.ReadPoint(0);
    if(point.x != checkVal && point.y != checkVal)
      robotState = ROBOT_IR;      
  }
}


int xCord;  //X coordinate of robot with respect to cells
int yCord;  //Y coordinate of robot with respect to cells
int Orot;   //Angle Coordinate of robot with respect to cells walls (0, 1, 2 ,3)
int cellLength = 16;  //length of each cell in inches 
int halfcell =  cellLength/2;
/**
 * @brief This function calculates the robot coordinates and and angle direction
 * base off of the updatePose function
 */
void cordCalc()
{
  xCord = (int((chassis.xLoc / 2.54) + halfcell) / cellLength) ;  //1/2*cellLength is to shift the robots coordinate to i,j = 0,0 because robot starts at 8,8
  yCord = (int(-(chassis.yLoc / 2.54) + halfcell) / cellLength);
  Orot = ((int(-180 / M_PI * (chassis.theta))));
  Serial.println(chassis.theta * 180 / M_PI);
  //Serial.println(Orot);
  if(Orot % 90 > 20)  //to give an accurate angle of 90, 180, 270, or 0
  {Orot += 45;}  //add 45 so if close to ceiling instead of floor
  Orot = Orot / 90;  //convert to 0, 1, 2, 3 for final password number
}

bool initial = true;  //function to do initial timer calculation
/**
 * @brief This function handles the robots IR position finder proportional control it also detects the imu spike which then stops the 
 * robot and sends the code / robots location which is the password
 * 
 */
void handleIR(){
  float IRerror;  //Error from center of robot to location based on IR position finders location of IR signal
  float lineUp = 405;  //Center of camera - centered on robot
  float irKp = 0.06;  //Proportional control Kp
  float amuSpike = -2000;  //amu spike amount when hitting button
  float distance;
unsigned long afterACC;
  if(initial)
  {
   afterACC = millis() + 200;  //500ms more than current time.
   initial = false;
  }
  irFinder.requestPosition();
  if(irFinder.available())    //proportional control when it sees the ir signal
   {
     Point point = irFinder.ReadPoint(0);
     if(point.y < 1023 && point.y != 0)
     {
     IRerror = (lineUp - point.y);
     if(IRerror > 10)
      IRerror = irKp * 10.0;
    if(IRerror < -10)
      IRerror = irKp * -10.0;
      baseSpeed = 15;  //increase basespeed
     chassis.setWheelTargetSpeeds(baseSpeed + IRerror, baseSpeed-IRerror);
     }
     if(point.y >= 1023)
      chassis.setWheelTargetSpeeds(baseSpeed, baseSpeed);
   }
  if(afterACC < millis())  //if imu spike is stop (not acceleration spike)
   if(chassis.imu.a.x < amuSpike  && checkApproachEvent(distance))
    {
      initial = true;
      cordCalc();  //calc xCord, yCord, and theta cell orientation
      String code = String(String(yCord) + String(xCord) + String(Orot));
      sendMessage("CE", code);  //Send Code to MQTT
      idle();
    }
}

void setup()
{
  Serial.begin(115200);
  
  delay(500);  //give it a moment to bring up the Serial

  Serial.println("setup()");

  Serial1.begin(115200);

  Serial.println("/setup()");  //setup wifi communication
  /*!
   *  @brief initailize the module.
   */
  irFinder.begin();  //beings the IR position finder
  Wire.begin();      //begins MQTT communication
  //while(!Serial) {}  //IF YOU UNCOMMENT THIS LINE, THEN YOU _MUST_ OPEN THE SERIAL MONITOR TO GET PAST IT!
  Serial.println("setup()");

  chassis.init();  //starts chassis
  hc_sr04.init();  //starts wall following sonar
  decoder.init();  //starts IR reciever
  
  Serial.println("/setup()");

  robotState = ROBOT_IDLE;  //sets robot default state
  chassis.globalTurn = false;  //turns global turn to false (this is for non blocking 90 degree turns)
  delay(500);
  sendMessage("Setup", "Setup");  //starts subscription of mqtt
}

//float test;  //for testing purposes
float wallDistance = 0; float targetDistance = WALL_DISTANCE; 
void loop() 
{ 
  //Serial.println("test");
  //Must be called regularly!!!  for pose!
  chassis.loop();   
  chassis.updatePitch();
  //Check for a key press on the remote 
  int16_t keyPress = decoder.getKeyCode(); 
  if(keyPress >= 0) handleKeyPress(keyPress);
  
  //code testing for all sensors to make sure nothing got unplugged
  //sendMessage("hello", "hello");
  //if(getMessage()) handleMessage();
  //Serial.println(robotState);
  // irFinder.requestPosition();
  // if(irFinder.available())
  // {
  //   Point point = irFinder.ReadPoint(0);
  //   Serial.println(point.y);
  // }
  // stopSonar.getDistance(test);
  // hc_sr04.getDistance(test);
  // if(test > 0)
  // Serial.println(test);
  //chassis.setWheelTargetSpeeds(10, 10);

  switch(robotState)  //state machine
  {

    case ROBOT_TURN:  //robot in turn state
      {
      handleTurn();
      break;
      }
    
    case ROBOT_IR:  //robot sees IR start proportional control
    {
      handleIR();
      //Serial.println(chassis.imu.a.x);
      break;
    }

    case ROBOT_WALL_FOLLOWING:  //robot is wall following do proportional and derivate control;
    {  
      float wallDistance = 0;
      float targetDistance = WALL_DISTANCE;
      if(hc_sr04.getDistance(wallDistance)) { if(wallDistance > 0) {handleWallFollowing(wallDistance, targetDistance);}}
      checkIR();
      if(robotState != ROBOT_WALL_FOLLOWING)
        break;
      if(checkApproachEvent(STOP_DISTANCE)) {handleApproach();}
      break;
    }
  
    default:
    {
      break;
    }

  }

}



