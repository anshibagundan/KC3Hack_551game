import base64
from django.core.files.base import ContentFile
from rest_framework import serializers

class Base64ImageField(serializers.ImageField):
    def to_internal_value(self, data):
        if isinstance(data, str) and data.startswith('data:image'):
            # Base64エンコードされた文字列からフォーマットとエンコードされたデータを取り出す
            format, imgstr = data.split(';base64,')
            ext = format.split('/')[-1]

            # ContentFileを使用してDjangoのImageFieldで利用できるようにする
            data = ContentFile(base64.b64decode(imgstr), name='temp.' + ext)

        return super().to_internal_value(data)
