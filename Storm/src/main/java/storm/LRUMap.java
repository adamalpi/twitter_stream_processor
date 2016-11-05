package storm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LRUMap<K, V> extends LinkedHashMap<K, V> {  
	private int maxCapacity;     

	public LRUMap(int maxCapacity)       {           
		super(0, 0.75F, true);          
		this.maxCapacity = maxCapacity;      
	}
	@Override  
	protected boolean removeEldestEntry(Map.Entry<K, V> eldest)       {    
		return size() >= this.maxCapacity;     
	} 

	public static void main(String[] args) {
		LRUMap<Integer,HashMap<String, Integer>> lru = new LRUMap<Integer,HashMap<String, Integer>>(4);
		
		lru.put(1,(new HashMap<String, Integer>()));
		lru.put(2,(new HashMap<String, Integer>()));
		lru.put(3,(new HashMap<String, Integer>()));
		lru.put(4,(new HashMap<String, Integer>()));
		lru.get(2).put("test2", 1);
		lru.get(4);
		lru.get(3).put("test3", 1);
		lru.put(1,(new HashMap<String, Integer>()));
		if (lru.containsKey(2)){
			lru.get(2).put("test2", 1);
		}
		
		
	    for (Map.Entry<Integer, HashMap<String, Integer>> entry : lru.entrySet()) {
	        String key = entry.getKey().toString();
	        HashMap<String, Integer> value = entry.getValue();
	        System.out.print("key, " + key + " values: " );
	        for (Map.Entry<String, Integer> entry2 : value.entrySet()) {
		        String key2 = entry2.getKey().toString();
		        Integer value2 = entry2.getValue();
		        System.out.print("key, " + key2 + " value, " + value2 );
		    }
	        System.out.println();
	    }

	}


}