"""practice_app URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/2.2/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path, include, re_path
from rest_framework_jwt.views import obtain_jwt_token

urlpatterns = [
    path('', include('home.urls')),
    path('admin/', admin.site.urls),
    path('polls/', include('polls.urls')),
    path('translate/', include('translate.urls')),
    path('detect/', include('detect.urls')),
    path('rec/', include('arda.urls')),
    path('obj/', include('obj.urls')),
    path('wordoftheday/', include('wordoftheday.urls')),
    path('texttospeech/', include('texttospeech.urls')),
    path('api-token-auth/', obtain_jwt_token, name='create-token'),
    re_path('music/(?P<version>(v1|v2))/', include('music.urls')),
    path('burhan/', include('burhan.urls'))
]
