import { AppBar, Toolbar, Typography, Box } from "@mui/material";

const Header = () => {
  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar sx={{ backgroundColor: "#222233" }} position="static">
        <Toolbar sx={{ display: "flex", justifyContent: "space-between" }}>
          <Typography variant="h6">Top 5 Stocks by VWAP</Typography>

          <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
            <Typography
              variant="body2"
              sx={{ display: { xs: "none", sm: "block" } }}
            >
              2-Minute Sliding Window · Simulated Stream
            </Typography>
          </Box>
        </Toolbar>
      </AppBar>
    </Box>
  );
};

export default Header;
