import psycopg2  # type: ignore
from dotenv import load_dotenv  # type: ignore
import os
from contextlib import contextmanager

load_dotenv()


@contextmanager
def db_connection():
    conn = cursor = None
    try:
        conn, cursor = get_db_resources()
        yield cursor
        conn.commit()
    except Exception:
        if conn:
            conn.rollback()
        raise
    finally:
        if conn and cursor:
            close_resources(conn, cursor)


def get_connection():
    return psycopg2.connect(
        dbname=os.getenv("DB_NAME"),
        user=os.getenv("DB_USER"),
        password=os.getenv("DB_PASSWORD"),
        host=os.getenv("DB_HOST"),  # must match what is in docker-compose
        port=os.getenv("DB_PORT")
    )


def get_db_resources():
    conn = get_connection()
    return conn, conn.cursor()


def close_resources(conn, cursor):
    conn.close()
    cursor.close()
