from __future__ import unicode_literals
from optparse import make_option
import os
import unicodedata
from zipfile import ZipFile, ZIP_DEFLATED

from django.utils.six import string_types
from django.core.management.base import BaseCommand, CommandError

class Command(BaseCommand):
    help = 'Encode txt files in ascii format'

    def add_arguments(self, parser):
        parser.add_argument('--input', '-i', help='Input directory')
        parser.add_argument('--output', '-o', help='Output directory')

    def handle(self, *args, **options):
        i = options['input']
        o = options['output']

        if not os.path.exists(o):
            os.makedirs(o)

        files = [f for f in os.listdir(i) if os.path.isfile(os.path.join(i, f))]

        for f in files:
            file_path = os.path.join(i, f)
            self.stdout.write(file_path)
            
            unicode_file = open(file_path, 'rb')
            unicode_data = unicode_file.read().decode('UTF-8')
            b = unicodedata.normalize('NFKD', unicode_data).encode('ascii','ignore')
            ascii_file = open(os.path.join(o, f), 'wb')
            ascii_file.write(b)

            ascii_file.close()
            unicode_file.close()
            
        self.stdout.write("Done")
