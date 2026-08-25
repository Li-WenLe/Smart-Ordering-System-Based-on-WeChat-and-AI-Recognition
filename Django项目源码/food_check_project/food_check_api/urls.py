# -*- coding:utf-8 -*-
from django.urls import path
from .views import detect_dish

urlpatterns = [
    path('', detect_dish, name='detect_dish'),

]