# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('multigtfs', '0001_initial'),
        ('capacity', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='Path',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('path', models.CharField(max_length=1024, null=True, blank=True)),
                ('stop', models.ForeignKey(to='multigtfs.Stop')),
                ('trip', models.ForeignKey(to='multigtfs.Trip')),
            ],
        ),
    ]
