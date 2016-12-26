# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('multigtfs', '0001_initial'),
        ('capacity', '0001_initial'),
    ]

    operations = [
        migrations.AddField(
            model_name='capacity',
            name='service_date',
            field=models.ForeignKey(blank=True, to='multigtfs.ServiceDate', null=True),
        ),
        migrations.AlterField(
            model_name='capacity',
            name='stop_time',
            field=models.ForeignKey(blank=True, to='multigtfs.StopTime', null=True),
        ),
        migrations.AlterField(
            model_name='capacity',
            name='trip',
            field=models.ForeignKey(blank=True, to='multigtfs.Trip', null=True),
        ),
    ]
