package com.rpc;

import org.zeromq.ZMQ;
import com.model.other.Request;
import com.model.other.*;
import com.rpc.Interface.IRequest.*;
import com.rpc.Interface.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

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
   
    public void send(List<Request> reqs){

      for (int i=0;i<reqs.size();i++){
        Request req = reqs.get(i);
        IRequest iRequest = req.copyToInterface();
	System.out.println("  [RPC] Send Package: "+req.getNum());
        requester.send(iRequest.toByteArray(), 0);
        byte[] reply = requester.recv(0);
        System.out.println("  [RPC] Received Package: " + new String(reply));
      }
    }
}
