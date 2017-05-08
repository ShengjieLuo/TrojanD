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
    Socket responder = context.socket(ZMQ.REP);
    responder.connect("tcp://localhost:5557");
    Executor executor = new Executor();

    while (!Thread.currentThread().isInterrupted()) {
      byte[] reply = responder.recv(0);
      IRequest ireq = IRequest.parseFrom(reply);
      Request req = new Request();
      req.copyFromInterface(ireq);
      executor.execute(req);
      IRequest irep = req.copyToInterface();
      responder.send("Request "+ ireq.getNum());
    }

    responder.close();
    context.term();
  }
}
