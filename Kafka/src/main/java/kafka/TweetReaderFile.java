package kafka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class TweetReaderFile extends TweetReader {
	private File f;
	
	public TweetReaderFile(String pathFile){
		super();
		this.f=new File(pathFile);
	}
	
	
	@Override
	void connect(int maxTweets, long timeLimit) {
		// Let's generate the request
        System.out.println("Openning Twitter File");
        
        this.setMaxTweets(maxTweets);
        this.setTimeLimit(timeLimit);
        
		try {
			this.setReader(new BufferedReader(new FileReader(this.f)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	

}