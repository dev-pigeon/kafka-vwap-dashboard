import pandas as pd  # type: ignore


def within_date_range(val):
    try:
        cutoff = 2000
        year = int(val[:4])
        return year >= cutoff
    except:
        return False


def price_in_range(val):
    min_price = .1
    max_price = 5000
    try:
        return val > min_price and val < max_price
    except:
        return False


def volume_in_range(val):
    min_volume = 5000
    try:
        return val > min_volume
    except:
        return False


def drop_invalid_dates(df_raw, date_col="Date"):
    df_valid_dates = df_raw[df_raw[date_col].apply(
        within_date_range)]
    return df_valid_dates


def adjust_for_splits(df_valid_dates, date_col="Date", ticker_col="Ticker", split_col="Stock Splits"):
    split_events = df_valid_dates[df_valid_dates[split_col] > 1]

    # Group by ticker
    adjusted_groups = []
    grouped = df_valid_dates.groupby(ticker_col)

    for ticker, group_df in grouped:
        group_df = group_df.sort_values(date_col).copy()
        splits = split_events[split_events[ticker_col]
                              == ticker].sort_values(date_col)

        group_df["split_factor"] = 1.0
        cumulative_factor = 1.0

        # apply cumulative factors based on date
        for _, split in splits.iterrows():
            ratio = split[split_col]
            split_date = split[date_col]
            mask = group_df[date_col] < split_date
            group_df.loc[mask, "split_factor"] *= ratio
            cumulative_factor *= ratio  # for bookkeeping, not required

        # adjust prices and volume based on split factor
        for col in ["Close", "Open", "High", "Low"]:
            group_df[col] = group_df[col] / group_df["split_factor"]
        group_df["Volume"] = group_df["Volume"] * group_df["split_factor"]

        group_df = group_df.drop("split_factor", axis=1)
        adjusted_groups.append(group_df)

    # Sort them again by date then by ticker stocks are chronological and interleaved by ticker
    df_adjusted = pd.concat(adjusted_groups, ignore_index=True).sort_values(
        by=["Date", "Ticker"]).reset_index(drop=True)
    df_cleaned = df_adjusted.drop("Stock Splits", axis=1)
    return df_cleaned


def drop_outliers(df_split_adjusted, volume_col="Volume", price_col="Close"):
    df_volume_check = df_split_adjusted[df_split_adjusted[volume_col].apply(
        volume_in_range)]
    df_outliers_dropped = df_volume_check[df_volume_check[price_col].apply(
        price_in_range)]
    return df_outliers_dropped


df_raw = pd.read_csv("../data/all_stock_data.csv")
df_no_dividends = df_raw.drop("Dividends", axis=1)
df_valid_dates = drop_invalid_dates(df_no_dividends)
df_split_adjusted = adjust_for_splits(df_valid_dates)
df_no_outliers = drop_outliers(df_split_adjusted)
df_no_outliers.to_csv("../data/stock_data_cleaned.csv", index=False)
