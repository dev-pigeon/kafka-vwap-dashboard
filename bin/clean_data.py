import pandas as pd


def within_date_range(val):
    try:
        cutoff = 2000
        year = int(val[:4])
        if year - cutoff < 0:
            return False
        return True
    except:
        return False


df_raw = pd.read_csv("../data/all_stock_data.csv")

df_cleaned = df_raw[df_raw['Date'].apply(within_date_range)].drop(
    "Dividends", axis=1).drop("Stock Splits", axis=1)

df_cleaned.to_csv("../data/stock_data_cleaned.csv")
