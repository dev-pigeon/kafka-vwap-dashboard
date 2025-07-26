DB_USER = "kafka_user"
DB_PASSWORD = "kafka_password"
DB_HOST = "localhost"
DB_PORT = "5432"
DB_NAME = "kafka_dashboard"

SQLALCHEMY_DATABASE_URI = (
    f"postgresql://{DB_USER}:{DB_PASSWORD}@{DB_HOST}:{DB_PORT}/{DB_NAME}"
)

SQLALCHEMY_TRACK_MODIFICATIONS = False
