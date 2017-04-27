/usr/local/spark/bin/spark-submit \
--class "com.rpc.Broker" \
target/scala-2.10/simple-project_2.10-1.0.jar &

sleep 3

/usr/local/spark/bin/spark-submit \
--executor-memory 10G  \
--driver-memory 20G \
--master spark://spark-master:7077 \
--class "TrojanDProducer" \
--total-executor-cores 10 \
/usr/local/TrojanD/target/scala-2.10/simple-project_2.10-1.0.jar \
spark-master:9092 10 \
/usr/local/TrojanD/sample/TrojanD.txt & 

sleep 3

/usr/local/spark/bin/spark-submit \
--class "com.TrojanTestBackend" \
target/scala-2.10/simple-project_2.10-1.0.jar &

sleep 10

/usr/local/spark/bin/spark-submit \
--class "com.TrojanTestFrontend" \
target/scala-2.10/simple-project_2.10-1.0.jar

