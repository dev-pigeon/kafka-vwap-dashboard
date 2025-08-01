import useVwapList, { valueFormatter } from "../hooks/useVwapList";
import { BarChart } from "@mui/x-charts";

const VwapList = () => {
  const vwapListHook = useVwapList();

  return (
    <BarChart
      height={window.innerHeight / 2}
      width={window.innerWidth / 2}
      dataset={vwapListHook.vwapList}
      yAxis={[{ scaleType: "band", dataKey: "ticker" }]}
      series={[{ dataKey: "vwap", label: "Current VWAP", valueFormatter }]}
      layout="horizontal"
      grid={{ vertical: true }}
      colors={["#8454dc"]}
    />
  );
};

export default VwapList;
