import java.io.Serializable;
import java.util.ArrayList;


public class Token implements Serializable {

	  ArrayList<Integer> nodes;
	  int sum;
	  int Id;
	  
	  Token()
	  {
		  nodes = new ArrayList<Integer>();
		  sum = 0;
	  }
	  
	  Token(ArrayList<Integer> List, int id, int sum)
	  {
		  nodes = List;
		  this.sum = sum;
		  Id = id;
	  }
}