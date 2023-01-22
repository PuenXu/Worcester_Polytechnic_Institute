// This example reads the raw values from the LSM6DS33
// accelerometer and gyro and prints those raw values to the
// Serial Monitor.

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
