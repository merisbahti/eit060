package model;

public class AddRequest implements Request {
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
