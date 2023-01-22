/**
 * This code adds the ability to tune the gains and change the targets
 * */
//DONE!
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
enum stateChoices{ROBOT_WALL_FOLLOWING, ROBOT_IDLE, ROBOT_TURN} robotState, nextRobotState;


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
bool first = true;
void handleApproach(){
  chassis.stop();
  first = true;
  robotState = ROBOT_TURN;
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

void handleKeyPress(int keyPress){
  if(keyPress == remotePlayPause) //exception to the rule 
    { 
        chassis.stop(); 
        robotState = ROBOT_TURN; 
        return; 
    }
  if(keyPress == remoteUp)
    {
      robotState = ROBOT_WALL_FOLLOWING;
      return;
    }
}

void setup()
{
  Serial.begin(115200);
  //while(!Serial) {}  //IF YOU UNCOMMENT THIS LINE, THEN YOU _MUST_ OPEN THE SERIAL MONITOR TO GET PAST IT!
  Serial.println("setup()");
  chassis.init();
  hc_sr04.init();
  decoder.init();
  Serial.println("/setup()");
  
  
}

void loop() 
{ 
  //Must be called regularly!!! 
  chassis.loop(); 
  // Everything below is in event checker/handler format 
  // Check for a key press on the remote 
  //int16_t keyPress = decoder.getKeyCode(); 
  //if(keyPress >= 0) handleKeyPress(keyPress); 
  // Check for a new distance reading 
  float wallDistance = 0; float targetDistance = WALL_DISTANCE; 

  if(hc_sr04.getDistance(wallDistance)) handleWallFollowing(wallDistance, targetDistance); 
  // Check to see if we've approached a wall. TODO: implement the functions! 
  
  if(checkApproachEvent(STOP_DISTANCE)) handleApproach();
  if(checkTurnEvent()) handleTurn();
    
  
}

