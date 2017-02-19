package kariminf.faris.philosophical;

import java.util.HashSet;
import java.util.Set;

import kariminf.faris.process.Generator;

public class QuantSubstance extends Being{
	
	private Substance substance;
	private Quantity plQuantity;
	private Quantity nbrQuantity;
	
	//Here the substance is the subject (doer)
	private Set<Action> actions = new HashSet<>();

	//Here the substance is the object (receiver of the action)
	private Set<Action> affections = new HashSet<>();
		
	//States
	private Set<State> states = new HashSet<>();
	
	private Set<Relative> relatives = new HashSet<>();
		
	
	public QuantSubstance(Substance substance) {
		this.substance = substance;
	}
	
	/**
	 * Set the quantity
	 * @param quantity if it is null; the two quantities will be set to null
	 */
	public void setQuantity(Quantity quantity){
		
		if (quantity == null){
			plQuantity = null;
			nbrQuantity = null;
			return;
		}
		
		if (quantity.isPlural()){
			plQuantity = quantity;
			return;
		}
		
		nbrQuantity = quantity;
	}
	
	public Quantity getPlQuanty(){
		return plQuantity;
	}
	
	public Quantity getNbrQuanty(){
		return nbrQuantity;
	}
	
	/**
	 * Creates the same QuantSubstance with a different Substance
	 * @param orig The original Quantified Substance
	 * @param newSubs The new Substance
	 * @return A new Quantified Substance with a different substance than the original had.
	 */
	public static QuantSubstance withNewSubstance(QuantSubstance orig, Substance newSubs){
		QuantSubstance result = new QuantSubstance(newSubs);
		result.plQuantity = orig.plQuantity;
		result.nbrQuantity = orig.nbrQuantity;
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
		result += (nbrQuantity == null)? "": "-" + nbrQuantity;
		result += (plQuantity == null)? "": "-" + plQuantity;
		return result;
	}


	/**
	 * @return the substance
	 */
	public Substance getSubstance() {
		return substance;
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
		HashSet<State> result = new HashSet<>();
		result.addAll(states);
		return result;
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

	@Override
	public void generate(Generator gr) {
		gr.processSubstance(this);
		
	}
	
	

}
