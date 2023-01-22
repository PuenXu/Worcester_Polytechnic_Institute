### Filtering (Section 3.1)

> What were your final selections for data rates and sensitivities? How did you decide on those values?

Data rate was decicded at 26Hz and 104Hz for the accleromter and gyroscope respectively.  We used the data sheets sensitivity because it is very close to being accurate.  We chose these rates because it faster than 13HZ and the romi can still read it accurately from 26HZ to 104HZ the romi is able to update in time with how long it takes to communicate the data.

> What happens when you set kappa = 1 in your filter?

The angle is completely based off of the accelerometer

> What happens when you set kappa = 0 in your filter?

The angle is completely based off of the gyro.

> What was your final choice for kappa? Why did you select that value?

kappa was set at 0.94 because we should use the observed angle as a majority of our angle and then taking into account the predicted angle from the gyro we can account for the observed angle sensor to be slightly off but it should only be a small portion off so 0.94 to 0.06 made the most sense when we were doing an experiment it looked the most accurate.

### Challenge (Section 4)

> Draw out the relevant parts of the state machine for the ramp detection.


> Include a link to your release here (since it will be in a different repo).
