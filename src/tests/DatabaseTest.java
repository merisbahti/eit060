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

    public void testInsertJournal() {
      /* populate database */
      boolean succeeded = db.insertJournal("Hanna", "Robin", "Meris", "Psyk","Massa info om patient");
      db.getJournal(1);
      assert(succeeded);
    }
    public void testUpdate(){
    	System.out.println("Journal before editing:");
    	db.getJournal(1);
    	boolean succeeded = db.updateJournal(1, "Hugo", "Berit", "Albin", "Right Wing", "Albin mar skit.");
    	System.out.println("Journal after editing:");
    	db.getJournal(1);
    	assert(succeeded);
    }
    public void testGetMyJournals() {
    	boolean succeeded = db.getMyJournals();
    	assert(succeeded);
    }
    public void testPrintLog() {
    	boolean succeeded = db.printLog();
    	assert(succeeded);
    }
    public void testDeleteJournal() {
    	boolean succeeded = db.deleteJournal(1);
    	assert(succeeded);
    }
    public void tearDown() {
    	boolean succeeded = db.dropJournalsAndClearLog();
    	assert(succeeded);
    }
}
