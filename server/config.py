import datetime

JWT_SECRET_KEY = 'secret'
SQLALCHEMY_DATABASE_URI = 'mysql://root:root@localhost/iPromise'
SQLALCHEMY_TRACK_MODIFICATIONS = False
JWT_ACCESS_TOKEN_EXPIRES = datetime.timedelta(days=1)
