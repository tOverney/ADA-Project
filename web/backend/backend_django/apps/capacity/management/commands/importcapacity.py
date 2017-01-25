from __future__ import unicode_literals
from optparse import make_option
import os
from csv import reader
from codecs import BOM_UTF8

from django.utils.six import string_types, PY3
from django.core.management.base import BaseCommand, CommandError

from ...models import Capacity

class Command(BaseCommand):
    help = 'Encode txt files in ascii format'

    def add_arguments(self, parser):
        parser.add_argument('--input', '-i', help='input file as csv')

    def handle(self, *args, **options):
        i = options['input']
     
        if not os.path.isfile(i):
            raise CommandError

        def no_convert(value): return value
        def date_convert(value): return datetime.strptime(value, '%Y%m%d')
        def bool_convert(value): return (value == '1')
        def char_convert(value): return (value or '')
        def null_convert(value): return (value or None)
        def point_convert(value): return (value or 0.0)
        def int_convert_capacity(value): 
            try:
                return int(float(value))
            except:
                return -1

        print(sum(1 for line in open(i, 'r')))
        csv_reader = reader(open(i, 'r'))
        CSV_BOM = BOM_UTF8.decode('utf-8') if PY3 else BOM_UTF8
        
        first = True
        for i,row in enumerate(csv_reader):
            if i  % 1000 == 0:
                print(i)
            if first:
                # Read the columns
                columns = row
                if columns[0].startswith(CSV_BOM):
                    columns[0] = columns[0][len(CSV_BOM):]
                first = False
                print(columns)
                continue
            try:
                _, created = Capacity.objects.get_or_create(
                    trip_id=int(float(row[7])),
                    stop_time_id=int(float(row[10])),
                    service_date_id=int(float(row[2])),
                    capacity1st=int_convert_capacity(row[-2]),
                    capacity2nd=int_convert_capacity(row[-1]),
                )
                pass


            except Exception as e:
                self.stdout.write("Error with row {} : {}".format(row, e))


        self.stdout.write("Done")
