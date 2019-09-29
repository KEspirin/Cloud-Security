import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.Arrays;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.crypto.cipher.CryptoCipher;
import org.apache.commons.crypto.utils.Utils;

import org.apache.commons.codec.binary.Base64;

public class AesEncryption {

	private SecretKeySpec key;
	private IvParameterSpec iv;
	private String transform = "AES/CBC/PKCS5Padding";
	final private int bufferSize = 1024;
	private Properties properties;

	public AesEncryption(String key, String iv) {

		this.key = new SecretKeySpec(getUTF8Bytes(key), "AES");
		this.iv = new IvParameterSpec(getUTF8Bytes(iv));
		this.properties = new Properties();
	}

	public byte[] encrypt(byte[] pt) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {

		// Creates a CryptoCipher instance with the transformation and properties.
		ByteBuffer outBuffer;

		int updateBytes;
		int finalBytes;

		try (CryptoCipher encipher = Utils.getCipherInstance(this.transform, this.properties)) {

			ByteBuffer inBuffer = ByteBuffer.allocateDirect(this.bufferSize);
			outBuffer = ByteBuffer.allocateDirect(this.bufferSize);
			//inBuffer.put(getUTF8Bytes(pt));
			inBuffer.put(pt);

			inBuffer.flip(); // ready for the cipher to read it

			// Show the data is there
//			System.out.println("inBuffer=" + asString(inBuffer));

			// Initializes the cipher with ENCRYPT_MODE,key and iv.
			encipher.init(Cipher.ENCRYPT_MODE, this.key, this.iv);
			// Continues a multiple-part encryption/decryption operation for byte buffer.
			updateBytes = encipher.update(inBuffer, outBuffer);
//			System.out.println(updateBytes);

			// We should call do final at the end of encryption/decryption.
			finalBytes = encipher.doFinal(inBuffer, outBuffer);
//			System.out.println(finalBytes);

			outBuffer.flip(); // ready for use as decrypt

			byte[] encoded = new byte[updateBytes + finalBytes];
			outBuffer.duplicate().get(encoded);

			return encoded;
		}

	}

	
	public byte[] decrypt(byte[] ct) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
		
		final ByteBuffer outBuffer = ByteBuffer.allocateDirect(this.bufferSize);
		
		
		// Now reverse the process
		try (CryptoCipher decipher = Utils.getCipherInstance(this.transform, this.properties)) {
			decipher.init(Cipher.DECRYPT_MODE, this.key, this.iv);
			ByteBuffer decoded = ByteBuffer.allocateDirect(bufferSize);
			
			outBuffer.put(ct);
			outBuffer.flip();
			
			decipher.update(outBuffer, decoded);
			decipher.doFinal(outBuffer, decoded);
			decoded.flip(); // ready for use
			
			byte[] pt = new byte[decoded.remaining()];
			decoded.get(pt);
			
			return pt;
		}
	}

	/**
	 * Converts String to UTF8 bytes
	 *
	 * @param input the input string
	 * @return UTF8 bytes
	 */
	private byte[] getUTF8Bytes(String input) {
		return input.getBytes(StandardCharsets.UTF_8);
	}

	/**
	 * Converts ByteBuffer to String
	 * 
	 * @param buffer input byte buffer
	 * @return the converted string
	 */
	private String asString(ByteBuffer buffer) {
		ByteBuffer copy = buffer.duplicate();
		byte[] bytes = new byte[copy.remaining()];
		copy.get(bytes);
		return new String(bytes, StandardCharsets.UTF_8);
	}

}
