/usr/local/spark/bin/spark-submit \
--executor-memory 10G  \
--driver-memory 20G \
--master spark://master:7077 \
--class "TrojanDProducer" \
--total-executor-cores 10 \
/usr/local/TrojanD/target/scala-2.11/intelligent-trojan-detection_2.11-1.0.jar \
master:9092 1000 \
/usr/local/TrojanD/sample/TrojanD.txt \
