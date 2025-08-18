#!/bin/bash
echo "Downloading dataset..."
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DATA_DIR="$SCRIPT_DIR/../kafka/data"
mkdir -p $DATA_DIR

curl -L -o $DATA_DIR/9000-tickers-of-stock-market-data-full-history.zip \
  https://www.kaggle.com/api/v1/datasets/download/jakewright/9000-tickers-of-stock-market-data-full-history

unzip $DATA_DIR/9000-tickers-of-stock-market-data-full-history.zip 

rm -rf all_stock_data.parquet
rm -rf $DATA_DIR/9000-tickers-of-stock-market-data-full-history.zip
mv all_stock_data.csv $DATA_DIR

echo "Download complete!"
