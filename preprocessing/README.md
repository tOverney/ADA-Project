# Data wrangling and pre-processing

The processing is separated in several notebooks. You should follow the steps in the following order.

1. Process the output CSV data to strip the stop_id using ```process_csv.ipynb```
2. Compute the path for each trips ```process_path.ipynb```
3. Request the capacity for each trip for one week ```process_capacity.ipynb```



## Issues encountered
* Absence of historical data related to the occupancy of the train in switzerland
	* To overcome this problem, have to rely on several different sources which bring to the table the problem of data integrations

* GTFS data from gtfs.geops.ch
	* Obscure relations and usage of the specification.
	* Require reparsing of several fields due to concatenation of several information to respect the uniqueness requirement

* Capacity information from transport.opendata.ch
	* Severe rate limiting imposes really long processing time
	* Difficulty to match the GTFS data to the returned one
	* Capacity limited to only three indicators
