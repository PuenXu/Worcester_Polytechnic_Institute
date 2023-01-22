/*!
 * @file simpleDemo.ino
 * @brief DFRobot's Positioning ir camera
 * @n [Get the module here](http://www.dfrobot.com/index.php?route=product/product&product_id=1088)
 * @n This example get the four lightest positions of the IR sources.
 * @n [Connection and Diagram](http://www.dfrobot.com/wiki/index.php/Positioning_ir_camera#Connection_Diagram)
 *
 * @copyright	[DFRobot](http://www.dfrobot.com), 2016
 * @copyright	GNU Lesser General Public License
 *
 * @author [Angelo](Angelo.qiao@dfrobot.com)
 * @version  V1.0
 * @date  2016-02-17
 */

#include <Arduino.h>
#include <Wire.h>

/*!
 * @brief Library for DFRobot's IR Position Camera
 * @author [Angelo](Angelo.qiao@dfrobot.com)
 */
#include <IRDirectionFinder.h>

IRDirectionFinder irFinder;

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

  //pinMode(15, INPUT);
}

void sendMessage(const String& topic, const String& message)
{
    Serial1.println(topic + String(':') + message);
}

void loop()
{
  // Serial.print(digitalRead(15));
  //Serial.print('\t');
  /*!
   *  @brief request the position
   */
  
  irFinder.requestPosition();
  
  /*!
   *  @brief If there is data available, print it. Otherwise show the error message.
   */
  if (irFinder.available()) {
  static uint32_t lastSend = 0;
    uint32_t currTime = millis();
    if(currTime - lastSend >= 1000) //send every five seconds
    {
      Point point = irFinder.ReadPoint(0);
      String Position = " X: " + String(point.x) + "Y: " + String(point.y);
      String topicPosition = "Position: ";
      //Serial.println(Position);
      sendMessage(topicPosition , Position);
   }
  }
  else{
    Serial.println("Device not available!");
  }

  
  
   //this is just a test program, so we'll allow a delay here
}
