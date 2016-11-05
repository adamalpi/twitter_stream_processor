package kafka;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class TweetReaderStream extends TweetReader{
	private OAuthService service;
	private Token accessToken;

	public TweetReaderStream(String apiKey,String apiSecret,String tokenValue, String tokenSecret){
		super();
		//To connect to Twitter
		service = new ServiceBuilder()
				.provider(TwitterApi.class)
				.apiKey(apiKey)
				.apiSecret(apiSecret)
				.build();
		
		// Set your access token
		accessToken = new Token(tokenValue, tokenSecret);

	}

	@Override
	public void connect(int maxTweets, long timeLimit) {
		// Let's generate the request
        System.out.println("Connecting to Twitter Public Stream");
        
        this.setMaxTweets(maxTweets);
        this.setTimeLimit(timeLimit);
        
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://stream.twitter.com/1.1/statuses/sample.json");
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        
     // Create a reader to read Twitter's stream
        this.setReader(new BufferedReader(new InputStreamReader(response.getStream())));
	}

}