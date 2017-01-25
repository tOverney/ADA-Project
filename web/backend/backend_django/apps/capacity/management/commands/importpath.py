from __future__ import unicode_literals
from optparse import make_option
import os
from csv import reader
from codecs import BOM_UTF8

import pickle

from django.utils.six import string_types, PY3
from django.core.management.base import BaseCommand, CommandError

from ...models import Path

class Command(BaseCommand):
    help = 'Encode txt files in ascii format'

    def add_arguments(self, parser):
        parser.add_argument('--input', '-i', help='input file as pickle')

    def handle(self, *args, **options):
        i = options['input']
     
        if not os.path.isfile(i):
            raise CommandError

        trips = pickle.load(open(i, "rb"))

        print(len(trips))

        for k, path in trips.items():
            trip_id = k[0]
            stop_id = k[1]

            try:
                _, created = Path.objects.get_or_create(
                   trip_id = int(trip_id),
                   stop_id = int(stop_id),
                   path = str(path)
                )
                pass
            except Exception as e:
                self.stdout.write("Error with row {} {} : {}".format(k, path, e))

        self.stdout.write("Done")