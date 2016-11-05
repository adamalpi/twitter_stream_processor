package hbase;

public class Rank {
	
	private String[] hashtag;
	private int[] count;
	private String lang;
	private Long timestamp;


	public Rank(String s){
		String[] c = (s).split(",");

		this.timestamp = Long.parseLong(c[0]);
		this.lang = c[1];
		
		int n = (c.length-2)/2;
		this.hashtag = new String[n];
		this.count = new int[n];
		
		for (int i = 0; i<n;i++){
			this.hashtag[i]=c[2+i*2];
			this.count[i]=Integer.parseInt(c[3+i*2]);
			
		}

	}


	public String[] getHashtag() {
		return hashtag;
	}


	public int[] getCount() {
		return count;
	}


	public String getLang() {
		return lang;
	}


	public Long getTimestamp() {
		return timestamp;
	}




}


