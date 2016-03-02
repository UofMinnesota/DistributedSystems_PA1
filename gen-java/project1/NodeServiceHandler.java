package project1;

import org.apache.thrift.TException;
import java.security.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
import java.util.Comparator;

import javax.print.DocFlavor.STRING;
import java.io.*;
import java.net.*;

import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class NodeServiceHandler implements NodeService.Iface {

  public static class  NodeInfo implements Comparable<NodeInfo>{
    String address = "";
    int port = 0;
    int hash = 0;
    
 
    public int compareTo(NodeInfo N) {
        return Integer.compare(hash, N.hash);
    }

    
  }

  private static ArrayList<NodeName> ListOfNodes = new ArrayList<NodeName>();
  
  private static String DHTList;
  private static int maxNumNodes = 8;
  private static NodeName myName;
  private static NodeName predecessor;
  private static int m;
  private static FingerTable[] fingerTable;
  private static int numDHT;
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

  public static void setConfig(String dht_list, String address, int port){
	  System.out.println("1Entering setConfig of service handler...");
	  DHTList = dht_list;
	  ListOfNodes = strToNodeNameArray(DHTList);
	  
	  myName = new NodeName(address, port, 0);
	  predecessor = new NodeName("NA", -1, -1);
	  
	  //myName.setIP(address);
	  //myName.setPort(port);
	  
	  System.out.println("2Entering setConfig of service handler...");
	  
	  myName.setID(findmyID(ListOfNodes));
	  
	  
	  //updating fingertable related values
	  m = (int) Math.ceil(Math.log(maxNumNodes) / Math.log(2));
	  System.out.println("Value of m is "+ m);
      fingerTable = new FingerTable[m+1];
      numDHT = (int)Math.pow(2,m);
      
      findPredecessor();
      buildFingerTable();
      
      printFingerTable();
      
  }
  
  private static int findmyID(ArrayList<NodeName> nodeList){
	  
	  int ID=-1;
	  //NodeName temp_node; 
	  
	  for(int i=0; i< nodeList.size(); i++){
		  System.out.println("iterating over the list..."+ nodeList.get(i).getIP() + " " +nodeList.get(i).getPort() + " " + nodeList.get(i).getID() + " " + myName.getIP() + " " + myName.getPort() + " answer is " + nodeList.get(i).getIP().compareTo(myName.getIP()));
		  if((myName.getPort() == nodeList.get(i).getPort()) && nodeList.get(i).getIP().equals(myName.getIP())){
			  System.out.println("My ID found is "+ nodeList.get(i).getID());
			  ID = nodeList.get(i).getID();
			  break;
		  }
		  else{
			  System.out.println("else iterating over the list..."+ nodeList.get(i).getIP() + " " +nodeList.get(i).getPort() + " " + nodeList.get(i).getID() + " " + myName.getIP() + " " + myName.getPort());
		  }
		  
	  }
	  
	  return ID;
	  
  }
  
  private static void findPredecessor(){
	    
	  Collections.sort(ListOfNodes);
	  
	  for(int i=0;i<ListOfNodes.size();i++){
		  System.out.println("Array list sorted is" + ListOfNodes.get(i).getIP() + " " +ListOfNodes.get(i).getPort() + " " + ListOfNodes.get(i).getID() + " ");
	  }
	  
	  //If there is only one node to the system the predecessor is the same node
	  if(ListOfNodes.size() == 1){
		  System.out.println("Only one node in the system...");
		  predecessor=myName;
	  }
	  //If there are multiple nodes, then predecessor is the previous element in the array
	  else{
		  
		  int myLocation=0;
		  
		  for(int i=0;i<ListOfNodes.size();i++){
			  if((myName.getPort() == ListOfNodes.get(i).getPort()) && ListOfNodes.get(i).getIP().equals(myName.getIP()) && (myName.getID() == ListOfNodes.get(i).getID())){
				  System.out.println("Found the ID...");
				  myLocation = i;
				  break;
			  }
		  }
		  
		  if(myLocation == 0){
			  predecessor.setIP(ListOfNodes.get(ListOfNodes.size()-1).getIP());
			  predecessor.setID(ListOfNodes.get(ListOfNodes.size()-1).getID());
			  predecessor.setPort(ListOfNodes.get(ListOfNodes.size()-1).getPort());
		  }
		  else{
			  //System.out.println("Address is "+ ListOfNodes.get(myLocation-1).address);
			  //System.out.println("Address is "+ ListOfNodes.get(myLocation-1).hash);
			  //System.out.println("Address is "+ ListOfNodes.get(myLocation-1).port);
			  System.out.println("Location is "+ myLocation);
			  //NodeName N = ListOfNodes.get(myLocation-1);
			  predecessor.setIP(ListOfNodes.get(myLocation-1).getIP());
			  predecessor.setID(ListOfNodes.get(myLocation-1).getID());
			  predecessor.setPort(ListOfNodes.get(myLocation-1).getPort());
		  }
		  
	  }
	  
  }
  
  private static void buildFingerTable(){
	  
	  Collections.sort(ListOfNodes);
	  
	  for(int i = 1; i <= m ; i++){
		  fingerTable[i] = new FingerTable();
		  fingerTable[i].setStart((myName.getID() + (int)Math.pow(2,i-1)) % numDHT);
	  }
	  
	  for (int i = 1; i < m; i++) {
          fingerTable[i].setInterval(fingerTable[i].getStart(),fingerTable[i+1].getStart()); 
      }
      fingerTable[m].setInterval(fingerTable[m].getStart(),fingerTable[1].getStart());
	  
      if (predecessor.getID() == myName.getID()) { //if predecessor is same as my ID -> only node in DHT
          for (int i = 1; i <= m; i++) {
              fingerTable[i].setSuccessor(myName);
          }
          System.out.println("Done, all finger tablet set as myName (only node in DHT)");
      }
      else {
          for (int i = 1; i <= m; i++) {
              fingerTable[i].findSuccessor(myName, ListOfNodes);
          }
      }
  }
  
  public static void printFingerTable(){
	  
	  System.out.println("  |  "+"Start"+"  |  "+"Interval Begin"+"  |  "+"Interval End"+"  |  "+"Successor"+"  |  ");
	  for(int i = 1; i <= m ; i++){
		  
		System.out.println("  |  "+fingerTable[i].getStart()+"  |  "+fingerTable[i].getIntervalBegin()+"  |  "+fingerTable[i].getIntervalEnd()+"  |  "+fingerTable[i].getSuccessor().getID()+"  |  ");
	  }
	  
  }
  
  
  public static NodeName strToNodeName(String input)
  {
    String data[] = input.split(":");
    NodeName newNo = new NodeName(data[0].trim(),Integer.parseInt(data[1]),Integer.parseInt(data[2]));
    //newNo.setIP(data[0].trim());
    //newNo.setPort(Integer.parseInt(data[1]));
    //newNo.setID(Integer.parseInt(data[2]));

    return newNo;
  }

  public static ArrayList<NodeName> strToNodeNameArray(String input)
  {
    ArrayList<NodeName> arrN = new ArrayList<NodeName>();
    String data[] = input.split("\\,");
    for(int c = 0; c < data.length; c++)
    {
      arrN.add(strToNodeName(data[c]));
    }
    return arrN;
  }

  public String MD5(String md5) {
     try {
          java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
          byte[] array = md.digest(md5.getBytes());
          StringBuffer sb = new StringBuffer();
          for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
         }
          return sb.toString();
      } catch (java.security.NoSuchAlgorithmException e) {

      return null;}
  }

