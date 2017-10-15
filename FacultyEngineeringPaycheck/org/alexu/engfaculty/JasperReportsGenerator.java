package org.alexu.engfaculty;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import org.alexu.engfaculty.database.JDBCMySQLConnection;

public class JasperReportsGenerator {
	Connection connection = null;
    
	public JasperReportsGenerator(){
		 
		}

	public void generateReportwithName(String name, String from, String to, String dept, String title ){
		try {
	        connection = JDBCMySQLConnection.getConnection();
	        
	        /* JasperReport is the object
	        that holds our compiled jrxml file */
	        String report = System.getProperty("user.dir")+"\\reporttemp.jrxml";
	        JasperReport jasperReport;


	        /* JasperPrint is the object contains
	        report after result filling process */
	        JasperPrint jasperPrint;
	        
		     // jasperParameter is a Hashmap contains the parameters
		     // passed from application to the jrxml layout
		     HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		     jasperParameter.put("empname", name.trim());
		     jasperParameter.put("from", from);
		     jasperParameter.put("to", to);
		     jasperParameter.put("dept", dept);
		     jasperParameter.put("position", title);
		     
	
		     // jrxml compiling process
		     jasperReport = JasperCompileManager.compileReport(report);
	
		     // filling report with data from data source
	
		     jasperPrint = JasperFillManager.fillReport(jasperReport,jasperParameter, connection); 
	      
		     
			 // exporting process
			 // 1- export to PDF
		     //JasperViewer.viewReport(jasperPrint);
		     
		    	new File(System.getProperty("user.dir")+"\\reports\\"+dept+"\\"+from+"الى"+to).mkdirs();
		      JasperExportManager.exportReportToPdfFile(jasperPrint, System.getProperty("user.dir")+"\\reports\\"+dept+"\\"+from+"الى"+to+"\\PAYCHECK"+name+".pdf");
			 

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        if (connection != null) {
	            try {
	                connection.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
	public String generateReportwithName(String name, String from, String to, String dept ){
		String result="";
		try {
	        connection = JDBCMySQLConnection.getConnection();
	        
	        /* JasperReport is the object
	        that holds our compiled jrxml file */
	        String report = System.getProperty("user.dir")+"\\reporttemp.jrxml";
	        JasperReport jasperReport;


	        /* JasperPrint is the object contains
	        report after result filling process */
	        JasperPrint jasperPrint;
	        
		     // jasperParameter is a Hashmap contains the parameters
		     // passed from application to the jrxml layout
		     HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		     jasperParameter.put("empname", name.trim());
		     jasperParameter.put("from", from);
		     jasperParameter.put("to", to);
		     jasperParameter.put("dept", dept);
		 	
		     // jrxml compiling process
		     jasperReport = JasperCompileManager.compileReport(report);
		     
		     // filling report with data from data source
	
		     jasperPrint = JasperFillManager.fillReport(jasperReport,jasperParameter, connection); 
	      
		     // exporting process
			 // 1- export to PDF
		     //JasperViewer.viewReport(jasperPrint);
			 JasperExportManager.exportReportToPdfFile(jasperPrint, System.getProperty("user.dir")+"\\"+name+".pdf");
			 JasperExportManager.exportReportToPdfFile(jasperPrint, System.getProperty("user.dir")+"\\filetobeshown.pdf");
			 
			 System.out.println("rundll32 url.dll,FileProtocolHandler "+System.getProperty("user.dir")+"\\filetobeshown.pdf");
			 Process p = Runtime
						   .getRuntime()
						   .exec("rundll32 url.dll,FileProtocolHandler "+System.getProperty("user.dir")+"\\filetobeshown.pdf");
						p.waitFor();
			 
			 result ="success";
	    } catch (Exception e) {
	        result="failure\n"+e.getMessage();
	    } finally {
	        if (connection != null) {
	            try {
	                connection.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        return result;
	    }
	}
	
	 public static void main(String[] args) throws IOException {
		  JasperReportsGenerator jrg= new JasperReportsGenerator();
		  jrg.generateReportwithName("كريم حسن محمد يوسف","11-09-2015","13-09-2015","i,fh");
	  }

	
	 //TODO this will be used for a better design
	public String generateReportwithID(long iD, String from, String to,
			String dept, Double sum) {
		JasperReportsGenerator jrg= new JasperReportsGenerator();
		 return null;
	}

	public String generateReportwithName(long id,String name, String from, String to, String dept, double sum) {
		String result="";
		try {
	        connection = JDBCMySQLConnection.getConnection();
	        
	        /* JasperReport is the object
	        that holds our compiled jrxml file */
	        String report = System.getProperty("user.dir")+"\\reporttemp.jrxml";
	        JasperReport jasperReport;


	        /* JasperPrint is the object contains
	        report after result filling process */
	        JasperPrint jasperPrint;
	        
		     // jasperParameter is a Hashmap contains the parameters
		     // passed from application to the jrxml layout
		     HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		     jasperParameter.put("empnid", id);
		     jasperParameter.put("empname", name.trim());
		     jasperParameter.put("from", from);
		     jasperParameter.put("to", to);
		     jasperParameter.put("dept", dept);
		     jasperParameter.put("sum", sum);
		 	
		     // jrxml compiling process
		     jasperReport = JasperCompileManager.compileReport(report);
		     
		     // filling report with data from data source
	
		     jasperPrint = JasperFillManager.fillReport(jasperReport,jasperParameter, connection); 
	      
		     // exporting process
			 // 1- export to PDF
		     //JasperViewer.viewReport(jasperPrint);
			 JasperExportManager.exportReportToPdfFile(jasperPrint, System.getProperty("user.dir")+"\\"+name+".pdf");
			 JasperExportManager.exportReportToPdfFile(jasperPrint, System.getProperty("user.dir")+"\\filetobeshown.pdf");
			 
			 System.out.println("rundll32 url.dll,FileProtocolHandler "+System.getProperty("user.dir")+"\\filetobeshown.pdf");
			 Process p = Runtime
						   .getRuntime()
						   .exec("rundll32 url.dll,FileProtocolHandler "+System.getProperty("user.dir")+"\\filetobeshown.pdf");
						p.waitFor();
			 
			 result ="success";
	    } catch (Exception e) {
	        result="failure\n"+e.getMessage();
	    } finally {
	        if (connection != null) {
	            try {
	                connection.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        return result;
	    }
	}
	
	

}
