import { Box } from "@mui/material";
import useVwapList, { valueFormatter } from "../hooks/useVwapList";
import { BarChart } from "@mui/x-charts";

const VwapList = () => {
  const vwapListHook = useVwapList();
  const styleLabel = {
    fontWeight: 800,
  };

  return (
    <Box
      sx={{
        backgroundColor: "#333333",
        borderRadius: 10,
        padding: "10px",
        alignSelf: "center",
        border: "2px solid #444",
      }}
    >
      {vwapListHook.vwapList.length > 0 && (
        <BarChart
          overflow={"visible"}
          height={window.innerHeight / 2}
          width={window.innerWidth / 2}
          dataset={vwapListHook.vwapList}
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
          series={[{ dataKey: "vwap", label: "Current VWAP", valueFormatter }]}
          layout="horizontal"
          colors={["#8454dc"]}
        />
      )}
    </Box>
  );
};

export default VwapList;
