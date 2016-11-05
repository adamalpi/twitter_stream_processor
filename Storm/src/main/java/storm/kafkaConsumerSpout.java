package storm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import kafka.consumer.ConsumerIterator;

public class kafkaConsumerSpout extends BaseRichSpout {
	private String lang;
	private String zookeeper;
	private ConsumerIterator<byte[], byte[]> stream;
	private SpoutOutputCollector collector;
	private ConsumerKafka consumer;

	public kafkaConsumerSpout(String lang, String zookeeper) {
		this.lang = lang;
		this.zookeeper = zookeeper;
	}
	
	public void open(Map arg0, TopologyContext arg1, SpoutOutputCollector collector) {
		this.collector=collector;
		System.out.println("abriendo kafka lang "+lang+"...");
		consumer = new ConsumerKafka(zookeeper, "Twitter", lang);
        stream = consumer.run();
        System.out.println( "kafka lang "+lang+" preparado.");
		
		
	}

	public void nextTuple() {
		String message;
		if (stream.hasNext()){
			System.out.println("next");
			message=new String(stream.next().message());
			String[] hashtags = (message).split(":");//[0]timestamp and hashtag
			if (hashtags.length >1){
				for (String hashtag: Arrays.copyOfRange(hashtags, 1, hashtags.length)){
					System.out.println("Spout emit: "+hashtags[0]+ " "+hashtag);
					collector.emit(new Values(hashtags[0], hashtag));
				}
			}
		}
	}

	

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare( new Fields("timestamp","hashtag"));
		
	}
	
	public static void main(String[] args) throws Exception{
		ConsumerKafka consumer = new ConsumerKafka("localhost:2181", "Twitter", "en");
		ConsumerIterator<byte[], byte[]> stream = consumer.run();

		String message;
		while (stream.hasNext()){

			message=new String(stream.next().message());
			System.out.println(message);
			String[] parts = (message).split(":");//timestamp and hashtag
			long a=Long.parseLong(parts[0])+1;
			System.out.println(a+":"+parts[1]);
			//collector.emit(new Values(parts[0], parts[1]));
		}
		System.out.println("fin");
	}
	
}
