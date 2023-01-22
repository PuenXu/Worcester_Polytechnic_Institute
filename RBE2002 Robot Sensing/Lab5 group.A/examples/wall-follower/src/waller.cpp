/*
 * Activity 02 -- Staying on track
 *
 * Line following with speed control. Pauses at an intersection and waits for a turn command.
 */ 

#include <Arduino.h>

#include <ir_codes.h>
#include <IRdecoder.h>

#include <Chassis.h>
#include <event_timer.h>

float baseSpeed = 10;

#define WALL_DISTANCE 20 // adjust as needed
#define STOP_DISTANCE 10 // adjust as needed

// Declare a chassis object with nominal dimensions
Chassis chassis;

EventTimer deadReckoningTimer;

// Setup the IR receiver/decoder object
const uint8_t IR_DETECTOR_PIN = 1;
IRDecoder decoder(IR_DETECTOR_PIN);

// Helper function for debugging
#define LED_PIN 13
void setLED(bool value)
{
  Serial.println("setLED()");
  digitalWrite(LED_PIN, value);
}

// Define the robot states
enum ROBOT_STATE {ROBOT_IDLE, ROBOT_WALL_FOLLOWING};
ROBOT_STATE robotState = ROBOT_IDLE;

// A helper function to stop the motors
void idle(void)
{
  Serial.println("idle()");
  setLED(LOW);

  //stop motors 
  chassis.setMotorEfforts(0, 0);

  //set state to idle
  robotState = ROBOT_IDLE;
}

// A helper function to turn a set angle
void turn(float ang)
{
  Serial.println("turn()");
  setLED(HIGH);

  // TODO: Start the robot turning and set a timer
}

void beginWallFollowing(void)
{
  Serial.println("beginWallFollowing()");
  setLED(HIGH);
  robotState = ROBOT_WALL_FOLLOWING;
}

// Used to check if the motions above are complete
void handleMotionComplete(void)
{
    if(robotState != ROBOT_IDLE) idle(); //avoid calling it unnecessarily
}

// Handles a key press on the IR remote
void handleKeyPress(int16_t keyPress)
{
  Serial.println("Key: " + String(keyPress));

  //ENTER_SAVE idles, regardless of state -- E-stop
  if(keyPress == ENTER_SAVE) idle(); 

  switch(robotState)
  {
    case ROBOT_IDLE:
      if(keyPress == PLAY_PAUSE) beginWallFollowing(); //drive(50, 10);
      break;
      
    case ROBOT_WALL_FOLLOWING:
      //TODO: Add ability to increase/decrease speed
      break;
 
     default:
      break;
  }
}

// TODO: Add wall following handler
void handleWallFollowing(float currDistance, float targetDistance, float speed = baseSpeed)
{
  if(robotState == ROBOT_WALL_FOLLOWING)
  {
    float leftSpeed = 0;
    float rightSpeed = 0;

#ifdef __WALL_DEBUG__
    Serial.print(currDistance);
    Serial.print('\t');
    Serial.print(leftSpeed);
    Serial.print('\t');
    Serial.print(rightSpeed);
    Serial.print('\n');
#endif

    chassis.setMotorTargetSpeeds(leftSpeed, rightSpeed);
  }
}

// TODO: Add obstacle checker
bool checkApproachEvent(float targetDistance)
{
  static bool isClose = false;
  bool retVal = false;

  // TODO: Add logic to detect wall approach as an event

  return retVal;
}

void handleApproach(void)
{
  Serial.println("Wall!");

  chassis.setMotorEfforts(0, 0);

  robotState = ROBOT_IDLE;
}

/*
 * This is the standard setup function that is called when the board is rebooted
 * It is used to initialize anything that needs to be done once.
 */
void setup() 
{
  // This will initialize the Serial at a baud rate of 115200 for prints
  // Be sure to set your Serial Monitor appropriately
  Serial.begin(115200);

  // initialize the chassis (which also initializes the motors)
  chassis.init();
  idle();

  // TODO: Set to what you found in the tuning part
  leftMotor.setPIDCoeffs(1, 0, 0);
  rightMotor.setPIDCoeffs(1, 0, 0);

  // initialize the IR decoder
  decoder.init();

  Serial.println("/setup()");
}

/*
 * The main loop for the program. The loop function is repeatedly called
 * after setup() is complete.
 */
void loop()
{
  // Must be called regularly!!!
  chassis.loop();

  // Everything below is in event checker/handler format

  // Check for a key press on the remote
  int16_t keyPress = decoder.getKeyCode();
  if(keyPress >= 0) handleKeyPress(keyPress);

  // Check for a new distance reading
  float wallDistance = 0; float targetDistance = WALL_DISTANCE;
  //if(....getDistance(wallDistance)) handleWallFollowing(wallDistance, targetDistance);

  // Check to see if we've approached a wall. TODO: implement the functions!
  if(checkApproachEvent(STOP_DISTANCE)) handleApproach();
}
