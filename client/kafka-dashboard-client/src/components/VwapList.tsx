import { Box } from "@mui/material";
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
    <Box
      sx={{
        backgroundColor: "#2f2f3f",
        borderRadius: 10,
        padding: "10px",
        alignSelf: "center",
        border: "2px solid #444",
      }}
    >
      {vwapList.length > 0 && (
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
          series={[{ dataKey: "vwap", label: "Current VWAP", valueFormatter }]}
          layout="horizontal"
          colors={["#8454dc"]}
        />
      )}
    </Box>
  );
};

export default VwapList;
