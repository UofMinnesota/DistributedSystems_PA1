package project1;

import org.apache.thrift.TException;

public class SuperNodeServiceHandler implements SuperNodeService.Iface {

 @Override
 public String Join(String IP, int Port) throws TException {
  String NodeList = " ";

  return NodeList;
 }

 @Override
 public String GetNode() throws TException {
  String NodeList = " ";

  return NodeList;
 }

 @Override
 public boolean PostJoin(String IP, int Port) throws TException {


  return false;
 }


}
