from django.conf.urls import url
from contact import views

urlpatterns = [
    url(r'^$', views.contact_list, name='contact_list'),
    url(r'^details/(?P<pk>\d+)/$', views.contact_details, name='contact_details'),
    url(r'^create/', views.create, name='create'),
    url(r'^edit/(?P<pk>\d+)/$', views.contact_edit, name='contact_edit'),
    url(r'^delete/(?P<pk>\d+)/$', views.contact_delete, name='contact_delete')
]