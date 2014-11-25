import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellRange;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;


public class ExcelHelper {
	public static void main(String[] args) {

		test obj = new test();
		obj.run();

	}

	public void run() {
		HSSFWorkbook wb;
		try {
			
			//strings that can change
			String workbookName = "wb.xls";
			String namedRangeName = "HEADER";
			String sourceWSName = "Template";
			String destWSName = "Calendar";
			String outputFileName = "endxls";
			
			
			//read in Builder_Dms workbook and saves as wb 
			//and grabs the DMSTemplate worksheet and saves as sourceTemplate
			wb = (HSSFWorkbook) WorkbookFactory.create(new File(
					workbookName));
			HSSFSheet sourceTemplate = (HSSFSheet) wb.getSheet(sourceWSName);

			
			
			
			//Creates worksheet for subject
			//TODO make automatic
			HSSFWorkbook wbFinal = new HSSFWorkbook();
			HSSFSheet temporary = (HSSFSheet) wbFinal.createSheet(destWSName);

			// retrieve the named range
			int namedCellIdx = wb.getNameIndex(namedRangeName);
			Name aNamedCell = wb.getNameAt(namedCellIdx);

			// gets the named range and puts as cells reference object crefs
			AreaReference aref = new AreaReference(aNamedCell.getRefersToFormula());
			
			//uses my helper class Range
			//puts the cells of the named range in namedRange object
			Range namedRange = new Range(aref, wb);
			
			//uses my helper function to copy range into new worksheet
			test.copyPaste(namedRange,temporary,sourceTemplate,0,0, true);
			
			// writes the wb to new.xls			
			FileOutputStream out = new FileOutputStream(outputFileName);
			wbFinal.write(out);
			out.close();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	/**
	 * Helper function that copys and paste a range
	 * 
	 * @param range	the range of cells that are being copied
	 * @param destWS the destination worksheet where the range is being pasted to
	 * @param sourceWS the source worksheet where the range is being copied from
	 * @param firstRow the first row in the destination worksheet you want to paste to
	 * @param firstCol the first col in the destination worksheet you want to paste to
	 * @param isDestNull set true if you are pasting to a location that for sure has nothing in it
	 */
	private static void copyPaste(Range range, HSSFSheet destWS,HSSFSheet sourceWS, int firstRow, int firstCol, boolean isDestNull) {
		
		int curRowNum = 0;
		int curColNum = 0;
		Cell newCell, oldCell;
		
		//iterates over the rows
		for (int i=0;i<range.getNumRows();i++) {
			
			//resets the col count after going through each row
			curColNum = 0;
			
			//creates a row in the destination worksheet if it does not exist
			if (destWS.getRow(firstRow+curRowNum) == null) {
				destWS.createRow(firstRow+curRowNum);
			}
			
			//iterates over the coloums
			for (int j=0;j<range.getNumCols();j++){
				
				//checks to see if the source cell is null and skips it if it is
				//if not it copies and paste it
				if (range.getCell(i, j) != null) {
					
					//creates cell in destination worksheet if it does not exist
					if (destWS.getRow(curRowNum).getCell(curColNum) == null) {
						destWS.getRow(curRowNum).createCell(curColNum);
					}
					
					//sets copied cell and cell being copied into
					oldCell = range.getCell(i,j);
					newCell = destWS.getRow(curRowNum).getCell(curColNum);
					
					//copies the styling from the old cell to new cell
					HSSFCellStyle newCellStyle = destWS.getWorkbook().createCellStyle();
		            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
		            newCell.setCellStyle(newCellStyle);
					
					//sets destination cell height and width
					newCell.getColumnIndex();
					destWS.setColumnWidth(newCell.getColumnIndex(), sourceWS.getColumnWidth(oldCell.getColumnIndex()));
					
					// Set the cell data type
					newCell.setCellType(oldCell.getCellType());
					// copies data from old cell to new cell based on its type
					switch (oldCell.getCellType()) {
					case Cell.CELL_TYPE_BLANK:
						newCell.setCellValue(oldCell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						newCell.setCellValue(oldCell.getBooleanCellValue());
						break;
					case Cell.CELL_TYPE_ERROR:
						newCell.setCellErrorValue(oldCell.getErrorCellValue());
						break;
					case Cell.CELL_TYPE_FORMULA:
						newCell.setCellFormula(oldCell.getCellFormula());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						newCell.setCellValue(oldCell.getNumericCellValue());
						break;
					case Cell.CELL_TYPE_STRING:
						newCell.setCellValue(oldCell.getRichStringCellValue());
						break;
					}
					
				}
				//increment col num
				curColNum++;
			}
			//increment row num
			curRowNum++;
		}		
	}
	
	private class Range implements CellRange<Cell> {

		private int numRows;
		private int numCols;
		private Cell[][] cells;
		
		
		Range(AreaReference aref, HSSFWorkbook wb) {
			
			//creates an array of all the cells in the range
			CellReference[] crefs = aref.getAllReferencedCells();
			
			//assumes the range is contiguous and calculates the number of rows
			Sheet s = wb.getSheet(crefs[0].getSheetName());
			Row firstRow = s.getRow(crefs[0].getRow());
			Row lastRow = s.getRow(crefs[crefs.length - 1].getRow());
			numRows = lastRow.getRowNum() - firstRow.getRowNum() + 1;
			
			//creates a 2d matrix for all the cells
			cells = new Cell[numRows][crefs.length];

			int colNum = 0;
			int curRow = firstRow.getRowNum();
			
			
			for (int i = 0; i < crefs.length; i++) {
				
				//sets number of coloumns
				if (colNum > numCols) {
					numCols = colNum;
				}

				//grabs the current row
				Row r = s.getRow(crefs[i].getRow());

				//increments curRow number if on a new row and resets column count
				if (r.getRowNum() != curRow) {
					curRow++;
					colNum = 0;
				}

				//grabs the cell
				Cell c = r.getCell(crefs[i].getCol());

				//skips if the cell is null
				//if not null it is copied into the cells array
				if (c != null) {
					cells[r.getRowNum() - firstRow.getRowNum()][colNum] = c;	
				}
				//increments coloumn number
				colNum++;
				
			}
			
		}
		@Override
		public Cell getCell(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return cells[arg0][arg1];
		}

		@Override
		public Cell[][] getCells() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Cell[] getFlattenedCells() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getHeight() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String getReferenceText() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Cell getTopLeftCell() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getWidth() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Iterator<Cell> iterator() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return 0;
		}
		public int getNumRows() {
			return numRows;
		}
		public void setNumRows(int numRows) {
			this.numRows = numRows;
		}
		public int getNumCols() {
			return numCols;
		}
		public void setNumCols(int numCols) {
			this.numCols = numCols;
		}

	}

}


