Here is where all of your libraries go.

They should be put in separate directories, roughly matching their functionality, e.g.

- LSM6
- IR-Position-Finder
- OpenMV-Camera
- MQTT
- Romi-32u4-Chassis
- etc.

Each library folder *must* have its own `src` directory to hold the `.h/.cpp` files.
