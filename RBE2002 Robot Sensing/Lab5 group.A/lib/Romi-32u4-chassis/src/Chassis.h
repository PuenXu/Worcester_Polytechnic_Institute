#pragma once

#include <Arduino.h>
#include <Romi32U4Motors.h>
#include <kinematics.h>
#include <LSM6.h>
class Chassis
{

Kinematics kinematics;
protected:
    //parameters -- these will need to be updated after you do experiments in lab 3
    float wheel_track = kinematics.wheel_track; //cm
    float wheel_diam = kinematics.wheel_diam; //cm
    float ticks_per_rotation = kinematics.ticks_per_rotation; // from the datasheet
    float cmPerEncoderTick = 3.1416 * wheel_diam / ticks_per_rotation;
    float robotRadius = wheel_track / 2.0;


public:
    LSM6 imu;  //added IMU  
    float filteredAngle;
    float estimatedAccPitchAngle;
    float romiRadius = robotRadius;
    uint8_t readyToPID = 0;
    bool globalTurn = false;
    float cmPerEncoderTickP = cmPerEncoderTick;
    float xLoc = 0;
    float yLoc = 0;
    float theta = 0;
    float xTo;
    float yTo;
    bool checker;
    Chassis(void);

    void init(void);
    void updatePitch(void);
    void loop(void);
    void update(void);
    void stop(void);
    void turn();
    void setTwist(float x, float y);\
    void updatePose(void);
    void driveToPoint();
    bool reachPoint();

    void setMotorEfforts(int16_t left, int16_t right) 
        {leftMotor.setMotorEffort(left); rightMotor.setMotorEffort(right);}
    

    void setMotorTargetSpeeds(float leftTicksPerInterval, float rightTicksPerInterval);
    void setWheelTargetSpeeds(float leftCMPerInterval, float rightCMPerInterval);
};

extern Chassis chassis;