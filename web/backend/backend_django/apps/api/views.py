from rest_framework import viewsets
from rest_framework import serializers
from rest_framework.generics import ListAPIView
from rest_framework_extensions.mixins import NestedViewSetMixin
from django.db.models import Count, Min, Max, Prefetch

import sys
import re

from multigtfs.models import (
    Block, Fare, FareRule, Feed, Frequency, Route, Service, ServiceDate, Shape,
    ShapePoint, Stop, StopTime, Trip, Agency)

from .serializers import (
    AgencySerializer, 
    RouteSerializer,
    StopSerializer,
    TripSerializer,
    TripWithServiceSerializer,
)

class AgencyViewSet(NestedViewSetMixin, viewsets.ReadOnlyModelViewSet):
    model = Agency
    lookup_field = 'agency_id'
    queryset = Agency.objects.all()
    serializer_class = AgencySerializer

class RouteViewSet(NestedViewSetMixin, viewsets.ReadOnlyModelViewSet):
    model = Route
    queryset = Route.objects.all()
    serializer_class = RouteSerializer

class StopViewSet(NestedViewSetMixin, viewsets.ReadOnlyModelViewSet):
    model = Stop
    lookup_field = 'stop_id'
    queryset = Stop.objects.all()
    serializer_class = StopSerializer

class TripByDay(ListAPIView):   
    def get_queryset(self):
        queryset = Trip.objects.all()
        
        #queryset = queryset.filter(headsign = 'Allaman')
        
        agency = self.request.query_params.getlist('agency', None)
        if agency:
            route_ids_dict = Agency.objects.select_related('route').filter(id__in=agency).values('route__id').distinct()
            route_ids = set([entry['route__id'] for entry in route_ids_dict])
            queryset = queryset.filter(route_id__in=route_ids)

        queryset = queryset.select_related('route')
        queryset = queryset.prefetch_related(
                'stoptime_set',
                'stoptime_set__stop',
            )
       
        queryset = queryset.prefetch_related(
                'stoptime_set__capacity_set',
            )

        if self.request.query_params.get('service', None):
            queryset = queryset.prefetch_related(
                'service__servicedate_set',
            )

        queryset = queryset.annotate(
                start=Min('stoptime__departure_time'), 
                end=Max('stoptime__arrival_time'), 
                count=Count('stoptime'),
            )

        date = self.request.query_params.get('date', None)
        if date:
            queryset = queryset.filter(service__servicedate__date=date)


        time = self.request.query_params.get('time', None)
        if time:
            time = int(time[:2]) * 3600 + int(time[3:]) * 60;
            queryset = queryset.filter(start__lt = time, end__gt = time)

        return queryset

    def get_serializer_class(self):
        if self.request.query_params.get('service', None):
            return TripWithServiceSerializer
        else:
            return TripSerializer




