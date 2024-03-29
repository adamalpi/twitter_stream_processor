package kafka;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Tweet {
	private String text;
	private Set<String> hashtags;
	private long timestamp;
	private String language;
	//Variable useless for the moment
	//private String hashtag;

	public Tweet(String s){
		JsonNode rootNode;

		ObjectMapper mapper = new ObjectMapper(); // create once, reuse
		try {
			rootNode = mapper.readValue(s, JsonNode.class);
			//we fill the hashtags attr with the parsed hashtags of the tweet
			this.hashtags = new HashSet<String>();
			if(!s.contains("{\"delete\":")){// Check if there entities in the tweet (avoid crash if someone delete a tweet
				JsonNode hashtagsNode = rootNode.get("entities").get("hashtags");
					if (hashtagsNode.isArray()) {
						for (JsonNode hashNode : hashtagsNode) {
							this.hashtags.add(hashNode.get("text").asText());
						}
					}
					//The same for the lang and timestamp attrs
					this.language= rootNode.get("lang").asText();
					this.timestamp= rootNode.get("timestamp_ms").asLong();
			}
			else{
				this.language="error";
				this.hashtags = new HashSet<String>();
				this.timestamp=0;
			}
			


		
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Set<String> getHashtags() {
		return hashtags;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getLanguage() {
		return language;
	}


}


