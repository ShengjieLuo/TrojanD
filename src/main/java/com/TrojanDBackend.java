package com;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.model.other.Request;
import com.model.*;
import com.rpc.Interface.IRequest.*;
import com.rpc.Interface.*;
import com.execute.Executor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;


public class TrojanDBackend
{
  public static void main(String[] args) throws Exception {
    Context context = ZMQ.context(1);

    //  Socket to talk to server
    String zmqport = System.getenv("ZMQ_IP_BACK");
    Socket responder = context.socket(ZMQ.REP);
    responder.connect(zmqport);
    Executor executor = new Executor();

    while (!Thread.currentThread().isInterrupted()) {
      //Step1: Get the request from Client
      byte[] reply = responder.recv(0);
      IRequest ireq = IRequest.parseFrom(reply);
      Request req = new Request();
      req.copyFromInterface(ireq);

      //Step2: Execute the request
      executor.execute(req);

      //Step3: Return the request dealing result
      IRequest irep = req.copyToInterface();
      responder.send(irep.toByteArray(), 0);
    }

    responder.close();
    context.term();
  }
}
