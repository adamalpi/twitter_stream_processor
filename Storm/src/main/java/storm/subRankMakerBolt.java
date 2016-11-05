package storm;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public class subRankMakerBolt implements IRichBolt {

	public subRankMakerBolt(int ntop) {
		// TODO Auto-generated constructor stub
	}

	public void cleanup() {
		// TODO Auto-generated method stub

	}

	public void execute(Tuple arg0) {
		// TODO Auto-generated method stub

	}

	public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
		// TODO Auto-generated method stub

	}

	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		// TODO Auto-generated method stub

	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
