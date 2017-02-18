/* FARIS : Factual Arrangement and Representation of Ideas in Sentences
 * FAris : Farabi & Aristotle
 * Faris : A knight (in Arabic)
 * --------------------------------------------------------------------
 * Copyright (C) 2017 Abdelkrime Aries (kariminfo0@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kariminf.faris.process;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import kariminf.faris.knowledge.Mind;
import kariminf.faris.knowledge.Mind.MentalState;
import kariminf.faris.linguistic.*;
import kariminf.faris.philosophical.*;
import kariminf.sentrep.univ.types.Relation.Adpositional;

/**
 * A generator which generates a text or any thing from a Faris representation (model)
 * @author Abdelkrime Aries
 *
 */
public abstract class Generator<T> {
	
	public static final String ACTION = "a";
	public static final String ROLE = "r";
	
	private ArrayDeque<QuantSubstance> currentMinds = new ArrayDeque<>();

	private HashMap<Action, Integer> actionIDs = new HashMap<Action, Integer>();
	private int actionsNbr = 0;
	
	private HashMap<Substance, Integer> substanceIDs = new HashMap<>();
	private HashMap<QuantSubstance, Integer> qsubstanceIDs = new HashMap<>();
	
	private int substancesNbr = 0;
	
	private MentalState mentalState;
	
	private boolean isMainIdea = false;
	
	private Action currentAction;
	
	private QuantSubstance currentSubstance;
	
	
	public void processPlace(Adpositional relation, Adverb adv, ArrayList<QuantSubstance> places){
		beginPlaceHandler(relation, adv);
		ArrayList<ArrayList<QuantSubstance>> disj = new ArrayList<>();
		disj.add(places);
		endPlaceHandler();
	}

	/**
	 * 
	 * @param action
	 */
	public void processAction(Action action){
		currentAction = action;
		//We don't add an action, already there
		if (actionIDs.containsKey(action)){
			/*if (isMainIdea && currentMinds.peek().getSubstance().getNounSynSet() == 0){
				System.out.println("main sentence1");
				String actID = ACTION + actionIDs.get(action);
				addIdeaHandler(actID);
				isMainIdea = false;
			}*/
			String actID = ACTION + actionIDs.get(action);
			actionFoundHandler(actID);
			return;
		}
		
		Action tmpLastAction = currentAction;
		QuantSubstance tmpSubstance = currentSubstance;

		actionIDs.put(action, actionsNbr);
		String actID = ACTION + actionsNbr;
		actionsNbr++;

		beginActionHandler(actID, action.getVerb(), action.getAdverbs());

		beginAgentsHandler();
		processDisjunctions(action.getAgents());
		endAgentsHandler();

		beginThemesHandler();
		processDisjunctions(action.getThemes());
		endThemesHandler();
		
		for(Place place: action.getPlaces()) place.generate(this);
		
		for(Time time: action.getTimes()) time.generate(this);
			
		endActionHandler(actID);
		
		if (isMainIdea && currentMinds.peek().getSubstance().getNounSynSet() == 0){
			//System.out.println("main sentence");
			addIdeaHandler(actID);
			isMainIdea = false;
		}
		
		currentAction = tmpLastAction;
		currentSubstance = tmpSubstance;

	}
	
	/**
	 * Called by a state 
	 * @param stateAction the state action
	 * @param mainActions the actions related to the state; i.e. the actions 
	 * when a substance is having this state
	 */
	public void processState(Action stateAction, List<Action> mainActions){
		
		/*
		//A state of a substance shows after generating its relative action
		List<String> processedActions = new ArrayList<>();
		
		for(Action act: mainActions)
			if (actionIDs.containsKey(act)){
				String actID = ACTION + actionIDs.get(act);
				processedActions.add(actID);
			}
		
		//If the state has no action relative to it
		if (processedActions.isEmpty()) return;
		*/
		
		if(!mainActions.contains(currentAction)) return;
		
		boolean isAgent = stateAction.hasAgent(currentSubstance);
		if(!(isAgent || stateAction.hasTheme(currentSubstance))) return;
		
		Action tmpLastAction = currentAction;
		QuantSubstance tmpSubstance = currentSubstance;
		
		stateAction.generate(this);
		
		String actID = ACTION + actionIDs.get(stateAction);
		
		
		addStateHandler(isAgent, actID);
		
		currentAction = tmpLastAction;
		currentSubstance = tmpSubstance;
	}

	private void processDisjunctions(ArrayList<ArrayList<QuantSubstance>> disj){
		//Disjunctions 
		for(ArrayList<QuantSubstance> conj: disj){
			beginDisjunctionHandler();
			for (QuantSubstance substance: conj){
				substance.generate(this);
			}

			endDisjunctionHandler();
		}
	}
	
	private void addSubstance(String id, Substance sub, Quantity q){
		
		beginSubstanceHandler(id, sub.getNoun());
		
		if (q != null) q.generate(this);
		for (Quality ql : sub.getQualities()) ql.generate(this);
		endSubstanceHandler();
	}
	
	public void processQuality(Quality ql){
		addQualityHandler(ql.getAdjective(), ql.getAdverbs());
	}
	
	public void processQuantity(Quantity q){
		Noun unit = (q.getUnit() == null)? null: q.getUnit().getNoun();
		addQuantityHandler(q.getNumber(), unit);
	}

