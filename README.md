# Switzerland and Trains
## Abstract Switzerland possesses one of the densest train networks in the world.
More than 15% [^1] of people working in Switzerland commute by train; a proportion that goes over 80% for people commuting between the cities biggest cities.

We decided to dive down on the train users habits and see how our train network is effectively used. We will do this analysis with two different focuses. First we will look at the train occupancy through time and space. Secondly we will investigate the relationship between the number of trains arriving at a station and the size of the cities hosting the station.
## Data description
The main data set we will use is the one provided directly by SBB (Swiss railway company covering the whole country) [^2] and we will complement it with either Wikipedia data or Swiss Government data [^3] (or even both) to get information about the cities related to the train stations.
So for the first part we will use data about the train occupancy and coordinates of the relevant cities.
For the second part we will need data about the train timetable to see how many connections there are, we will also need as much information about the cities in order to establish a feature list for our ml pipeline to try to predict how many connections a city will get.
## Feasibility and risks
### Feasibility
We took a glance at the SBB data set and it seems that the data are well organized and will not require much sanitation. We do not know exactly how/what we can fetch regarding the train use. Plus it seems that we can only gather the prediction of how full will the train be. (as in the mobile app and on their website) and not the actual occupancy.
The Wikipedia Dataset is also clean should allow us to retrieve Swiss cities information (population, coordinates, etc.) quite easily. Population information could also be extracted from the data given directly from the Swiss government and Coordinates from the Google Maps API. (So we can have a lot of different sources to get our information related to the cities)

### Risks
We want to make a dynamic yet readable visualization of the train occupancy depending on the time of the week (allowing you to travel through time). But none of us as any experience in doing such a custom tailored visualization.  
We also do not know which ml pipeline and data features to use to predict train/hour vs. city. There is a lot of factor to account for and we fear that, depending on what we include or not, our results will not be conclusive.

## Deliverables
Our main deliverable will be a single page web-app (stack not yet decided) displaying our analysis and visualization in the nicest possible way.
We will also provide you with all our source code (through this repository) including the app itself and all the preprocessing done to obtain the different datasets required to display our visualization (including the ml pipeline).
## Timeplan
This is the part where we are the least sure about. We know the line we want to follow but there are too many unknown variables for us to be sure about how it will unfold.

That being said here is a plan on how we roughly see things going:

* __6 November:__ Final version of this project proposal
* __End. November:__ First functional version of the train occupancy visualization
* __End. November:__ Data about train/hours in station vs. city size handled and first stories about "outliers".
* __Mid. December:__ Visualization about train/hours in station vs. city size and Röstigraben done.
* __End. December:__ Analysis done with ML to predict the number of connections depending on the location and size of a city (real or not).
* __End. January:__ All our results and visualization nicely packed in a single page webapp.
* __End. January:__ Final version of the slides for the mini-symposium

[^1]: [Swiss Info: Les Suisses se déplacent toujours plus loin pour aller au travail](http://www.swissinfo.ch/fre/les-suisses-se-dÃ©placent-toujours-plus-loin-pour-aller-au-travail/41507140)
[^2]: [http://data.sbb.ch/](http://data.sbb.ch/page/einstieg/)
[^3]: [https://opendata.swiss/en/](https://opendata.swiss/en/)

## Followed Pipeline
### Data sources

* Use General Transit Feed Specification (GTFS) [data](http://gtfs.geops.ch/) provided by [geops.ch](geops.ch) parsed from the official swiss schedule (originally published in HAFAS format) as schedule dataset
	* Clearly defined [schema](https://developers.google.com/transit/gtfs/) 
	* Dataset already available
* Forecast occupancy shown in timetable queries up to 30 days in advance
	* API readily available provided by [opendata.ch](https://transport.opendata.ch)
* Swiss railways network & stations map
	* Readily available in geojson
	* Improve readability of the map (avoid station to station stroke)

### Data wrangling
* GTFS integration into PostgreSQL spatial database using the Django framework
	* Ease of importation using libraries (django-multigtfs)
	* Ease of creation of a Rest API for exploratory purposes
* Capacity retrieval using the transport.opendata.ch API
	* Available for the next 30 days
	* Wrap the SBB API into a more usable API
* Computation of the path between two stations using a breadth first search algorithm on the swiss railways network geojson
* Augmentation of the GTFS data to reflect both occupancy of a train and the route it follows for each trip

### Data processing
* Aggregation of the dataset to get the amount of passenger on each railway path during a given interval (e.g. 15min).
	* Finding capacity (number of seats) of each train to have a better view of how many people are travelling using reisezuege.ch
* Create itinerary from stops of a same trip to get the time where a train is on a certain railway path. Combine this information with the train occupancy and capacity to know how many people are on that train
* Then combine all people on each railway segment from the individual trip data.

### Data visualization
* Creation of a REST API backend in scala for data retrieval
* Cache all raw day data and interval already computed to enable the live view not to lag.
* Creation of a animated visualization using D3js





