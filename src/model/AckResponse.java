package model;

public class AckResponse implements Response {
  private final boolean ack;
  private final String msg;

  public AckResponse(boolean ack, String msg) {
    this.ack = ack;
    this.msg = msg;
  }

  public String getMessage() {
    return msg;
  }
  

}
