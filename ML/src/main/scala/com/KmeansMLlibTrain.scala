package com

import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.clustering.KMeansModel
import org.apache.spark.sql.SparkSession
import org.apache.spark._
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.rdd.RDD
import java.io._

object KmeansMLlibTrain{

  def main(args : Array[String]):Unit = {
    val sparkconf = new SparkConf().setAppName("KMeansTrain")
    val sc = new SparkContext(sparkconf)

    //Step1 : Load the data & Save the model
    val data = sc.textFile("file:///usr/local/TrojanD/sample/MLlib.txt")
    val parsedData = data.map(s => Vectors.dense(s.split(' ').map(_.toDouble))).cache()
    val model = KMeans.train(parsedData, 15, 100)
    val WSSSE = model.computeCost(parsedData)
    println("Within Set Sum of Squared Errors = " + WSSSE)
    model.save(sc, "file:///usr/local/TrojanD/KMeansModel")

    //Step2 : Predict the original data & Count the cluster number
    val rawTestData = sc.textFile("file:///usr/local/TrojanD/sample/MLlib.txt")
    val parsedTestData = rawTestData.map(line => {
      Vectors.dense(line.split(" ").map(_.trim).filter(!"".equals(_)).map(_.toDouble))
    }).cache()
    var stat:Array[Int] = Array(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    val clusters = KMeansModel.load(sc,"file:///usr/local/TrojanD/KMeansModel")
    parsedTestData.collect().foreach(testDataLine => {
      val predictedClusterIndex:Int = clusters.predict(testDataLine)
      stat(predictedClusterIndex) = stat(predictedClusterIndex) + 1
      //println("The data " + testDataLine.toString + " belongs to cluster " + predictedClusterIndex)
    })
    println("Spark MLlib K-means clustering test finished.")

    //Step3 : Find the problem cluster
    val problemWriter = new PrintWriter(new File("../sample/KMeansMeta/ProblemClusters.txt"))
    val accum = sc.accumulator(0)
    val order = sc.accumulator(0)
    var problemStr = "Problem: "
    stat.foreach( item => accum += item)
    stat.foreach( item => {
      if ( item/accum.value.toDouble < 0.01 ) {
         problemStr = problemStr + order.value.toString + " "}
      order += 1
    })
    problemWriter.write(problemStr)
    problemWriter.close()
    
    /*// Shows the result. It can only be used in spark.ml. >2.0 VERSION.
    val metaDataWriter = new PrintWriter(new File("../sample/MetaData.txt")) 
    println("Cluster Centers: ")
    model.clusterCenters.foreach(println)
    println(model.summary.k)
    val accum1 = sc.accumulator(0)
    val accum2 = sc.accumulator(0) 
    model.summary.clusterSizes.foreach(x => accum1 += x.toInt)
    model.summary.clusterSizes.foreach(csize => { 
      println("Kind: " + accum2.value + " Number: "+csize)
      if (csize < 0.01 * accum1.value) {metaDataWriter.write("Kind: " + accum2.value + " Number: "+csize)}

    })
    //model.summary.predictions.foreach(println)
    metaDataWriter.close()*/
  }
}
