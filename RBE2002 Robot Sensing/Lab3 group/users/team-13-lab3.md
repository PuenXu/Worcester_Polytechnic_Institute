## In your md file, describe how well your Romi was able to drive to a point.  What happens if the "close enough" zone is too small.

Our robot was able to drive to each point very well.  It was able to get within 3cm every time.  If the close enough zone is too small the robot will never reach it because the epsilonDistance is too small to make the robot move forward causing your robot to stall. It could also oscilate around the point because your x variable is there but your y variable isn't and when it tries move to make both variables in the zone it can never feasibly reach both.  Causing the oscilation.

## State Diagram
Its the IMG_7422.png
![state diagram2](https://user-images.githubusercontent.com/114944956/201493197-8c1d31ab-9248-4214-9bb9-3466630f7a03.jpeg)
