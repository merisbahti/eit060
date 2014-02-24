package model;

public class DeleteRequest implements Request{
  private final String id;

  public DeleteRequest(String id) {
    this.id = id; 
  } 

  public String getID() {
    return id; 
  }

}
