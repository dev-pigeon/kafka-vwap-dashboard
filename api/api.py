from flask import Flask, jsonify
from db import get_connection

app = Flask(__name__)


def get_resources():
    conn = get_connection()
    return conn, conn.cursor()


def close_resources(conn, cursor):
    conn.close()
    cursor.close()


@app.route("/stocks", methods=['GET'])
def get_stocks():
    conn, cursor = get_resources()
    cursor.execute("SELECT * FROM kafka_dashboard.stock_vwap;")
    rows = cursor.fetchall()
    close_resources(conn, cursor)
    return jsonify([{"ticker": row[0], "vwap": row[1]} for row in rows])


@app.route("/")
def hello_world():
    return "<p>Hello, World!</p>"


if __name__ == '__main__':
    app.run(debug=True, host='localhost', port=5335)
