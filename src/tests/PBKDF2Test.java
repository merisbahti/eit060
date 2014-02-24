package tests;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import org.junit.*;
import server.*;

public class PBKDF2Test extends junit.framework.TestCase {
  @Test
  public void createHashTest() {
    try {
      String hash = PBKDF2.generateHash("mitt lösenord som är hemligt",PBKDF2.generateSalt());
    } catch (InvalidKeySpecException e) {
      fail("Nyckelns specifikation är ogiltig. Detta kan bero på koden\neller så är det ett symptom på ett större fel");
    } catch (NoSuchAlgorithmException e) {
      fail("Denna dator stödjer inte PBKDF2 med Hmac SHA1 och är därför inte lämplig som server");
    }
    /*
     * Notera gärna att detta test endast är avsett för att undersöka om 
     * denna server stödjer den utmärkta säkerheten vi har i våra hash
     */
    assertTrue(true);
  }
}
