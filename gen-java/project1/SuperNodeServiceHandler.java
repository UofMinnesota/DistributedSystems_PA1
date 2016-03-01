package project1;

import org.apache.thrift.TException;
import java.util.ArrayList;
import java.util.Random;

public class SuperNodeServiceHandler implements SuperNodeService.Iface {

  public class  NodeInfo{
    String address = "";
    int port = 0;
    int hash = 0;
  }

  ArrayList<NodeInfo> ListOfNodes = new ArrayList<NodeInfo>();
  boolean isBusy = false;
  private Random randomGenerator = new Random();
  private static int max_keys = 4;
  private int num_keys = max_keys; 


  int keyHash(String key)
  {
      int k = (int)key.length();
      int u = 0,n = 0;

      for (int i=0; i<k; i++)
      {
          n = (int)key.charAt(i);
          u += i*n%31;
      }
      return u%max_keys;
  }

 @Override
 public String Join(String IP, int Port) throws TException {
  

	 
	 System.out.println("Node "+ IP+" : "+Port+" requests for joining DHT...");
  if(isBusy)
  {
    return "NACK";
  }
  String NodeList = " ";
  
  if(num_keys > 0)
  {
	   NodeInfo newNode = new NodeInfo();
	   newNode.address = IP;
	   newNode.port = Port;
	   newNode.hash = keyHash(IP+":"+String.valueOf(Port));

	   ListOfNodes.add(newNode);

	  for(int x = 0; x < ListOfNodes.size(); x++)
	  {
		  NodeList += ListOfNodes.get(x).address + ":" + String.valueOf(ListOfNodes.get(x).port) + ":" + String.valueOf(ListOfNodes.get(x).hash) + ",";
	  }

	  isBusy = true;
	  num_keys--;

	  return NodeList;
  }
  else{
	  
	  System.out.println("Max number of nodes reached in DHT ....");
	  return "NACK";
  }
 }

 @Override
 public String GetNode() throws TException {
  int x = randomGenerator.nextInt(ListOfNodes.size());
  return  ListOfNodes.get(x).address + ":" + String.valueOf(ListOfNodes.get(x).port) + ":" + String.valueOf(ListOfNodes.get(x).hash);

 }

 @Override
 public boolean PostJoin(String IP, int Port) throws TException {

   isBusy = false;

   return isBusy;
 }


}
