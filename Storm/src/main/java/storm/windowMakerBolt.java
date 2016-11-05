package storm;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class windowMakerBolt extends BaseRichBolt  {

	private int size;
	private int advance;
	private long scout;
	private WindowContainer wc;
	private OutputCollector collector;
	
	public windowMakerBolt(int size, int advance) {
		this.size=size;
		this.advance=advance;
		this.scout=0L;
		
	}
	
	@Override
	public void execute(Tuple tuple) {
		String hashtag=(String)tuple.getValueByField("hashtag");
		String timestamp=(String)tuple.getValueByField("timestamp");
		System.out.println("WMBolt receive: "+timestamp+ " "+hashtag);
		if (this.scout>Long.parseLong(timestamp)){
			System.out.println("WMBolt OUT OF ORDER WARNING!!!");
		}

			this.scout=Long.parseLong(timestamp);
			List<Long> cs=wc.getContainers(Long.parseLong(timestamp));
			//Arrays.toString(cs);
			for (long c:cs){
				System.out.println("WMBolt emit: "+c+ " "+hashtag);
				collector.emit(new Values(Long.toString(c),hashtag));
			}

			
	}
	
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector collector) {
		this.collector = collector;
		this.wc = new WindowContainer(this.size, this.advance);
	}
	

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("window","hashtag"));
		
	}


}
