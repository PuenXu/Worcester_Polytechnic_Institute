#include <Sharp-IR.h>

const uint32_t SHARP_IR_INTERVAL = 100;
float bits = 1023;
float Vcc = 5;
bool SharpIR::getDistance(float& distance)
{
    bool newReading = false;
    uint32_t currTime = millis();
    if(currTime - lastReading >= SHARP_IR_INTERVAL)
    {
        lastReading = currTime;

        uint16_t adcResult = analogRead(adcPin);
        float Voltage = (float) adcResult * Vcc/ bits;
        distance = 15.849/(Voltage - 0.5071);

        newReading = true;
    }

    return newReading;
}
