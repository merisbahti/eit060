package tests;
import server.Database;

public class DatabaseTest extends junit.framework.TestCase {

    public void testNothing() {
    }
    
    public void testWillAlwaysFail() {
      /* Initera DB */
      Database db = null;

      /* Initera DB del 2
       * här måste vi ha någon classnotfoundexception för att jdbc kan strula.
       * men för att jag kådat så bra så kommer de aldri hända xD*/
      try {
        db = new Database();
      } catch (ClassNotFoundException e) {
        System.err.print("You done good'd");
      }

      /* populate database */
      db.insertJournal("fittpenis", "juggalowhoress");
      db.insertJournal("fittpenis2", "juggalowhoress2");

      /* titta lite på database */
      db.getJournal("fittpenis");
      db.getJournal("fittpenis2");
      fail("An error message");
    }
    
}
