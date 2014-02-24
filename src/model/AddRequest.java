package model;

public class AddRequest implements Request {
	private Journal journal;
	
  public AddRequest(Journal journal) {
    this.journal = journal;
  } 

  public String getID() {
    return id; 
  }
  
  public String getJournal(){
	  return journal;
  }

}
