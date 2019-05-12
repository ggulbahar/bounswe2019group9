from django.urls import path
from .views import ListCreateSongsView, SongsDetailView, LoginView, RegisterUsers, index

app_name = 'music'

urlpatterns = [
    path('', index, name="index"),
    path('songs/', ListCreateSongsView.as_view(), name="songs-list-create"),
    path('songs/<int:pk>/', SongsDetailView.as_view(), name="songs-detail"),
    path('auth/login/', LoginView.as_view(), name="auth-login"),
    path('auth/register/', RegisterUsers.as_view(), name="auth-register")
]
