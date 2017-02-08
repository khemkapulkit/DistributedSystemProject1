import java.io.*;
import java.net.*;
import java.util.ArrayList;

class Server implements Runnable 
{
	static int recieved = 0;
   private Socket client;
   
   Server(Socket client) 
   {
      this.client = client;
   }

   public void run()
   {
      String line;
      ObjectInputStream in = null;
      PrintWriter out = null;
      Token t;
      try 
      {
		 in = new ObjectInputStream(client.getInputStream());
		 out = new PrintWriter(client.getOutputStream(), true);
      } 
      catch (IOException e) 
      {
		 System.out.println("in or out failed");
		 System.exit(-1);
      }

      try 
      {
		 // Receive object from client
		t = (Token)in.readObject();
		t.nodes = new ArrayList<Integer>(t.nodes.subList(1, t.nodes.size()));
		
		if (t.nodes.size()>0)
		{
			t.sum = t.sum + Project1.label;
	   	 	Client client = new Client();
	        client.listenSocket( ((Address)Project1.mapper.get( t.nodes.get(0))).host, ((Address)Project1.mapper.get( t.nodes.get(0))).port);
	        client.sendToken(t);
		}
		else
		{
			 Project1.wr.println("Received token "+t.Id +" Token sum = "+ t.sum);
			 recieved++;
			if(recieved >= Project1.numOfTokens)
			{
				Project1.wr.println("All tokens Received");
				Project1.wr.println("# END");
				Project1.wr.close();
				System.out.println("All tokens Recieved at node: " + Project1.nodeID);
			}
		}
		
      } 
      catch (Exception e) 
      {
		 System.out.println(e.toString());
		 System.exit(-1);
      }

      try 
      {
    	  client.close();
      } 
      catch (IOException e) 
      {
		 System.out.println("Close failed");
		 System.exit(-1);
      }
   }
}
