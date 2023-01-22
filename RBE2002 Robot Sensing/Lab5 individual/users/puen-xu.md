## Individual Activities

_All students will need to answer these questions independently, regardless of which system they implement._

> What is the I2C address of the IR Positioning Camera? Write your answer in hexadecimal. How many IR pixels can it report by default?

address: 0x58

128x96 pixel

> The library uses an address of 0x12 for the OpenMV camera. Describe what you would need to do to change the address to, say, 0x3D. That is, identify which line(s) of code you would change in which file(s).

line 22 in apriltags_i2c.py

change i2c_address = 0x12 to i2c_address = 0x3D

> Draw out the schematic for a circuit that will allow your Romi to _safely_ send signals to your ESP32.

![circuit schematic](https://user-images.githubusercontent.com/114944956/204886986-5c5cd737-e446-4142-a4b6-5af261305e2d.jpeg)
![Circuit Schematic 2](https://user-images.githubusercontent.com/114944956/204886999-78124692-ed82-406e-b90f-261b9b39d9de.jpeg)


> Note which device you implemented: IR Position Finder, OpenMV w/I2C, or OpenMV w/UART.

OpenMV w/UART

> Annotate the figure below to describe the field of view of the device that you implemented. Label the axes as if seen from "inside" the sensor. Label the coordinates of the corners and center pixel. 

top left 0,0

top right 160,0

botton left 0,120

bottom right 160,120

center 80,60

![Field of view](https://user-images.githubusercontent.com/114944956/204887017-ecbd90f4-c731-4679-90a3-04c54b4a3bb2.jpeg)
