import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.crypto.cipher.CryptoCipher;
import org.apache.commons.crypto.utils.Utils;



public class main {

	public static void main(String[] args) throws IOException, GeneralSecurityException {
		
		
		String id = "s3711351";
		byte[] idbyte = id.getBytes(StandardCharsets.UTF_8);
		BigInteger idInt = new BigInteger(idbyte);
		System.out.println("Alice's id: " + id);
		System.out.println("Alice's id byte: " + Arrays.toString(idbyte));
		System.out.println("Alice's id int: " + idInt);

		
		String msg = "Cloud security";
		byte[] msgbyte = msg.getBytes(StandardCharsets.UTF_8);
		BigInteger msgInt = new BigInteger(msgbyte);
		System.out.println("Alice's message: " + msg);
		System.out.println("Alice's message byte: " + Arrays.toString(msgbyte));
		System.out.println("Alice's message int: " + msgInt);
		
		
		Random ran = new Random();
		BigInteger p = BigInteger.probablePrime(512, ran);//bit length: 512
		BigInteger q = BigInteger.probablePrime(512, ran);
		System.out.println("Prime p: " + p.toString());
		System.out.println("Prime q: " + q.toString());
		
		BigInteger n = p.multiply(q);
		System.out.println("Modulus n: " + n.toString());
		System.out.println("bitLength: " + n.bitLength());
		
		BigInteger phi = (p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)));
		BigInteger e = BigInteger.valueOf(65537);
		BigInteger d = e.modInverse(phi);
		System.out.println("e: " + e.toString());
		System.out.println("d: " + d.toString());
		
		BigInteger sk = idInt.modPow(d, n);
		String skAliceBase64 = Base64.encodeBase64String(sk.toByteArray());
		System.out.println("[PKG side] Sign Alice's secrete key (Base64 encode): " + skAliceBase64);
		
		
		CryptoRandomGenerator ranGen = new CryptoRandomGenerator();
		int length = 32;
		byte[] r = ranGen.genCryotoRandom(length);
		BigInteger rInt = new BigInteger(r);
		String rBase64 = Base64.encodeBase64String(r);
		System.out.println("[Alice side] Alice generates random r");

		BigInteger t = rInt.modPow(e, n);
		System.out.println("[Alice side] t: " + t.toString());
		byte[] tByte = t.toByteArray();
		
		byte[] tm = Util.concatenateByteArray(tByte, msgbyte);
		byte[] tmHash = CryptoHash.genSha256DigestMulti(tm, 1);
		BigInteger hash = new BigInteger(tmHash);
		String tmHashBase64 = Base64.encodeBase64String(tmHash);
		System.out.println("[Alice side] Generate has for t||m. (SHA-256, 32Bytes)");
		System.out.println("Digest (Base64): " + tmHashBase64);
		
		
		BigInteger rr = rInt.modPow(hash, n);
		BigInteger s = (sk.multiply(rr)).mod(n);
		System.out.println("[Alice side] Generate s: " + s.toString());
		System.out.println("[Alice side] Signature (s,t) (Base64): " + Base64.encodeBase64String(s.toByteArray()) 
								+ Base64.encodeBase64String(tByte));
		
		
		BigInteger v1 = s.modPow(e, n);
		BigInteger v2 = (idInt.multiply(t.modPow(hash, n))).mod(n);
		System.out.println("[Bob side] Generate s^e (mod n): " + v1.toString());
		System.out.println("[Bob side] Generate ID*t^hash (mod n): " + v2.toString());
		
		if(v1.equals(v2)) {
			System.out.println("Valid Message.");
		}else {
			System.out.println("Invalid Message!!");
		}
		
	}

}
