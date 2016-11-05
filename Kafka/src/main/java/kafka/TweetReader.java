package kafka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class TweetReader extends Thread {
	private BufferedReader reader;
	private int numberOfTweets;
	private String outputFile;
	private int maxTweets;//if 0 infinite
	private long timeLimit;//if 0 infinite
	private long timestamp;
	
	public TweetReader(){
		this.numberOfTweets=0;
		this.timestamp=System.currentTimeMillis();
	}
	
	
	abstract void connect(int maxTweets, long timeLimit);
	
	public void disconnect(){
		try {
			this.reader.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			System.out.println("Disconnecting, thank you for playing. The total of read Tweets is: "+this.numberOfTweets );
		}
		
	}
	public String getNextTweet() throws IOException{
		String line=null;
		this.numberOfTweets++;
		
		if(this.maxTweets==0 && this.timeLimit==0){
			line = this.reader.readLine();
		}
		else if (this.maxTweets!=0 && this.numberOfTweets>this.maxTweets){
			line = null;
			this.disconnect();
		}
		else if (this.timeLimit!=0 && System.currentTimeMillis()-this.timestamp > this.timeLimit){
			line=null;
			this.disconnect();
		}
		else {
			line =this.reader.readLine();
		}
		if (line==null) this.numberOfTweets--;
		return line;
		
	}

	public void connect(int maxTweets){
		this.connect(maxTweets, 0);
	}
	
	public void connect(long timeLimit){
		this.connect(0, timeLimit);
	}
	
	public void connect(){
		this.connect(0, 0);
	}
	
	public void writeToFile(){
		if (reader==null){
			this.connect();
		}
		String output= (this.outputFile==null)?	"./TwitterFile" : this.outputFile;
		long time= (this.timeLimit==0)?	5000 : this.timeLimit;
		int max= (this.maxTweets==0)? 100 : this.maxTweets;
		
		File f = new File(output);
		String path= f.getAbsolutePath();
		System.out.println("The file path is: "+path+ " | The time limit is: ~"+time+"ms | The number of tweet is: "+max);
		System.out.println("This params can be configured!!");
		if(!f.exists()) {
		    f.delete();
		}
		System.out.println("We start! :)");
		try {
			f.createNewFile();
			FileWriter fw = new FileWriter(f);
	        try {
	            String line;
	        	while ((line = this.getNextTweet()) != null && System.currentTimeMillis()-this.timestamp < time && max > this.numberOfTweets-1) {
        			fw.write(line+"\n");  
	        	}
	        } finally {
	            fw.close();
	        }
		
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		System.out.println("Done!! The total lines is: "+this.numberOfTweets+". Enjoy your File ;)");
	}
	
	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}
	public String getOutputFile() {
		return outputFile;
	}
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}
	public void setMaxTweets(int maxTweets) {
		this.maxTweets = maxTweets;
	}
	public void setTimeLimit(long timeLimit) {
		this.timeLimit = timeLimit;
	}
	public int getNumberOfTweets() {
		return this.numberOfTweets;
	}

	
}