public int isSuccessor(int hash)
{
  return -1;
}



 @Override
 public boolean Write(String Filename, String Contents) throws TException {
  //String NodeList = " ";

  int hash = keyHash(Filename);
  System.out.println("Filename is"+Filename);

  int succ = isSuccessor(hash);
  if(succ == -1)
  {
    files.put(Filename, Contents);

    return true;
  }
  else{


    TTransport NodeTransport;
    NodeTransport = new TSocket("localhost", 9090); //map nodes in the ports
    NodeTransport.open();

    TProtocol NodeProtocol = new TBinaryProtocol(NodeTransport);

    NodeService.Client nodeclient = new NodeService.Client(NodeProtocol);

    return nodeclient.Write(Filename, Contents);
  }

  //return false;
 }

 @Override
 public String Read(String Filename) throws TException {
   //String NodeList = " ";

   int hash = keyHash(Filename);
   System.out.println("Filename is"+Filename);

   int succ = isSuccessor(hash);
   if(succ == -1)
   {
     //read locally
     return (files.get(Filename));
   }
   else{


     TTransport NodeTransport;
     NodeTransport = new TSocket("localhost", 9090); //map nodes in the ports
     NodeTransport.open();

     TProtocol NodeProtocol = new TBinaryProtocol(NodeTransport);

     NodeService.Client nodeclient = new NodeService.Client(NodeProtocol);

     return nodeclient.Read(Filename);
   }

   //return false;
 }

 @Override
 public boolean UpdateDHT(String NodeList) throws TException {


  return false;
 }
 

}
