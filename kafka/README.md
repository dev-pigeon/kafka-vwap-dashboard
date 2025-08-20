# Kafka

### Role in the System

This component calculates volume weighted average price (VWAP) based on stock trade information over a two-minute sliding window. It then sinks the VWAPs into a PostgreSQL instance. These VWAPs are then consumed by the Flask API to be displayed in the frontend.

## Contents

**Note:** Any file name containing the word `serializer`, `deserializer`, or `serde` is a serialization file required by Kafka. Such a file allows the object (who's name is the prefix to any of the above identifiers in the filename) to be serialized by Kafka and used as a custom ProducerRecord, state store, etc. These files are **NOT** included in the description below.

- `StockRecord.java` - An object representation of a data record containing basic information such as ticker, price, and volume
- `Database.java` - A config file that allows for things such as connection pooling and persistent connections to PostgreSQL via HikariCP
- `Window.java` - A custom state store that utilizes a `Deque` to maintain a two minute sliding window of all `StockRecords` for a given ticker
- `WindowTransformer.java` - A custom Kafka Transformer that calls each tickers' `Window.java` object to perform a VWAP calculation, collects each VWAP, and batch inserts the updated VWAPs every second
- `StreamProducer.java` - A Kafka producer that emits one record per millisecond of a historical databse to simulate real time data
- `StreamConsumer.java` - A Kafka consumer that utilizes the Stream API, Window.java, and WindowTransformer.java to aggregate records by ticker and perform VWAP calculations

## Setup & Run

### Local

**Note:** Make sure you run the command `chmod +x ../bin/run.sh`

```bash
../bin/run.sh [class_name]
```

- _Example Usage:_ `../bin/run.sh StreamConsumer`

### Containerized

```bash
docker-compose up --build -d kafka-app
```

**Watch out! ⚠️ :** This command must be run from the root directory of the project

## Notes

The eviction process in `Window.java` functions by removing the head of the deque until an unexpired record is found, at which point, the loop terminates. This is functional due to the _simulated_ nature of the data stream. Reading the file line-by-line and then writing each record to a Kafka topic that is consumed by a consumer running on the same machine all but guarantees that records will arrive in order at the window. However, if this was consuming actual real-time streaming data, a more nuanced approach would be required. For example, if the window was a min-heap, this would ensure that the oldest records are ALWAYS at the front, no matter the order of arrival.
