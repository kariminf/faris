package kariminf.faris.tools;

import java.util.ArrayList;
import java.util.HashSet;

import kariminf.faris.philosophical.QuantSubstance;

public final class ConjunctedSubstances extends HashSet<QuantSubstance> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ArrayList<QuantSubstance> getSubstances(){
		ArrayList<QuantSubstance> result = new ArrayList<>();
		result.addAll(this);
		return result;
	}
	
	public ConjunctedSubstances fuse(ConjunctedSubstances cs){
		ConjunctedSubstances result = new ConjunctedSubstances();
		result.addAll(cs);
		return result;
	}

}
