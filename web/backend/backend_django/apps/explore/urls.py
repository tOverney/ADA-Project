from django.conf.urls import patterns, url
from django.views.generic import DetailView, ListView
from multigtfs.models import (
    Agency, Block, Fare, FareRule, Feed, FeedInfo, Route, Service, ServiceDate,
    Shape, ShapePoint, Stop, StopTime, Trip, Zone)

urlpatterns = patterns(
    '',
    url(r'route/(?P<pk>\d+)/$', DetailView.as_view(model=Route), name='route_detail'),
)
