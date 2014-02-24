package tests;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import org.junit.*;
import server.*;

public class PBKDF2Test extends junit.framework.TestCase {
  public void testCreateHash() {
    try {
      String hash = PBKDF2.generateHash("mitt lösenord som är hemligt",PBKDF2.generateSalt());
    } catch (InvalidKeySpecException e) {
      fail("Nyckelns specifikation är ogiltig. Detta kan bero på koden\neller så är det ett symptom på ett större fel");
    } catch (NoSuchAlgorithmException e) {
      fail("Denna dator stödjer inte PBKDF2 med Hmac SHA1 och är därför inte lämplig som server");
    }
    /*
     * Please note that this test is only used to
     * examine if the server supports out hash algorithms.
     */
    assertTrue(true);
  }
}
