//Code for the robot that climbs the ramp and read April tags

//library
#include <Chassis.h>
#include <Romi32U4Buttons.h>
#include <IRdecoder.h>
#include "params.h"
#include <RemoteConstants.h>
#include <Rangefinder.h>
#include <HC-SR04.h>
#include <Sharp-IR.h>
#include <LSM6.h>
#include <IRDirectionFinder.h>
#include <Arduino.h>
#include <Wire.h>
#include <openmv.h>

//pin assignment and peripheral setup
int irRemotePin = 14;
IRDecoder decoder(irRemotePin);
Romi32U4ButtonA buttonA;
Chassis chassis;
int sharpPin = A0;
SharpIR stopSonar(sharpPin);
OpenMV camera;

//constants
float error = 0;  //error for derivative control
float prevError = 0; //previous error for derivative control
float wallKp = 0.8; //wall following proportional control constant
float wallKd = 0.1; //wall following derivative control constant
float baseSpeed = 8;  //base linefollow speed
float WALL_DISTANCE = 15; //distance from wall in CM
float WALL_DISTANCE_CLIMB = 12; //distance from wall in CM, in the climbing situation
float STOP_DISTANCE = 10; //distance from wall to stop forward SONAR

//robot states
enum stateChoices{ROBOT_IDLE, ROBOT_WALL_FOLLOWING, ROBOT_TURN, ROBOT_CLIMB, ROBOT_CAMERA} robotState;


/**
 * @brief send message to the MQTT
 * @param topic the topic of the message
 * @param message the message itself
*/
void sendMessage(const String& topic, const String& message)
{
    Serial1.println(topic + String(':') + message);
}

/**
 * @brief stop the robot and set state to idle
*/
void idle(void)
{
  chassis.setMotorEfforts(0, 0);

  //set state to idle
  robotState = ROBOT_IDLE;
}

/**
 * @brief to wall follow using proportional and derivative control
 * @param currDistance current distance to the side wall
 * @param targetDistance target distance to the side wall
 * @param speed speed of the robot
*/
void handleWallFollowing(float currDistance, float targetDistance, float speed = baseSpeed)
{
  if(robotState == ROBOT_WALL_FOLLOWING || robotState == ROBOT_CLIMB) //check the state
  {
    error = currDistance - targetDistance; //error for proportional control

    float leftSpeed = baseSpeed + (wallKp*error + wallKd*(error - prevError));
    float rightSpeed = baseSpeed - (wallKp*error + wallKd*(error - prevError));  //currError - prevError * Kd

    chassis.setMotorTargetSpeeds(leftSpeed, rightSpeed); //set the speed of the robot
    prevError = error; //update the error
  }
}

/**
 * @brief to check whether a robot is within certain distance to the front wall
 * @param distance current distance to the front wall
 * @return true if the robot is within certain distance to the front wall, false otherwise
*/
bool checkApproachEvent(float distance){
  stopSonar.getDistance(distance);
  if(distance < STOP_DISTANCE && distance > 0)  //compare the distance is within stop distance
  {
    return true;
  }
  return false;
}

/**
 * @brief to handle the case when the robot approaches the front wall, which is to stop and switch to turn state
*/
void handleApproach(){
  chassis.stop();
  robotState = ROBOT_TURN;
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
  chassis.turn(false); //turn left (false to turn left, true to turn right)
  if(chassis.globalTurn){  //check if the turn is complete
    chassis.globalTurn = false;
    robotState = ROBOT_WALL_FOLLOWING;
  }
}

/**
 * @brief to check whether a robot is climbing the ramp
 * @return true if the robot is climbing the ramp, false otherwise
*/
bool checkClimb(){
  return chassis.filteredAngle * 180 / M_PI > 8;  //check if the angle of the robot is greater than 8 deg
}

/**
 * @brief to handle the case when the robot is on the ramp, which is to switch to climb state
 */
void handleClimb(){

  robotState = ROBOT_CLIMB;
  return;
}
/**
 * @brief to check whether a robot has arrived after climbing the ramp
 * @return true if the angle become small, which means robot has arrived, false otherwise
*/
bool checkArrival(){
  float currAngle = chassis.filteredAngle * 180.0 / M_PI;
  if(currAngle < 3.0) //check if the angle of the robot is smaller than 3 deg
    return true;
  return false;
}

