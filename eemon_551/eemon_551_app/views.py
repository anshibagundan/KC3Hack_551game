from rest_framework import viewsets
from .models import Location, Genre, Question, UserData, UserQuestionData
from .serializers import LocationSerializer, GenreSerializer, QuestionSerializer, UserDataSerializer, UserQuestionDataSerializer

class LocationViewSet(viewsets.ModelViewSet):
    queryset = Location.objects.all()
    serializer_class = LocationSerializer

class GenreViewSet(viewsets.ModelViewSet):
    queryset = Genre.objects.all()
    serializer_class = GenreSerializer

class QuestionViewSet(viewsets.ModelViewSet):
    queryset = Question.objects.all()
    serializer_class = QuestionSerializer

class UserDataViewSet(viewsets.ModelViewSet):
    queryset = UserData.objects.all()
    serializer_class = UserDataSerializer

class UserQuestionDataViewSet(viewsets.ModelViewSet):
    queryset = UserQuestionData.objects.all()
    serializer_class = UserQuestionDataSerializer
