#pragma once
/*
 * Datasheet: 
 */

#include <Arduino.h>
#include <Rangefinder.h>

class SharpIR : public Rangefinder
{
protected:
    int adcPin = -1;
    uint32_t lastReading = 0;

public:
    SharpIR(uint8_t adc) : adcPin(adc) {}

    virtual void init(void) {pinMode(adcPin, INPUT);}
    bool getDistance(float& distance);
};
