import django_filters
from eemon_551_app.models import UserQuestionData, Question, UserTitle, UserBackGround

class UserQuestionDataFilter(django_filters.FilterSet):
    class Meta:
        model = UserQuestionData
        fields = ['qes_id', 'user_data_id']

class QuestionFilter(django_filters.FilterSet):
    class Meta:
        model = Question
        fields = ['rare']

class UserTitlesFilter(django_filters.FilterSet):
    class Meta:
        model = UserTitle
        fields = ['title_id', 'user_data_id']

class UserBackgroundFilter(django_filters.FilterSet):
    class Meta:
        model = UserBackGround
        fields = ['background_id', 'user_data_id']
