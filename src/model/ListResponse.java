package model;

import java.util.ArrayList;

public class ListResponse implements Response {
  private ArrayList<Journal> journals;

  public ListResponse(ArrayList<Journal> journals) {
    this.journals = journals;
  }

  public ArrayList<Journal> getJournals() {
    return journals;
  }
  public String getMessage() {
    return "List returned.";
  }
}

