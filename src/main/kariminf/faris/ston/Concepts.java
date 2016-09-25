package kariminf.faris.ston;

import kariminf.faris.knowledge.Mind.MentalState;

public class Concepts {
	
	public static final int BELIEVE = 689344;
	public static final int THINK = 631737;
	//TODO hope has many meanings, but can be used in this situation
	public static final int HOPE = 1826723;
	public static final int FEAR = 1780729;
	
	public static MentalState getMentalState(int synset){
		switch(synset){
		case BELIEVE: return MentalState.BELIEVE;
		case THINK: return MentalState.THINK;
		case HOPE: return MentalState.HOPE;
		case FEAR: return MentalState.FEAR;
		default: return MentalState.FACT;
		}
	}

}
