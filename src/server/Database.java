package server;

import java.sql.Connection;
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
      Statement statement = conn.createStatement();
      statement.setQueryTimeout(30);  
      statement.executeUpdate("drop table if exists journal");
      statement.executeUpdate("create table journal(id string, content string)");
    } catch (SQLException e) {
      System.err.println("SQL Exception: " + e.getMessage());
    }
  }

  /*
   * Add journal
   * Return false if ID already exists.
   */
  public void insertJournal(String id, String content) {
    try { 
      PreparedStatement pstatement = conn.prepareStatement("insert into journal values(?, ?)");
      pstatement.setString(1,id);
      pstatement.setString(2,content);
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
  public void getJournal(String id) {
    try { 
      PreparedStatement pstatement = conn.prepareStatement("select * from journal where id=? ");
      pstatement.setString(1,id);
      pstatement.setQueryTimeout(30);  
      pstatement.executeQuery();
      ResultSet rs = pstatement.executeQuery();
      // @TODO: Handle empty journal
      // @TODO: Return a journal
      while(rs.next())
      {
        /*
        System.out.println("id = " + rs.getString("id"));
        System.out.println("content = " + rs.getString("content"));
        */
      }
    } catch (SQLException e) {
      System.err.println("SQL Exception: " + e.getMessage());
    }
  }

  /*
   * Return boolean if sucess!
   */
  public void deleteJournal(String id) {}

  /*
   * Return an implementation of List<Journal>
   */
  public void getMyJournals(String sslFingerPrint) {}


}
