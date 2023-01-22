#pragma once



class Kinematics
{
public:
    //parameters -- these will need to be updated after you do experiments in lab 3
    float wheel_track = 14.85; //cm
    float wheel_diam = 6.947; //cm
    float ticks_per_rotation = 1390; // from the datasheet
    float cmPerEncoderTick = 3.1416 * wheel_diam / ticks_per_rotation;
    float robotRadius = wheel_track / 2.0;



};

//extern Kinematics kinematics;