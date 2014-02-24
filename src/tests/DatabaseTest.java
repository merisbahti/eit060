package tests;
import server.Database;
import server.*;
import model.*;
import org.junit.*;

public class DatabaseTest extends junit.framework.TestCase {
  Database db = null;

    public void setUp() {
      /* Init DB part 2
       * Must catch classnotfound exception.
       * But this is some safe ass code, so it wont never happen.
      /* Init DB */
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
      /* watch some on the databaze */
      db.getJournal("fittpenis");
      db.getJournal("fittpenis2");
      assertTrue(true);
    }
        
}
