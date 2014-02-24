package model;

public class AddRequest implements Request {
  int id;

  public AddRequest(int id) {
    this.id = id;
  } 

  public String getID() {
    return id; 
  }

}
