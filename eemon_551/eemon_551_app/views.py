from rest_framework import viewsets
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework import generics
from .models import Location, Genre, Question, UserData, UserQuestionData
from .serializers import LocationSerializer, GenreSerializer, QuestionSerializer, UserDataSerializer, UserQuestionDataSerializer
from django_filters.rest_framework import DjangoFilterBackend
from .filters import UserQuestionDataFilter
import logging

logging.basicConfig(level=logging.DEBUG)
logger = logging.getLogger(__name__)

def get_queryset(self):
    genre_id = self.request.query_params.get('genre_id', None)
    logger.debug(f"Genre ID: {genre_id}")  # genre_idの値をログに出力

    if genre_id is not None:
        queryset = Question.objects.filter(genre_id=genre_id)
        logger.debug(f"Filtered queryset count: {queryset.count()}")  # フィルタリング後のクエリセットの数
        return queryset
    else:
        queryset = Question.objects.all()
        logger.debug("Returning all questions.")  # 全てのQuestionオブジェクトを返すことをログに出力
        return queryset


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
    filter_backends = (DjangoFilterBackend,)
    filterset_class = UserQuestionDataFilter

class UserQuestionDataDelete(APIView):
    def delete(self, request, *args, **kwargs):
        # クエリパラメータから qes_id と user_data_id を取得
        qes_id = request.query_params.get('qes_id')
        user_data_id = request.query_params.get('user_data_id')

        # qes_id と user_data_id が両方提供されているか確認
        if qes_id and user_data_id:
            # 対象のオブジェクトを検索
            objects = UserQuestionData.objects.filter(qes_id=qes_id, user_data_id=user_data_id)
            # 対象オブジェクトが存在する場合は削除
            if objects.exists():
                objects.delete()
                return Response(status=status.HTTP_204_NO_CONTENT)
            else:
                # 指定された条件に一致するオブジェクトが存在しない場合
                return Response({"error": "No matching objects found to delete."}, status=status.HTTP_404_NOT_FOUND)
        else:
            # 必要なクエリパラメータが提供されていない場合
            return Response({"error": "qes_id and user_data_id are required."}, status=status.HTTP_400_BAD_REQUEST)

class UserIdView(APIView):
    def get(self, request, *args, **kwargs):
        # クエリパラメータからフィルタ条件を取得
        name = request.query_params.get('name')
        level = request.query_params.get('level')
        money = request.query_params.get('money')

        # データベースから条件に一致するレコードを検索
        user = UserData.objects.filter(name=name, level=level, money=money).first()
        if user:
            # IDをレスポンスとして返す
            return Response({'id': user.id})
        else:
            return Response({'error': 'User not found'}, status=404)



