
import java.io.*;
import java.net.*;

public class Client
{
   Socket socket = null;
   ObjectOutputStream out = null;
   BufferedReader in = null;
   
   public void sendToken(Token t)
   {
      //Send object over socket
      try {
		out.writeObject(t);
      } 
      catch (IOException e)
      {
         System.out.println(e.toString());
         System.exit(1);
      }
   }
  
   public void listenSocket(String host, int port)
   {
      //Create socket connection
	   boolean success = false;
      while(success == false)
      {
		   try
	      {
			 socket = new Socket(host, port);
			 out = new ObjectOutputStream(socket.getOutputStream());
			 in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			 success = true;
	      } 
	      catch (UnknownHostException e) 
	      {
			 System.out.println("Unknown host");
	      } 
	      catch (IOException e) 
	      {
			 //System.out.println("Server not found!!");
			 try 
			 {
				Thread.sleep(5000);
			 }
			 catch (InterruptedException e1) 
			 {
				e1.printStackTrace();
			 }
	      }		 
      }
   }
}