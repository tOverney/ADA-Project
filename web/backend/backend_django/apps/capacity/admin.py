from django.contrib import admin
from .models import Capacity

class CapacityAdmin(admin.ModelAdmin):
    raw_id_fields = ('stop_time', 'trip', 'service_date')
    list_display = ('trip_id2', 'stop_time_id2',  'service_date_id2', 'capacity1st', 'capacity2nd')

    def trip_id2(self, instance):
        return instance.trip.pk
    
    def stop_time_id2(self, instance):
        return instance.stop_time.pk
    
    def service_date_id2(self, instance):
        return instance.service_date.pk

admin.site.register(Capacity, CapacityAdmin)