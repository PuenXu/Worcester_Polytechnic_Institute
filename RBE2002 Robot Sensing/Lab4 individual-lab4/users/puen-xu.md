Note: updated chassis library in lab 2 repository, see here: https://github.com/RBE-2002/lab-2-wall-following-team-13/releases/tag/v4.0.1

### Introductory questions (Section 1.1)

> What IMU chip is used on the Romi?

LSM6DS33

> What is the I2C address of the chip _as implemented on the Romi Control Board_?

0b1101011

> What is the largest (nominal) acceleration you can measure with the IMU (in _g_)?

16g

### Basic functionality (Section 1.2)

> Indicate the positive direction for each axis of the accelerometer and each axis of the gyroscope using one of UP, DOWN, LEFT, RIGHT, FRONT, BACK, STRANGE, CHARM. The +z accelerometer is done for you. 

| Device | +x | +y | +z |
|---|:---:|:---:|:---:| 
| Accelerometer | FRONT | LEFT | UP | 
| Gyroscope | FRONT | LEFT | UP | 
|
> Are the coordinate systems for the accelerometer and gyroscope coincident?

Yes, they are coincident

> Fill in the table below for your predictions for the accelerometer readings.

| Orientation | a_x | a_y | a_z |
|---|:---:|:---:|:---:| 
| Top pointing up | **0g** | **0g** | **1g** | 
| Front pointing up | **1g** | **0g** | **0g** | 
| Left pointing up | **0g** | **1g** | **0g** | 
| Right pointing up | **0g** | **-1g** | **0g** | 
| Bottom pointing up | **0g** | **0g** | **-1g** | 
| Rear pointing up | **-1g** | **0g** | **0g** | 
|

### Calibration (Section 1.3)

> Record the default values for the following.

| Sensor | ODR | FS | Sensitivity (from datasheet) |
|:---:|:---:|:---:|:---:|
| Gyro | 13 Hz | 245 | 8.75 mdps/LSB |
| Acc | 13 Hz | 2 | 0.061 mg/LSB |
|

> Describe your experiment to measure the sensitivity of the accelerometer. Record your results. How do your measurements compare to the datasheet values above?

I collected 100 readings of the accelerometer on z-axis when the romi's top is facing up, now the average reading is around 16800, and the actual acceleration should be 1g. Then I place the romi, top pointing left, and collect 100 readings again. The average reading is around 150, but the actual acceleration should be 0g. Then I place the romi, top pointing down, and record the reading and the actual acceleration again. Finally, I plot the actual acceleration vs accelerometer readings and get a linear approximation of the data to get y = ax + b. Then the a is the sensitivity and b is the offset. At last, I do that the same for the x-axis and the y-axis to get the sensitivity and offset fot those.

<img width="918" alt="Sensitivity   Offset" src="https://user-images.githubusercontent.com/114944956/202866884-6ac552d2-0d06-4a76-b035-2cb2688052cc.png">

| Axis | Sensitivity | Offset|
|:---:|:---:|:---:|
| x | 0.0610 mg/LSB | -0.00187g |
| y | 0.0608 mg/LSB | -0.00245g |
| z | 0.0608 mg/LSB | -0.03360g |
|

> Record the standard deviation of the accelerometer readings when sitting still.

| Axis | Std. dev. (in _g_)
|:---:|:---:|
| x | 0.00117 | 
| y | 0.00148 | 
| z | 0.00230 | 
|

> If the Romi were spinning on level ground at 1 revolution per second and the gyroscope was set to a full-scale of 500 dps, what value would the IMU return on each gyro axis? Ignore noise and bias. Use the conversion factor given in Table 3 of the datasheet (linked in the code).

The sensitivity of the gyro when FS is 500 dps is 17.50 mdps/LSB. The angular velocity is 1 rev/s, which is 360 deg/s. 

Z-axis : 360 / 17.5 mdps/LSB -> 20571

X-axis : 0  //since the romi is not turning with repect to x and y-axis

Y-axis : 0

### Timing (Section 1.4)

> From your oscilloscope readings, roughly how long does one I2C transaction take when only the accelerometer is enabled?

Around 1.5 ms

![Oscilloscope](https://user-images.githubusercontent.com/114944956/202866903-6eeffeff-3541-4f32-9f64-80149f23e26a.jpeg)

> What is the maximum rate at which you can read the accelerometer? What limits the speed?

416 Hz. The transmission speed limits the speed. The IMU is using I2C protocal when comunicating with the microcontroller and I2C takes long time to read/write data since there is only one data line. When the rate is too high, the interval it reads the reading is smaller than transmission time so it cannot function anymore.

### Gyroscope (Section 1.5)

> Describe your experiment to measure the gyroscope bias and noise and your results.

To let the romi sit still, top facing straight up, and collect 100 gyroscope readings in x,y,z-axes. As the romi is not turning, the angular rate of the romi in all three axes should be 0; however, there is a non-zero reading from the gyroscope, and this is the gyroscope bias. So we just need to average the 100 readings to compute the bias. To measure the noise, we can just calculate the standard deviation of the 100 readings.

| Gyro | Z | Y | X |
|---|:---:|:---:|:---:| 
| Bias | -8.13 deg | -6.41 deg | 3.68 deg |
| Noise | 0.049 deg | 0.090 deg	| 0.088 deg |
|

### The `STATUS` Register (Section 2.1)

> Explain what the following line of code is doing: `if(imu.getStatus() & 0x01)`

To check whether the LSB of what imu.getStatus() returns is 1, which is that it is able to read the readings. This is because when imu.getStatus() & 0000 0001, no matter what the most significant 7 bits of imu.getStatus() is, when they & 0, they got 0. So only if the LSB of imu.getStatus() is 1, when it & 1, it gets 1 so that the if statement return true.

### Basic angle calculations (Section 2.3)
