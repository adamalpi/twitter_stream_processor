package storm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class hashtagCounterBolt extends BaseRichBolt {
	private OutputCollector collector;
	private int size;
	private int advance;
	private LRUMap<Long,HashMap<String, Integer>> counts; //timestamp, (hashtag, count)
	
	public hashtagCounterBolt(int size, int advance) {
		this.size=size;
		this.advance=advance;
	}

	public void execute(Tuple tuple) {
		String hashtag = (String)tuple.getValueByField("hashtag");
		String windowS= (String)tuple.getValueByField("window");

		Long window=Long.parseLong(windowS);

		System.out.println("HCBolt receive: "+window+ " "+hashtag);
		HashMap<String, Integer> hashCount;
		int count=1;
		if (counts.containsKey(window)){
			hashCount=counts.get(window);
			if(hashCount.containsKey(hashtag)){
				count+=hashCount.get(hashtag);
			}
		}
		else {
			hashCount = new HashMap<String, Integer>();
			counts.put(window, hashCount);	
		}
		hashCount.put(hashtag, count);
		System.out.println("HCBolt emit: "+hashtag+ " "+window+" "+count);
		collector.emit(new Values(hashtag, Long.toString(window), Integer.toString(count)));

	}

	public void prepare(Map arg0, TopologyContext arg1, OutputCollector collector) {
		this.collector= collector;
		counts = new LRUMap<Long,HashMap<String, Integer>>(size/advance+10);

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("hashtag","window","count"));
		

	}

}
