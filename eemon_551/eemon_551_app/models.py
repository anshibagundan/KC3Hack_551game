from django.db import models

class location(models.Model):
    name = models.CharField(max_length=255)
    font = models.CharField(max_length=255)
    img = models.TextField(default='no image')
    iskansai = models.BooleanField()

class genre(models.Model):
    name = models.CharField(max_length=255)
    col = models.CharField(max_length=6, default='000000')

class question(models.Model):
    name = models.CharField(max_length=255)
    img = models.TextField(default='no image')
    txt = models.TextField()
    link = models.CharField(max_length=255)
    loc = models.ForeignKey(location, on_delete=models.CASCADE)
    genre = models.ForeignKey(genre, on_delete=models.CASCADE)

class userdata(models.Model):
    name = models.CharField(max_length=255)

class userquestiondata(models.Model):
    user_data = models.ForeignKey(userdata, on_delete=models.CASCADE)
    cor = models.BooleanField()
    qes = models.ForeignKey(question, on_delete=models.CASCADE)
