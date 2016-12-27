# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('multigtfs', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='Capacity',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('capacity1st', models.IntegerField(null=True, verbose_name=b'capacity1st', blank=True)),
                ('capacity2nd', models.IntegerField(null=True, verbose_name=b'capacity2nd', blank=True)),
                ('service_date', models.ForeignKey(to='multigtfs.ServiceDate')),
                ('stop_time', models.ForeignKey(to='multigtfs.StopTime')),
                ('trip', models.ForeignKey(to='multigtfs.Trip')),
            ],
        ),
    ]
