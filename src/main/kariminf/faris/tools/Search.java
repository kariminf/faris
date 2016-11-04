package kariminf.faris.tools;

import java.util.HashSet;

import kariminf.faris.philosophical.Substance;

public class Search {
	
	
	/**
	 * Search for a substance in a HashSet of substances using a model. <br>
	 * If found: it returns the element equals to the model<br>
	 * If not: it adds the model to the set and return it.
	 * @param substances the set in which we search the substance
	 * @param model the model used to compare when we search
	 * @return the found substance or the model itself
	 */
	public static Substance getSubstance(HashSet<Substance> substances, Substance model){
		
		if (! substances.contains(model)) return model;
		
		for(Substance substance: substances)
			if (substance.equals(model))
				return substance;
		
		substances.add(model);
		return model;
	}

}
