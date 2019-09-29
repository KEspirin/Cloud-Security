import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.crypto.cipher.CryptoCipher;
import org.apache.commons.crypto.utils.Utils;

public class main {

	public static void main(String[] args) throws Exception {

		String secretTrigger = "s3711351s3711351s3711351s3711351";
		String ivTrigger = "s3711351s3711351";
		System.out.println("[Shared Password]: " + secretTrigger);
		CryptoRandomGenerator ranGen = new CryptoRandomGenerator();
		int ranLen = 16;

		AesEncryption aesEnc = new AesEncryption(secretTrigger, ivTrigger);

		System.out.println("------------------Communication 1------------------");

		System.out.println("[Trudy side] Hi I'm Alice.");

		byte[] r2BArr = ranGen.genCryotoRandom(ranLen);
		System.out.println("[Trudy side] R2: " + Base64.encodeBase64String(r2BArr));
		System.out.println("Trudy sends the R2 to Bob.");

		byte[] r1BArr = ranGen.genCryotoRandom(ranLen);
		byte[] cipherR2 = aesEnc.encrypt(r2BArr);
		System.out.println("[Bob side] R1: " + Base64.encodeBase64String(r1BArr));
		System.out.println("[Bob side] The Encrypted R2: " + Base64.encodeBase64String(cipherR2));
		System.out.println("Bob sends the R1, and the Encrypted R2 to Trudy.");

		System.out.println("------------------Communication 2------------------");

		System.out.println("[Trudy side] Hi I'm Lily.");

		System.out.println("[Trudy side] R1: " + Base64.encodeBase64String(r1BArr));
		System.out.println("Trudy sends the R1 to Bob.");

		byte[] r3BArr = ranGen.genCryotoRandom(ranLen);
		byte[] cipherR1 = aesEnc.encrypt(r1BArr);
		System.out.println("[Bob side] R3: " + Base64.encodeBase64String(r3BArr));
		System.out.println("[Bob side] The Encrypted R1: " + Base64.encodeBase64String(cipherR1));
		System.out.println("Bob sends the R3, and the Encrypted R1 to Trudy.");

		System.out.println("------------------Communication 1------------------");

		System.out.println("[Trudy side] The encrypted R1: " + Base64.encodeBase64String(cipherR1));
		System.out.println("Trudy sends the encrypted R1 to Bob.");

		byte[] plainR1 = aesEnc.decrypt(cipherR1);
		System.out.println("[Bob side] decrypted R1: " + Base64.encodeBase64String(plainR1));
		System.out.println("[Bob side] original R1: " + Base64.encodeBase64String(r1BArr));
		if (Base64.encodeBase64String(plainR1).equals(Base64.encodeBase64String(r1BArr))) {
			System.out.println("[Bob side] Alice (Trudy) is a legitimate user.");
		}

	}

}
