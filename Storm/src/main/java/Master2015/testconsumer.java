package Master2015;

import java.util.Arrays;

public class testconsumer {
	
	public static void main(String[] args) throws Exception{
		String a = "12341234:test";
		String b = "123412341";
		String[] parts = a.split(":");
		//System.out.println(parts[-1]);
		if (parts.length >1){
			for (String c: Arrays.copyOfRange(parts, 1, parts.length)){
				System.out.println(c);
			}
		}

	}

	

}
