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
import model.*;



public class Server implements Runnable {
	private ServerSocket serverSocket = null;
	private static int numConnectedClients = 0;
	private static Database db;

	public Server(ServerSocket ss) throws IOException {
		serverSocket = ss;
		newListener();
		try {
			db = new Database();
		} catch (ClassNotFoundException e) {
			System.err.print("You done good'd");
		}
		// add two journals
	}

	public void run() {
		try {
			SSLSocket socket=(SSLSocket)serverSocket.accept();
			newListener();
			SSLSession session = socket.getSession();
			X509Certificate cert = (X509Certificate)session.getPeerCertificateChain()[0];
			String cn = (cert.getSubjectDN().getName().split(", ")[0].split("=")[1]);
      String type = (cert.getSubjectDN().getName().split(", ")[1].split("=")[1]);
      String district = (cert.getSubjectDN().getName().split(", ")[2].split("=")[1]);
			numConnectedClients++;

			System.out.println("client connected");
			System.out.println("id: " + cn);
			System.out.println("type: " + type);
      System.out.println("district: " + district);
			System.out.println(numConnectedClients + " concurrent connection(s)\n");

      ObjectOutputStream outSocket = new ObjectOutputStream(socket.getOutputStream());
      ObjectInputStream inSocket = new ObjectInputStream(socket.getInputStream());

      Request req = null;
      try {
        while ((req = (Request) inSocket.readObject()) != null) {
          AckResponse resp = generateResponse(req, cn, type, district); 
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
			System.out.println("Client probably didn't die but disconnected instead: " + e.getMessage());
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

    private static AckResponse generateResponse(Request req, String userID, String type, String groupID) {
          if (req instanceof ReadRequest) 
          {
            Journal jurre = db.getJournal(req.getID(), userID, groupID, type);
            if (jurre == null) 
              return new AckResponse(false, "acces requred to red file");
            return new AckResponse(true, jurre.toString());
          } 
          else if (req instanceof ListRequest) 
          {
            String journals = "\n";
            for (Journal j : db.getMyJournals(userID, groupID, type))
              journals += j.getMetaData() + "\n";
            return new AckResponse(false, "Retrieved journals:"+journals);
          } 
          else if (req instanceof DeleteRequest) 
          {
            return new AckResponse(true, "attempted delete of: " + req.getID() + " result: " 
                + db.deleteJournal(req.getID(), userID, type));
          } 
          else if (req instanceof AddRequest) 
          {
            AddRequest addRequest = (AddRequest) req;
            Journal inJourn = addRequest.getJournal();
            Journal journ = new Journal(userID, inJourn.getNurse(), inJourn.getPatient(), groupID, inJourn.getContent());

            return new AckResponse(true, "Addrequest recieved from: " + userID + " Containing: \n" + journ + "\n" + 
                "result : " + db.insertJournal(journ, userID, type));
            
          } 
          else if (req instanceof EditRequest) 
          {
            EditRequest eReq = (EditRequest) req;
            return new AckResponse(true, "Requesting to edit: " + eReq.getID() + 
                " result: " + db.updateJournal(eReq.getID(), eReq.getContent(), userID, type));
          } 
          else if (req instanceof LogRequest) 
          {
            try {
              LogDatabase db2 = new LogDatabase();
              return new AckResponse(true, "Requesting log: \n" + db2.printLog());
            } catch (Exception e) {
              return new AckResponse(false, "ClassNotFounde");
            }
          } 
          else 
          {
            return new AckResponse(false, "Don't know wtf recieved from: " + userID);
          }
    
    }
}
