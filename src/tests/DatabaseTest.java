package tests;
import org.junit.*;
import server.*;
import model.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;


public class DatabaseTest {
  Database db = null;


    @Test
    public void testWillAlwaysFail() {
      /* Initera DB */
      /* Initera DB del 2
       * här måste vi ha någon classnotfoundexception för att jdbc kan strula.
       * men för att jag kådat så bra så kommer de aldri hända xD*/
      /* populate database */

      try {
        db = new Database();
      } catch (ClassNotFoundException e) {
        fail("JDBC drivern är inte laddad");
      }
      
      db.insertJournal("fittpenis", "juggalowhoress");
      db.insertJournal("fittpenis2", "juggalowhoress2");
      /* titta lite på database */
      db.getJournal("fittpenis");
      db.getJournal("fittpenis2");
      assertTrue(true);
    }
        
}
