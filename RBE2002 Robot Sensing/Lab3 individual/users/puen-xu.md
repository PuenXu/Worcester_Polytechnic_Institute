Questions:

From a kinematic perspective, is your Romi holonomic? If not, what term is used to describe it?  

Non-holonomic

From the product page, how many encoder counts ("ticks") are there for each rotation of the motor.

1440

What is the gear ratio of the gear box in the motor assembly?

120:1

According to the product page, what is the diameter of the drive wheels?

7.0cm

From the product page, what is the wheel track of the Romi?

14.9cm

Draw out a state transition diagram that includes the functionality from last week, plus the functionality described below. 
![State Diagram](https://user-images.githubusercontent.com/114944956/200421658-61500e49-58d8-4f56-9f59-b2fe3768378c.jpeg)

Record the actual final pose and the estimate from your kinematic calculations, along with the values you used for cmPerTick and wheelTrack. How close was the estimated pose to the actual? For this trial and each trial below, record the kinematic paramaters and the actual and estimated poses in your md file.

<img width="1169" alt="Calibration" src="https://user-images.githubusercontent.com/114944956/201365530-e2a6bede-b0dc-4903-9714-577948672047.png">
Note: here I used different ticks per rotation value for the two motors.
After calibration, the estimated pose is within 3 mm & 1.72 deg of the actual pose.

Questions:

How fast and how long did you spin the wheels to drive the prescribed arc?

left wheel 11.2cm/s, right wheel 8.9cm/s, total time 7.853s

For the arc, record the estimated and actual final poses. Comment on how well the pose estimate matched the experiment. Identify potential sources of error.

<img width="576" alt="Draw Arc" src="https://user-images.githubusercontent.com/114944956/201367103-a6e9b6ab-22d9-4f54-a55a-348543a13db8.png">

The estimated pose is within 2 cm & 10 deg of the actual pose.
Potential sources of error:
1) The interval of update is not small enough so that error accumulates.
2) The dimensions of wheel track, and wheel diameter is not precisely enough measured due to measurement error.
3) The gear ratio is not exactly 120:1, like it says on the product webpage.

Compare the parameterized coefficients that you found in your experiments to the values from the product pages for the Romi or your measurements. How well do they agree? Is it possible to discern between errors in encoder counts per revolution and errors in wheel diameter?

The wheel diamter and wheel track measure is roughly the same to the values from the product page. However, the left encoder count is 1449, and the right encoder count is 1420, determined through some experiments. Therefore, we cannot discern these errors, and we also need to take gear ratio into account.

Which FK method did you implement and why? What are the advantages and disadvantages of the method that you chose?

I chose to use the instantaneous center method because it gives better estimation for curves than using the other two linear approximation methods. The advantage is it already accounts for several speacial cases, such as driving straight is when dl = dr, turning in place is dl = -dr, so we just need a general algorithm to cover all the scenarios. The disadvantage is that it needs more calculation as not only it needs to compute deltaDistance and deltaTheta, it also needs to calculate for the curvature radius.
