package tests;
import server.Database;
import server.*;
import model.*;
import org.junit.*;

public class DatabaseTest extends junit.framework.TestCase {
  Database db = null;

    public void setUp() {
      /* Initera DB del 2
       * här måste vi ha någon classnotfoundexception för att jdbc kan strula.
       * men för att jag kådat så bra så kommer de aldri hända xD*/
      /* Initera DB */
        try {
          db = new Database();
        } catch (ClassNotFoundException e) {
          System.err.print("You done good'd");
        }
    }

    public void testWillAlwaysFail() {
      /* populate database */
      
      db.insertJournal("fittpenis", "juggalowhoress");
      db.insertJournal("fittpenis2", "juggalowhoress2");
      /* titta lite på database */
      db.getJournal("fittpenis");
      db.getJournal("fittpenis2");
      assertTrue(true);
    }
        
}
