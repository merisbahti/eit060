package server;

import java.sql.Connection;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import model.*;
import java.util.ArrayList;

public class Database {
  Connection conn; 
  LogDatabase log;

  /*
   * Init class and look for SQLITE-JDBC drive
   */
  public Database() throws ClassNotFoundException {
    Class.forName("org.sqlite.JDBC");
    conn = null;
    log = new LogDatabase();
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
      Statement statement = conn.createStatement();
      statement.setQueryTimeout(30); 
      statement.executeUpdate("create table if not exists journal(ID INTEGER PRIMARY KEY   AUTOINCREMENT, doctor string, nurse string, patient string, district string, content string)");
    } catch (SQLException e) {
      System.err.println("SQL Exception: " + e.getMessage());
    }
  }
  public boolean dropJournalsAndClearLog(){
	  try{
	    Statement statement = conn.createStatement();
	    statement.setQueryTimeout(30); 
	    statement.executeUpdate("drop table journal");
	    log.clearLog();
	    return true;
	  }catch(SQLException e){
		  System.err.println("SQL Exception: " + e.getMessage());
		  return false;
	  }
  }
  /*
   * Add journal
   * Return false if ID already exists.
   */
  public boolean insertJournal(Journal journal) {
    try { 
      String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
	  String dbContent = String.format(date + "%n" + journal.getContent());
      PreparedStatement pstatement = conn.prepareStatement("insert into journal (doctor, nurse, patient, district, content) values(?, ?, ?, ?, ?)");
      pstatement.setString(1, journal.getDoctor());
      pstatement.setString(2, journal.getNurse());
      pstatement.setString(3, journal.getPatient());
      pstatement.setString(4, journal.getDistrict());
      pstatement.setString(5, dbContent);
      pstatement.setQueryTimeout(30);  
      pstatement.executeUpdate();
      /*
       * TODO:
       * Find primary key for last insert
       * Update log
       */
      return true;
    } catch (SQLException e) {
      System.err.println("SQL Exception: " + e.getMessage());
      return false;
    }
  }


  /*
   * Get specific Journal
   * NullJournal / null if Journal doesn't exist, or access not granted.
   */
  public Journal getJournal(int id) {
	  Journal journal = null;
    try { 
      PreparedStatement pstatement = conn.prepareStatement("select * from journal where ID=? ");
      pstatement.setInt(1,id);
      pstatement.setQueryTimeout(30);  
      ResultSet rs = pstatement.executeQuery();
      // @TODO: Handle empty journal
      // @TODO: Return a journal
      while(rs.next())
      {
    	  journal = new Journal(rs.getInt("id"), rs.getString("doctor"), rs.getString("nurse"), rs.getString("patient"), rs.getString("district"), rs.getString("content"));
          System.out.println("id = " + rs.getInt("id"));
          System.out.println("doctor = " + rs.getString("doctor"));
          System.out.println("nurse = " + rs.getString("nurse"));
          System.out.println("patient = " + rs.getString("patient"));
          System.out.println("district = " + rs.getString("district"));
          System.out.println("content = " + rs.getString("content"));
      }
      log.writeLog(id, "Random Nurse", "read");
    } catch (SQLException e) {
      System.err.println("SQL Exception: " + e.getMessage());
    }
    return journal;
  }

  /*
   * Return boolean if sucess!
   */
  public boolean deleteJournal(int id) {
	  try{
		  PreparedStatement pstatement = conn.prepareStatement("delete from journal where ID = ?");
		  pstatement.setInt(1, id);
		  pstatement.executeUpdate();
		  log.writeLog(id, "Some Doctor", "delete");
		  return true;
	  }catch(SQLException e){
		  System.err.println("SQL Exception: " + e.getMessage());
		  return false;
	  }
  }
  public boolean updateJournal(int id, String content){
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
		  pstatement = conn.prepareStatement("update journal set content = ? where ID = ?");
		  pstatement.setString(1,dbContent);
		  pstatement.setInt(2,id);
		  pstatement.executeUpdate();
		  log.writeLog(id, "Some Doctor", "update");
		  return true;
	  }catch(SQLException e){
		  System.err.println("SQL Exception: " + e.getMessage());
		  return false;
	  }
  }
  /*
   * Return an implementation of List<Journal>
   */
  public ArrayList<Journal> getMyJournals() {
	  ArrayList<Journal> journals = new ArrayList<Journal>();
	  try{
		  PreparedStatement pstatement = conn.prepareStatement("select * from journal");
		  ResultSet rs = pstatement.executeQuery();
		  while(rs.next()){
			  Journal journal = new Journal(rs.getInt("id"), rs.getString("doctor"), rs.getString("nurse"), rs.getString("patient"), rs.getString("district"), rs.getString("content"));
			  journals.add(journal);
		      System.out.println("id = " + rs.getInt("id"));
		      System.out.println("doctor = " + rs.getString("doctor"));
		      System.out.println("nurse = " + rs.getString("nurse"));
		      System.out.println("patient = " + rs.getString("patient"));
		      System.out.println("district = " + rs.getString("district"));
		      System.out.println("content = " + rs.getString("content"));
		      log.writeLog(rs.getInt("id"), "Some Doctor", "read");
		  }
	  }catch(SQLException e){
		  System.err.println("SQL Exception: " + e.getMessage());

	  }
	  return journals;
  }
  public boolean printLog(){
	  boolean printed = log.printLog();
	  return printed;
  }
}
