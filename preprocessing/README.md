# Postgresql query

Unfortunately, making the queries directly in django is complicated and takes a long time to run. Instead, we query directly the database using [postico]() and export the results to csv for further processing.



## Retrieve all for one day
	
	SELECT
	agency.id,
	service_date.id, service_date.date, 
	route.id, route.short_name, route.long_name,
	trip.id, trip.headsign, trip.short_name,
	stop_time.id, stop_time.arrival_time, stop_time.departure_time, stop_time.stop_sequence, 
	stop.id, stop.stop_id, stop.name, 
	capacity_path.id, capacity_path.path, 
	capacity_capacity.id, capacity_capacity.capacity1st, capacity_capacity.capacity2nd
	
	FROM service_date
	LEFT OUTER JOIN trip ON (service_date.service_id = trip.service_id)
	LEFT OUTER JOIN route ON (route.id = trip.route_id)
	LEFT OUTER JOIN agency ON (agency.id = route.agency_id)
	LEFT OUTER JOIN stop_time ON (stop_time.trip_id = trip.id)	
	LEFT OUTER JOIN stop ON (stop.id = stop_time.stop_id)
	LEFT OUTER JOIN capacity_path ON (capacity_path.trip_id = trip.id AND capacity_path.stop_id = stop.id)
	LEFT OUTER JOIN capacity_capacity ON (capacity_capacity.trip_id = trip.id AND capacity_capacity.stop_time_id = stop_time.id AND capacity_capacity.service_date_id = service_date.id)
	WHERE 
	(agency.id = 31 OR agency.id = 45 OR agency.id = 52) 
	AND service_date.date = '2017-02-06'
	AND stop.stop_id NOT IN ('132','133','134','135','136','137','138', '139', '140', '141','142','174','175', '176')
	ORDER BY 
	trip.id ASC,
	stop_time.stop_sequence ASC
