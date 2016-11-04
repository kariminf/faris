package kariminf.faris.ston;

import java.util.ArrayList;

import kariminf.faris.knowledge.Mind.MentalState;

public class Concepts {
	
	public static enum AdvType {
		OTHER,
		PLACE,
		TIME
	}
	
	public static final int BELIEVE = 689344;
	public static final int THINK = 631737;
	//TODO hope has many meanings, but can be used in this situation
	public static final int HOPE = 1826723;
	public static final int FEAR = 1780729;
	
	public static final int ALSO = 47534;
	
	private static ArrayList<Integer> placeAdv = initPlaceAdv();
	
	private static ArrayList<Integer> initPlaceAdv() {
		ArrayList<Integer> padv = new ArrayList<>();
		
		return padv;
	}
	
	private static ArrayList<Integer> timeAdv = initTimeAdv();
	
	private static ArrayList<Integer> initTimeAdv() {
		ArrayList<Integer> tadv = new ArrayList<>();
		
		return tadv;
	}
	
	
	public static MentalState getMentalState(int synset){
		switch(synset){
		case BELIEVE: return MentalState.BELIEVE;
		case THINK: return MentalState.THINK;
		case HOPE: return MentalState.HOPE;
		case FEAR: return MentalState.FEAR;
		default: return MentalState.FACT;
		}
	}
	
	public static AdvType getAdverbType(int advSynSet){
		if (placeAdv.contains(advSynSet)) return AdvType.PLACE;
		if (timeAdv.contains(advSynSet)) return AdvType.TIME;
		return AdvType.OTHER;
	}

}
