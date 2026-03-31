package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.xssf.usermodel.*;

public class ExcelUtility {

    String path;
    public FileInputStream fi;
    public FileOutputStream fo;
    public XSSFWorkbook wb;
    public XSSFSheet ws;
    public XSSFRow row;
    public XSSFCell cell;

    public ExcelUtility(String path)
    {
        this.path = path;
    }

    // Check file exists
    public boolean isFileExists()
    {
        File f = new File(path);
        return f.exists();
    }

    // Get row count
    public int getRowCount(String sheetName) throws Exception
    {
        fi = new FileInputStream(path);
        wb = new XSSFWorkbook(fi);
        ws = wb.getSheet(sheetName);

        if(ws==null)
            return 0;

        int rowcount = ws.getLastRowNum();

        wb.close();
        fi.close();

        return rowcount;
    }

    // Get cell count
    public int getCellCount(String sheetName,int rownum) throws Exception
    {
        fi = new FileInputStream(path);
        wb = new XSSFWorkbook(fi);
        ws = wb.getSheet(sheetName);

        row = ws.getRow(rownum);

        if(row==null)
            return 0;

        int cellcount = row.getLastCellNum();

        wb.close();
        fi.close();

        return cellcount;
    }

    // Get cell data
    public String getCellData(String sheetName,int rownum,int colnum) throws Exception
    {
        fi = new FileInputStream(path);
        wb = new XSSFWorkbook(fi);
        ws = wb.getSheet(sheetName);

        row = ws.getRow(rownum);
        cell = row.getCell(colnum);

        String data="";

        if(cell!=null)
        {
            data = cell.toString();
        }

        wb.close();
        fi.close();

        return data;
    }

    // Set cell data
    public void setCellData(String sheetName,int rownum,int colnum,String data) throws Exception
    {
        fi = new FileInputStream(path);
        wb = new XSSFWorkbook(fi);
        ws = wb.getSheet(sheetName);

        if(ws==null)
        {
            ws = wb.createSheet(sheetName);
        }

        row = ws.getRow(rownum);

        if(row==null)
        {
            row = ws.createRow(rownum);
        }

        cell = row.getCell(colnum);

        if(cell==null)
        {
            cell = row.createCell(colnum);
        }

        cell.setCellValue(data);

        fo = new FileOutputStream(path);
        wb.write(fo);

        wb.close();
        fi.close();
        fo.close();
    }
}