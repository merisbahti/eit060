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
        db.insertJournal(new Journal("199105069158", "199112175279", "199003225522", "Psyk","Massa info om patient"), "199105069158","doctor");
        db.insertJournal(new Journal("199112175279", "199112175279", "199003225522", "Emergency room","Lots and lots of info"), "199112175279","nurse");
    }
    public void testUpdate(){
    	System.out.println("Journal before editing:");
    	/*for (Journal j : db.getMyJournals("199105069158", "Emergency room", "doctor")) 
				System.out.println(j.toString());*/
    	Journal journal = db.getJournal("1", "199105069158", "Psyk", "doctor");
    	boolean succeeded = db.updateJournal("1", "Albin mar skit.", "199105069158", "doctor");
    	System.out.println(journal.toString());
    	System.out.println("Journal after editing:");
    	/*for (Journal j : db.getMyJournals("199105069158", "Emergency room", "doctor")){
				System.out.println(j.toString());
    	}*/
    	journal = db.getJournal("1", "199105069158", "Emergency room", "doctor");
    	System.out.println(journal.toString());
    	assert(succeeded);
    }
    public void testGetMyJournals() {
    	for (Journal j : db.getMyJournals("199105069158", "Emergency room", "doctor")) 
			System.out.println(j.toString());
    	assert(true);
    }

    public void tearDown() {
    	db.printLog();
    	db.deleteJournal("1","199104220158", "government");
    	boolean succeeded = db.dropJournalsAndClearLog();
    	assert(succeeded);
    }
}
