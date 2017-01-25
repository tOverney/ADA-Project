# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('multigtfs', '0001_initial'),
        ('capacity', '0002_path'),
    ]

    operations = [
        migrations.AddField(
            model_name='capacity',
            name='stop',
            field=models.ForeignKey(default=0, to='multigtfs.Stop'),
            preserve_default=False,
        ),
    ]
