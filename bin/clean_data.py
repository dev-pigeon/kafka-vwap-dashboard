import pandas as pd  # type: ignore


def within_date_range(val):
    try:
        cutoff = 2000
        year = int(val[:4])
        if year - cutoff < 0:
            return False
        return True
    except:
        return False


def adjust_for_splits(df_raw, date_col="Date", ticker_col="Ticker", split_col="Stock Splits"):
    df_range_adjusted = df_raw[df_raw[date_col].apply(
        within_date_range)].drop("Dividends", axis=1)

    split_events = df_range_adjusted[df_range_adjusted[split_col] > 1]

    adjusted_groups = []
    grouped = df_range_adjusted.groupby(ticker_col)

    for ticker, group_df in grouped:
        group_df = group_df.sort_values(date_col).copy()
        splits = split_events[split_events[ticker_col]
                              == ticker].sort_values(date_col)

        for _, split in splits.iterrows():
            ratio = split[split_col]
            split_date = split[date_col]
            mask = group_df[date_col] < split_date  # will break

            # adjust close price and volume
            for col in ["Close", "Open", "High", "Low"]:
                group_df.loc[mask, col] /= ratio
            group_df.loc[mask, 'Volume'] *= ratio

        adjusted_groups.append(group_df)
    df_adjusted = pd.concat(adjusted_groups, ignore_index=True)
    df_cleaned = df_adjusted.drop("Stock Splits", axis=1)
    return df_cleaned


df_raw = pd.read_csv("../data/all_stock_data.csv")
df_cleaned = adjust_for_splits(df_raw)
df_cleaned.to_csv("../data/stock_data_cleaned.csv", index=False)
