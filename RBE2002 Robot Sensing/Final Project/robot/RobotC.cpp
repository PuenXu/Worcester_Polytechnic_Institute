
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


//IR reciever Setup
int irRemotePin = 14;
IRDecoder decoder(irRemotePin);

Chassis chassis;

//Wall following constants
float WALL_DISTANCE = 15; //distance from wall in CM
float baseSpeed = 8;  //base linefollow speed

//Robot States
enum stateChoices{ROBOT_IDLE, ENTER, SEND_CODE, ROBOT_WALL_FOLLOWING, ROBOT_TURN, ROBOT_DRIVE_FOR, ROBOT_IN_ROW, ROBOT_IN_CELL, ROBOT_DEAD_REACKONING} robotState;

/**
 * @brief stop the robot and set state to idle
*/
void idle(void)
{
  //Serial.println("idle()"); 
  chassis.setMotorEfforts(0, 0);

  //set state to idle
  robotState = ROBOT_IDLE;
}

float prevError = 0;  //keep track of previous error
/**
 * @brief to wall follow using proportional and derivative control
 * @param currDistance current distance to the side wall
 * @param targetDistance target distance to the side wall
 * @param speed speed of the robot
*/
void handleWallFollowing(float currDistance, float targetDistance, float speed = baseSpeed) 
{ 
  float error = 0;
  float wallKp = 0.8;  //wall following Kp
  float wallKd = 0.1;  //wall following Kd
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
  chassis.turn(true);  //true equals right turn, false left
  if(chassis.globalTurn){chassis.globalTurn = false;
    robotState = ROBOT_WALL_FOLLOWING;}
}

/**
 * @brief to handle key presses by the remote control
 * @param keyPress a integer that represents key press by the remote control
 */
