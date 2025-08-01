import useVwapList, { valueFormatter } from "../hooks/useVwapList";
import { BarChart } from "@mui/x-charts";

const VwapList = () => {
  const vwapListHook = useVwapList();
  const styleLabel = {
    fontWeight: 500,
    fontSize: "18px",
  };

  return (
    <div>
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
              labelStyle: styleLabel,
            },
          ]}
          xAxis={[
            {
              label: "Volume Weighted Average Price (USD)",
              labelStyle: styleLabel,
            },
          ]}
          series={[{ dataKey: "vwap", label: "Current VWAP", valueFormatter }]}
          layout="horizontal"
          colors={["#8454dc"]}
        />
      )}
    </div>
  );
};

export default VwapList;
