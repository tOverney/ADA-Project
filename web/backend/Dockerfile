FROM python:2.7

# RUN apt-get -qq update
RUN apt-get update -y && apt-get upgrade -y
RUN apt-get install -y sqlite3 spatialite-bin gdal-bin

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .