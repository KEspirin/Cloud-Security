
public class main {

	public static void main(String[] args) {
		
		protocol alice = new protocol("Alice");
		protocol bob = new protocol("Bob");
		
		String g = alice.getG();
		String p = alice.getP();
		
		bob.receive(p, g, alice.getName());
		
		alice.generatePublicKey();
		bob.generatePublicKey();
		
		String apublickey = alice.getPublickey(bob.getName());
		bob.receive(apublickey, alice.getName());
		
		String bpublickey = bob.getPublickey(alice.getName());
		alice.receive(bpublickey, bob.getName());
		
		if(alice.getSecretekey().equals(bob.getSecretekey())) {
			System.out.println("Shared secrets are the same.");
		}else {
			System.out.println("Shared secrets are different.");
		}
	}

}
