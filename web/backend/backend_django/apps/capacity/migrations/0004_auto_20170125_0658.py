# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('capacity', '0003_capacity_stop'),
    ]

    operations = [
        migrations.AlterUniqueTogether(
            name='capacity',
            unique_together=set([('trip', 'stop_time', 'service_date')]),
        ),
        migrations.AlterUniqueTogether(
            name='path',
            unique_together=set([('trip', 'stop')]),
        ),
    ]
