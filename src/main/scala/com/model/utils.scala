package com.model

import org.apache.spark.sql.{SQLContext, Row}
import org.apache.spark.sql.types._
import java.util.Properties
import java.text.SimpleDateFormat  
import java.util.Date
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import java.sql.{DriverManager, Connection} 

object SQLHelper{
  
  var value:Long = 0
  def queryId():Long = { value += 1; value - 1}
  def getTime():String = { new Date().toString }

  val schema = StructType(List(
    StructField("id",LongType,true),
    StructField("IP",StringType,true),
    StructField("UP_COUNT",LongType,true),
    StructField("DOWN_COUNT",LongType,true),
    StructField("UP_SIZE",LongType,true),
    StructField("DOWN_SIZE",LongType,true),
    StructField("UP_SMALL",LongType,true),
    StructField("UP_DOWN_COUNT_RATIO",DoubleType,true),
    StructField("UP_DOWN_SIZE_RATIO",DoubleType,true),
    StructField("SYN_RATIO",DoubleType,true),
    StructField("PSH_RATIO",DoubleType,true),
    StructField("SESSION_TIME",DoubleType,true),
    StructField("DNS",LongType,true),
    StructField("SMALL_RATIO",DoubleType,true),
    StructField("TIME",StringType,true)
  ))

  def getSchema():StructType = schema
}

object SQLWriter{

  val url="jdbc:MySQL://127.0.0.1:3306/trojanD"

  def send(data: Row): Unit = { 
    var conn: Connection= null 
    var ps:java.sql.PreparedStatement=null 
    val sql = "insert into target(id, IP, UP_COUNT, DOWN_COUNT, UP_SIZE, DOWN_SIZE, UP_SMALL, UP_DOWN_COUNT_RATIO, UP_DOWN_SIZE_RATIO, SYN_RATIO, PSH_RATIO, SESSION_TIME, DNS, SMALL_RATIO, TIME,KMEANS,BISECT,GMM) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
    conn=DriverManager.getConnection(url,"root","123456") 
    ps = conn.prepareStatement(sql) 
    ps.setLong(1,data.get(0).asInstanceOf[Long]) 
    ps.setString(2, data.get(1).asInstanceOf[String]) 
    ps.setLong(3, data.get(2).asInstanceOf[Long]) 
    ps.setLong(4, data.get(3).asInstanceOf[Long]) 
    ps.setLong(5, data.get(4).asInstanceOf[Long]) 
    ps.setLong(6, data.get(5).asInstanceOf[Long]) 
    ps.setLong(7, data.get(6).asInstanceOf[Long]) 
    ps.setDouble(8, data.get(7).asInstanceOf[Double]) 
    ps.setDouble(9, data.get(8).asInstanceOf[Double]) 
    ps.setDouble(10, data.get(9).asInstanceOf[Double]) 
    ps.setDouble(11, data.get(10).asInstanceOf[Double]) 
    ps.setDouble(12, data.get(11).asInstanceOf[Double])
    ps.setLong(13, data.get(12).asInstanceOf[Long])
    ps.setDouble(14, data.get(13).asInstanceOf[Double])
    ps.setString(15, data.get(14).asInstanceOf[String])
    ps.setInt(16,data.get(15).asInstanceOf[Int])
    ps.setInt(17,data.get(16).asInstanceOf[Int])
    ps.setInt(18,data.get(17).asInstanceOf[Int])
    ps.executeUpdate() //执行了Sql语句 
  }
}


object SQLClient{

  val prop = new Properties()
  val sparkConf = new SparkConf().setAppName("MySQL Client")
  val sc = new SparkContext(sparkConf)
  val sqlContext = new SQLContext(sc)
  
  /*MySQL table
  CREATE TABLE target(
    id int auto_increment primary key not null,
    IP varchar(20),
    UP_COUNT bigint,
    DOWN_COUNT bigint, 
    UP_SIZE bigint, 
    DOWN_SIZE bigint, 
    UP_SMALL bigint, 
    UP_DOWN_COUNT_RATIO double, 
    UP_DOWN_SIZE_RATIO double, 
    SYN_RATIO double,
    PSH_RATIO double, 
    SESSION_TIME double, 
    DNS bigint, 
    SMALL_RATIO double, 
    TIME datetime);*/

  val schema = StructType(List(
    StructField("id",LongType,true),
    StructField("IP",StringType,true),
    StructField("UP_COUNT",LongType,true),
    StructField("DOWN_COUNT",LongType,true),
    StructField("UP_SIZE",LongType,true),
    StructField("DOWN_SIZE",LongType,true),
    StructField("UP_SMALL",LongType,true),
    StructField("UP_DOWN_COUNT_RATIO",DoubleType,true),
    StructField("UP_DOWN_SIZE_RATIO",DoubleType,true),
    StructField("SYN_RATIO",DoubleType,true),
    StructField("PSH_RATIO",DoubleType,true),
    StructField("SESSION_TIME",DoubleType,true),
    StructField("DNS",LongType,true),
    StructField("SMALL_RATIO",DoubleType,true),
    StructField("TIME",StringType,true)
  ))    

  def init(){
    prop.put("user", "root")
    prop.put("password", "123456")
    prop.put("driver","com.mysql.jdbc.Driver")
  }
  
  init()

  def getContext():SQLContext = sqlContext
  def getSC():SparkContext = sc
  def getProp():Properties = prop

  def send(data:List[Row]){
    var dataset = sqlContext.createDataFrame(sc.parallelize(data),schema)
    dataset.write.mode("append").jdbc("jdbc:mysql://localhost:3306/trojanD", "trojanD.target", prop)
  }

}
