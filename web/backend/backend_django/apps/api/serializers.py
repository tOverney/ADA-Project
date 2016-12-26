
from ..capacity.models import Capacity

from multigtfs.models import (
    Block, Fare, FareRule, Feed, Frequency, Route, Service, ServiceDate, Shape,
    ShapePoint, Stop, StopTime, Trip, Agency)

from rest_framework import serializers
from rest_framework_cache.serializers import CachedSerializerMixin
from rest_framework_cache.registry import cache_registry

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

class CapacitySerializer(serializers.ModelSerializer):
    class Meta:
        model = Capacity
        fields = ('id', 'capacity1st','capacity2nd')


class StopTimeSerializer(serializers.ModelSerializer):
    stop_id_full = serializers.CharField(source='stop.stop_id', read_only=True)
    stop_id =serializers.SerializerMethodField()
    stop_name = serializers.CharField(source='stop.name', read_only=True)
    capacity = CapacitySerializer(many=True, read_only=True, source='capacity_set')
    
    class Meta:
        model = StopTime
        fields =  ('id', 'stop', 'stop_id','stop_id_full','stop_name','arrival_time','departure_time','stop_sequence', 'capacity')

    def get_stop_id(self, obj):
        s = obj.stop.stop_id
        try:
            index = s.index(':')
        except ValueError:
            index = len(s)
        return s[:index]


class ServiceDateSerializer(serializers.ModelSerializer):
    class Meta:
        model = ServiceDate
        fields =  ('id', 'date',)


class ServiceSerializer(serializers.ModelSerializer):
    dates = ServiceDateSerializer(many=True, read_only=True, source='servicedate_set')
    class Meta:
        model = Service
        fields =  ('id', 'service_id' , 'dates')


class RouteSerializer(serializers.ModelSerializer):
    class Meta:
        model = Route
        fields =  ('id', 'route_id', 'short_name', 'long_name', 'rtype', 'agency')


class TripSerializer(serializers.ModelSerializer):
    start = serializers.CharField()
    end = serializers.CharField()
    count = serializers.IntegerField()
    route = RouteSerializer()
    stops = StopTimeSerializer(many=True, read_only=True, source='stoptime_set')
    
    class Meta:
        model = Trip
        fields = ('id', 'headsign' , 'short_name' ,'route' , 'service', 'start', 'end', 'count', 'stops',)


class TripWithServiceSerializer(TripSerializer):
    service = ServiceSerializer()

    class Meta:
        model = Trip
        fields = ('id', 'headsign' , 'short_name' ,'route' , 'service', 'start', 'end', 'count', 'stops',)
