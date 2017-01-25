from django.db import models

from multigtfs.models import (
    Block, Fare, FareRule, Feed, Frequency, Route, Service, ServiceDate, Shape,
    ShapePoint, Stop, StopTime, Trip, Agency)


class Path(models.Model):
    trip = models.ForeignKey(Trip)
    stop = models.ForeignKey(Stop)
    path = models.CharField(max_length=1024, null=True, blank=True)
    
    class Meta:
        unique_together = ('trip', 'stop',)

class Capacity(models.Model):
    trip = models.ForeignKey(Trip)
    stop_time = models.ForeignKey(StopTime)
    service_date = models.ForeignKey(ServiceDate)

    capacity1st = models.IntegerField('capacity1st', null=True, blank=True)
    capacity2nd = models.IntegerField('capacity2nd', null=True, blank=True)

    class Meta:
        unique_together = ('trip', 'stop_time', 'service_date')