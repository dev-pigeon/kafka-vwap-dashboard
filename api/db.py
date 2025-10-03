import psycopg2  # type: ignore
from dotenv import load_dotenv  # type: ignore
import os

load_dotenv()


def get_connection():
    return psycopg2.connect(
        dbname=os.getenv("DB_NAME"),
        user=os.getenv("DB_USER"),
        password=os.getenv("DB_PASSWORD"),
        host=os.getenv("DB_HOST"),  # must match what is in docker-compose
        port=os.getenv("DB_PORT")
    )