/**
 * @brief to handle the case when the robot has arrived, which is to stop and switch to camera state
 */
void handleArrival(){
  chassis.setMotorEfforts(0,0);  //stop the motor
  robotState = ROBOT_CAMERA;
  return;
}

/**
 * @brief to handle key presses by the remote control
 * @param keyPress a integer that represents key press by the remote control
 */
void handleKeyPress(int keyPress){
  if(keyPress == remotePlayPause) //exception to the rule
    {
        idle();
        return;
    }
  switch(robotState)
  {
    case ROBOT_IDLE:  //to start the robot to wall follow
      if(keyPress == remote6){robotState = ROBOT_WALL_FOLLOWING;}
      break;

    case ROBOT_WALL_FOLLOWING: //control the speed of the robot
      if(keyPress == remoteVolPlus) baseSpeed++;
      if(keyPress == remoteVolMinus) baseSpeed--;
      break;

     default:
      break;
  }
}

/**
 * @brief find if there are april tags found by the camera, if so send the tag id and tag rotation to MQTT
 * @return an integer that indicates that tags found by the camera
 */
bool readAprilTag = true;  //check it we need to read the april tag
uint8_t FindAprilTags()
{
    uint8_t tagCount = camera.getTagCount();
    if(tagCount)
    {
      Serial.println(tagCount);
      AprilTagDatum tag;
      if(camera.readTag(tag) && readAprilTag)
      {
        sendMessage("TI", String(tag.id));  //send April tags info if there is one
        sendMessage("TO", String(tag.rot));
          
        readAprilTag = false;  //after it reads the april tag, stop reading it to prevent misintepreting tag orientation when the tag is flipped back
        
      }
    }

    return tagCount;
}

//set up the robot
void setup()
{
  Serial.begin(115200); //set up serial monitor
  Serial1.begin(115200); //set up TX1 and RX1
  delay(1000);  //give it a moment to bring up the Serial
  Serial.println("setup()");
  Serial.println("/setup()");  //setup wifi communication
  Wire.begin(); //set up I2C communication
  Wire.setClock(100000ul);
  chassis.init(); //initiate chassis
  hc_sr04.init(); //initiate rangefinder
  decoder.init(); //initiate remote control decoder
  robotState = ROBOT_IDLE; //swtich robot state to ide
  chassis.globalTurn = false;  //set up the boolean to check whether a turn is complete
}

//the function to loop through
void loop()
{
  //update speed, pose and pitch
  chassis.loop();

  // Check for a key press on the remote
  int16_t keyPress = decoder.getKeyCode();
  if(keyPress >= 0) handleKeyPress(keyPress);  //if there is a key press, handle it

  //state machine
  switch(robotState)
  {
    case ROBOT_WALL_FOLLOWING:
    {
      float wallDistance = 0;
      float targetDistance = WALL_DISTANCE;
      if(hc_sr04.getDistance(wallDistance)) {if(wallDistance > 0) {handleWallFollowing(wallDistance, targetDistance);}} //handle wall following
      if(checkApproachEvent(STOP_DISTANCE)) {handleApproach();} //check whether the robot is approaching front wall
      if(checkClimb()){handleClimb();} //check whether the robot is climbing

      break;
    }

    case ROBOT_TURN:
    {
      handleTurn();
      break;
    }

    case ROBOT_CLIMB:
    {
      float wallDistance = 0;
      float targetDistance = WALL_DISTANCE_CLIMB;
      if(hc_sr04.getDistance(wallDistance)) {if(wallDistance > 0) {handleWallFollowing(wallDistance, targetDistance);}}  //wall following while climbing
      if(checkArrival()){handleArrival();} //check whether the robot has arrived

      break;
    }

    case ROBOT_CAMERA:
    {
      uint32_t nextTime = 0;

      //report the april tags every 5 ms to not overwhelm the MQTT
      if(millis() >= nextTime){
        FindAprilTags();
        nextTime = millis() + 5;
      }
    }

    default:
    {
      break;
    }
  }
}
