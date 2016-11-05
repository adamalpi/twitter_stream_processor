package Master2015;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;
import storm.StormRunner;
import storm.hashtagCounterBolt;
import storm.kafkaConsumerSpout;
import storm.rankMakerBolt;
import storm.windowMakerBolt;


public class Top3App {
	
	private static void deleteDir(File file) {
	    File[] contents = file.listFiles();
	    if (contents != null) {
	        for (File f : contents) {
	            deleteDir(f);
	        }
	    }
	    file.delete();
	}

	public static void main(String[] args){
//		String [] langs= (args[0]).split(",");
//		String zookeeper= args[1];
//		String [] params= (args[2]).split(",");
//		int size = Integer.parseInt(params[0])*1000;
//		int advance = Integer.parseInt(params[1])*1000;
//		String topologyName = args[3];
//		String path = args[4];
		
		String topologyName = "Top3App";
		int runtimeInSeconds = 100;
		String [] langs={"en","es"};
		int size = 100*1000;
		int advance = 50*1000;
		String path = "/tmp/stormTest/";
		String zookeeper = "localhost:2181";

		int ntop = 3;

		TopologyBuilder topology = new TopologyBuilder();

		String filePath; 
		String spoutID,windowID,counterID,rankID;



		File theDir = new File(path);

		// if the directory does not exist, create it
		try{
			if (theDir.exists()) {
				deleteDir(theDir);
			}
			System.out.println("creating directory: " + path);
			boolean result = false;

			theDir.mkdir();
			result = true;
			if(result)  
				System.out.println("DIR created");  
		} 
		catch(SecurityException se){se.printStackTrace();} 
     
		
		for (String lang: langs){
			filePath = path+"/"+lang+"_15.log";
			spoutID = "kafkaConsumer_"+lang;
			windowID = "windowMaker_"+lang;
			counterID = "hashtagCounter_"+lang;
			//subRankID = "subRankMaker_"+lang;
			rankID = "rankMaker_"+lang;
			
			topology.setSpout(spoutID, new kafkaConsumerSpout(lang, zookeeper));
			topology.setBolt(windowID, new windowMakerBolt(size,advance)).globalGrouping(spoutID);
			topology.setBolt(counterID, new hashtagCounterBolt(size,advance)).globalGrouping(windowID);
			topology.setBolt(rankID, new rankMakerBolt(size,advance,filePath,lang,ntop)).globalGrouping(counterID);
//			topology.setSpout(spoutID, new kafkaConsumerSpout(lang, zookeeper));
//			topology.setBolt(windowID, new windowMakerBolt(size,advance)).localOrShuffleGrouping(spoutID);
//			topology.setBolt(counterID, new hashtagCounterBolt(size,advance)).fieldsGrouping(windowID, new Fields("window"));
//			topology.setBolt(rankID, new rankMakerBolt(size,advance,filePath,lang,ntop)).globalGrouping(counterID);
		}

		Config topologyConfig = new Config();
		//topologyConfig.setDebug(true);
		//topologyConfig.setMaxTaskParallelism(3);
		//StormRunner.runTopologyLocally(topology.createTopology(), topologyName, topologyConfig, runtimeInSeconds);
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology(topologyName, topologyConfig, topology.createTopology());
		Utils.sleep((long) 3600 * 1000);
		cluster.killTopology(topologyName);
		cluster.shutdown();

		//System.out.println("The program has ended");
//		try {
//			StormRunner.runTopologyRemotely(topology.createTopology(), topologyName, topologyConfig);
//		} catch (AlreadyAliveException e) {
//			e.printStackTrace();
//		} catch (InvalidTopologyException e) {
//			e.printStackTrace();
//		}


		/* Bolt1: Breaks tweets and makes tuple(timestamp, hashtag)
		 * Bolt2: Piles up tuples and makes tuple(timestamp,hashtag) //the timestamp in caracteristic to the interval
		 * Bolt3: Counts word for an interval and emits the new count (hashta, count)
		 * Bolt4: Saves the best 3 counts and updates it according to its entries. writes the file
		 * 
		 * */
	}
}