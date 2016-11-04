package kariminf.faris.philosophical;

import java.util.HashSet;
import java.util.Set;

public class QuantSubstance {
	
	private Substance substance;
	private Quantity quantity;
	
	//Here the substance is the subject (doer)
	private Set<Action> actions = new HashSet<Action>();

	//Here the substance is the object (receiver of the action)
	private Set<Action> affections = new HashSet<Action>();
		
	//States
	private Set<State> states = new HashSet<State>();
		
	
	public QuantSubstance(Substance substance, Quantity quantity) {
		this.substance = substance;
		this.quantity = quantity;
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
