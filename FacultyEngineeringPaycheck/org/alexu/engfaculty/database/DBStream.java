package org.alexu.engfaculty.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.alexu.engfaculty.Employee;

import com.sun.jmx.snmp.Timestamp;




public class DBStream {
	
	ResultSet rs = null;
    Connection connection = null;
    Statement statement = null;
    
    public DBStream(){
		
	    
	}
	private String getTitlefromPos(String pos){
		String title = "";
		if (pos.equals("أستاذ")) return "أ.د.";
		if (pos.equals("استاذ متفرغ : غير متفرغ")) return "أ.د.";
		if (pos.equals("أستاذ مساعد")) return "د.";
		if (pos.equals("مدرس")) return "د.";
		if (pos.equals("مدرس مساعد ، معيد")) return "م.";
		if (pos.equals("موظف دائم")) return "أ";
		if (pos.equals("موظف مؤقت")) return "أ";
		if (pos.equals("عامل دائم")) return "";
		return title; 		
		
	}
	public void updateDatabasewithExcelRows (String empid, String type, String numindept , String pos,String empname, double paycheck, String date , String filename, String desc){
		
		String Posquery = "INSERT INTO POSITIONS (POSNAME) VALUES (\""+pos+"\") ON DUPLICATE KEY UPDATE TITLE= \""+getTitlefromPos(pos)+"\";";	
		String Typequery = "INSERT INTO PAYCHECKTYPE (TYPENAME,TYPEID) VALUES (\""+type+"\",0) ON DUPLICATE KEY UPDATE TYPEID= TYPEID ;";
		String EMPquery = "INSERT INTO EMPLOYEE (EMPNAME ,	EMPNID,	EMPPOSITION ,EMPNUMINPLACE) SELECT \""+empname+"\","+empid+",POSID,"+numindept+" FROM POSITIONS WHERE POSNAME=\""+pos+"\"  ON DUPLICATE KEY UPDATE EMPPOSITION = (select POSID from POSITIONS where POSNAME =\""+pos+"\" );" ;  
		String paycheckquery = "INSERT INTO EMPLOYEE_PAYCHECK (EMPID, PAYTYPE, DT, SERIAL, FILENAME, DESCRIPTION ,AMT) SELECT EMPNID, TYPEID, STR_TO_DATE(\""+date+"\",\"%d-%m-%Y\"),0,\""+filename+"\",\""+desc+"\","+paycheck+" FROM EMPLOYEE, PAYCHECKTYPE WHERE EMPNAME LIKE \"%"+empname+"%\" AND TYPENAME LIKE \"%"+type+"%\";"; 
        System.out.println(empname);
		try {
            connection = JDBCMySQLConnection.getConnection();
            statement = connection.createStatement();
            System.out.println(new Timestamp(new java.util.Date().getTime()));
            statement.executeUpdate(Posquery);
            statement.executeUpdate(Typequery);
            statement.executeUpdate(EMPquery);
            statement.executeUpdate(paycheckquery);
            //System.out.println(new Timestamp(new java.util.Date().getTime()));
        } catch (SQLException e) {
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
	public void updateDatabasewithEmployees (String empname, String dept, String title){
		String EMPquery = "INSERT INTO EMPLOYEE (EMPNAME ,EMPNID,DEPT,TITLE) SELECT \""+empname+"\",EMPNID,\""+dept+"\",\""+title+"\" FROM EMPNAME LIKE \"%"+empname+"%\" ON DUPLICATE KEY UPDATE TITLE = \""+title+"\";" ;  
		
		try {
            connection = JDBCMySQLConnection.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(EMPquery);
        } catch (SQLException e) {
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
//String [] depts= new String [] {"قسم الرياضيات والفيزياء الهندسيه","قسم الهندسة الانشائية","قسم الهندسة الكهربية"," قسم هند سة الرى والهيدروليكا"," قسم هندسة المواصلات","قسم الهندسة المعمارية","قسم الهندسة الميكانيكية","قسم هندسة الأنتاج","قسم الهندسة البحرية وعمارة السفن","قسم الهندسة الكيميائية","قسم هندسة الغزل والنسيج"," قسم الهندسة النووية والاشعاعية","قسم هندسة الحاسب والنظم"};
	
	public ArrayList<String> retrieveEmployeeNames (){
		ArrayList<String> employees = new ArrayList<String>();
		String eMPquery = "SELECT EMPNAME FROM EMPLOYEE;" ;  
		try{
			connection = JDBCMySQLConnection.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(eMPquery);
            int i = 0;
            while (rs.next()) {
            	employees.add(i,(rs.getString("EMPNAME")));
            	i++;
            }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			return employees;
		}
		
	}
	// to be used for better design
	// TODO better for design
	public ArrayList<Employee> retrieveEmployees(){
		ArrayList<Employee> employees = new ArrayList<Employee>();
		String eMPquery = "SELECT EMPNID, EMPNAME, DEPT FROM EMPLOYEE;" ;  
		try{
			connection = JDBCMySQLConnection.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(eMPquery);
            int i = 0;
            while (rs.next()) {
            	
            	employees.add(i,new Employee(rs.getLong("EMPNID"),rs.getString("EMPNAME"),rs.getString("DEPT")));
            	i++;
            }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			return employees;
		}
		
	}
	
	public ArrayList<String> retrieveFileNames (){
		ArrayList<String> files = new ArrayList<String>();
		String filequery = "SELECT DISTINCT FILENAME FROM EMPLOYEE_PAYCHECK;" ;  
		try{
			connection = JDBCMySQLConnection.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(filequery);
            int i = 0;
            while (rs.next()) {
            	files.add(i,(rs.getString("FILENAME")));
            	i++;
            }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			return files;
		}
		
	}
	public void deleteRowsofFile (String filename){
		String filedelquery = "DELETE FROM EMPLOYEE_PAYCHECK WHERE FILENAME LIKE \""+filename.trim()+"\";" ;  
		try{
			connection = JDBCMySQLConnection.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(filedelquery);
            
		}catch(Exception e){
			e.printStackTrace();
		}finally{

		}
		
	}
	public long retrieveEmployeeID(String empName) {
		// done
		long id = 0 ;
		String IdQuery = "SELECT EMPNID,EMPNAME FROM EMPLOYEE where EMPNAME = '"+ empName +"' ;" ;  
		System.out.println(IdQuery);
		try{
			connection = JDBCMySQLConnection.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(IdQuery);
            int i = 0;
            while (rs.next()) {
            	System.out.println(rs.getString("EMPNAME"));
            	// since mysql matches all strings containing the name, this is to overcome the son dad problem
            	if (empName.trim().equals(rs.getString("EMPNAME").trim())){
            		System.out.println(rs.getLong("EMPNID"));
            		id = rs.getLong("EMPNID");
            	}
            	
            }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			return id;
		}
	
	}
	public String retrieveEmployeeDept(long iD) {
		String dept = "" ;
		String deptQuery = "SELECT DEPT FROM EMPLOYEE where EMPNID = "+ iD +" ;" ;  
		try{
			connection = JDBCMySQLConnection.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(deptQuery);
            while (rs.next()) {	
            	dept = rs.getString("DEPT");
            }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			return dept;
			
		}
	
	}
	public double retrieveSum(long iD, String from, String to) {
		// done
		double sum = 0 ;
		String sumQuery = "SELECT sum(amt)as A FROM EMPLOYEE_PAYCHECK WHERE EMPID = "+ iD +" AND  DT BETWEEN STR_TO_DATE('" +from+ "','%d-%m-%Y') AND STR_TO_DATE('"+to+"','%d-%m-%Y');";
		System.out.println(sumQuery);
		try{
			connection = JDBCMySQLConnection.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sumQuery);
            int i = 0;
            while (rs.next()) {
            	sum = rs.getDouble("A");
            	System.out.println(sum);
            	
            }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			return sum;
		}
	}
}
	


