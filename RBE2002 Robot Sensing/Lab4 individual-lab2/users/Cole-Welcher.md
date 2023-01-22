
## From a kinematic perspective, is your Romi holonomic?  If not, what term is used to describe it.
    The Romi is holonomic.  The controllable degree of freedom is equal to the total degree of freedom.
## From the product page, how many encoder counts ("ticks") are there for each roation of the motor.
    12 counts per revolution
## What is the gear ratio of the gear box in the motor assembly?
    120:1 reduction
## According to the product page, what is the diameter of the drive wheel?
    70mm
## From the product page, what is the wheel track of the Romi?
    5.86" or 149mm

## 
![Untitled](https://user-images.githubusercontent.com/68122848/200639370-a756ef9e-14b5-4ae4-a8c7-4fd21aaedcd3.png)


## How fast and how long did you spin the wheels to drive the prescribed arc?
    I drove the right wheel at 11.484 cm/s for 7.85s
    I drove the left wheel at 8.516cm/s for 7.85s
## For the arc, record the estimated and actual final poses.  Comment on how well the pose estimates matched the experiment.  Identify potential sources of error.
    Estimated: x: 47.75, y: 49.27, theta: 1.60
    Actual: x:47, y:48, theta: ~3
    Error: Starting position each time is not exactly the same so, physical measurment of where it is, is slightly off and one of the constants might be slightly off which causes exponential problems down the line.  It could also be the when your timing stuff like this with a romi, when you tell the wheel to go at 10cm/s it takes time to ramp the wheel up to that speed which means the physical calculations for the speed it should be going is going to be off.
## Compare the parameterized coefficents that you found in your experiments to the values from the product pages for the Romi or your measurements.  How well do they agree?  Is it possible to discen between errors in encoder counts per revolution and error in wheel diameter?
    Wheel Diameter:  Actual: 69.47mm        Product Page: 70mm
    Encoder Counts:  Actual: 1390           Product Page: 1440
    Wheel Track:     Actual: 14.85cm        Product Page: 14.9
    They are very close to each other but are slightly off which makes sense that the initial run was off because a small change in encoder counts is a large problem later on it is compounding.
    It is very hard to discern between encoder count per revolution and error in wheel diameter because both affect the same thing.
## Which FK method did you implement and why?  What are the advantages and disadvantages of the method that you chose?
    The ICC method is easier to code and is more accurate.  While the 2nd order is a bunch of small changes.  And is less math so its better for the microcontroller but worse for the position.
## THIS IS MY TESTING SPREADSHEET
WheelTrack	cmPerTick	Xdis	Ydis	Theta	ActualDistance

14.9	    1440	    57.28	0.53	0.03	58.5

14.7	    1440	    55.07	-0.69	0.02	58.5

14.85	    1440	    56.84	-0.62	-0.01	58.5

14.85	    1400	    59.66	0	    0.1	    59

14.85	    1405	    58.62	58.6	0.57	59.1

14.85	    1390	    58.32	0.6	    0.01	58.5
					
					
Turn	    Actual				
7.03 rads	400degrees	difference	3 degrees		
