package model;

public class ReadRequest implements Request {
  static String id;

  public ReadRequest(String id) {
    this.id = id;
  }

  public String getID() {
    return id;
  }

}
