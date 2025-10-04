from flask import Flask, jsonify  # type: ignore
from flask_socketio import SocketIO  # type: ignore
from db import get_connection
from flask_cors import CORS  # type: ignore
import logging
import os
from dotenv import load_dotenv  # type: ignore


load_dotenv()
logging.basicConfig(level=logging.DEBUG)
app = Flask(__name__)
socketio = SocketIO(
    app, cors_allowed_origins=os.getenv("API_ALLOWED_ORIGINS"), async_mode="eventlet")  # type:ignore


def get_resources():
    conn = get_connection()
    return conn, conn.cursor()


def close_resources(conn, cursor):
    conn.close()
    cursor.close()


def emit_top_five():
    with app.app_context():
        conn, cursor = get_resources()
        while True:
            cursor.execute(
                "SELECT ticker, vwap, last_updated FROM kafka_dashboard.stock_vwap ORDER BY vwap DESC LIMIT 5;")
            top_five = cursor.fetchall()
            data = jsonify({"data": [
                {"ticker": row[0], "vwap": row[1], "last_updated": row[2]} for row in top_five]})
            socketio.emit("message", data.get_json())
            logging.info("Emitted top five stocks")
            socketio.sleep(1)

    close_resources(conn, cursor)


@socketio.on('connect')
def handle_join():
    logging.info("Client connected to server")


# Legacy function used for debugging
@app.route("/stocks", methods=['GET'])
def get_stocks():
    conn, cursor = get_resources()
    cursor.execute("SELECT * FROM kafka_dashboard.stock_vwap;")
    rows = cursor.fetchall()
    close_resources(conn, cursor)
    return jsonify([{"ticker": row[0], "vwap": row[1], "last_updated": row[2]} for row in rows])


@app.route("/")
def hello_world():
    return "<p>Hello, World!</p>"


if __name__ == '__main__':
    socketio.start_background_task(emit_top_five)
    host_url = os.getenv("API_HOST_URL")
    host_port = os.getenv("API_PORT")
    host_port = None if host_port is None else int(host_port)

    socketio.run(app=app, debug=True, host=host_url, port=host_port,
                 use_reloader=False)
