from django.db import models

from multigtfs.models import (
    Block, Fare, FareRule, Feed, Frequency, Route, Service, ServiceDate, Shape,
    ShapePoint, Stop, StopTime, Trip, Agency)

class Capacity(models.Model):
    trip = models.ForeignKey(Trip)
    stop_time = models.ForeignKey(StopTime)
    service_date = models.ForeignKey(ServiceDate)
    capacity1st = models.IntegerField('capacity1st', null=True, blank=True)
    capacity2nd = models.IntegerField('capacity2nd', null=True, blank=True)