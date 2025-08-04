import useVwapList from "../hooks/useVwapList";
import "../styles/App.css";
import Header from "./Header";
import VwapList from "./VwapList";

function App() {
  const vwapListHook = useVwapList();
  return (
    <div>
      <div id="header">
        <Header lastUpdated={vwapListHook.lastUpdated} />
      </div>
      <div id="outer-container">
        <VwapList vwapList={vwapListHook.vwapList} />
      </div>
    </div>
  );
}

export default App;
