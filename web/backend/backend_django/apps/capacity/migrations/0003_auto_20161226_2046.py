# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('capacity', '0002_auto_20161226_2038'),
    ]

    operations = [
        migrations.AlterField(
            model_name='capacity',
            name='service_date',
            field=models.ForeignKey(to='multigtfs.ServiceDate'),
        ),
        migrations.AlterField(
            model_name='capacity',
            name='stop_time',
            field=models.ForeignKey(to='multigtfs.StopTime'),
        ),
        migrations.AlterField(
            model_name='capacity',
            name='trip',
            field=models.ForeignKey(to='multigtfs.Trip'),
        ),
    ]
