import useVwapList from "../hooks/useVwapList";
import "../styles/App.css";
import { Button, Stack } from "@mui/material";

function App() {
  const VwapListHook = useVwapList();
  return (
    <div id="outer-container">
      <Stack>
        <Button id="basic-button">Press Me!</Button>
      </Stack>
    </div>
  );
}

export default App;
