#ifndef __PARAMS_H
#define __PARAMS_H

//PID gains -- you can edit the constructors later, after you tune the motors
float Kp = 8;
float Ki = 2;
float Kd = 0;

/*
 * target wheel speeds; these are in encoder ticks per PID loop!
 * 
 * Even though ticks/loop are always reported in integer number of ticks
 * we use a float here so that the target can be fractional. In practice, 
 * the fraction will cause the error to be sometimes a fraction positive
 * and sometimes a fraction negative, but they'll all wash out in the end.
 */

float targetLeft = 10;
float targetRight = 10;

#endif
