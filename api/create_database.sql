CREATE SCHEMA IF NOT EXISTS kafka_dashboard;

CREATE TABLE IF NOT EXISTS kafka_dashboard.stock_vwap (
    ticker varchar(255) UNIQUE NOT NULL,
    vwap float,
    last_updated TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY(ticker)
);