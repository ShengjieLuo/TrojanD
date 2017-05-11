package com.rpc;

import org.zeromq.ZMQ;
import com.model.other.Request;
import com.model.other.*;
import com.rpc.Interface.IRequest.*;
import com.rpc.Interface.*;
import scala.collection.JavaConverters.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.*;

public class Client {

    ZMQ.Context context = null;
    ZMQ.Socket requester = null;

    public Client(){
      context = ZMQ.context(1);
      System.out.println("  [RPC] Connect to server spark-master:5556 ");
      requester = context.socket(ZMQ.REQ);
      requester.connect("tcp://master:5556");
    }

    protected void finalize(){
      requester.close();
      context.term();      
    }
   
    public List<Request> send(List<Request> reqs){

      //Initiallize the reply list
      List<Request> replys = new ArrayList<Request>();

      for (int i=0;i<reqs.size();i++){
        try{ 
        //Step1: Convert the request into the Interface
        Request req = reqs.get(i);
        IRequest iRequest = req.copyToInterface();
	System.out.println("  [RPC] Send Package: " + req.getNum());

        //Step2: Send the interface data
        requester.send(iRequest.toByteArray(), 0);

        //Step3: Receive the reply
        byte[] rep = requester.recv(0);
        IRequest ireply = IRequest.parseFrom(rep);
        Request reply = new Request();
        reply.copyFromInterface(ireply);
        reply.print();
        replys.add(reply);
        System.out.println("  [RPC] Received Package: " + reply.getNum());
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          System.out.println("  [RPC] RPC  Parse Error!");
        }
      }

      return replys;
    }
}
