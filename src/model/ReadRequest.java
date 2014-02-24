package model;

import java.io.Serializable;

public class ReadRequest implements Request, Serializable {
  private final String id;

  public ReadRequest(String id) {
    this.id = id;
  }

  public String getID() {
    return id;
  }

}
