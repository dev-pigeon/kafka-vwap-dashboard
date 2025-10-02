from flask import Flask, jsonify, request  # type: ignore
from flask_socketio import SocketIO, send, emit, join_room, leave_room  # type: ignore
from db import get_connection
from flask_cors import CORS  # type: ignore
import logging


logging.basicConfig(level=logging.DEBUG)
app = Flask(__name__)
socketio = SocketIO(
    app, cors_allowed_origins="*", async_mode="eventlet")
CORS(app)


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
    socketio.run(app=app, debug=True, host='0.0.0.0', port=5335,
                 use_reloader=False)
