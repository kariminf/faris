package kariminf.faris.process.ston;

import java.util.ArrayList;

import kariminf.faris.knowledge.Mind.MentalState;
import kariminf.faris.philosophical.Relative.RelativeType;
import kariminf.langpi.wordnet.WNTools;
import kariminf.sentrep.univ.types.Relation.Adpositional;

public class Concepts {
	
	public static final int BELIEVE = 689344;
	public static final int THINK = 631737;
	//TODO hope has many meanings, but can be used in this situation
	public static final int HOPE = 1826723;
	public static final int FEAR = 1780729;
	
	public static final int ALSO = 47534;
	
	
	public static enum PlaceTime {
		OTHER,
		PLACE,
		TIME
	}
	
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
	
	private static ArrayList<Adpositional> placeAdj = initPlaceAdj();
	private static ArrayList<Adpositional> initPlaceAdj() {
		ArrayList<Adpositional> padj = new ArrayList<>();
		padj.add(Adpositional.INSIDE);
		padj.add(Adpositional.OUTSIDE);
		padj.add(Adpositional.BELOW);
		padj.add(Adpositional.ABOVE);
		return padj;
	}
	
	private static ArrayList<Adpositional> timeAdj = initTimeAdj();
	private static ArrayList<Adpositional> initTimeAdj() {
		ArrayList<Adpositional> tadj = new ArrayList<>();
		tadj.add(Adpositional.PAST);
		tadj.add(Adpositional.SINCE);
		return tadj;
	}
	
	private static ArrayList<Adpositional> otherAdj = initOtherAdj();
	private static ArrayList<Adpositional> initOtherAdj() {
		ArrayList<Adpositional> oadj = new ArrayList<>();
		oadj.add(Adpositional.SUBJECT);
		oadj.add(Adpositional.ACCOMPANY);
		oadj.add(Adpositional.POSSESSION);
		oadj.add(Adpositional.ROLE);
		oadj.add(Adpositional.SITUATION);
		return oadj;
	}
	
	private static ArrayList<Integer> timeLex = initTimeLex();
	private static ArrayList<Integer> initTimeLex() {
		ArrayList<Integer> tl = new ArrayList<>();
		tl.add(4); //noun.act	nouns denoting acts or actions
		tl.add(11); //noun.event	nouns denoting natural events
		tl.add(28); //noun.time	nouns denoting time and temporal relations
		return tl;
	}
	
	private static ArrayList<Integer> placeLex = initPlaceLex();
	private static ArrayList<Integer> initPlaceLex() {
		ArrayList<Integer> pl = new ArrayList<>();
		pl.add(5); //noun.animal	nouns denoting animals
		pl.add(6); //noun.artifact	nouns denoting man-made objects
		pl.add(8); //noun.body	nouns denoting body parts
		pl.add(13); //noun.food	nouns denoting foods and drinks
		pl.add(14); //noun.group	nouns denoting groupings of people or objects
		pl.add(15); //noun.location	nouns denoting spatial position
		pl.add(17); //noun.object	nouns denoting natural objects (not man-made)
		pl.add(18); //noun.person	nouns denoting people
		pl.add(20); //noun.plant	nouns denoting plants
		pl.add(25); //noun.shape	nouns denoting two and three dimensional shapes
		pl.add(27); //noun.substance	nouns denoting substances
		return pl;
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
	
	/**
	 * 
	 * @param advSynSet
	 * @return
	 */
	public static PlaceTime getAdverbType(int advSynSet){
		if (placeAdv.contains(advSynSet)) return PlaceTime.PLACE;
		if (timeAdv.contains(advSynSet)) return PlaceTime.TIME;
		return PlaceTime.OTHER;
	}
	
	/**
	 * 
	 * @param adp
	 * @param nounSySet
	 * @return
	 */
	public static PlaceTime getAdjType(Adpositional adp, int nounSySet){
		
		if(timeAdj.contains(adp)) return PlaceTime.TIME;
		if(placeAdj.contains(adp)) return PlaceTime.PLACE;
		if(otherAdj.contains(adp)) return PlaceTime.OTHER;
		
		/*
		EXIST, // particular time or location in, at (time, place, situation)
		SOURCE, //from (time, place, other)
		DESTINATION, // till, to (time, place, intention: verb)
		INTENTION, // for (time, intention)
		BEFORE, //before, in front (time, place)
		AFTER, //after, behind (time, place)
		PROXIMITY, //by (time, place)
		BETWEEN, // place, time, other(between me and you)
		THROUGH,//place, time
		*/
		
		int lexNum = WNTools.getLexFileNumber(nounSySet, "NOUN");
		
		//System.out.println(" LexNum: " + lexNum);
		
		if (timeLex.contains(lexNum)) return PlaceTime.TIME;
		if (placeLex.contains(lexNum)) return PlaceTime.PLACE;
		
		// 07	noun.attribute	nouns denoting attributes of people and objects
		// 09	noun.cognition	nouns denoting cognitive processes and contents
		// 10	noun.communication	nouns denoting communicative processes and contents
		// 12	noun.feeling	nouns denoting feelings and emotions
		// 16	noun.motive	nouns denoting goals
		// 19	noun.phenomenon	nouns denoting natural phenomena
		// 21	noun.possession	nouns denoting possession and transfer of possession
		// 22	noun.process	nouns denoting natural processes
		// 23	noun.quantity	nouns denoting quantities and units of measure
		// 24	noun.relation	nouns denoting relations between people or things or ideas
		// 26	noun.state	nouns denoting stable states of affairs
		return PlaceTime.OTHER;
	}
	
	

}
