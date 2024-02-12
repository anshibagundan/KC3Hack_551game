from rest_framework import serializers
from .models import Location, Genre, Question, UserData, UserQuestionData

class LocationSerializer(serializers.ModelSerializer):
    class Meta:
        model = Location
        fields = '__all__'

class GenreSerializer(serializers.ModelSerializer):
    class Meta:
        model = Genre
        fields = '__all__'

class QuestionSerializer(serializers.ModelSerializer):
    class Meta:
        model = Question
        fields = '__all__'

class UserDataSerializer(serializers.ModelSerializer):
    class Meta:
        model = UserData
        fields = '__all__'

class UserQuestionDataSerializer(serializers.ModelSerializer):
    class Meta:
        model = UserQuestionData
        fields = '__all__'
