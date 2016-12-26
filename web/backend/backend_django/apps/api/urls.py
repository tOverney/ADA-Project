from django.conf.urls import include, url

from .views import *

from rest_framework_extensions.routers import ExtendedSimpleRouter

urlpatterns = [
    url(r'^trip/$', TripByDay.as_view() , name='route-list'),
]

router = ExtendedSimpleRouter()
(
    router
        .register(r'agency', AgencyViewSet, base_name='agency')
        .register(r'route',
            RouteViewSet,
            base_name='agency-route',
            parents_query_lookups=['agency__agency_id'])
)
urlpatterns = urlpatterns + router.urls


router = ExtendedSimpleRouter()
(
    router
        .register(r'stop', StopViewSet, base_name='stop')
)
urlpatterns = urlpatterns + router.urls