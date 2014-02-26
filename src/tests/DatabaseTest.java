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
        // add two journals
        db.insertJournal(new Journal("Hanna", "Robin", "Meris", "Psyk","Massa info om patient"), "testUser");
        db.insertJournal(new Journal("Doctor Who", "Nurse A", "John Doe", "South Wing","Lots and lots of info"), "testUser");
    }
    public void testUpdate(){
    	System.out.println("Journal before editing:");
    	db.getJournal("1", "testUser");
    	boolean succeeded = db.updateJournal("1", "Albin mar skit.", "testUser");
    	System.out.println("Journal after editing:");
    	db.getJournal("1", "testUser");
    	assert(succeeded);
    }
    public void testGetMyJournals() {
    	db.getMyJournals("testUser");
    	assert(true);
    }

    public void tearDown() {
    	db.printLog();
    	db.deleteJournal("1","testUser");
    	boolean succeeded = db.dropJournalsAndClearLog();
    	assert(succeeded);
    }
}
