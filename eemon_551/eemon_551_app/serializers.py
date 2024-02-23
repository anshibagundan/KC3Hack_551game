from rest_framework import serializers
from .models import Location, Genre, Question, UserData, UserQuestionData,UserTitle,Title,BackGround,UserBackGround

class LocationSerializer(serializers.ModelSerializer):
    class Meta:
        model = Location
        fields = '__all__'

class GenreSerializer(serializers.ModelSerializer):
    class Meta:
        model = Genre
        fields = '__all__'

class TitleSerializer(serializers.ModelSerializer):
    class Meta:
        model = Title
        fields = '__all__'

class BackGroundSerializer(serializers.ModelSerializer):
    class Meta:
        model = BackGround
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

class UserTitleSerializer(serializers.ModelSerializer):
    class Meta:
        model = UserTitle
        fields = '__all__'
class UserBackGroundSerializer(serializers.ModelSerializer):
    class Meta:
        model = UserBackGround
        fields = '__all__'


