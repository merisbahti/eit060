package model;
import java.io.Serializable;

public class AckResponse implements Serializable{
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
