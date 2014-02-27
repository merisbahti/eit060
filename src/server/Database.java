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
      statement.close();
    } catch (SQLException e) {
      System.err.println("SQL Exception: " + e.getMessage());
    }
  }
  public boolean dropJournalsAndClearLog(){
	  try{
	    Statement statement = conn.createStatement();
	    statement.setQueryTimeout(30); 
	    statement.executeUpdate("drop table journal");
	    statement.close();
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
  public boolean insertJournal(Journal journal, String userID, String type) {
	if (type.equals("doctor")) {
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
			ResultSet rs = pstatement.getGeneratedKeys();
			rs.next();
			int id = rs.getInt(1);
			journal.setID(id);
			rs.close();
			pstatement.close();
			log.writeLog(Integer.toString(id), userID, "insert");
			return true;
		} catch (SQLException e) {
			System.err.println("SQL Exception: " + e.getMessage());
			log.writeLog("new ID", userID, "attempted insert, sql error");
			return false;
		}
	} else {
		log.writeLog("new ID", userID, "attempted insert, unauthorized");
		return false;
	}
  }


  /*
   * Get specific Journal
   * NullJournal / null if Journal doesn't exist, or access not granted.
   */
  public Journal getJournal(String journalID, String userID, String groupID, String type) {
	  int id = Integer.parseInt(journalID);
	  Journal journal = null;
    try { 
    	PreparedStatement pstatement;
      if(type.equals("government")){
    	  pstatement = conn.prepareStatement("select * from journal where ID=?");
    	  pstatement.setInt(1,id);
      }else if(type.equals("patient")){
    	  pstatement = conn.prepareStatement("select * from journal where ID=? and patient=?");
    	  pstatement.setInt(1,id);
    	  pstatement.setString(2,userID);
      }else if((type.equals("nurse")) || (type.equals("doctor"))){
    	  System.out.println("---read----doctor/nurse-----");
    	  pstatement = conn.prepareStatement("select * from journal where ID=? and ("+type+"=? or district=?)"); 
    	  pstatement.setInt(1,id);
    	  pstatement.setString(2, userID);
    	  pstatement.setString(3, groupID);
      }else{
    	  log.writeLog(Integer.toString(id), userID, "attempted read, unauthorized");
    	  return journal;
      }

      pstatement.setQueryTimeout(30);  
      ResultSet rs = pstatement.executeQuery();
      // @TODO: Handle empty journal
      // @TODO: Return a journal
      boolean exists = rs.next();
      if(exists){
    	  journal = new Journal(rs.getInt("id"), rs.getString("doctor"), rs.getString("nurse"), rs.getString("patient"), rs.getString("district"), rs.getString("content"));
    	  log.writeLog(Integer.toString(id), userID, "read");
      }else{
    	  journal = null;
    	  log.writeLog(Integer.toString(id), userID, "attempted read, journal does not exist");
      }
      rs.close();
      pstatement.close();
    } catch (SQLException e) {
      System.err.println("SQL Exception: " + e.getMessage());
      log.writeLog(Integer.toString(id), userID, "attempted read, unauthorized or not found");
    }
    return journal;
  }

  /*
   * Return boolean if sucess!
   */
  public boolean deleteJournal(String journalID, String userID, String type) {
	  if(type.equals("government")){
		  int id = Integer.parseInt(journalID);
		  try{
			  PreparedStatement pstatement = conn.prepareStatement("delete from journal where ID = ?");
			  pstatement.setInt(1, id);
			  pstatement.executeUpdate();
			  log.writeLog(Integer.toString(id), userID, "delete");
			  pstatement.close();
			  return true;
		  }catch(SQLException e){
			  System.err.println("SQL Exception: " + e.getMessage());
			  log.writeLog(Integer.toString(id), userID, "attempted delete, sql error");
			  return false;
		  }
	  }else{
		  log.writeLog(journalID, userID, "attempted delete, unauthorized");
		  return false;
	  }
  }

  public boolean updateJournal(String journalID, String content, String userID, String type){
	  if((type.equals("doctor")) || (type.equals("nurse"))){
		  int id = Integer.parseInt(journalID);
		  try{
			  PreparedStatement pstatement = conn.prepareStatement("select content from journal where id=? and "+type+"=?");
			  pstatement.setInt(1, id);
			  pstatement.setString(2, userID);
			  ResultSet rs = pstatement.executeQuery();
			  String dbContent = "";
			  boolean exists = rs.next();
			  if(exists){
				  dbContent = rs.getString("content");
				  String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
				  dbContent = String.format(dbContent + "%n %n" + date + "%n" + content);
				  pstatement = conn.prepareStatement("update journal set content = ? where ID = ?");
				  pstatement.setString(1,dbContent);
				  pstatement.setInt(2,id);
				  pstatement.executeUpdate();
				  log.writeLog(journalID, userID, "update");
				  rs.close();
				  pstatement.close();
				  return true;
			  }else{
				  rs.close();
				  pstatement.close();
				  log.writeLog(journalID, userID, "attempted update, unauthorized or not found");
				  return false;
			  }
		  }catch(SQLException e){
			  System.err.println("SQL Exception: " + e.getMessage());
			  log.writeLog(Integer.toString(id), userID, "attempted update, unauthorized or not found");
			  return false;
		  }
	  }else{
		  log.writeLog(journalID, userID, "attempted update, unauthorized");
		  return false;
	  }
  }
  /*
   * Return an implementation of List<Journal>
   */
  public ArrayList<Journal> getMyJournals(String userID, String groupID, String type) {
	  ArrayList<Journal> journals = new ArrayList<Journal>();
	  try{
		  PreparedStatement pstatement;
		  if((type.equals("doctor")) || (type.equals("nurse"))){
			  pstatement = conn.prepareStatement("select * from journal where district=? or "+type+"=?");
			  pstatement.setString(1, groupID);
			  pstatement.setString(2, userID);
		  }else if (type.equals("government")){
			  pstatement = conn.prepareStatement("select * from journal");
		  }else if(type.equals("patient")){
			  pstatement = conn.prepareStatement("select * from journal where patient=?");
			  pstatement.setString(1, userID);
		  }else{
			  log.writeLog("multiple IDs", userID, "attempted list, unauthorized");
			  return journals;
		  }
		  ResultSet rs = pstatement.executeQuery();
		  while(rs.next()){
			  Journal journal = new Journal(rs.getInt("id"), rs.getString("doctor"), rs.getString("nurse"), rs.getString("patient"), rs.getString("district"), rs.getString("content"));
			  journals.add(journal);
		  }
		  log.writeLog("multiple IDs", userID, "list");
		  rs.close();
		  pstatement.close();
	  }catch(SQLException e){
		  System.err.println("SQL Exception: " + e.getMessage());
		  log.writeLog("multiple IDs", userID, "attempted list, sql error");
	  }
	  return journals;
  }
  public boolean printLog(){
	  boolean printed = log.printLog();
	  return printed;
  }
}
