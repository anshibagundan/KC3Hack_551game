from django.db import models

# Locationモデル
class Location(models.Model):
    name = models.CharField(max_length=255)
    font = models.CharField(max_length=255)
    img = models.TextField(default='no image')
    iskansai = models.BooleanField()

# Genreモデル
class Genre(models.Model):
    name = models.CharField(max_length=255)
    col = models.CharField(max_length=6, default='000000')

# Questionモデル
class Question(models.Model):
    name = models.CharField(max_length=255)
    img = models.TextField(default='no image')
    txt = models.TextField()
    link = models.CharField(max_length=255, blank=True, null=True)
    loc_id = models.ForeignKey(Location, on_delete=models.CASCADE)
    genre_id = models.ForeignKey(Genre, on_delete=models.CASCADE)

# UserDataモデル
class UserData(models.Model):
    name = models.CharField(max_length=255)

# UserQuestionDataモデル
class UserQuestionData(models.Model):
    user_data_id = models.ForeignKey(UserData, on_delete=models.CASCADE)
    cor = models.BooleanField()
    qes_id = models.ForeignKey(Question, on_delete=models.CASCADE)
