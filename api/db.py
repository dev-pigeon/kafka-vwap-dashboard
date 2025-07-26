import psycopg2


def get_connection():
    return psycopg2.connect(
        dbname="kafka_dashboard",
        user="kafka_user",
        password="kafka_password",
        host="localhost",
        port="5432"
    )
