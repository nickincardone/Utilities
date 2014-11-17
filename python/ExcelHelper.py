import xlsxwriter


class ExcelHelper(object):

    def __init__(self, wb_name):
        self.wb = xlsxwriter.Workbook(wb_name)

    def create_ws(self, ws_name):
        self.wb.add_worksheet(ws_name)

    def get_ws(self, ws_name):
        for worksheet in wb.worksheets():
            if worksheet.get_name() == ws_name:
                return worksheet
        return None

    def write_row(self, ws, row, col, arr):
        # arr is single dimensional
        # TODO make where you don't need row/col
        for cur_col, cell in enumerate(arr):
            ws.write(row, cur_col + col, cell)

    def write_csv(self, ws, row, col, arr):
        # assumes arr is 2x2
        # TODO make where you don't need row/col
        for cur_row, cells in enumerate(arr):
            self.write_row(ws, cur_row + row, col, cells)
