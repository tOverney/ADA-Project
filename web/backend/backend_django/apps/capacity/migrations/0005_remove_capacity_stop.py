# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('capacity', '0004_auto_20170125_0658'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='capacity',
            name='stop',
        ),
    ]
