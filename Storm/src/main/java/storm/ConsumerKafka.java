package storm;

import java.util.Properties;
import java.util.UUID;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class ConsumerKafka{
	private final ConsumerConnector consumer;
	private final String topic;
	private ExecutorService executor;
	public ConsumerKafka(String a_zookeeper, String a_groupId, String a_topic) {
		consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
				createConsumerConfig(a_zookeeper, a_groupId));
		this.topic = a_topic;
	}

	public ConsumerIterator<byte[], byte[]> run(){
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic,1);
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
		List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

		return streams.get(0).iterator();
		//		ConsumerIterator<byte[], byte[]> it = streams.get(0).iterator();
		//		while (it.hasNext())
		//			System.out.println("Received : " + new String(it.next().message()));

	}

	//	public void run() {
	//		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
	//		topicCountMap.put(topic,1);
	//		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
	//		List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
	//
	//		ConsumerIterator<byte[], byte[]> it = streams.get(0).iterator();
	//		System.out.println("hey");
	//		while (it.hasNext()){
	//			System.out.println("hey2");
	//			System.out.println("Received : " + new String(it.next().message()));
	//		}
	//		System.out.println("hey3");
	//
	//	}

	private kafka.consumer.ConsumerConfig createConsumerConfig(String a_zookeeper, String a_groupId) {
		Properties props = new Properties();
		props.put("zookeeper.connect", a_zookeeper);
		//props.put("group.id", a_groupId);
		props.put("zookeeper.session.timeout.ms", "400");
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "1000");
		props.put("group.id", UUID.randomUUID().toString());

		return new ConsumerConfig(props);
	}

	public static void main(String[] args) {
		String zooKeeper = "localhost:2181";
		String groupId = "Twitter";
		String topic = "en";
		int threads = 1;

		ConsumerKafka example = new ConsumerKafka(zooKeeper, groupId, topic);
		//		try {
		example.run();



		//			//Thread.sleep(10000);
		//		} catch (InterruptedException ie) {
		//
		//		}
	}

}