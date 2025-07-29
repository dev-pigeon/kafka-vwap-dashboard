from flask import Flask, jsonify  # type: ignore
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
    return jsonify([{"ticker": row[0], "vwap": row[1], "last_updated": row[2]} for row in rows])


@app.route("/top-five", methods=['GET'])
def get_top_five():
    conn, cursor = get_resources()
    cursor.execute(
        "SELECT ticker, vwap, last_updated FROM kafka_dashboard.stock_vwap ORDER BY vwap DESC LIMIT 5;")
    top_five = cursor.fetchall()
    close_resources(conn, cursor)
    return jsonify([{"ticker": row[0], "vwap": row[1], "last_updated": row[2]} for row in top_five])


@app.route("/")
def hello_world():
    return "<p>Hello, World!</p>"


if __name__ == '__main__':
    app.run(debug=True, host='localhost', port=5335)
