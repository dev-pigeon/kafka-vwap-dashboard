import { Box, LinearProgress, Stack, Typography } from "@mui/material";
import { valueFormatter, type VwapListItem } from "../hooks/useVwapList";
import { BarChart } from "@mui/x-charts";

interface VwapListProps {
  vwapList: VwapListItem[];
}

const VwapList = ({ vwapList }: VwapListProps) => {
  const styleLabel = {
    fontWeight: 800,
  };

  return (
    <>
      {vwapList.length > 0 && (
        <Box
          sx={{
            backgroundColor: "#2f2f3f",
            borderRadius: 10,
            padding: "10px",
            alignSelf: "center",
            border: "2px solid #444",
          }}
        >
          <BarChart
            overflow={"visible"}
            height={window.innerHeight / 2}
            width={window.innerWidth / 2}
            dataset={vwapList}
            yAxis={[
              {
                scaleType: "band",
                dataKey: "ticker",
                label: "Stock Ticker",
                labelStyle: { ...styleLabel },
              },
            ]}
            xAxis={[
              {
                label: "Volume Weighted Average Price (USD)",
                labelStyle: { ...styleLabel },
              },
            ]}
            series={[
              { dataKey: "vwap", label: "Current VWAP", valueFormatter },
            ]}
            layout="horizontal"
            colors={["#8454dc"]}
          />
        </Box>
      )}{" "}
      {
        // else vwap list has not loaded
        <Stack
          sx={{
            width: "50%",
            display: "flex",
            alignItems: "stretch",
            justifyItems: "center",
          }}
        >
          <Typography variant="h6">Loading VWAP List</Typography>
          <LinearProgress
            sx={{
              "& .MuiLinearProgress-bar": {
                backgroundColor: "#8454dc",
              },
            }}
          />
        </Stack>
      }
    </>
  );
};

export default VwapList;
