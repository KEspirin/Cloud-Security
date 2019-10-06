import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoHash {


	public static byte[] genSha256DigestMulti(byte[] msg, int round) {
		
		MessageDigest sha256;
		byte[] hash256 = new byte[32];
		try {
			sha256 = MessageDigest.getInstance("SHA-256");
			for(int i=0; i<round-1; i++) {
				sha256.update(msg);
			}
			
			hash256 = sha256.digest();
			
		}catch (NoSuchAlgorithmException e) {
			System.err.println("ERROR|| sha-256 NoSuchAlgorithmException");
			e.printStackTrace();
		}
		
		
		
		return hash256;
	}
	
	
}
