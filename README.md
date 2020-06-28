![banner](doc/banner.png)
# CE152-4
## Why?
This repo is me learning Java. I also had to follow an assignment spec, that is why some things seem a bit forced. The main package is the assignment package, which is the final parctical test of the year. Currently the only one visible, because of the cleanup.


## What?
The task is to create a map of world given .xzy file full of coordinates and their corresponding altitudes. The whole thing is basically a color coded scatter plot. The point of this exercise is to be able to see the possible effects of global warming, with a bit of salt I might add, and to raise awareness amongst the students. Which I have accomplished, sort of. This primitive models definitely shows you which areas would be hit first. 


## How?
Uncompress the data in the data directory and make sure that the fileName path in [Main.java](./src/assignment/Main.java) points to that data. That is all!

Go ahead and give it a try, if you were Noah, what place would be your best bet? 

One is able to select a certain point via mouse click, view its true altitude with respect to the sea level in the top bar. You can also see how much of the land disapears and reapears by affecting the sea level with your mouse wheel.

## BONUST TIME
For the fun of it, I have also added panning and zooming, go ahead and try to find your hometown!

![World map preview](doc/EarthAltitudeMap.png)

![World map preview](doc/EarthAltitudeMap2.png)
