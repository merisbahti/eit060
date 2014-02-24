package model;
import java.io.Serializable;

public class AddRequest implements Request, Serializable {
	private Journal journal;
	
  public AddRequest(Journal journal) {
    this.journal = journal;
  } 

  public String getID() {
    return null; 
  }
  
  public Journal getJournal(){
	  return journal;
  }

}
