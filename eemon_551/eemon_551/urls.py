from django.urls import path, include
from rest_framework.routers import DefaultRouter
from eemon_551_app import views
from eemon_551_app.views import UserIdView, UserBackgroundUseUpdateView

# ルーターの作成とビューセットの登録
router = DefaultRouter()
router.register(r'locations', views.LocationViewSet)
router.register(r'genres', views.GenreViewSet)
router.register(r'titles', views.TitleViewSet)
router.register(r'questions', views.QuestionViewSet)
router.register(r'userdatas', views.UserDataViewSet)
router.register(r'userquestiondatas', views.UserQuestionDataViewSet)
router.register(r'backgrounds', views.BackGroundViewSet)
router.register(r'usertitles', views.UserTitleViewSet)
router.register(r'userbackgrounds', views.UserBackGroundViewSet)

# APIのURLパターンをurlpatternsに追加
urlpatterns = [
    path('', include(router.urls)),
    path('delete_userquestiondata/', views.UserQuestionDataDelete.as_view()),
    path('api/user-id/', UserIdView.as_view(), name='user-id'),
    path('userbackgrounds/updateUseStatus', UserBackgroundUseUpdateView.as_view(), name='update-use-status'),
]
