from rest_framework import serializers
from .models import question, location, genre, userdata, userquestiondata

class QuestionSerializer(serializers.ModelSerializer):
    class Meta:
        model = question
        fields = '__all__'

class LocationSerializer(serializers.ModelSerializer):
    class Meta:
        model = location
        fields = '__all__'

class GenreSerializer(serializers.ModelSerializer):
    class Meta:
        model = genre
        fields = '__all__'

class UserdataSerializer(serializers.ModelSerializer):
    class Meta:
        model = userdata
        fields = '__all__'

class UserquestiondataSerializer(serializers.ModelSerializer):
    class Meta:
        model = userquestiondata
        fields = '__all__'

