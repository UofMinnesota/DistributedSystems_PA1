package project1;

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

public class Node {

 public static void StartsimpleServer(NodeService.Processor<NodeServiceHandler> processor) {
  try {
   TServerTransport serverTransport = new TServerSocket(9091);
   TServer server = new TSimpleServer(
     new Args(serverTransport).processor(processor));
   
   System.out.println("Establishing connection with the SuperNode...");
   
   TTransport SuperNodeTransport;
   SuperNodeTransport = new TSocket("csel-x29-10", 9090);
   SuperNodeTransport.open();
   
   TProtocol SuperNodeProtocol = new TBinaryProtocol(SuperNodeTransport);
   SuperNodeService.Client supernodeclient = new SuperNodeService.Client(SuperNodeProtocol);
   
  System.out.println("Requesting SuperNode for joining DHT through Join Call...");
  String dht_list;
  dht_list = supernodeclient.Join(getHostAddress(),9090); 
  System.out.println("The returned list is "+ dht_list);
   
   if(dht_list.equals("NACK")){
	   System.out.println("Supernode busy...");
      }
   else{
	   System.out.println("Joining DHT...");
	   
	   //Create finger table
	   
	          
       //send DHTList string to the nodeservicehandler
       NodeServiceHandler.setConfig(dht_list, getHostAddress(),9090);
       
	   //for 1 to n
	   //send DHT request to other nodes 
	   
	   supernodeclient.PostJoin(getHostAddress(),9090);
	   SuperNodeTransport.close();  
	   
	   System.out.println("Successfully joined DHT...");
	   System.out.println("Starting simple NodeServer...");
	   server.serve();
   }
   
  } catch (Exception e) {
   e.printStackTrace();
  }
 }

 public static void main(String[] args) {
  StartsimpleServer(new NodeService.Processor<NodeServiceHandler>(new NodeServiceHandler()));
 }
 
 private static String getHostAddress(){
	 try {
		   InetAddress addr = InetAddress.getLocalHost();            
		   	return (addr.getHostAddress());
		 } catch (UnknownHostException e) {
			 return null;
		 }
 }
 


}
//Use this for a multithreaded server
// TServer server = new TThreadPoolServer(new
// TThreadPoolServer.Args(serverTransport).processor(processor));
