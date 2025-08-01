import {
  Paper,
  TableContainer,
  Table,
  TableRow,
  TableCell,
  TableHead,
  TableBody,
  tableCellClasses,
  styled,
} from "@mui/material";
import useVwapList from "../hooks/useVwapList";

const VwapList = () => {
  const StyledTableCell = styled(TableCell)(({ theme }) => ({
    [`&.${tableCellClasses.head}`]: {
      backgroundColor: theme.palette.common.black,
      color: theme.palette.common.white,
    },
    [`&.${tableCellClasses.body}`]: {
      fontSize: 14,
    },
  }));
  const vwapListHook = useVwapList();

  // <BarChart
  //   dataset={dataset}
  //   yAxis={[{ scaleType: 'band', dataKey: 'month' }]}
  //   series={[{ dataKey: 'seoul', label: 'Seoul rainfall', valueFormatter }]}
  //   layout="horizontal"
  //   grid={{ vertical: true }}
  //   {...chartSetting}
  // />
  return (
    <TableContainer sx={{ maxWidth: 300 }} component={Paper}>
      <Table>
        {/* header section / column names */}
        <TableHead>
          <TableRow>
            <StyledTableCell sx={{ fontSize: "14px" }}>Ticker</StyledTableCell>
            <StyledTableCell>VWAP</StyledTableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {vwapListHook.vwapList.map((value) => (
            <TableRow key={value.ticker}>
              <TableCell align="left">{value.ticker}</TableCell>
              <TableCell align="left">{value.vwap}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default VwapList;