	public void processSubstance(QuantSubstance qsub){
		currentSubstance = qsub;
		
		Action tmpLastAction = currentAction;
		QuantSubstance tmpSubstance = currentSubstance;
		
		String id = ROLE + substancesNbr;
		if (qsubstanceIDs.containsKey(qsub)){
			substanceFoundHandler(id);
			return;
		}
		
		substancesNbr++;
		qsubstanceIDs.put(qsub, substancesNbr);
		addSubstance(id, qsub.getSubstance(), qsub.getQuantity());
		
		String subID = ROLE + qsubstanceIDs.get(tmpSubstance);
		String actID = ACTION + actionIDs.get(tmpLastAction);
		
		beginStateHandler(subID, actID);
		for (State state: qsub.getStates()){
			
			state.generate(this);
		}
		endStateHandler(subID, actID);
		
		//TODO add more
		
		currentAction = tmpLastAction;
		currentSubstance = tmpSubstance;
	}

	public void processSubstance(Substance sub){
		String id = ROLE + substancesNbr;
		if (substanceIDs.containsKey(sub)){
			substanceFoundHandler(id);
			return;
		}
		substancesNbr++;
		substanceIDs.put(sub, substancesNbr);
		addSubstance(id, sub, null);
	}
	
	/**
	 * This is called by {@link kariminf.faris.knowledge.Mind} to process the caller's mind
	 * @param s the Mind of the caller as a substance
	 */
	public void processMind(QuantSubstance s){
		currentMinds.push(s);
		
		//These cases when a substance or its noun are null may never happen
		//but as a security measure, I added the two checks
		Substance sub = s.getSubstance();
		if (sub == null) return;
		Noun n = sub.getNoun();
		if (n == null) return;
		
		//If the synset is null (0), it is the main mind
		if (n.getSynSet() == 0) return; 
		
		//Else, it is a new mind, so we have to process the substance
		
		s.generate(this);
	}
	
	public void endMindProcessing(QuantSubstance s){
		if (s == currentMinds.peek()) currentMinds.pop();
	}
	
	public void processMentalState(MentalState ms){
		//A mental state of a mind
		mentalState = ms;
	}
	
	/**
	 * Called by different Ideas to mark the first action as the main action
	 */
	public void mainIdea(){
		isMainIdea = true;
	}
	
	
	//Abstract methods
	//=====================
	
	/**
	 * When an action is found, this method will be called
	 * @param id the ID of the action
	 * @param verb the verb describing the action
	 * @param adverbs the adverbs modifying the verb
	 */
	protected abstract void beginActionHandler(String id, Verb verb, Set<Adverb> adverbs);

	/**
	 * This is called when the action ends (all its components have been processed
	 * @param id The ID of the action
	 */
	protected abstract void endActionHandler(String id);

	/**
	 * This is called to mark the start of the current action's agents enumeration
	 */
	protected abstract void beginAgentsHandler();

	/**
	 * This is called to mark the end of current action's agents enumeration
	 */
	protected abstract void endAgentsHandler();

	/**
	 * This is called to mark the start of current action's themes enumeration
	 */
	protected abstract void beginThemesHandler();

	/**
	 * This is called to mark the end of current action's themes enumeration
	 */
	protected abstract void endThemesHandler();

	/**
	 * This is called whenever there is an enumeration; each time it is called 
	 * it marks a disjunction "OR". The components called after are conjunctions "AND"
	 */
	protected abstract void beginDisjunctionHandler();

	/**
	 * This is called when the disjunction of elements is over, and to start a new disjunction
	 * if there is any
	 */
	protected abstract void endDisjunctionHandler();

	//If the substance has a noun with synset 0, so it is the pronoun it
	//for example it is believed
	/**
	 * This is called when a substance is found; A substance and a quantified substance
	 * identical to it are considered as two distinct substances
	 * @param id the ID of the substance
	 * @param noun the noun, which can of type ProperNoun as well
	 */
	protected abstract void beginSubstanceHandler(String id, Noun noun);
	
	/**
	 * This is called when a substance is found, but it was already processed earlier
	 * @param id the ID of the substance
	 */
	protected abstract void substanceFoundHandler(String id);
	
	/**
	 * This is called when an action is found, but it was already processed earlier
	 * @param id
	 */
	protected abstract void actionFoundHandler(String id);
	
	/**
	 * This marks the end of a substance processing
	 */
	protected abstract void endSubstanceHandler();

	/**
	 * This is called when we want to add a quantity to the current substance
	 * @param nbr The quantity
	 * @param unit the unit of the quantity
	 */
	protected abstract void addQuantityHandler(double nbr, Noun unit);
	
	/**
	 * This is called when we want to add a quality to the current substance
	 * @param adjective the adjective that describes this quality
	 * @param adverbs the adverbs modifying this adjective
	 */
	protected abstract void addQualityHandler(Adjective adjective, ArrayList<Adverb> adverbs);
	
	/**
	 * This is called when an Idea has been found
	 */
	protected abstract void addIdeaHandler(String actionID);
	
	/**
	 * This is called when the current substance has a probable action state in one of 
	 * the relative actions in a substance
	 * @param isAgent if true, then the current substance is an agent, 
	 * otherwise it is a theme
	 * @param relIDs a list of probable relative actions
	 */
	protected abstract void addStateHandler(boolean isAgent, String stateID);
	
	protected abstract void beginStateHandler(String subID, String actID);
	
	protected abstract void endStateHandler(String subID, String actID);
	
	protected abstract void beginPlaceHandler(Adpositional relation, Adverb adv);
	
	protected abstract void endPlaceHandler();
	
	/**
	 * This is called to generate a representation of a given type 
	 * @return
	 */
	public abstract T generate();
}
