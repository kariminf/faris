package kariminf.faris.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import kariminf.faris.linguistic.*;
import kariminf.faris.philosophical.*;

public abstract class Generator {

	private HashMap<Action, Integer> actionIDs = new HashMap<Action, Integer>();
	private int actionsNbr = 0;


	/**
	 * 
	 * @param action
	 */
	public void addAction(Action action){

		//We don't add an action, already there
		if (actionIDs.containsKey(action)) return;

		actionIDs.put(action, actionsNbr);
		actionsNbr++;

		beginActionHandler(action.getVerb(), action.getAdverbs());

		beginAgents();
		processDisjunctions(action.getAgents());
		endAgents();

		beginThemes();
		processDisjunctions(action.getThemes());
		endThemes();



		endActionHandler();


	}

	private void processDisjunctions(ArrayList<ArrayList<QuantSubstance>> disj){
		//Disjunctions 
		for(ArrayList<QuantSubstance> conj: disj){
			beginDisjunction();
			for (QuantSubstance substance: conj){
				substance.generate(this);
			}

			endDisjunction();
		}
	}

	public void addSubstance(QuantSubstance qsub){
		Substance s = qsub.getSubstance();
		beginSubstance(s.getNoun(), s.getQualities());
		
		endSubstance();
	}

	public void addSubstance(Substance sub){
		beginSubstance(sub.getNoun(), sub.getQualities());
		endSubstance();
	}


	protected abstract void beginActionHandler(Verb verb, Set<Adverb> adverbs);

	protected abstract void endActionHandler();

	protected abstract void beginAgents();

	protected abstract void endAgents();

	protected abstract void beginThemes();

	protected abstract void endThemes();

	protected abstract void beginDisjunction();

	protected abstract void endDisjunction();

	protected abstract void beginSubstance(Noun noun, Set<Quality> qualities);
	
	protected abstract void endSubstance();

	protected abstract void beginQuantity(double nbr);
	
	protected abstract void endQuantity();

	public abstract String generate();
}