void handleKeyPress(int keyPress){
  if(keyPress == remote8) //exception to the rule 
    { 
        idle();
        return; 
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
}

String topic;  //global topic var
String message; //global message var
/**
 * @brief Parse the message from MQTT sent via ESP32
 * 
 * @return true  new message recieved and parsed correctly
 * @return false  new message not recieve or not parsed correctly
 */
bool getMessage()
{
    String serialString = "";
    bool newReading = false;
    while(Serial1.available())
    {
        char c = Serial1.read();
        if(c != '\n')  //end line of message from ESP32
        {
          serialString += c;
          Serial.println(c);
        } 
        if(c == '\n')   //Parse message into topic and message via colon
        {

            int Colon = serialString.indexOf(':');
            topic = serialString.substring(0, Colon);
            message = serialString.substring(Colon+1);
            serialString = "";
            newReading = true;
        }
    }

  return newReading;
}

int tagID = -1;   //Declaring global ID var of april tag
int tagRot = -1;  //Declaring global tag Orientation var of april tag
String code;  //Global code message for garage door based on RobotB x,y,theta
/**
 * @brief called if getMessage is true meaning new message to parse, if one of the parameter we are looking for store in correct
 * variable
 */
void handleMessage(){
    if(topic.compareTo("TO") == 0)
    {  
      tagRot = message.toInt();
      //Serial.println(tagRot);  //testing purpose
    }
    else if(topic.compareTo("TI") == 0)
    {
      tagID = message.toInt();
      //Serial.println(tagID);  //testing purpose
    }
    else if(topic.compareTo("CE") == 0)
    {
      code = message;
      //Serial.println(code);  //testing purpose
    }
    else
      return;  //if not message we want do nothing with it
}



uint8_t firstCode;  //first digit to send to door
uint8_t secondCode; //second digit to send to door
uint8_t thirdCode;  //third digit to send to door
/**
 * @brief Takes the code String from parseMessage and store correct variables as binary numbers
 * 
 * @param code String of code from RobotB that is garage door password
 */
void parsePass(String code){  //example "252"
     firstCode = (uint8_t) (code[0] - 48); //2
     secondCode = (uint8_t) (code[1] - 48);  //5
     thirdCode = (uint8_t) (code[2] - 48);  //2
}

/**
 * @brief Tells whether to send 38KHz signal or not 
 * 
 * @param highorlow true means send 38kHz and false means stop sending
 */
void write38(bool highorlow)
{
  uint8_t bitHandler = 0b00001000;  //correct byte for bitwise operations
  if(highorlow)
    TCCR1A |= bitHandler;  //change to 1
  else
    TCCR1A &= ~bitHandler;  //change to 0 or dont change
}

/**
 * @brief Sends the correct 38kHz signal to the garage door 
 * 
 * @param lineSig binary representation of digit you want to send
 */
void handlePass(uint8_t lineSig)
{
  write38(true);  //initial start high
  delayMicroseconds(9000);
  write38(false);
  delayMicroseconds(4500); //initial start low
  for(int i = 0; i<16; i++)  //sends all zeroes and then all 1 for address
  {
    write38(true);
    delayMicroseconds(562);
    write38(false);
    if(i < 8) //1s for address
    delayMicroseconds(562);
    else
    delayMicroseconds(1688);
  }

  for(int y = 0; y < 16; y++)   //sends number in little edian format via bitwise operations
  {
    write38(true);
    delayMicroseconds(562);
    write38(false);
    if(y < 8)
    {
    if((lineSig >> y) & 0b00000001)  //01000100 & 00000001 -> 0 (false), 00100010 -> false, 00010001 & 00000001 -> 00000001 -> (true)
    {
      delayMicroseconds(1688);
    }
    else
      delayMicroseconds(562);
    }

    else  //sends number in little edian format via bitwise operations and inverts them for inverse of passcode
    {
    if((~lineSig >> (y-8)) & 0b00000001)  //01000100 & 00000001 -> 0 (false), 00100010 -> false, 00010001 & 00000001 -> 00000001 -> (true)
    {
      delayMicroseconds(1688);
    }
    else
      delayMicroseconds(562);
    }
  }
  write38(true);
  delayMicroseconds(562);
  write38(false);

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

/**
 * @brief waits for a tagID and tagRot to be recieve because escape door code will be sent before that so waiting on tagID and tagRot
 * 
 */
void start(){
  if(tagID != -1 && tagRot != -1)
  {
    Serial.println(code);
    parsePass(code);
   // Serial.println("started");
    robotState = ROBOT_WALL_FOLLOWING;
  }
}

/**
 * @brief returns if the robot is in the correct cell
 * 
 * @return true if robot is in correct cell given from tagID
 * @return false if robot is not in the correct cell given from tagID
 */
bool inRowCell(){
  
  cordCalc();

  return (xCord == tagID % 10);
}

/**
 * @brief turns robotState to ROBOT_IN_ROW once robot is in the correct row
 * 
 */
void handleInRowCell(){

  //Serial.println("in row");
  robotState = ROBOT_IN_ROW;
}

/**
 * @brief check if robot needs to move from current column if so, turns then deadReackons forward one cell
 * 
 */
void findColumCell(){
  int column = tagID / 10;
  if(column == 0)
    robotState = ROBOT_IN_CELL;
  else
  {
    chassis.turn(true);
    if(chassis.globalTurn){chassis.globalTurn = false;
    robotState =  ROBOT_DEAD_REACKONING;}
  }
}

bool drive = true;     //lets robot know that it still needs to drive
bool arrived = false;  //lets robot know it has deadreackoned correct
/**
 * @brief This deadReackons the robot forwarwd by 1 column if garage door is in column one otherwise 
 * wallFollows to send column 
 */
void handleDeadReackoning(){
  int column = tagID/10;
  if(column == 1)
  {
    if(!arrived){
      chassis.driveFor(41.0, drive, arrived);
    }
    if(arrived){
      chassis.setMotorEfforts(0,0);
      robotState = ROBOT_IN_CELL;
    }

  }
  else
  {
    float wallDistance = 0; float targetDistance = WALL_DISTANCE; 
    if(hc_sr04.getDistance(wallDistance)) { if(wallDistance > 0) {handleWallFollowing(wallDistance, targetDistance);}}
    cordCalc();
    if(yCord == 2)
    {
      robotState = ROBOT_IN_CELL;
    }
  }
}

/**
 * @brief Turns based on where robot is, if in column 0 hasn't turned right then so orienation is correct
 * if in column 1 it needs to shift every number by 1 to account for robot at 90degrees
 * 
 */
void turnToEscapeDoor()
{
  int stub = tagRot % 90;
  if(stub > 45)
    tagRot += (90 - stub);
  if(tagRot > 340)
    tagRot = 0;
  int turns = tagRot / 90;
  
  if(tagID / 10 != 0)
  {
    if(turns == 2)
    {
        chassis.turn(true);
      if(chassis.globalTurn){chassis.globalTurn = false;
        //Serial.println("sending");
        robotState = SEND_CODE;}
    }
    else if(turns == 0)
    {
        chassis.turn(false);
        if(chassis.globalTurn){chassis.globalTurn = false;
        robotState = SEND_CODE;}
      }
    else if (turns ==  3)
    {
      robotState = SEND_CODE;
    }
  }

  else
  {
    Serial.println(turns);
    if(turns == 3)
  {
      chassis.turn(true);
    if(chassis.globalTurn){chassis.globalTurn = false;
      //Serial.println("sending");
      robotState = SEND_CODE;}
  }
  else if(turns == 1)
  {
      chassis.turn(false);
    if(chassis.globalTurn){chassis.globalTurn = false;
      //Serial.println("sending");
      robotState = SEND_CODE;}
  }
  else if (turns ==  0)
  {
    robotState = SEND_CODE;
  }
  else
    idle();

  }

}

void setup()
{
  
  Serial.begin(115200);
  delay(500);  //give it a moment to bring up the Serial

  //Serial.println("setup()");

  Serial1.begin(115200);

  //Serial.println("/setup()");  //setup wifi communication
  /*!
   *  @brief initailize the module.
   */
  //while(!Serial) {}  //IF YOU UNCOMMENT THIS LINE, THEN YOU _MUST_ OPEN THE SERIAL MONITOR TO GET PAST IT!
  //Serial.println("setup()");
  chassis.init();
  hc_sr04.init();
  pinMode(11, OUTPUT);  //setting up IR emitter
  //Serial.println("/setup()");
  robotState = ROBOT_IDLE;  //default robotState
  chassis.globalTurn = false;  //global turn variable should be false
  sendMessage("Setup", "Setup");  //subscribing to all topics
  delay(500);
}


bool robotInit = true;
unsigned long waitTime;
unsigned long currRobotTime;
float test;
float wallDistance = 0;
bool initTim = true;
void loop() 
{ 
  
  //Serial.println("test");
  //Must be called regularly!!! 
  chassis.loop();   
  chassis.updatePitch();
  //sendMessage("hello", "hello");
  
  //Serial.println(robotState);
  // Check for a key press on the remote 
  int16_t keyPress = decoder.getKeyCode(); 
  if(keyPress >= 0) handleKeyPress(keyPress);
  
  switch(robotState)
  {

    case ROBOT_IDLE:  //waiting for password, tagID, and tagRot to be sent to start
    {
      //Serial.println(code + "\t" + tagID + "\t" + tagRot);
      if(getMessage()) handleMessage();  //get readings
      start();  //will start if readings have been recieved
      break;
    }

    case SEND_CODE:  //sending password
    {
      handlePass(99);
      delay(125);
      handlePass(firstCode);
      delay(125);
      handlePass(secondCode);
      delay(125);
      handlePass(thirdCode);
      delay(125);
      handlePass(99);
      delay(125);
      handlePass(firstCode);
      delay(125);
      handlePass(secondCode);
      delay(125);
      handlePass(thirdCode);
      delay(125);
      robotState = ENTER;
      break;
    }

    case ROBOT_TURN:  //turning 90 
      {
      handleTurn();
      break;
      }
    
    
     case ENTER:  //entering garage door
    {

      uint32_t endTime;
      uint32_t startTime;
      if(initTim){
        endTime = millis() + 3750;
        startTime = millis() + 500;
        
        initTim =  false;
      }
      
      if(millis() >= startTime)
      chassis.setWheelTargetSpeeds(15,15);
      //delay for 5000 ms
      if(millis() >= endTime){
        chassis.setMotorEfforts(0,0);
      }

      break;
    }

    case ROBOT_IN_CELL:  //turning to garage door if in cell
    {
      turnToEscapeDoor();
      break;
    }

    case ROBOT_DEAD_REACKONING:  //dead Reackoning to garage door cell before
    {
      handleDeadReackoning();
      break;
    }

    case ROBOT_IN_ROW:  //robot getting to correct row then find column
    {
      findColumCell();
      break;
    }

    case ROBOT_WALL_FOLLOWING:  //wall following to correct row
    {  
      //Serial.println("wall following");
      float wallDistance = 0; 
      float targetDistance = WALL_DISTANCE; 
      //chassis.setWheelTargetSpeeds(10,10);
      //if(hc_sr04.getDistance(wallDistance)){Serial.println(wallDistance);}
      if(hc_sr04.getDistance(wallDistance))      
      { if(wallDistance > 0) 
        {handleWallFollowing(wallDistance, targetDistance);
        //Serial.println("Wall follow");
        }
      }
      if(inRowCell()){handleInRowCell();}
      break;
    }

    default:  //default state should be idle
    {
      idle();
      break;
    }

  }




}
