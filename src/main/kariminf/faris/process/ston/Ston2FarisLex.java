package kariminf.faris.process.ston;

import kariminf.faris.linguistic.Verb.Tense;
import kariminf.sentrep.ston.StonLex;

public final class Ston2FarisLex {
	
	public static Tense getTense(String stonTense){
		int idx = StonLex.getTenseIndex(stonTense);
		
		switch (idx){
		case 1: return Tense.PAST;
		case 2: return Tense.FUTURE;
		
		}
		
		return Tense.PRESENT;
	}

}
