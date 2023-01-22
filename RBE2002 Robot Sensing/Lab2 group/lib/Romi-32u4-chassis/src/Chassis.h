#pragma once

#include <Arduino.h>
#include <Romi32U4Motors.h>

class Chassis
{
protected:
    //parameters -- these will need to be updated after you do experiments in lab 3
    float wheel_track = 14.9; //cm
    float wheel_diam = 7.0; //cm
    float ticks_per_rotation = 1440; // from the datasheet
    float cmPerEncoderTick = 3.1416 * wheel_diam / ticks_per_rotation;
    float robotRadius = wheel_track / 2.0;


public:
    uint8_t readyToPID = 0;
    bool globalTurn = false;
    float cmPerEncoderTickP = cmPerEncoderTick;
    Chassis(void);

    void init(void);
    void loop(void);
    void update(void);
    void stop(void);
    void turn();

    void setMotorEfforts(int16_t left, int16_t right) 
        {leftMotor.setMotorEffort(left); rightMotor.setMotorEffort(right);}
    

    void setMotorTargetSpeeds(float leftTicksPerInterval, float rightTicksPerInterval);
    void setWheelTargetSpeeds(float leftCMPerInterval, float rightCMPerInterval);
};

extern Chassis chassis;