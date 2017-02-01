# GTFS Backend

Django backed backend for exploratory purposes of the GTFS data from [gtfs.geops.ch](gtfs.geops.ch). The filled PostgreSQL is also used as main source of "truth" for the Swiss train schedule.

## Setup

As stated above the backend is created using the Django framework. The app is Dockerised for easy deployement and loading on different operating systems.


### Setup

1. Create an env file
		 
		 touch .env
	
	Example of content
	
		SECRET_KEY=YOURLONGKEY
		DB_NAME=explore
		DB_USER=explore
		DB_PASS=explore
		DB_SERVICE=database
		DB_PORT=5432
		
2. **Docker setup**
	1. Assure that you have docker installed on your machine
	2. Create the docker machine if needed and find its ip
	
		    docker-machine create -d virtualbox dev;
		    docker-machine start dev
		    eval "$(docker-machine env dev)"
		    docker-machine ip dev
	
	3. Build the containers

			docker-compose build
			
	4. Run the containers using docker-compose

			docker-compose up

3. **Database setup**, connect to the database and create the necessary user and database

	    docker exec -it web_database_1 bash
	    createdb -U postgres *DB_NAME*
	    psql -U postgres
	    CREATE USER *DB_USER* WITH PASSWORD '*DB_PASS*';
	    ALTER ROLE *DB_USER* SUPERUSER;
	    GRANT ALL PRIVILEGES ON DATABASE *DB_NAME* to *DB_USER*;
	
4. 	Website data preparation			
			
		docker exec -it web_backend_1 bash
		./manage.py collectstatic
		
		
	* **_Option 1_**: Import manually the data into the database

		1.  The GTFS files need to be in ascii format. You can use a management command to ease the transformation.
			
				./manage.py cleangtfs -i gtfs_train/ -o gtfs_train_ascii/

		2. Then load the dataset in the database

    			./manage.py importgtfs gtfs_train_ascii/ -n train 
    			
    * **_Option 2_**: Import our data directly into the database, first unzip the pg.dump.zip file in the data folder.
			
			docker exec -it web_database_1 bash
   			psql -d *DB_NAME* -U *DB_USER* < /data/pg.dump
	
### Cleanup command for docker
	 
	docker rm -f `docker ps --no-trunc -aq`
	docker rmi -f `docker images | awk '{ print $3; }'`


## Backend API endpoint

The api can be accessed on ```http://ip_of_docker_machine/```

- ```api/agency/```
    List of all agencies
- ```api/agency/<agency_id>```
    Detail of an agency
- ```api/agency/<agency_id>/route/```
    List of all routes of a specific agency
- ```api/agency/<agency_id>/route/<route_id>```
    Detail of a route
- ```api/stop/```
    List of all stops
- ```stop/<stop_id>```
    Detail of a stop
- ```trip/```
    List of trips optionnaly filtered by 
        
        - date (format 2016-12-02)
        - time (format 12:00)
        - id (format True/false)

#####Example

	192.168.99.101/api/trip/?date=2017-01-09&time=12:00
