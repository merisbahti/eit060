package server;

import java.sql.Connection;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class Database {
  Connection conn; 

  /*
   * Init class and look for SQLITE-JDBC drive
   */
  public Database() throws ClassNotFoundException {
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
      conn = DriverManager.getConnection("jdbc:sqlite:database.db");
    } catch(SQLException e){
      System.err.println(e.getMessage());
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
      statement.executeUpdate("create table if not exists journal(ID INTEGER PRIMARY KEY   AUTOINCREMENT, doctor string, nurse string, patient string, district string, content string)");
    } catch (SQLException e) {
      System.err.println("SQL Exception: " + e.getMessage());
    }
  }

  /*
   * Add journal
   * Return false if ID already exists.
   */
  public void insertJournal(String doctor, String nurse, String patient, String district, String content) {
    try { 
      String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
	  String dbContent = String.format(date + "%n" + content);
      PreparedStatement pstatement = conn.prepareStatement("insert into journal (doctor, nurse, patient, district, content) values(?, ?, ?, ?, ?)");
      pstatement.setString(1, doctor);
      pstatement.setString(2, nurse);
      pstatement.setString(3, patient);
      pstatement.setString(4, district);
      pstatement.setString(5, dbContent);
      pstatement.setQueryTimeout(30);  
      pstatement.executeUpdate();
    } catch (SQLException e) {
      System.err.println("SQL Exception: " + e.getMessage());
    }
  }


  /*
   * Get specific Journal
   * NullJournal / null if Journal doesn't exist, or access not granted.
   */
  public void getJournal(int id) {
    try { 
      PreparedStatement pstatement = conn.prepareStatement("select * from journal where ID=? ");
      pstatement.setInt(1,id);
      pstatement.setQueryTimeout(30);  
      pstatement.executeQuery();
      ResultSet rs = pstatement.executeQuery();
      // @TODO: Handle empty journal
      // @TODO: Return a journal
      while(rs.next())
      {
          System.out.println("id = " + rs.getInt("id"));
          System.out.println("doctor = " + rs.getString("doctor"));
          System.out.println("nurse = " + rs.getString("nurse"));
          System.out.println("patient = " + rs.getString("patient"));
          System.out.println("district = " + rs.getString("district"));
          System.out.println("content = " + rs.getString("content"));
      }
    } catch (SQLException e) {
      System.err.println("SQL Exception: " + e.getMessage());
    }
  }

  /*
   * Return boolean if sucess!
   */
  public boolean deleteJournal(int id) {
	  try{
		  PreparedStatement pstatement = conn.prepareStatement("delete from journal where ID = ?");
		  pstatement.setInt(1, id);
		  pstatement.executeUpdate();
		  return true;
	  }catch(SQLException e){
		  System.err.println("SQL Exception: " + e.getMessage());
		  return false;
	  }
  }
  public boolean updateJournal(int id, String doctor, String nurse, String patient, String district, String content){
	  try{
		  PreparedStatement pstatement = conn.prepareStatement("select content from journal where id=?");
		  pstatement.setInt(1, id);
		  ResultSet rs = pstatement.executeQuery();
		  String dbContent = "";
		  while(rs.next()){
			  dbContent = rs.getString("content");
		  }
		  String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		  dbContent = String.format(dbContent + "%n %n" + date + "%n" + content);
		  pstatement = conn.prepareStatement("update journal set doctor = ?, nurse = ?, patient = ?, district = ?, content = ? where ID = ?");
		  pstatement.setString(1,doctor);
		  pstatement.setString(2,nurse);
		  pstatement.setString(3,patient);
		  pstatement.setString(4,district);
		  pstatement.setString(5,dbContent);
		  pstatement.setInt(6,id);
		  pstatement.executeUpdate();
		  return true;
	  }catch(SQLException e){
		  System.err.println("SQL Exception: " + e.getMessage());
		  return false;
	  }
  }
  /*
   * Return an implementation of List<Journal>
   */
  public void getMyJournals() {
	  try{
		  PreparedStatement pstatement = conn.prepareStatement("select * from journal");
		  ResultSet rs = pstatement.executeQuery();
		  while(rs.next()){
		      System.out.println("id = " + rs.getInt("id"));
		      System.out.println("doctor = " + rs.getString("doctor"));
		      System.out.println("nurse = " + rs.getString("nurse"));
		      System.out.println("patient = " + rs.getString("patient"));
		      System.out.println("district = " + rs.getString("district"));
		      System.out.println("content = " + rs.getString("content"));
		  }
	  }catch(SQLException e){
		  System.err.println("SQL Exception: " + e.getMessage());
	  }
  }
}
