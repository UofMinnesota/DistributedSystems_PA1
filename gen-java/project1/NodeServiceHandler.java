package project1;

import org.apache.thrift.TException;
import java.security.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Random;

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



  public static class  NodeInfo{
    String address = "";
    int port = 0;
    int hash = 0;
  }

  private static ArrayList<NodeInfo> ListOfNodes = new ArrayList<NodeInfo>();
  Map<String, String> files = new HashMap<String, String>();


  private static String DHTList;
  private static int maxNumNodes = 4;
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
	  ListOfNodes = strToNodeInfoArray(DHTList);

	  myName = new NodeName(address, port, 0);

	  //myName.setIP(address);
	  //myName.setPort(port);

	  System.out.println("2Entering setConfig of service handler...");

	  myName.setID(findmyID(ListOfNodes));


	  //updating fingertable related values
	  m = (int) Math.ceil(Math.log(maxNumNodes) / Math.log(2));
      fingerTable = new FingerTable[m+1];
      numDHT = (int)Math.pow(2,m);




  }

  private static int findmyID(ArrayList<NodeInfo> nodeList){

	  int ID=-1;
	  //NodeInfo temp_node;

	  for(int i=0; i< nodeList.size(); i++){
		  System.out.println("iterating over the list..."+ nodeList.get(i).address + " " +nodeList.get(i).port + " " + nodeList.get(i).hash + " " + myName.getIP() + " " + myName.getPort() + " answer is " + nodeList.get(i).address.compareTo(myName.getIP()));
		  if((myName.getPort() == nodeList.get(i).port) && nodeList.get(i).address.equals(myName.getIP())){
			  System.out.println("My ID found is "+ nodeList.get(i).hash);
			  ID = nodeList.get(i).hash;
			  break;
		  }
		  else{
			  System.out.println("else iterating over the list..."+ nodeList.get(i).address + " " +nodeList.get(i).port + " " + nodeList.get(i).hash + " " + myName.getIP() + " " + myName.getPort());
		  }

	  }

	  return ID;

  }


  public static NodeInfo strToNodeInfo(String input)
  {
    String data[] = input.split(":");
    NodeInfo newNo = new NodeInfo();
    newNo.address = data[0].trim();
    newNo.port = Integer.parseInt(data[1]);
    newNo.hash = Integer.parseInt(data[2]);

    return newNo;
  }

  public static ArrayList<NodeInfo> strToNodeInfoArray(String input)
  {
    ArrayList<NodeInfo> arrN = new ArrayList<NodeInfo>();
    String data[] = input.split("\\,");
    for(int c = 0; c < data.length; c++)
    {
      arrN.add(strToNodeInfo(data[c]));
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
