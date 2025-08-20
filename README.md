# Kafka VWAP Dashboard

## Overview

This project calculates **volume weighted average price (VWAP)** for simluated real-time stock trade data using Kafka. It processes the data in Java with a two-minute sliding window, batch inserts the results into PostgreSQL, and serves them through a Flask API to a React frontend. It was created so that I could gain a deeper understanding of real-time analytics, data processing, full-stack integration, containerization, and Apache Kafka.

<br>

## Tech Stack

- Java
- Apache Kafka
  -PostgreSQL
- Python / Flask
- React / TypeScript
- Docker / Docker Compose

<br>

## Requirements

- **Unix-based system** (Linux or macOS)
- **Docker Desktop** installed and running
- **Python 3.x** (used to run `setup.sh` and manage virtual environment)

> Note: Java, Kafka, and PostgreSQL are all containerized — you only need Python locally to run the setup script.

<br>

## System Architecture

The system consists of four components working together to process and display real-time VWAP data:

1. **StreamProducer (Kafka Producer)** - Emits one record per millisecond from a historical trade dataset to a Kafka topic to simulate a real-time data stream.
2. **StreamConsumer + Window + WindowTransformer (Kafka Consumer)** - Consumes records from the Kafka topic and maintains a two-minute sliding window per ticker. Uses the windows to calculate VWAPs and batch inserts them into PostgreSQL.
3. **PostgreSQL Database** - Stores the current VWAP for each ticker. Provides storage for the Flask API to query.
4. **Flask API + React Frontend** - The API exposes endpoints to retrieve the lateset VWAPs.The frontend visualizes these values.

<br>

## Components

- [`api/`](api/README.md) – Flask REST API serving VWAP data to the frontend.
- [`bin/`](bin/README.md) – Utility scripts to set up, reset, and run the demo environment.
- [`kafka/`](kafka/README.md) – Kafka producer, consumer, and custom transformer to compute VWAPs.
- [`client/`](client/README.md) – Frontend application to display VWAP data.

<br>

## Setup & Run

Clone the repository

```bash
git clone https://github.com/dev-pigeon/kafka-dashboard.git
```

Move to the correct directory

```bash
cd kafka-dashboard
```

Give permissions to the setup script:

```bash
chmod +x bin/setup.sh
```

Download and clean the dataset:

```bash
./bin/setup.sh
```

Build the images and start the container:

```bash
docker-compose up --build
```

Click [here](http://localhost:80) or open your browser of choice and navigate to `http://localhost:80`
