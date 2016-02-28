package project1;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class FilesClient {

 public static void main(String[] args) {

  try {
   TTransport SuperNodeTransport;
   TTransport NodeTransport;

   SuperNodeTransport = new TSocket("localhost", 9090);
   SuperNodeTransport.open();

   TProtocol SuperNodeProtocol = new TBinaryProtocol(SuperNodeTransport);
   SuperNodeService.Client supernodeclient = new SuperNodeService.Client(SuperNodeProtocol);
   
   NodeTransport = new TSocket("localhost", 9091);
   SuperNodeTransport.open();

   TProtocol NodeProtocol = new TBinaryProtocol(NodeTransport);
   NodeService.Client nodeclient = new NodeService.Client(NodeProtocol);


   SuperNodeTransport.close();
   NodeTransport.close();

  }// catch (TTransportException e) {
   //e.printStackTrace();
  //} 
  catch (TException x) {
   x.printStackTrace();
  }
 }

}
