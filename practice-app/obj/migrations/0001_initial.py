# Generated by Django 2.0.3 on 2019-05-03 16:05

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Pictures',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('picUrl', models.CharField(max_length=255)),
                ('objectsInside', models.CharField(max_length=255)),
            ],
        ),
    ]
