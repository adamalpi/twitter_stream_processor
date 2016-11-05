package storm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Collator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class rankMakerBolt extends BaseRichBolt {
	private int size;
	private int advance;
	private int ntop;
	private String lang;
	private String path;
	private WindowContainer wc;
	private NavigableMap<Long,TreeMap<String, Integer>> rank;

	public rankMakerBolt(int size, int advance, String path, String lang, int ntop) {
		this.size=size;
		this.advance=advance;
		this.ntop=ntop;
		this.path=path;
		this.lang=lang;
	}

	public void prepare(Map arg0, TopologyContext arg1, OutputCollector collector) {
		this.rank = new TreeMap<Long,TreeMap<String, Integer>>();
		this.wc = new WindowContainer(this.size, this.advance);
	}

	@Override
	public void execute(Tuple tuple) {
		String windowS= (String)tuple.getValueByField("window");
		long window = Long.parseLong(windowS);
		String hashtag = (String)tuple.getValueByField("hashtag");
		String countedS = (String)tuple.getValueByField("count");
		int counted = Integer.parseInt(countedS);
		System.out.println("RMBolt receive: "+hashtag+ " "+window+" "+counted);
		TreeMap<String, Integer> windowRank;
		long lastWindow = window-this.size+this.advance;
		Map.Entry<Long,TreeMap<String, Integer>> entry;
		
		while ( (entry=rank.firstEntry())!=null && entry.getKey() < lastWindow){
			//Time to write a new line with the last rank
			rank.get(entry.getKey()).remove(".!.MYHASHTAGMINCOUNT.!.");
			String line=entry.getKey()+","+lang;
			int i=0;
			for (Entry<String, Integer> subEntry : entriesSortedByValues(entry.getValue())){
				line+=","+subEntry.getKey()+","+subEntry.getValue();
				i++;
			}

			while (i<this.ntop){
				line+=",null,0";
				i++;
			}

			File f = new File(this.path);
			
			try {
				if(!f.exists()) 
					f.createNewFile();
				FileWriter fw = new FileWriter(f,true); //the true will append the new data
				fw.write(line+"\n");//appends the string to the file
				fw.close();
				System.out.println("new line: "+line);
			}
			catch(IOException ioe) {
				System.err.println("IOException: " + ioe.getMessage());
			}
			rank.remove(entry.getKey());
		}
		if(!rank.containsKey(window)){
			
			rank.put(window, new TreeMap<String,Integer>(Collator.getInstance()));
			rank.get(window).put(".!.MYHASHTAGMINCOUNT.!.", 0);
		}
		windowRank=rank.get(window);
		if (windowRank.size()>this.ntop){
			int myCount;
			if ((myCount=windowRank.get(".!.MYHASHTAGMINCOUNT.!."))<=counted){ //we must update de rank
				windowRank.remove(".!.MYHASHTAGMINCOUNT.!.");
				if (windowRank.containsKey(hashtag)){
					if (windowRank.get(hashtag)<=myCount){
						windowRank.put(hashtag, counted);
						windowRank.put(".!.MYHASHTAGMINCOUNT.!.",entriesSortedByValues(windowRank).last().getValue());
					} else {
						windowRank.put(hashtag, counted);
						windowRank.put(".!.MYHASHTAGMINCOUNT.!.",myCount);
					}
				} else {
					windowRank.put(hashtag, counted);
					windowRank.remove(entriesSortedByValues(windowRank).last().getKey());
					windowRank.put(".!.MYHASHTAGMINCOUNT.!.",entriesSortedByValues(windowRank).last().getValue());
				}
			}
		} else {
			windowRank.put(hashtag, counted);
			if (windowRank.get(".!.MYHASHTAGMINCOUNT.!.")>counted){
				windowRank.put(".!.MYHASHTAGMINCOUNT.!.",counted);
			}
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		// nothing to do

	}

	static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
		SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
				new Comparator<Map.Entry<K,V>>() {
					public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
						int res = ((Integer)e1.getValue()).compareTo((Integer) e2.getValue());
						if (e1.getKey().equals(e2.getKey())) {
							return res; // Code will now handle equality properly
						} else {
							return res != 0 ? -1*res : 1; // While still adding all entries
						}
					}
				}
				);
		sortedEntries.addAll(map.entrySet());
		return sortedEntries;
	}
	


	public static void main(String[] args) {

		NavigableMap<Long,TreeMap<String, Integer>> lru= new TreeMap<Long,TreeMap<String, Integer>>();
		lru.put((long) 1, new TreeMap<String, Integer>());
		lru.put((long) 2,(new TreeMap<String, Integer>(Collator.getInstance())));
		lru.put((long) 3,(new TreeMap<String, Integer>()));
		lru.put((long) 4,(new TreeMap<String, Integer>()));
		lru.get((long) 2).put("test2", 2);
		lru.get((long) 2).put("test6", 2);
		lru.get((long) 2).put("Free", 3);
		lru.get((long) 2).put("hollow", 3);
		lru.get((long) 2).put("hollow", 4);

		lru.get((long) 2).put("test5", 4);
		lru.get((long) 2).put("SoundsLiveFeelsLive", 3);
		lru.get((long) 2).put("MUFC", 3);


		lru.get((long)4);
		lru.get((long)3).put("test3", 1);
		lru.put((long) 1,(new TreeMap<String, Integer>()));
		if (lru.containsKey((long)2)){
			lru.get((long)2).put("test2", 1);
		}


		//		for (Map.Entry<String, Integer> entry2 : lru.get((long)2).entrySet()) {
		//			String key2 = entry2.getKey().toString();
		//			Integer value2 = entry2.getValue();
		//			System.out.println("key: " + key2 + "| value: " + value2 );
		//		}
		Map.Entry<Long,TreeMap<String, Integer>> entry;
		while ( (entry=lru.firstEntry())!=null && entry.getKey() <= (long)4){
		//while (((entry=lru.floorEntry((long)3)))!=null){
			System.out.println(entry.getKey());
			for (Entry<String, Integer> subEntry  : entriesSortedByValues(entry.getValue())) {
				System.out.println(subEntry.getKey()+":"+subEntry.getValue());
			}
			lru.remove(entry.getKey());
		}
		//System.out.println(lru.floorEntry((long)0).getKey());
		Collator c = Collator.getInstance();
		System.out.println(c.compare("SoundsLiveFeelsLive", "MUFC"));
		//SortedSet<Entry<String, Integer>> otrotest = entriesSortedByValues(lru.get((long)2));
		//System.out.println(otrotest.first().getKey());


	}




}
