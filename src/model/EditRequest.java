package model;

public class EditRequest implements Request{
  String id;
  String content;

  public EditRequest(String id, String content) {
    this.id = id;
    this.content = content;
  } 

  public String getID() {
    return id; 
  }

  public String getContent() {
    return content;
  }

}
