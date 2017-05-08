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
import com.model.Item
import com.model.other.Type
import com.model.problem.Trojan
import com.service.TroDItem

import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming.kafka.KafkaUtils
import scala.util.matching.Regex

//This progrma is only used in test, DO NOT incude in the released version

object TrojanDTest {

  def main(args : Array[String]):Unit ={
    
    println("Run Trojan Test by Distributed Rule Engine")
    val sparkconf = new SparkConf().setAppName("TrojanDetectionTest")
    val sc = new SparkContext(sparkconf)

    val data = List("202.121.66.121 syn 35 104 up 104 7173 small 42 psh 199 199 down 199 15216 small 106 dns 2255 0 com 3 0 14")
    println("  [Debug] Begin Execution!")

    val pairRDD = sc.parallelize(data.map(p=>p.trim),1)
                    .mapPartitions{ partition => {
			println("  [Debug] Load the knowledge session");
			var ksession:StatefulKnowledgeSession = GetKnowledgeSession()
			val time = new Time();
			ksession.insert(time);
			val ty   = new Type();
			ksession.insert(ty);
			
                        val newPartition = partition.map(p => {
			  println("  [Debug] Begin Dealing the element: "+p);
			  val item = new TroDItem("IP-hostmachine",p.split(" ")(0));
			  item.setFromDataLine(p);
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
  }

  def GetKnowledgeSession() : StatefulKnowledgeSession = {
    val config:KnowledgeBuilderConfiguration = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration()
    config.setProperty("drools.dialect.mvel.strict", "false")
    var kbuilder : KnowledgeBuilder  = KnowledgeBuilderFactory.newKnowledgeBuilder(config)
    kbuilder.add(ResourceFactory.newFileResource("/usr/local/TrojanD/rules/trojanD.drl"), ResourceType.DRL)
    println("  [Debug]  Rule Error:"+kbuilder.getErrors().toString())
    var kbase : KnowledgeBase = KnowledgeBaseFactory.newKnowledgeBase()
    kbase.addKnowledgePackages(kbuilder.getKnowledgePackages())
    var ksession : StatefulKnowledgeSession = kbase.newStatefulKnowledgeSession()
    var logger : KnowledgeRuntimeLogger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession,"/usr/local/DBNS/drools.log")
    ksession
  }
}

