# Kafka VWAP Dashboard

## Overview &nbsp; 🔎

This project calculates **volume weighted average price (VWAP)** for simluated real-time stock trade data using Kafka. It processes the data in Java with a two-minute sliding window, batch inserts the results into PostgreSQL, and serves them through a Flask API to a React frontend. It was created so that I could gain a deeper understanding of real-time analytics, data processing, full-stack integration, containerization, and Apache Kafka.

<br>

## Tech Stack & Requirements &nbsp; 🧰

### Stack 📚

- Java / JUnit
- Apache Kafka
  -PostgreSQL
- Python / Flask
- React / TypeScript
- Docker / Docker Compose
- MUI

### Requirements &nbsp; 📝

- **Unix-based system** (Linux or macOS)
- **Docker Desktop** installed and running
- **Python 3.x** (used to run `setup.sh` and manage virtual environment)

> Note: Java, Kafka, TypeScript, React, and PostgreSQL are all containerized — you only need Python locally to run the setup script.

<br>

## Setup & Run &nbsp; 🚀

Clone the repository:

```bash
git clone https://github.com/dev-pigeon/kafka-dashboard.git
```

Then run:

```bash
cd kafka-dashboard
chmod +x bin/setup.sh
./bin/setup.sh
docker-compose up --build
```

Click [here](http://localhost:80) or open your browser of choice and navigate to http://localhost:80

<br>

## System Architecture &nbsp; ⚙️

The system consists of four components working together to process and display real-time VWAP data:

1. **StreamProducer (Kafka Producer)** - Emits one record per millisecond from a historical trade dataset to a Kafka topic to simulate a real-time data stream.
2. **StreamConsumer + Window + WindowTransformer (Kafka Consumer)** - Consumes records from the Kafka topic and maintains a two-minute sliding window per ticker. Uses the windows to calculate VWAPs and batch insert them into PostgreSQL.
3. **PostgreSQL Database** - Stores the current VWAP for each ticker. Provides storage for the Flask API to query.
4. **Flask API + React Frontend** - The API exposes an endpoint to retrieve the lateset VWAPs. The frontend then visualizes these values.

<br>

## Quicklinks &nbsp; 🔗

- [`api/`](api/README.md) – Flask REST API serving VWAP data to the frontend.
- [`bin/`](bin/README.md) – Utility scripts to set up, reset, and run the demo environment.
- [`kafka/`](kafka/README.md) – Kafka producer, consumer, and custom transformer to compute VWAPs.
- [`client/`](client/README.md) – Frontend application to display VWAP data.

<br>
