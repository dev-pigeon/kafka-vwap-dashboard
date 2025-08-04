import {
  AppBar,
  Toolbar,
  Typography,
  Box,
  Chip,
  Stack,
  Tooltip,
} from "@mui/material";
import InfoOutlineIcon from "@mui/icons-material/InfoOutline";

interface HeaderProps {
  lastUpdated: string | null;
}

const Header = ({ lastUpdated }: HeaderProps) => {
  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar sx={{ backgroundColor: "#222233" }} position="static">
        <Toolbar sx={{ display: "flex", justifyContent: "space-between" }}>
          <Stack
            display={"flex"}
            alignItems={"center"}
            direction={"row"}
            spacing={2}
          >
            <Typography variant="h6">Top 5 Stocks by VWAP</Typography>
            <Tooltip
              slotProps={{
                tooltip: {
                  sx: {
                    fontSize: "1em",
                  },
                },
              }}
              arrow
              placement="bottom"
              title="This app displays the top five stocks based on volume weighted average price over a 2-minute sliding window."
            >
              <InfoOutlineIcon />
            </Tooltip>
          </Stack>

          <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
            <Typography
              variant="body2"
              sx={{ display: { xs: "none", sm: "block" } }}
            >
              2-Minute Sliding Window · Simulated Stream
            </Typography>
            {lastUpdated && (
              <Chip
                label={`Last Updated: ${lastUpdated}`}
                color="success"
                size="small"
              />
            )}
          </Box>
        </Toolbar>
      </AppBar>
    </Box>
  );
};

export default Header;
