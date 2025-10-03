#!/bin/bash

filename="$1"
shift

if [ -z "$filename" ]; then
    echo "You must enter a filename."
    echo "Example: ./bin/run.sh com.example.StreamProducer"
    exit 1
fi

params="$*"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
KAFKA_DIR="$SCRIPT_DIR/../kafka"

cd "$KAFKA_DIR"
mvn compile exec:java -Dexec.mainClass="$filename" -Dexec.args="$params"
