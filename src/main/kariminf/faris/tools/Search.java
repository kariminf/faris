package kariminf.faris.tools;

import java.util.HashSet;

import kariminf.faris.philosophical.Substance;

public class Search {
	
	
	/*
	public static Substance getSubstance(HashSet<Substance> substances, Substance model){
		
		if (! substances.contains(model)) return model;
		
		for(Substance substance: substances)
			if (substance.equals(model))
				return substance;
		
		//substances.add(model);
		return model;
	}*/
	
	/**
	 * Search for an element in a HashSet using another element as model<br>
	 * If found: it returns the element equals to the model<br>
	 * If not: it returns the model.
	 * @param set the set in which we search the element
	 * @param model the model used to compare when we search
	 * @return the found element or the model itself
	 */
	public static <E> E getElement(HashSet<E> set, E model){

		if (! set.contains(model)) return model;
		
		for(E e: set)
			if (e.equals(model))
				return e;
		
		//substances.add(model);
		return model;
	}

}
