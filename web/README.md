## Install
    # Create the docker-machine if needed
    docker-machine create -d virtualbox dev;
    docker-machine start dev

    # Find its ip
    docker-machine ip dev

    eval "$(docker-machine env dev)"

    # Build the containers 
    docker-compose build

    # Run the containers
    docker-compose up

## Setup Django
    
    # Create env file
    touch .env

        Example
        SECRET_KEY=5(15ds+i2+%ik6z&!yer+ga9m=e%jcqiz_5wszg)r-z!2--b2d
        DB_NAME=explore
        DB_USER=explore
        DB_PASS=explore
        DB_SERVICE=database
        DB_PORT=5432

    # Setup Database
    docker exec -it web_database_1 bash
    createdb -U postgres *DB_NAME*
    psql -U postgres
    CREATE USER *DB_USER* WITH PASSWORD '*DB_PASS*';
    ALTER ROLE *DB_USER* SUPERUSER;
    GRANT ALL PRIVILEGES ON DATABASE *DB_NAME* to *DB_USER*;

    # Setup Django
    docker exec -it web_backend_1 bash
    ./manage.py syncdb
    ./manage.py migrate
    
    # Import Dump
    ./manage.py loaddata dump.data

    # Import Data (Optional)
    ./manage.py createsuperuser
    ./manage.py importgtfs --name "test" gtfs_train.zip

## Clean Docker

    docker rm -f `docker ps --no-trunc -aq`
    docker rmi -f `docker images | awk '{ print $3; }'`

## Connect to Docker Container

    docker exec -it <containerIdOrName> bash

# Preprocessing
    
The GTFS files need to be in ascii format. You can use a management command to ease the transformation.

    ./manage.py cleangtfs -i gtfs_train/ -o gtfs_train_ascii/

Then we can load the dataset in the database

    ./manage.py importgtfs gtfs_train_ascii/ -n train 


# API END POINT

- ```agency/```
    List of all agencies
- ```agency/<agency_id>```
    Detail of an agency
- ```agency/<agency_id>/route/```
    List of all routes of a specific agency
- ```agency/<agency_id>/route/<route_id>```
    Detail of a route
- ```stop/```
    List of all stops
- ```stop/<stop_id>```
    Detail of a stop
- ```trip/```
    List of trips optionnaly filtered by 
        
        - date (format 2016-12-02)
        - time (format 12:00)
        - agency (format _id_)
        - service (format True/false)
