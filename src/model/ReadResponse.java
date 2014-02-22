package model;

public class ReadResponse implements Response {
  static Journal journal;

  public ReadResponse(Journal journal) {
    this.journal = journal;
  }

  public Journal getJournal() {
    return journal; 
  }
}

