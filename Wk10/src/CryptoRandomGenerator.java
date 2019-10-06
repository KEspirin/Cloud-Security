import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

import org.apache.commons.crypto.random.*;

public class CryptoRandomGenerator {
	
	
	public byte[] genCryotoRandom(int length) throws IOException, GeneralSecurityException {
		byte[] r = new byte[length];
		Properties properties = new Properties();
		
		try (CryptoRandom random = CryptoRandomFactory.getCryptoRandom(properties)){
			
			System.out.println(random.getClass().getCanonicalName());
			
			random.nextBytes(r);
			
			return r;
		}
	}
	

}
