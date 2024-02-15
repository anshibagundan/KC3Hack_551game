import django_filters
from eemon_551_app.models import UserQuestionData

class UserQuestionDataFilter(django_filters.FilterSet):
    class Meta:
        model = UserQuestionData
        fields = ['qes_id', 'user_data_id']