package model;

import java.io.Serializable;

public class DeleteRequest implements Request, Serializable{
  private final String id;

  public DeleteRequest(String id) {
    this.id = id; 
  } 

  public String getID() {
    return id; 
  }

}
