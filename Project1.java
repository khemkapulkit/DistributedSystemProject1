import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

class Project1
{
   ServerSocket server = null;
   static int label;
   static int nodeID;
   static int numOfNodes;
   static int numOfTokens;
   static HashMap <Integer, Object> mapper;
   static PrintWriter wr;
   
   public void listenSocket(int port)
   {
      try
      {
		 server = new ServerSocket(port); 
		 System.out.println("Server running on "+((Address)mapper.get(nodeID)).host+" at port " + port + "," + " use ctrl-C to end");
		 System.out.println();
      } 
      catch (IOException e) 
      {
		 System.out.println(e.toString());
		 System.exit(-1);
      }
      while(true)
      {
         Server w;
         try
         {
        	 
            w = new Server(server.accept());
            Thread t = new Thread(w);
            t.start();
            
            
		 } 
		 catch (IOException e) 
		 {
		    System.out.println("Accept failed");
		    System.exit(-1);
         }
      }
   }

   protected void finalize()
   {
      try
      {
         server.close();
      } 
      catch (IOException e) 
      {
         System.out.println("Could not close socket");
         System.exit(-1);
      }
   }
   
   

   public static void main(String[] args) throws IOException
   {
	   Random rand = new Random();
	   label = rand.nextInt(50 - 10 + 1) + 10; // label is random between 10 and 50
	   mapper = new HashMap<Integer, Object>();
	   nodeID = Integer.parseInt(args[0]);
	   ArrayList<Token> TokenList = new ArrayList<Token>();
	   
	   BufferedReader br = new BufferedReader(new FileReader("config.txt"));
	   String line;
	   while ( (line = br.readLine()) != null)
	   {
		   if (!(line.isEmpty() || line.trim().equals("") || line.trim().equals("\n") || line.trim().charAt(0)=='#'))
		   {
			   if(line.contains("#"))
			   {
				   line = line.split("#")[0];
			   }
			   line = line.trim();
			   numOfNodes =line.charAt(0) - '0';
			   break;
		   }
	   }
	   int i =0;
	   while ( (line = br.readLine()) != null)
	   {
		   if (!(line.isEmpty() || line.trim().equals("") || line.trim().equals("\n") || line.trim().charAt(0)=='#'))
		   {
			   if(line.contains("#"))
			   {
				   line = line.split("#")[0];
			   }
			   String[] line1 = line.split(" ");
			   mapper.put(Integer.parseInt(line1[0]), new Address(line1[1]+".utdallas.edu",Integer.parseInt(line1[2])));
			   i++;
			   if(i>=numOfNodes)
			   {
				   break;
			   }
		   }
	   }
	   int id = 0;
	   while ( (line = br.readLine()) != null)
	   {
		   if (!(line.isEmpty() || line.trim().equals("") || line.trim().equals("\n") || line.trim().charAt(0)=='#'))
		   {
			   if(line.contains("#"))
			   {
				   line = line.split("#")[0];
			   }
			   line = line.trim();
			   String line1[] = line.split(" ");
			   if(Integer.parseInt(line1[0]) == nodeID)
			   {
				   ArrayList<Integer> nodes = new ArrayList<Integer>();
				   for(int j=0;j<line1.length;j++)
				   {
					   nodes.add(Integer.parseInt(line1[j]));	   
				   }
				   nodes.add(nodeID);
				   TokenList.add(new Token(nodes,id,label));
				   id++;
			   }
		   }
	   }
	  numOfTokens = TokenList.size();
	  wr = new PrintWriter("config-pxk146230-"+ nodeID +".out.txt");
	 
	  wr.println("Net ID: pxk146230");
	  wr.println("Node ID: "+nodeID);
	  wr.println("Listening on "+((Address)mapper.get(nodeID)).host+": "+((Address)mapper.get(nodeID)).port);
	  wr.println("Random Number: "+label);
	  if(numOfTokens ==0)
	  {
		  Project1.wr.println("All tokens Received");
		  Project1.wr.println("# END");
		  Project1.wr.close();
		  System.out.println("All tokens Recieved at node: " + nodeID);
	  }
	  
	   Project1 n = new Project1();
	   Thread t1 = new Thread(new Runnable() {
	         public void run() {
	        	 
	             int port = ((Address)mapper.get(nodeID)).port;
	             n.listenSocket(port);
	         }
	      });
	      t1.start();
	      
	      Iterator<Token> iterator = TokenList.iterator();
	      while(iterator.hasNext())
	      {
	    	  Token t;
	          t = iterator.next();
	          
		      Thread t2 = new Thread(new Runnable() 
		      {
		         public void run() 
		         {	
		        	 String path = "";
		        	 for(int k = 0; k < t.nodes.size(); k++) 
		        	 {
			        	  if(k==(t.nodes.size()-1))
			        	  {
			        		  path = path + t.nodes.get(k);
			        	  }
			        	  else
			        	  {
			        	    path = path + t.nodes.get(k) + "->";
			        	  }
		        	 }
		        	 t.nodes = new ArrayList<Integer>(t.nodes.subList(1, t.nodes.size()));
		        	 Client client = new Client();
		             client.listenSocket( ((Address)mapper.get( t.nodes.get(0))).host, ((Address)mapper.get( t.nodes.get(0))).port);
		             wr.println("Emitting Token "+ t.Id +" with path " + path);
		             client.sendToken(t);
		         }
		      });
		      t2.start();
	      }
      
   }
}
