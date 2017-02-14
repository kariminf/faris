package kariminf.faris.philosophical;

import java.util.HashSet;
import java.util.Set;

public class QuantSubstance {
	
	private Substance substance;
	private Quantity quantity;
	
	//Here the substance is the subject (doer)
	private HashSet<Action> actions = new HashSet<>();

	//Here the substance is the object (receiver of the action)
	private HashSet<Action> affections = new HashSet<>();
		
	//States
	private HashSet<State> states = new HashSet<>();
	
	private HashSet<Relative> relatives = new HashSet<>();
		
	
	public QuantSubstance(Substance substance, Quantity quantity) {
		this.substance = substance;
		this.quantity = quantity;
	}
	
	/**
	 * Creates the same QuantSubstance with a different Substance
	 * @param orig The original Quantified Substance
	 * @param newSubs The new Substance
	 * @return A new Quantified Substance with a different substance than the original had.
	 */
	public static QuantSubstance withNewSubstance(QuantSubstance orig, Substance newSubs){
		QuantSubstance result = new QuantSubstance(newSubs, orig.quantity);
		result.actions = orig.actions;
		result.affections = orig.affections;
		result.states = orig.states;
		
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "";
		result += substance;
		//States are related to actions
		//result += (states.isEmpty())? "": "-S:" + states;
		result += (quantity != null)? "-" + quantity : "";
		return result;
	}


	/**
	 * @return the substance
	 */
	public Substance getSubstance() {
		return substance;
	}


	/**
	 * @return the quantity
	 */
	public Quantity getQuantity() {
		return quantity;
	}
	
	public void addAction(Action action){
		actions.add(action);
	}
	
	public void addAffection(Action action){
		affections.add(action);
	}
	
	/**
	 * @return the states
	 */
	public Set<State> getStates() {
		//Alert: security problem
		return states;
	}

	/**
	 * @param states the states to set
	 */
	public void addStates(Set<State> states) {
		this.states.addAll(states);
	}
	
	public void addState(State state) {
		this.states.add(state);
	}
	
	

}
