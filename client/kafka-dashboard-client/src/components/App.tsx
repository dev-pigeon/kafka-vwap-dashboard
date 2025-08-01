import "../styles/App.css";
import Header from "./Header";
import VwapList from "./VwapList";

function App() {
  return (
    <div>
      <div id="header">
        <Header />
      </div>
      <div id="outer-container">
        <VwapList />
      </div>
    </div>
  );
}

export default App;
