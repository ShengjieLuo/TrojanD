package com;

import org.apache.spark.mllib.clustering.{GaussianMixture, GaussianMixtureModel}
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.mllib.linalg.Vectors

object GaussianPredict {

  def main (args: Array[String]) {
    val conf = new SparkConf().setAppName("Gaussian Predict")
    val sc = new SparkContext(conf)

    val rawTestData = sc.textFile("file:///usr/local/TrojanD/sample/MLlib.txt")
    val parsedTestData = rawTestData.map(line => {
      Vectors.dense(line.split(" ").map(_.trim).filter(!"".equals(_)).map(_.toDouble))
    }).cache()

    var stat:Array[Int] = Array(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    val clusters = GaussianMixtureModel.load(sc,"file:///usr/local/TrojanD/GaussianModel") 
    var count = 1
    parsedTestData.collect().foreach(testDataLine => {
      val predictedClusterIndex:Int = clusters.predict(testDataLine)
      stat(predictedClusterIndex) = stat(predictedClusterIndex) + 1
      if ( count%100 == 0 ){
        println("The data " + testDataLine.toString + " belongs to cluster " + predictedClusterIndex)
      }
      count += 1
    })

    println("Spark MLlib K-means clustering test finished.")
    stat.foreach(item => println(item))
 }

}

