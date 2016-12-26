from django.conf.urls import include, url
from django.contrib import admin
from django.views.generic import TemplateView
from django.conf import settings
from django.contrib import admin
from rest_framework_cache.registry import cache_registry

cache_registry.autodiscover()
admin.autodiscover()

urlpatterns = [
    url(r'^admin/', include(admin.site.urls)),
    url(r'api/', include('backend_django.apps.api.urls')),
    url(r'', include('backend_django.apps.explore.urls')),
]

if settings.DEBUG:
    import debug_toolbar
    urlpatterns += [
        url(r'^__debug__/', include(debug_toolbar.urls)),
    ]