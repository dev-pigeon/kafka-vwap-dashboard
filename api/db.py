import psycopg2  # type: ignore


def get_connection():
    return psycopg2.connect(
        dbname="kafka_dashboard",
        user="kafka_user",
        password="kafka_password",
        host="db",  # must match what is in docker-compose
        port="5432"
    )
