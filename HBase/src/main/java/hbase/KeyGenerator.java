package hbase;

import org.apache.hadoop.hbase.util.Bytes;

public class KeyGenerator {

	public byte[] generateKey(String lang, long timestamp) {
		byte[] key = new byte[10];
		System.arraycopy(Bytes.toBytes(timestamp),0,key,0,8);
		System.arraycopy(Bytes.toBytes(lang),0,key,8,2);
		return key;
	}

	byte[] generateStartKey(long ts) {
		byte[] key = new byte[10];
		System.arraycopy(Bytes.toBytes(ts),0,key,0,8);
		for (int i = 8; i < 10; i++){
			key[i] = (byte)-255;
		}
		return key;
	}

	byte[] generateEndKey(long ts) {
		byte[] key = new byte[10];
		System.arraycopy(Bytes.toBytes(ts),0,key,0,8);
		for (int i = 8; i < 10; i++){
			key[i] = (byte)255;
		}
		return key;
	}


}
