#!/bin/bash
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DATA_DIR="$SCRIPT_DIR/../kafka/data"

# ------------- DOWNLOAD DATA -------------
echo "Downloading zip file..."
mkdir -p $DATA_DIR

curl -L -o $DATA_DIR/9000-tickers-of-stock-market-data-full-history.zip \
  https://www.kaggle.com/api/v1/datasets/download/jakewright/9000-tickers-of-stock-market-data-full-history

echo "Extracting contents..."

unzip $DATA_DIR/9000-tickers-of-stock-market-data-full-history.zip 

rm -rf all_stock_data.parquet
rm -rf $DATA_DIR/9000-tickers-of-stock-market-data-full-history.zip
mv all_stock_data.csv $DATA_DIR

echo "Download complete!"


# ------------- CLEAN DATA -------------
echo "Initializing Python environment..."
cd bin
python3 -m venv venv
source venv/bin/activate
pip3 install --no-cache-dir -r requirements.txt

echo "Cleaning dataset..."
python3 clean_data.py
rm -rf $DATA_DIR/all_stock_data.csv
echo "Dataset cleaned!"
echo "Please run 'docker-compose up --build' to demo the project"

