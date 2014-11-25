# TODO
import openpyxl


class ExcelToCSV(object):

    def __init__(self, wb_path):
        self.wb = load_workbook(wb_path, use_iterators=True)
        self.worksheets = self.wb.get_sheet_names()

    def export_ws(self, ws_name, num_col):
        # Takes the name of the worksheet and the number of columns to
        # tranverse for each row and returns a list of the values for each row
        # up to the column number
        ws = self.wb.get_sheet_by_name(ws_name)
        final_list = []

        for row in ws.iter_rows():
            row_list = []
            for i in range(num_col):
                row_list.append(row[i].value)
            final_list.append(row_list)

        return final_list
