from rest_framework import generics
from .models import location, genre, question, userdata, userquestiondata
from .serializers import QuestionSerializer

class QuestionListCreate(generics.ListCreateAPIView):
    queryset = question.objects.all()  # 問題モデルのクエリセットを取得
    serializer_class = QuestionSerializer  # 問題モデル用のシリアライザを使用
