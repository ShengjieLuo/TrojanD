./sbin/trojan/stopTroSingle.sh

echo "==========TrojanD=============="
echo "          Broker               "
echo "==============================="
/usr/local/spark/bin/spark-submit \
--class "com.rpc.Broker" \
target/scala-2.11/intelligent-trojan-detection_2.11-1.0.jar &

sleep 3

echo "==========TrojanD=============="
echo "          Data Producer        "
echo "==============================="
/usr/local/spark/bin/spark-submit \
--executor-memory 10G  \
--driver-memory 20G \
--master spark://master:7077 \
--class "TrojanDProducer" \
--total-executor-cores 10 \
/usr/local/TrojanD/target/scala-2.11/intelligent-trojan-detection_2.11-1.0.jar \
master:9092 1000 \
/usr/local/TrojanD/sample/TrojanDRisk.txt & 

sleep 3

echo "==========TrojanD=============="
echo "          Backend              "
echo "==============================="
/usr/local/spark/bin/spark-submit \
--class "com.TrojanDBackend" \
target/scala-2.11/intelligent-trojan-detection_2.11-1.0.jar &

sleep 10

echo "==========TrojanD=============="
echo "          FrontEnd             "
echo "==============================="
/usr/local/spark/bin/spark-submit \
--class "com.TrojanTestFrontend" \
target/scala-2.11/intelligent-trojan-detection_2.11-1.0.jar
