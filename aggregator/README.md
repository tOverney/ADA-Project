# How to run the aggregator

First you need to make sure that the data folder (in the root repository) exists and that it has at least the data for one day (one CSV).

You need scala 2.12 and sbt installed.

Then, from the aggregtor folder simply type `sbt run` and this will start a small server on `localhost:8080` which serves the app.

## Features
* The result is computed only once then cached in the `cache` subfolder plus in memory. When you restart the app it will first load all the computed results from the disk.
* The data for a day gets loaded and treated only the first time it fetches it, then it is cached in memory
* The file `utils.TrainTypes` contains all the information manually gathered from reisezueg.ch and wikipedia  
