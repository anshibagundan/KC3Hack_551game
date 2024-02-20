from django.db import models

class Location(models.Model):
    name = models.CharField(max_length=255)
    img = models.TextField(default='no image')  # TEXTデフォルト値の設定
    iskansai = models.BooleanField(default=False)

    def __str__(self):
        return self.name

class Genre(models.Model):
    name = models.CharField(max_length=255)

    def __str__(self):
        return self.name

class Title(models.Model):
    name = models.CharField(max_length=255)
    rare = models.IntegerField(default=0)

    def __str__(self):
        return self.name

class Question(models.Model):
    name = models.CharField(max_length=255, null=False)
    img = models.CharField(max_length=255, null=False)
    card = models.CharField(max_length=255, null=False)
    txt = models.TextField(null=False)
    link = models.CharField(max_length=255, blank=True, null=True)
    rare = models.IntegerField(default=0)
    loc_id = models.ForeignKey(Location, on_delete=models.CASCADE, related_name='questions')
    genre_id = models.ForeignKey(Genre, on_delete=models.CASCADE, related_name='questions')

    def __str__(self):
        return self.name

class UserData(models.Model):
    name = models.CharField(max_length=255)
    score = models.IntegerField(default=0)  # スコアの追加
    combo = models.IntegerField(default=0)  # コンボの追加

    def __str__(self):
        return self.name

class UserQuestionData(models.Model):
    user_data_id = models.ForeignKey(UserData, on_delete=models.CASCADE, related_name='question_data')
    cor = models.BooleanField()
    qes_id = models.ForeignKey(Question, on_delete=models.CASCADE, related_name='user_data')

    def __str__(self):
        return f"{self.user_data_id.name} - {self.qes_id.name}"

class UserTitle(models.Model):
    isOwn = models.BooleanField()
    user_data_id = models.ForeignKey(UserData, on_delete=models.CASCADE, related_name='titles')
    title_id = models.ForeignKey(Title, on_delete=models.CASCADE, related_name='users')

    def __str__(self):
        return f"{self.user_data_id.name} - {self.title_id.name}"
