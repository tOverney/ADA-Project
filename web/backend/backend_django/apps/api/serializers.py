
from ..capacity.models import Capacity, Path

from multigtfs.models import (
    Block, Fare, FareRule, Feed, Frequency, Route, Service, ServiceDate, Shape,
    ShapePoint, Stop, StopTime, Trip, Agency)

from rest_framework import serializers
from rest_framework_cache.serializers import CachedSerializerMixin
from rest_framework_cache.registry import cache_registry
from ast import literal_eval
import re 

class AgencySerializer(serializers.ModelSerializer):
    class Meta:
        model = Agency
        fields =  '__all__'

class RouteSerializer(serializers.ModelSerializer):
    preview = serializers.HyperlinkedIdentityField(view_name='route_detail')
    
    class Meta:
        model = Route
        fields =  '__all__'

class StopSerializer(serializers.ModelSerializer):
    class Meta:
        model = Stop
        fields =  '__all__'

######################################
#
#   API
#
######################################


class StopTimeSerializer(serializers.ModelSerializer):
    station_id = serializers.SerializerMethodField()
    path = serializers.SerializerMethodField()
   
    class Meta:
        model = StopTime
        fields =  ('station_id', 'arrival_time', 'departure_time', 'path')

    def get_station_id(self, obj):
        s = obj.stop.stop_id
        try:
            index = s.index(':')
        except ValueError:
            index = len(s)
        return s[:index]

    def get_path(self, obj):
        try:
            paths = Path.objects.get(trip_id=obj.trip_id, stop_id=obj.stop_id)
            return literal_eval(paths.path)
        except Path.DoesNotExist:
            return []


class TripSerializer(serializers.ModelSerializer):
    trip_id = serializers.SerializerMethodField()
    trip_short_name = serializers.CharField(source='route.short_name', read_only=True)
    trip_vehicle = serializers.SerializerMethodField()
    stops = StopTimeSerializer(many=True, read_only=True, source='stoptime_set')
    
    class Meta:
        model = Trip
        fields = ('trip_id', 'trip_short_name', 'trip_vehicle', 'stops')

    def get_trip_id(self, obj):
        s = obj.trip_id
        try:
            index = s.index(':')
        except ValueError:
            index = len(s)
        return s[:index]

    def get_trip_vehicle(self, obj):
        s = obj.route.short_name
        r = re.compile("([a-zA-Z]*)\d*")
        c = r.match(s)
        if c :
            return c.group(1).lower()
        else :
            return s


            