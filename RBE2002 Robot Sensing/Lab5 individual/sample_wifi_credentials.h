#include <Arduino.h>

#ifdef __HOME_NETWORK__
// Update these with values suitable for your network.
const char* ssid = ...;
const char* password = ...;

#elif defined __RBE_NETWORK__
//RBE
const char* ssid = "RBE";
const char* password = "elm69wisest16poisoned";

#endif

// robomqtt credientials
const char* mqtt_server = "robomqtt.cs.wpi.edu";
#define mqtt_port 1883
#define MQTT_USER <teamNN>
#define MQTT_PASSWORD <password>
