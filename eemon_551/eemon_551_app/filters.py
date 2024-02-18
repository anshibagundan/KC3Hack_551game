import django_filters
from eemon_551_app.models import UserQuestionData, Question

class UserQuestionDataFilter(django_filters.FilterSet):
    class Meta:
        model = UserQuestionData
        fields = ['qes_id', 'user_data_id']

class QuestionFilter(django_filters.FilterSet):
    class Meta:
        model = Question
        fields = ['rare']
