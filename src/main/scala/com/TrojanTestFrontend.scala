package com

import org.drools.KnowledgeBase
import org.drools.KnowledgeBaseFactory
import org.drools.builder.KnowledgeBuilder
import org.drools.builder.KnowledgeBuilderError
import org.drools.builder.KnowledgeBuilderErrors
import org.drools.builder.KnowledgeBuilderFactory
import org.drools.builder.ResourceType
import org.drools.logger.KnowledgeRuntimeLogger
import org.drools.logger.KnowledgeRuntimeLoggerFactory
import org.drools.io.ResourceFactory
import org.drools.runtime.StatefulKnowledgeSession
import org.drools.event.KnowledgeRuntimeEventManager

import org.apache.spark._
import org.apache.spark.rdd.RDD

import org.drools.event._
import org.drools.runtime._
import org.drools.builder._

import com.model.other.Time
import com.model.other.Item
import com.model.other.Type
import com.model.problem.Trojan

object TrojanTestFrontend {

  def main(args : Array[String]):Unit ={
    
    println("Run Trojan Test by Distributed Rule Engine")
    val sparkconf = new SparkConf().setAppName("TrojanDetectionTest")
    val sc = new SparkContext(sparkconf)
    val ssc = new StreamingContext(sc,Seconds(60))

    //Step1:Receive Data From Kafka
    val zkQuorum = "spark-master:2182" //Zookeeper服务器地址
    val group = "TrojanD"  //Kafka Group Name
    val topic = "TrojanD" //Kafka Topic Name
    val numThreads = 1
    val topicMap = topic1.split(",").map((_,numThreads.toInt)).toMap
    val lineMap = KafkaUtils.createStream(ssc,zkQuorum,group,topicMap)
    val lines = lineMap.map(_._2).map(_.split("\t")).filter(_.length>=16)

    println("  [Debug] Begin Execution!")
    val pairRDD = lines.foreachRDD( dataRDD =>
                     dataRDD.mapPartitions{ partition => {
			println("  [Debug] Load the knowledge session");
			ksession:StatefulKnowledgeSession = GetKnowledgeSession()
			val time = new Time();
			ksession.insert(time);
			val ty   = new Type();
			ksession.insert(ty);
			
                        val newPartition = partition.map(p => {
			  println("  [Debug] Begin Dealing the element: "+p);
			  val item = new Item("IP-hostmachine",p(0));
                          item.setSYN(p(2).toInt);
			  item.setUP(p(5).toInt,p(6).toInt,p(8).toInt);
                          item.setPSH(p(10).toInt);
			  item.setDOWN(p(13).toInt,p(14).toInt,p(16).toInt);
			  ksession.insert(item);
                          ksession.fireAllRules();
			  (p,1)
			})						
    			//println("  [Debug] Finish the data partition!");
			newPartition
                        }
                     }
                    .reduceByKey(_+_)
                    .foreach{p => println("  [Debug] Detection Object: " + p._1.toString + ": Number :"+ p._2.toString)}
                  )
  }

  def GetKnowledgeSession() : StatefulKnowledgeSession = {
    val config:KnowledgeBuilderConfiguration = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration()
    config.setProperty("drools.dialect.mvel.strict", "false")
    var kbuilder : KnowledgeBuilder  = KnowledgeBuilderFactory.newKnowledgeBuilder(config)
    //kbuilder.add(ResourceFactory.newFileResource("/usr/local/DBNS/src/main/scala/com/rules/trojan/RuleV1.drl"), ResourceType.DRL)
    kbuilder.add(ResourceFactory.newFileResource("/usr/local/DBNS/mini.drl"), ResourceType.DRL)
    println("  [Debug]  Rule Error:"+kbuilder.getErrors().toString())
    var kbase : KnowledgeBase = KnowledgeBaseFactory.newKnowledgeBase()
    kbase.addKnowledgePackages(kbuilder.getKnowledgePackages())
    var ksession : StatefulKnowledgeSession = kbase.newStatefulKnowledgeSession()
    var logger : KnowledgeRuntimeLogger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession,"/usr/local/DBNS/drools.log")
    ksession
  }
}

