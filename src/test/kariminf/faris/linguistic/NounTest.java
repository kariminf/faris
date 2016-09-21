package kariminf.faris.linguistic;

import java.util.HashSet;

import kariminf.faris.linguistic.Noun.Gender;

public class NounTest {

	public static void main(String[] args) {
		Noun mother = Noun.getNew(10332385); //mother
		mother.setAttributs(Gender.FEMININE, true);
		
		Noun childF = Noun.getNew(9917593); //child-female
		childF.setAttributs(Gender.FEMININE, true);
		
		Noun childM = Noun.getNew(9917593); //child-male
		childM.setAttributs(Gender.MASCULINE, true);
		
		Noun childM2 = Noun.getNew(9917593); //child-male2
		childM2.setAttributs(Gender.MASCULINE, true);
		
		HashSet<Noun> set = new HashSet<>();
		set.add(mother);
		set.add(childF);
		set.add(childM);
		set.add(childM2);
		
		System.out.println("mother hash: " + mother.hashCode());
		System.out.println("childF hash: " + childF.hashCode());
		System.out.println("childM hash: " + childM.hashCode());
		
		System.out.println("mother = childF? " + mother.equals(childF));
		System.out.println("mother = childM? " + mother.equals(childM));
		System.out.println("childM = childF? " + childM.equals(childF));
		System.out.println("childM = childM2? " + childM.equals(childM2));
		System.out.println("--------------");
		System.out.println(set);

	}

}
