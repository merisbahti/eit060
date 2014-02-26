package client;

import java.net.*;
import java.io.*;
import javax.net.ssl.*;
import javax.security.cert.X509Certificate;
import java.security.KeyStore;
import java.security.cert.*;
import model.*;
import java.util.ArrayList;

/*
 * This example shows how to set up a key manager to perform client
 * authentication.
 *
 * This program assumes that the client is not inside a firewall.
 * The application can be modified to connect to a server outside
 * the firewall by following SSLSocketClientWithTunneling.java.
 */

public class Client {

	public static void main(String[] args) throws Exception {
		String host = null;
		int port = -1;
		for (int i = 0; i < args.length; i++) {
			System.out.println("args[" + i + "] = " + args[i]);
		}
		if (args.length < 2) {
			System.out.println("USAGE: java client host port");
			System.exit(-1);
		}
		try { /* get input parameters */
			host = args[0];
			port = Integer.parseInt(args[1]);
		} catch (IllegalArgumentException e) {
			System.out.println("USAGE: java client host port");
			System.exit(-1);
		}

		try { /* set up a key manager for client authentication */
			SSLSocketFactory factory = null;
			try {
				char[] password = "hejsan2".toCharArray();
				KeyStore ks = KeyStore.getInstance("JKS");
				KeyStore ts = KeyStore.getInstance("JKS");
				KeyManagerFactory kmf = KeyManagerFactory
						.getInstance("SunX509");
				TrustManagerFactory tmf = TrustManagerFactory
						.getInstance("SunX509");
				SSLContext ctx = SSLContext.getInstance("TLS");
				ks.load(new FileInputStream("stores/clientkeystore"), password); // keystore
																					// password
																					// (storepass)
				ts.load(new FileInputStream("stores/clienttruststore"),
						password); // truststore password (storepass);
				kmf.init(ks, password); // user password (keypass)
				tmf.init(ts); // keystore can be used as truststore here
				ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
				factory = ctx.getSocketFactory();
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			}
			SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
			System.out.println("\nsocket before handshake:\n" + socket + "\n");

			/*
			 * send http request
			 * 
			 * See SSLSocketClient.java for more information about why there is
			 * a forced handshake here when using PrintWriters.
			 */
			socket.startHandshake();

			SSLSession session = socket.getSession();
			X509Certificate cert = (X509Certificate) session
					.getPeerCertificateChain()[0];
			String subject = cert.getSubjectDN().getName();
			System.out
					.println("certificate name (subject DN field) on certificate received from server:\n"
							+ subject + "\n");
			System.out.println("socket after handshake:\n" + socket + "\n");
			System.out.println("secure connection established\n\n");


			BufferedReader read = new BufferedReader(new InputStreamReader(
					System.in));
      
      ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
      ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());

			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			String msg;

			for (;;) {
				msg = read.readLine();
				if (msg.equalsIgnoreCase("quit")) {
					break;
				}
        Request req = generateRequest(msg);
        if (req!=null) {
          outStream.writeObject(req);
          outStream.flush();
          System.out.print("sending '"+ req.getID() +"' to server...");
          System.out.println("done");
          Response resp = (Response) inStream.readObject();
          System.out.println("Recieved response with message:\n" + resp.getMessage());
        }
			}
			in.close();
			outStream.close();
			read.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Request generateRequest(String input) {
		Request request = null;
		String[] cmd = input.split(" ");
		switch (cmd[0]) {
		case "R":
      if (cmd.length != 2) {
        System.out.println("Wrongly formatted command");
        return null;
      }
			request = new ReadRequest(cmd[1]);
			break;
		case "A":
      Journal journal = null;
      while (journal == null) {
        journal = promptJournal();
      }
      request = new AddRequest(journal);
			break;
		case "D":
      if (cmd.length != 2) {
        System.out.println("Wrongly formatted command");
        return null;
      }
      request = new DeleteRequest(cmd[1]);
			break;
		case "W":
      if (cmd.length != 2) {
        System.out.println("Wrongly formatted command");
        return null;
      }
      try {
        BufferedReader readr = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Say what you want to add to the journal: ");
        String robinaerfull= readr.readLine();
        request = new EditRequest(cmd[1], robinaerfull);
      } catch (IOException e) {
        System.out.print("You are not really dooing stuff right");
        return null;
      }
			break;
		case "L":
			System.out.println("Sending list request");
      request = new ListRequest();
			break;
		case "H":
			System.out
					.print("Usage:\nTo read a file: R id\nTo add a file: A content\nTo delete a file: D id\nTo write to a file: W id\nTo list all files that you have access to: L\n");
			break;
		default:
			System.out
					.println("Not sure what to do with this, type \"H\" for usage.");
			break;
		}
		return request;
	}

  private static Journal promptJournal(){
    try {
      BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("Nurse social security number: ");
      String nurseSSN = read.readLine();
      System.out.println("Patient social security number: ");
      String patientSSN = read.readLine();
      System.out.println("Content: ");
      String content = read.readLine();
      return new Journal("roppe", nurseSSN, patientSSN, content, "district 9");
    } catch (IOException e) {
      return null;
    }
  }

}
