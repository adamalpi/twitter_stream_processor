package kafka;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class Main {

	public static void main(String[] args) {
		/*	
		String apiKey="Pu4b7cNSxQjSIzfjTf6sLVb80";
		String apiSecret="j4n6iJXnqJxjI9QcnAdpLYzu14dQF3jNUoylkYQK6nfsrRXZZs";
		String tokenValue="3140835573-HNr2yeRhWvGvtzijQyDB5lTMsbXpSNcNFbVQyXz";
		String tokenSecret="nUpGBH9Ucl5acBkyEI49RicsG1tZg9P5mZDQNIFJkaCBP";
		String brokerList="localhost:9092";
		 */
		if (args.length==0){
			System.out.println("You need to put at least 3 arguments");
		}
		else{
			int mode=3;
			try {
				mode = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.err.println("Argument n°1: " + args[0] + " must be an integer.");
				System.exit(1);
			}
			if (mode!=1 && mode!=2){
				System.out.println("First argument must be 1 or 2 not "+args[0]);
			}
			else{
				Properties props = new Properties();
				String tweet;
				if ( mode==1){
					if (args.length < 7){
						System.out.println("You have to give 2 arguments for the mode 1 : the file path and the brokerList");
					}
					else{
						String filename=args[6];
						String brokerList=args[5];
						//PRODUCER CONFIG
						props.put("metadata.broker.list",brokerList);
						props.put("serializer.class", "kafka.serializer.StringEncoder");
						ProducerConfig config = new ProducerConfig(props);	

						final Producer<String, String> producer = new Producer<String, String>(config);

						TweetReader trs = new TweetReaderFile(filename);
						trs.connect();
						
						String line;

						Tweet t;

						try {
							while ((line = trs.getNextTweet()) != null) {
								t= new Tweet(line);
								String s=""+t.getTimestamp();
								for (String tag : t.getHashtags()){
									s+=":"+tag;
								}
								System.out.println("Tweet="+s);
								System.out.println(t.getLanguage());
								KeyedMessage<String, String> data = new KeyedMessage<String, String>(t.getLanguage(),s);
								producer.send(data);
							}
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							trs.disconnect();
						}
					}
				}
				if(mode==2){
					if (args.length<6){
						System.out.println("You have to give 6 arguments : apiKey, apiSecret, tokenValue, tokenSecret and the brokerList");
					}
					else{
						String apiKey=args[1];
						String apiSecret=args[2];
						String tokenValue=args[3];
						String tokenSecret=args[4];
						String brokerList=args[5];

						//PRODUCER CONFIG
						props.put("metadata.broker.list",brokerList);
						props.put("serializer.class", "kafka.serializer.StringEncoder");
						ProducerConfig config = new ProducerConfig(props);	

						final Producer<String, String> producer = new Producer<String, String>(config);

						//Time in ms of the stream
						long timing =10000;
						int max = 80;

						TweetReaderStream trs= new TweetReaderStream(apiKey, apiSecret, tokenValue, tokenSecret);
						//trs.connect(max, timing);
						trs.connect();

						String line;

						Tweet t;

						try {
							while ((line = trs.getNextTweet()) != null) {
								t= new Tweet(line);
								String s=""+t.getTimestamp();
								for (String tag : t.getHashtags()){
									s+=":"+tag;
								}
								System.out.println("Tweet="+s);
								System.out.println(t.getLanguage());
								KeyedMessage<String, String> data = new KeyedMessage<String, String>(t.getLanguage(),s);
								producer.send(data);
							}
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							trs.disconnect();
						}
					}

				}

			}
		}



	}
}