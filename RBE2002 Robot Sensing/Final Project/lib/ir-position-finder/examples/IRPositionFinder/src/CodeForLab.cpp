#include <Wire.h>

uint8_t addy = 0x27;
uint8_t reg = 0x10;
float pressure;

bool ReadPressure(){
  //This all reading from sensor
  Wire.beginTransmission(addy);  //Calibrating communication line at address 0x27
  Wire.write(reg);  //Letting you know where in the address the data is
  Wire.endTransmission();  //close off calibration of communication
  Wire.requestFrom(addy, 2);  //requesting the two pieces of data I asked for
  uint8_t mostI = Wire.read();  //storing first data which is 0x10
  uint8_t leastI = Wire.read();   //storing second byte which is 0x11

  
  //Function to check for new data and if so get data

  if(mostI & 0x80 >> 7)  //checks if first bit is 1 and then true or false by right shifting 7 times
  {
    mostI &= 0x7F; //will turn the 1st bit into a 0 because 0x7F is 127 which is 0111 1111
    int16_t Value = (int16_t)(mostI << 8 | leastI);  //shifts most important byte first into the 16bit then ors the least sig. onto it;
    pressure = Value * 4;  //Resolution is 4Pa/LSB
    return true;
  }

else
  return false;

}


