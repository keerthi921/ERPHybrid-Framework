package utilites;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelFileUtil 
{
Workbook wb;
//To read path of the excel constructor
public ExcelFileUtil(String Excelpath)throws Throwable
{
	FileInputStream fi = new FileInputStream(Excelpath);
	wb = WorkbookFactory.create(fi);
}
//method for counting rows in sheet
public int rowCount(String sheetName)
{
	return wb.getSheet(sheetName).getLastRowNum();
}
//method for reading cell data
public String getCellData(String sheetName,int row,int column)
{
	String data ="";
	if(wb.getSheet(sheetName).getRow(row).getCell(column).getCellType()==CellType.NUMERIC)
	{
		int celldata =(int) wb.getSheet(sheetName).getRow(row).getCell(column).getNumericCellValue();
		data = String.valueOf(celldata);		
	}
	else
	{
	    data = wb.getSheet(sheetName).getRow(row).getCell(column).getStringCellValue();	
	}
	return data;
}

public void setCellData(String sheetName,int row,int column,String status,String WriteExcel)throws Throwable
{
	//get sheet from wb
	Sheet ws = wb.getSheet(sheetName);
	//get row from sheet
	Row rowNum = ws.getRow(row);
	//create cell in row
	Cell cell = rowNum.createCell(column);
	//write status
	cell.setCellValue(status);
	if(status.equalsIgnoreCase("Pass"))
	{
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setColor(IndexedColors.GREEN.getIndex());
		font.setBold(true);
		style.setFont(font);
	}
	else if(status.equalsIgnoreCase("Fail"))
	{
	    CellStyle style = wb.createCellStyle();
	    Font font = wb.createFont();
		font.setColor(IndexedColors.RED.getIndex());
		font.setBold(true);
		style.setFont(font);
	}
	else if(status.equalsIgnoreCase("Blocked"))
	{
		 CellStyle style = wb.createCellStyle();
		    Font font = wb.createFont();
			font.setColor(IndexedColors.BLUE.getIndex());
			font.setBold(true);
			style.setFont(font);
			ws.getRow(row).getCell(column).setCellStyle(style);
	}
	FileOutputStream fo = new FileOutputStream(WriteExcel);
	wb.write(fo);
}
public static void main(String[] args) throws Throwable
	{
	ExcelFileUtil xl = new ExcelFileUtil("D:/Sample/Sample.xlsx");
	//count no of rows in sheet
	int rc =xl.rowCount("Emp");
	System.out.println(rc);
	for(int i=1;i<=rc;i++)
	{
		String fname = xl.getCellData("Emp", i, 0);
		String mname =xl.getCellData("Emp", i, 1);
		String lname =xl.getCellData("Emp", i, 2);
		String eid =xl.getCellData("Emp", i, 3);
		System.out.println(fname+"  "+mname+"  "+lname+"  "+eid);
		xl.setCellData("Emp", i, 4, "Fail", "D:/Sample/Results.xlsx");
	}
	
}
}

