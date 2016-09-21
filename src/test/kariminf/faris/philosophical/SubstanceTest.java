package kariminf.faris.philosophical;

import kariminf.faris.linguistic.Adjective;

public class SubstanceTest {

	public static void main(String[] args) {
		Substance s1 = new Substance(10332385);
		Adjective adj = new Adjective(1123148);
		Quality q = new Quality(adj);
		s1.addQuality(q);
		
		Substance s2 = new Substance(10332385);
		
		Substance s3 = new Substance(10332385);
		s3.addQuality(q);
		
		System.out.println("s1 = s2? " + s1.equals(s2));
		System.out.println("s3 = s2? " + s3.equals(s2));
		System.out.println("s1 = s3? " + s1.equals(s3));

	}

}
