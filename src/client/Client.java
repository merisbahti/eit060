package client;

import java.net.*;
import java.io.*;
import javax.net.ssl.*;
import javax.security.cert.X509Certificate;
import java.security.KeyStore;
import java.security.cert.*;
import model.*;

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
				char[] password = "password".toCharArray();
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


      
      //ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
      ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());

			//PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
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
          System.out.println("received '" + in.readLine() +
          "' from server\n");
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
			System.out.println("Requesting to read file id: " + cmd[1]);
			request = new ReadRequest(cmd[1]);
			break;
		case "A":
			System.out.println("Sending add request");
			break;
		case "D":
			System.out.println("Sending delete request");
      request = new DeleteRequest(cmd[2]);
			break;
		case "W":
			System.out.println("Sending write request");
			System.out.println("Requesting to write to file id: " + cmd[1] + "\ncontent to append: " + cmd[2]);
      request = new EditRequest(cmd[1], cmd[2]);
			break;
		case "L":
			System.out.println("Sending list request");
      request = new ListRequest();
			break;
		case "H":
			System.out
					.print("Usage:\nTo read a file: R id\nTo add a file: A content\nTo delete a fil: D id\nTo write to a file: w id content\nTo list all files that you have access to: L\n");
			break;
		default:
			System.out
					.println("Not sure what to do with this, type \"H\" for usage.");
			break;
		}
		return request;
	}
}
