package server;

import java.sql.Connection;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class LogDatabase {
	Connection conn;
	  /*
	   * Init class and look for SQLITE-JDBC drive
	   */
	  public LogDatabase() throws ClassNotFoundException {
	    Class.forName("org.sqlite.JDBC");
	    conn = null;
	    connect();
	    init();
	  }
	  /*
	   * Try to connect to sqlite
	   */
	  private void connect() {
	    try {
	      conn = DriverManager.getConnection("jdbc:sqlite:logdatabase.db");
	    } catch(SQLException e){
	      System.err.println(e.getMessage());
	    }
	  }
	  public void clearLog(){
		  try{
			  PreparedStatement pstatement = conn.prepareStatement("delete from log");
			  pstatement.setQueryTimeout(30);
			  pstatement.executeUpdate();
		  }catch(SQLException e){
			  System.err.println("SQL Exception: " + e.getMessage());
		  }
	  }
	  /*
	   * Remove old Journals and create tables for new one
	   */
	  private void init() {
	    try { 
	    	//Statement statement = conn.createStatement();
	    	//String sql = "drop table journal";
	    	
	      Statement statement = conn.createStatement();
	      statement.setQueryTimeout(30); 
	      //statement.executeUpdate(sql);
	      //clearLog();
	      statement.executeUpdate("create table if not exists log(id string, user string, query string, date string)");
	    } catch (SQLException e) {
	      System.err.println("SQL Exception: " + e.getMessage());
	    }
	  }
	  public boolean writeLog(String id, String user, String query){
		  try{
			  String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
			  PreparedStatement pstatement = conn.prepareStatement("insert into log (id, user, query, date) values(?, ?, ?, ?)");
			  pstatement.setString(1,id);
			  pstatement.setString(2, user);
			  pstatement.setString(3, query);
			  pstatement.setString(4, date);
			  pstatement.setQueryTimeout(30);
			  pstatement.executeUpdate();
			  return true;
		  }catch(SQLException e){
			  System.err.println("SQL Exception: " + e.getMessage());
			  return false;
		  }
	  }
	  public boolean printLog(){
		  try{
			  System.out.println("LOG:");
			  PreparedStatement pstatement = conn.prepareStatement("select * from log");
			  ResultSet rs = pstatement.executeQuery();
			  while(rs.next()){
				  System.out.println("ID: "+ rs.getString("id") + "  User: " + rs.getString("user") + "  Query: " + rs.getString("query") + "  Date: " + rs.getString("date"));
			  }
			  return true;
		  }catch(SQLException e){
			  System.err.println("SQL Exception: " + e.getMessage());
			  return false;
		  }
	  }

	  public String printLog(String groupID){
      if (!groupID.equals("admin"))
        return "empty";
		  try{
        String res = "";
			  PreparedStatement pstatement = conn.prepareStatement("select * from log");
			  ResultSet rs = pstatement.executeQuery();
			  while(rs.next()){
				 res += ("ID: "+ rs.getString("id") + "  User: " + rs.getString("user") + "  Query: " + rs.getString("query") + "  Date: " + rs.getString("date"))+"\n";
			  }
			  return res;
		  }catch(SQLException e){
			  System.err.println("SQL Exception: " + e.getMessage());
			  return "error";
		  }
	  }
}
