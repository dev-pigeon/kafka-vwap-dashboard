#!/bin/bash

# Reset Kafka
KAFKA_CONFIG_DIR="~/Documents/Projects/kafka_2.13-4.0.0/"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"
cd ../../kafka_2.13-4.0.0/
bin/kafka-topics.sh --delete --topic stock-records --bootstrap-server localhost:9092
bin/kafka-topics.sh --create --topic stock-records --bootstrap-server localhost:9092
cd "$SCRIPT_DIR"


