import java.math.BigInteger;
import java.util.Random;
import org.apache.commons.codec.binary.Base64;

public class protocol {
	
	private String name;
	
	private BigInteger p;
	private BigInteger g;
	
	private int privatekey;
	private BigInteger publickey;
	
	private BigInteger secretekey;
	
	public protocol(String name) {
		this.name = name;
		this.generateDHkeyPair();
		System.out.println(this.name+": initialization ...");
	}
	
	
	public void generateDHkeyPair() {
		this.p = BigInteger.probablePrime(512, new Random());
		Random rand = new Random();
		this.g = BigInteger.valueOf(rand.nextInt(1000));
		this.privatekey = (int)(Math.random()*1000);
		
		System.out.println(this.name+": Generate DH keypair ...");
	}
	
	
	public String getP() {
		System.out.println(this.name+": encode public key ...");
		return Base64.encodeBase64String(Base64.encodeInteger(this.p));
		 
	}
	
	public String getG() {
		return Base64.encodeBase64String(Base64.encodeInteger(this.g));
	}
	
	
	public void generatePublicKey() {
		this.publickey = this.g.pow(this.privatekey).remainder(this.p);
		System.out.println("["+this.name+" side]: "+this.name+" generates diffie-hellman key agreement.");

		
	}
	

	public String getPublickey(String name) {
		String pk = Base64.encodeBase64String(Base64.encodeInteger(this.publickey));
		System.out.println("["+this.name+" side]: "+this.name+" sends public key to "+name+": "+pk);
		return pk;
	}
	
	

	public void setG(BigInteger g) {
		this.g = g;
	}
	
	
	public void setP(BigInteger p) {
		this.p = p;
	}
	

	public void setSecretekey(BigInteger secretekey) {
		this.secretekey = secretekey;
		System.out.println("["+this.name+" side]: "+this.name+" generaates shared key: " + getSecretekey());

	}


	public void receive(String p, String g, String name) {
		this.setP(Base64.decodeInteger(Base64.decodeBase64(p)));
		this.setG(Base64.decodeInteger(Base64.decodeBase64(g)));
		
		System.out.println(this.name+": set key factory ...");
		
		
	}
	
	public void receive(String pk, String name) {
		System.out.println("["+this.name+" side]: "+this.name+" decodes "+name+"'s public key");
		BigInteger publicKey = Base64.decodeInteger(Base64.decodeBase64(pk));
		BigInteger sk =  publicKey.pow(this.privatekey).remainder(this.p);
		this.setSecretekey(sk);
		

		
	}

	public String getSecretekey() {
		return Base64.encodeBase64String(Base64.encodeInteger(this.secretekey));
	}
	
//	public void compare(String sk) {
//		BigInteger secrete = 
//		
//		
//	}
	
	public String getName() {
		return this.name;
	}


}
