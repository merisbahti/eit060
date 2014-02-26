package server;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.security.KeyStore;
import javax.net.*;
import javax.net.ssl.*;
import javax.security.cert.X509Certificate;
import model.*;
import java.util.ArrayList;



public class Server implements Runnable {
	private ServerSocket serverSocket = null;
	private static int numConnectedClients = 0;

	public Server(ServerSocket ss) throws IOException {
		serverSocket = ss;
		newListener();
	}

	public void run() {
		try {
			SSLSocket socket=(SSLSocket)serverSocket.accept();
			newListener();
			SSLSession session = socket.getSession();
			X509Certificate cert = (X509Certificate)session.getPeerCertificateChain()[0];
			String subject = cert.getSubjectDN().getName();
			numConnectedClients++;

			System.out.println("client connected");
			System.out.println("client name (cert subject DN field): " + subject);
			System.out.println(numConnectedClients + " concurrent connection(s)\n");

      ObjectOutputStream outSocket = new ObjectOutputStream(socket.getOutputStream());
      ObjectInputStream inSocket = new ObjectInputStream(socket.getInputStream());

      Request req = null;
      try {
        while ((req = (Request) inSocket.readObject()) != null) {
          Response resp = generateResponse(req); 
          if (resp!=null) {
            outSocket.writeObject(resp);
            outSocket.flush();
            System.out.println("done");
          }
        } 
      } catch (ClassNotFoundException e) {
        System.err.println("Classnotfoundexception: " + e.getMessage());
      }
      inSocket.close();
      outSocket.close();
			socket.close();
			numConnectedClients--;
			System.out.println("client disconnected");
			System.out.println(numConnectedClients + " concurrent connection(s)\n");
		} catch (IOException e) {
			System.out.println("Client died: " + e.getMessage());
			e.printStackTrace();
			return;
		}
	}

    private void newListener() { (new Thread(this)).start(); } // calls run()

    public static void main(String args[]) {
        System.out.println("\nServer Started\n");
        int port = -1;
        if (args.length >= 1) {
            port = Integer.parseInt(args[0]);
        }
        String type = "TLS";
        try {
            ServerSocketFactory ssf = getServerSocketFactory(type);
            ServerSocket ss = ssf.createServerSocket(port);
            ((SSLServerSocket)ss).setNeedClientAuth(true); // enables client authentication
            new Server(ss);
            System.out.println("\nServer actually started\n");
        } catch (IOException e) {
            System.out.println("Unable to start Server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static ServerSocketFactory getServerSocketFactory(String type) {
        if (type.equals("TLS")) {
            SSLServerSocketFactory ssf = null;
            try { // set up key manager to perform server authentication
                SSLContext ctx = SSLContext.getInstance("TLS");
                KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
                KeyStore ks = KeyStore.getInstance("JKS");
				KeyStore ts = KeyStore.getInstance("JKS");
                char[] password = "password".toCharArray();

                ks.load(new FileInputStream("stores/serverkeystore"), password);  // keystore password (storepass)
                ts.load(new FileInputStream("stores/servertruststore"), password); // truststore password (storepass)
                kmf.init(ks, password); // certificate password (keypass)
                tmf.init(ts);  // possible to use keystore as truststore here
                ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
                ssf = ctx.getServerSocketFactory();
                return ssf;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return ServerSocketFactory.getDefault();
        }
        return null;
    }

    private static Response generateResponse(Request req) {
          if (req instanceof ReadRequest) {
            ReadRequest readRequest = (ReadRequest) req;
            String id = readRequest.getID();
            return new AckResponse(true, "ReadRequest recieved");
          } else if (req instanceof ListRequest) {
            return new AckResponse(false, "Listrequest recieved");
          } else if (req instanceof DeleteRequest) {
            return new AckResponse(false, "Deleterequest recieved");
          } else if (req instanceof AddRequest) {
            return new AckResponse(false, " Addrequest recieved");
          } else if (req instanceof EditRequest) {
            return new AckResponse(false, "EditRequest recieved");
          } else {
            return new AckResponse(false, "Don't know wtf recieved");
          }
    
    }
}