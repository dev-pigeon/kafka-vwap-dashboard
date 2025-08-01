import { AppBar, Toolbar, Typography, Box } from "@mui/material";

const Header = () => {
  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar sx={{ backgroundColor: "#1c1c1c" }} position="static">
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>
            Top 5 Stocks by VWAP
          </Typography>
          <Typography
            variant="body2"
            sx={{ display: { xs: "none", sm: "block" } }}
          >
            2-Minute Sliding Window · Simulated Stream
          </Typography>
        </Toolbar>
      </AppBar>
    </Box>
  );
};

export default Header;
