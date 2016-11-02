# Switzerland and Trains
## Abstract
Switzerland possess one of the densest train networks in the world.
More than 15%[^1] of people working in Switerland comute by train; a proportion that goes over 80% for people comuting between the cities biggest cities.

We decided to dive down on the train users habits and see how our train network is effectively used. We will do this analysis with two different focuses. First we'll look at the train occupancy through time and space. Secondly we'll investigate the relationship between the number of trains arriving at a station and the size of the cities hosting the station.
## Data description
The main data set we'll use is the one provided direcly by SBB (swiss railway company covering the whole country) and we'll complement it with either Wikipedia data or Swiss Government data (or even both) to get informations about the cities related to the train stations.
So for the first part we'll use data about the train occupancy and coordinates of the relevant cities.
For the second part we'll need data about the train timetable to see how many connections there are, we'll also need as much informations about the cites in order to establish a feature list for our ml pipeline in order to try and predict how many connections a city will get.
## Feasability and risks
### Feasability
We took a glance at the SBB data set and it seems that the data is well organized and won't require much sanitization. We do not know exactly how/what we can fetch regarding the train use. Plus it seems that we can only gather the prediction of how full will the train be. (as in the mobile app and on their website) and not the actual occupancy.
The Wikipedia Dataset is also clean should allow us to retrieve swiss cities information (population, coordinates, etc.) quite easily. Population information could also be extracted from the data given directly from the swiss government and Coordinates from the Google Maps API. (So we can have a lot of different sources to get our informations related to the cities)

### Risks
We want to make a dynamic yet readable visualisation of the train occupancy depending on the time of the week (allowing you to travel throught time). But none of us as any experience in doing such a custom tailored visualisation.  
We also do not know which ml pipeline and data features to use to predict train/hour vs city size. There are a lot of factor to account for and we fear that, depending on what we include or not, our results won't be conclusive.

## Deliverables
Our main deliverable will be a single page web-app (stack not yet decided) displaying our analysis and visualisations in the nicest possible way.
We will also provide you with all our source code (throught this repository) including the app itself and all the preprocessing done to obtain the different datasets required to display our visualisations (including the ml pipeline).
## Timeplan
This is the part where we're the least sure about. We know the line we want to follow but there are too many unknown variables for us to be sure about how it will unfold.

That being said here is a plan on how we roughly see things going:

* __6 November:__ Final version of this project proposal
* __End. November:__ First functionnal version of the train occupancy visualisation
* __End. November:__ Data about train/hours in station vs city size handled and first stories about "outliers".
* __Mid. December:__ Visualisation about train/hours in station vs city size and Röstigraben done.
* __End. December:__ Analysis done with ML to predict the number of connections depending on the location and size of a city (real or not).
* __End. January:__ All of our results and visualisations nicely packed in a single page webapp.
* __End. January:__ Final version of the sildes for the mini-symposium

[^1]: [Swiss Infos: Les Suisses se déplacent toujours plus loin pour aller au travail](http://www.swissinfo.ch/fre/les-suisses-se-d%C3%A9placent-toujours-plus-loin-pour-aller-au-travail/41507140)