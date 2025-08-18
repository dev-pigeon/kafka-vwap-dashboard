#!/bin/bash
echo "Downloading dataset..."

curl -L -o ~/Downloads/9000-tickers-of-stock-market-data-full-history.zip\
  https://www.kaggle.com/api/v1/datasets/download/jakewright/9000-tickers-of-stock-market-data-full-history

mv ~/Downloads/9000-tickers-of-stock-market-data-full-history.zip ~/Documents/Projects/kafka-dashboard/kafka/data
unzip ~/Documents/Projects/kafka-dashboard/kafka/data/9000-tickers-of-stock-market-data-full-history.zip 

mv ./all_stock_data.csv ../kafka/data
rm -rf ../bin/all_stock_data.parquet
rm -rf ../kafka/data/9000-tickers-of-stock-market-data-full-history.zip

echo "Download complete!"
