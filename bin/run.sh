#!/bin/bash

# Store the filename
filename="$1"
shift

# Check for filename
if [ -z "$filename" ]; then
    echo "You must enter a filename."
    echo "Example: ./bin/run.sh StreamProducer"
    exit
fi

# Store the rest of the params
params="$*"

# Compile & run
mvn compile exec:java -Dexec.mainClass="$filename" -Dexec.args="$params"

