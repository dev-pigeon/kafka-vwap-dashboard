# API

### Role in the System

This directory contains a Flask REST API. The API consumes data written by Kafka Streams and persisted in PostgreSQL. The frontend queries this API to display volume weighted average price values in real time.

## Contents

- `app.py` – API entry point
- `requirements.txt` – Python dependencies
- `db.py` - PostgreSQL / psycopg2 config file
- `create_database.sql` - Script to initialize PostgreSQL table

## Setup & Run

### Local

```bash
python3 -m venv venv
source venv/bin/activate
pip3 install -r requirements.txt
flask run
```

### Containerized

```bash
docker-compose up --build -d api
```

**Watch out! ⚠️ :** This command must be run from the root directory of the project

## Notes

The Flask API currently utilizes the default development server. This design choice was made in order to prioritize swift development. Should this application be deployed to the web in the future, the API will have to be migrated to a WSGI server.
