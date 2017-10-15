package org.alexu.engfaculty;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.alexu.engfaculty.database.DBStream;


import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class ExcelFileReader {
	
	private File inputWorkbook;
	
	
	public void setInputFile(File inputFile) {
	    this.inputWorkbook = inputFile;
	}
	
	public String importData(String date/* dd-mm-yyyy*/, String paychecktype, String description) throws IOException  {
	    
		System.err.println("ENTERRRRRRRRRRRRRRIIINNNNNGGGGGG"+inputWorkbook.getName());
		//File out = new File("E:\\outfromprog.txt");
	    //Writer outw = new BufferedWriter(new OutputStreamWriter(
	    //	    new FileOutputStream(out), "UTF-8"));
	    String result= "";
	    Workbook w;
	    WorkbookSettings workbookSettings = new WorkbookSettings();
	    workbookSettings.setEncoding( "UTF8" );
	    workbookSettings.setLocale(new Locale("ar", "AR"));
	    try {
		    w = Workbook.getWorkbook( inputWorkbook, workbookSettings );
		    // Get the first sheet
		    Sheet sheet = w.getSheet(0);
		    // Loop over first 10 column and lines
		
		    for (int i = 1; i < sheet.getRows(); i++) {
		    //	الرقم القومى	نوع المدفوعه	القطاع	الإدارة	رقم الموظف بجهته الأصلية	الاسم	المرتب
				try{
			    	String empid = sheet.getCell(0, i).getContents();
					//String type = sheet.getCell(1, i).getContents();
					
					String pos = sheet.getCell(3, i).getContents();
					String numindept = sheet.getCell(4, i).getContents();
					String empname = sheet.getCell(5, i).getContents();
			    	double paycheck = Double.parseDouble(sheet.getCell(6, i).getContents());
			    	//dump in database rows if not zero 
			    	if (paycheck >0.01){
			    		DBStream udb = new DBStream();
			    		udb.updateDatabasewithExcelRows(empid, paychecktype, numindept, pos, empname, paycheck, date, inputWorkbook.getName(), description);
			    	}
			    	
				}catch (Exception e){
					e.printStackTrace();
					result = "يوجد خطأ فى الملف المدخل";
				}
		    }
		    result += "يتم ادخال الملف بنجاح";
		  //  outw.close();
	    } catch (BiffException e) {
	      e.printStackTrace();
	      return "حدث خطأ: برجاء التأكد من جودة الملف";
	    }
	    return result;
	  }
	
public String updateEmployees(File employees) throws IOException  {
	    
	    String result= "";
	    Workbook w;
	    WorkbookSettings workbookSettings = new WorkbookSettings();
	    workbookSettings.setEncoding( "UTF8" );
	    workbookSettings.setLocale(new Locale("ar", "AR"));
	    try {
		    w = Workbook.getWorkbook( inputWorkbook, workbookSettings );
		    // Get the first sheet
		    Sheet sheet = w.getSheet(0);
		    // Loop over first 10 column and lines
		
		    for (int i = 1; i < sheet.getRows(); i++) {
		
				try{
			    	String empname = sheet.getCell(0, i).getContents();
					String dept = sheet.getCell(1, i).getContents();
					
					String title = sheet.getCell(3, i).getContents();
					
					DBStream udb = new DBStream();
					udb.updateDatabasewithEmployees(empname.trim(), dept.trim(),title.trim());
		    	//dump in database rows
				}catch (Exception e){
					e.printStackTrace();
					result = "يوجد خطأ فى الملف المدخل";
				}
		    }
		    result += "يتم ادخال الملف بنجاح";
		  //  outw.close();
	    } catch (BiffException e) {
	      e.printStackTrace();
	      return "حدث خطأ: برجاء التأكد من جودة الملف";
	    }
	    return result;
	  }
public String prepareReports(File employees, String from, String to) throws IOException  {
    
    String result= "";
    Workbook w;
    WorkbookSettings workbookSettings = new WorkbookSettings();
    workbookSettings.setEncoding( "UTF8" );
    workbookSettings.setLocale(new Locale("ar", "AR"));
    try {
	    w = Workbook.getWorkbook( inputWorkbook, workbookSettings );
	    // Get the first sheet
	    Sheet sheet = w.getSheet(0);
	        for (int i = 1; i < sheet.getRows(); i++) {
	    
			try{
		    	String empname = sheet.getCell(0, i).getContents();
				String dept = sheet.getCell(1, i).getContents();
				
				String title = sheet.getCell(2, i).getContents();
				JasperReportsGenerator jrg = new JasperReportsGenerator();
				jrg.generateReportwithName(empname.trim(), from.trim(), to.trim(), dept.trim(), title.trim());
				result = "يتم ادخال الملف بنجاح";
			}catch (Exception e){
				e.printStackTrace();
				result = "يوجد خطأ فى الملف المدخل";
			}
	    }
	    
	  //  outw.close();
    } catch (BiffException e) {
      e.printStackTrace();
      return "حدث خطأ: برجاء التأكد من جودة الملف";
    }
    return result;
  }


	
		public static void main(String[] args) throws IOException {
		  ExcelFileReader test = new ExcelFileReader();
		  File file = new File("C:\\Users\\Samia\\workspace\\Pay-engx_rehab-20150504-124450.xls");
		  test.setInputFile(file);
		  test.importData("09-09-2015", "مرتب","september");
		 
	  }
	
	

}
