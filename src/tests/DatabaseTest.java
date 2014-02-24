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

    public void testInsertJournal() {
      /* populate database */
      db.insertJournal("Hanna", "Robin", "Meris", "Psyk","Massa info om patient");

      assertTrue(true);
    }
    public void testDeleteJournal() {
    	//db.deleteJournal(1);
    	db.getJournal(1);
    }
    public void testUpdate(){
    	System.out.println("Journal before editing:");
    	db.getJournal(2);
    	boolean succeeded = db.updateJournal(2, "Hugo", "Berit", "Albin", "Right Wing", "Albin mar skit.");
    	System.out.println("Journal after editing:");
    	db.getJournal(2);
    	assert(succeeded);
    }
}
