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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kariminf.faris.knowledge.Thought;
import kariminf.faris.knowledge.Thought.ThoughtWrapper;
import kariminf.faris.knowledge.Conditional.ConditionalWrapper;
import kariminf.faris.knowledge.Faris.FarisWrapper;
import kariminf.faris.knowledge.Mind;
import kariminf.faris.knowledge.Mind.MentalState;
import kariminf.faris.knowledge.Mind.MindWrapper;
import kariminf.faris.knowledge.Opinion.OpinionWrapper;
import kariminf.faris.linguistic.*;
import kariminf.faris.philosophical.*;
import kariminf.faris.philosophical.Action.ActionWrapper;
import kariminf.faris.philosophical.Place.PlaceWrapper;
import kariminf.faris.philosophical.Quality.QualityWrapper;
import kariminf.faris.philosophical.QuantSubstance.QSubstanceWrapper;
import kariminf.faris.philosophical.Quantity.QuantityWrapper;
import kariminf.faris.philosophical.Relative.RelativeType;
import kariminf.faris.philosophical.Relative.RelativeWrapper;
import kariminf.faris.philosophical.State.StateWrapper;
import kariminf.faris.philosophical.Substance.SubstanceWrapper;
import kariminf.faris.philosophical.Time.TimeWrapper;
import kariminf.faris.tools.ConjunctedSubstances;
import kariminf.sentrep.types.Comparison;

/**
 * A generator which processs a text or any thing from a Faris representation (model)
 * @author Abdelkrime Aries
 *
 */
public class Generator implements Processor {
	
	public static final String ACTION = "a";
	public static final String ROLE = "r";
	
	private ArrayDeque<QuantSubstance> currentMinds = new ArrayDeque<>();

	private HashMap<Action, Integer> actionIDs = new HashMap<Action, Integer>();
	private int actionsNbr = 0;
	
	private HashMap<Substance, Integer> substanceIDs = new HashMap<>();
	private HashMap<QuantSubstance, Integer> qsubstanceIDs = new HashMap<>();
	
	//A substance can have many IDs according to the states 
	//private HashMap<QuantSubstance, List<HashMap<Integer, List<State>>>> qsubstanceIDs = new HashMap<>();
	
	
	private int substancesNbr = 0;
	
	private MentalState mentalState;
	
	private boolean isMainIdea = false;
	
	private Action currentAction;
	
	private QuantSubstance currentSubstance;
	
	private GeneratorHandler<?> handler;
	
	public Generator(GeneratorHandler<?> handler){
		this.handler = handler;
	}
	
	
	public void processRelative(RelativeWrapper wrapper){
		Comparison cmp = RelativeType.toComparison(wrapper.relationType);
		
		//System.out.println("Generator.processRelative: " + wrapper.relSubstance);
		
		//If the relative substance is not defined already, no need to process
		if (! qsubstanceIDs.containsKey(wrapper.relSubstance)){
			wrapper.relSubstance.process(this);
			//System.out.println("created substance" + qsubstanceIDs.get(wrapper.relSubstance));
		}
		
		String relID = ROLE + qsubstanceIDs.get(wrapper.relSubstance);
		
		//It is an OF relation, between current substance and another
		/*if (cmp == null){
			//if (wrapper.owner == null || wrapper.owner != currentSubstance) return;
			addRelative(null, null, relID);
			//System.out.println("relative OF called");
		}*/
		
		//if (wrapper.actOwner == null && wrapper.actOwner != currentAction) return;
		
		handler.addRelativeHandler(cmp, wrapper.adjective, relID);
		
	}
	
	public void processPlace(PlaceWrapper wrapper){
		handler.beginPlaceHandler(wrapper.relation, wrapper.adv);
		//System.out.println("Generator: Place=" + relation);
		if (wrapper.places != null && !wrapper.places.isEmpty()){
			Set<ConjunctedSubstances> disj = new HashSet<>();
			ConjunctedSubstances conj = new ConjunctedSubstances();
			conj.addAll(wrapper.places);
			disj.add(conj);
			processDisjunctions(disj);
		}
		
		handler.endPlaceHandler(wrapper.relation, wrapper.adv);
	}
	
	public void processTime(TimeWrapper wrapper){
		handler.beginTimeHandler(wrapper.relation, wrapper.adv, wrapper.datetime);
		
		if (wrapper.times != null && !wrapper.times.isEmpty()){
			Set<ConjunctedSubstances> disj = new HashSet<>();
			ConjunctedSubstances conj = new ConjunctedSubstances();
			conj.addAll(wrapper.times);
			disj.add(conj);
			processDisjunctions(disj);
		}
		handler.endTimeHandler(wrapper.relation, wrapper.adv, wrapper.datetime);
	}

	/**
	 * 
	 * @param action
	 */
	public void processAction(ActionWrapper wrapper){
		currentAction = wrapper.action;
		//We don't add an action, already there
		if (actionIDs.containsKey(wrapper.action)){
			/*if (isMainIdea && currentMinds.peek().getSubstance().getNounSynSet() == 0){
				System.out.println("main sentence1");
				String actID = ACTION + actionIDs.get(action);
				addIdeaHandler(actID);
				isMainIdea = false;
			}*/
			String actID = ACTION + actionIDs.get(wrapper.action);
			handler.actionFoundHandler(actID);
			return;
		}
		
		Action tmpLastAction = currentAction;
		QuantSubstance tmpSubstance = currentSubstance;

		actionIDs.put(wrapper.action, actionsNbr);
		String actID = ACTION + actionsNbr;
		actionsNbr++;

		handler.beginActionHandler(actID, wrapper.verb, wrapper.adverbs);
		currentAction = tmpLastAction;
		currentSubstance = tmpSubstance;

		handler.beginAgentsHandler(actID);
		processDisjunctions(wrapper.doers);
		handler.endAgentsHandler(actID);
		
		currentAction = tmpLastAction;
		currentSubstance = tmpSubstance;

		handler.beginThemesHandler(actID);
		processDisjunctions(wrapper.receivers);
		handler.endThemesHandler(actID);
		
		currentAction = tmpLastAction;
		currentSubstance = tmpSubstance;
		
		for(Place place: wrapper.locations) place.process(this);
		
		currentAction = tmpLastAction;
		currentSubstance = tmpSubstance;
		
		for(Time time: wrapper.times) time.process(this);
		
		currentAction = tmpLastAction;
		currentSubstance = tmpSubstance;
		
		handler.beginActionRelativeHandler(actID);
		for (Relative relative: wrapper.relatives){
			relative.process(this);
		}
		handler.endActionRelativeHandler(actID);
			
		handler.endActionHandler(actID, wrapper.verb, wrapper.adverbs);
		
		if (isMainIdea && currentMinds.peek().getSubstance().getNounSynSet() == 0){
			//System.out.println("main sentence");
			handler.addIdeaHandler(actID);
			isMainIdea = false;
		}
		
		currentAction = tmpLastAction;
		currentSubstance = tmpSubstance;

	}
	

	public void processState(StateWrapper wrapper){
		
		if(!wrapper.mainActions.contains(currentAction)) return;
		
		boolean isAgent = wrapper.stateAction.hasAgent(currentSubstance);
		boolean isTheme = wrapper.stateAction.hasTheme(currentSubstance);
		if(!( isAgent || isTheme )) return;
		
		Action tmpLastAction = currentAction;
		QuantSubstance tmpSubstance = currentSubstance;
		
		Action stateAction = wrapper.stateAction;
		if (isAgent)  stateAction= stateAction.copyAgentTheme(false, true);
		if (isTheme)  stateAction= stateAction.copyAgentTheme(true, false);
		
		stateAction.process(this);
		
		String actID = ACTION + actionIDs.get(stateAction);
		
		
		handler.addStateHandler(isAgent, actID);
		
		currentAction = tmpLastAction;
		currentSubstance = tmpSubstance;
	}

	private void processDisjunctions(Set<ConjunctedSubstances> disjSub){
		//Disjunctions 
		for(ConjunctedSubstances conj: disjSub){
			handler.beginDisjunctionHandler();
			for (QuantSubstance substance: conj){
				substance.process(this);
			}

			handler.endDisjunctionHandler();
		}
	}
	
	/*private void addSubstance(String id, Substance sub, Quantity pl, Quantity nbr){
		
		beginSubstanceHandler(id, sub.getNoun());
		
		if(pl != null) pl.process(this);
		
		if(nbr != null) nbr.process(this);
		
		for (Quality ql : sub.getQualities()) ql.process(this);
		endSubstanceHandler();
	}*/
	
	public void processQuality(QualityWrapper wrapper){
		handler.addQualityHandler(wrapper.adjective, wrapper.adverbs);
	}
	
	public void processQuantity(QuantityWrapper wrapper){
		Noun unit = (wrapper.unit == null)? null: wrapper.unit.getNoun();
		if(wrapper.plural) handler.addQuantityHandler(unit);
		else handler.addQuantityHandler(wrapper.nbr, unit, wrapper.cardinal);
	}

	
	public void processSubstance(QSubstanceWrapper wrapper){
		currentSubstance = wrapper.qsubstance;
		
		Action tmpLastAction = currentAction;
		//QuantSubstance tmpSubstance = currentSubstance;
		if (qsubstanceIDs.containsKey(wrapper.qsubstance)){
			String subID = ROLE + qsubstanceIDs.get(wrapper.qsubstance);
			handler.substanceFoundHandler(subID);
			return;
		}
		
		String subID = ROLE + substancesNbr;
		
		
		//List<HashMap<Integer, List<State>>> idStates = new ArrayList<>();
		
		qsubstanceIDs.put(wrapper.qsubstance, substancesNbr);
		
		substancesNbr++;
		
		handler.beginSubstanceHandler(subID, wrapper.noun);
		
		if(wrapper.plQuantity != null) wrapper.plQuantity.process(this);
		
		if(wrapper.nbrQuantity != null) wrapper.nbrQuantity.process(this);
		
		for (Quality ql : wrapper.qualities) ql.process(this);
		
		String actID = ACTION + actionIDs.get(tmpLastAction);
		
		handler.beginStateHandler(subID, actID);
		for (State state: wrapper.states){
			
			state.process(this);
		}
		handler.endStateHandler(subID, actID);
		
		handler.beginSubstanceRelativeHandler(subID);
		for (Relative relative: wrapper.relatives){
			relative.process(this);
		}
		handler.endSubstanceRelativeHandler(subID);
		
		
		handler.endSubstanceHandler(subID, wrapper.noun);
		
		//currentAction = tmpLastAction;
		//currentSubstance = tmpSubstance;
	}

	public void processSubstance(SubstanceWrapper wrapper){
		
		if (substanceIDs.containsKey(wrapper.substance)){
			String subID = ROLE + substanceIDs.get(wrapper.substance);
			handler.substanceFoundHandler(subID);
			return;
		}
		
		String subID = ROLE + substancesNbr;
		substanceIDs.put(wrapper.substance, substancesNbr);
		substancesNbr++;
		
		handler.beginSubstanceHandler(subID, wrapper.noun);
		for (Quality ql : wrapper.qualities) ql.process(this);
		handler.endSubstanceHandler(subID, wrapper.noun);
	}
	
	/**
	 * This is called by {@link kariminf.faris.knowledge.Mind} to process the caller's mind
	 * @param wrapper the Mind wrapper
	 */
	public void processMind(MindWrapper wrapper){
		
		//These cases when a substance or its noun are null may never happen
		//but as a security measure, I added the two checks
		Substance sub = wrapper.owner.getSubstance();
		if (sub == null) return;
		Noun n = sub.getNoun();
		if (n == null) return;
		
		currentMinds.push(wrapper.owner);
		
		//if the owner of the mind is not the common sense ($) or faris global
		// mind: process that mind
		if (!wrapper.name.equals("$")) {
			wrapper.owner.process(this);
		}

		for(MentalState ms: wrapper.mentalStates){
			this.processMentalState(ms);
			for(Thought th: wrapper.thoughts.get(ms)){
				th.process(this);
			}
				
		}
		
		currentMinds.pop();
	}
	
	
	public void processMentalState(MentalState ms){
		//A mental state of a mind
		mentalState = ms;
	}
	
	
	@Override
	public void processFaris(FarisWrapper wrapper) {
		Mind mainMind = wrapper.minds.get("$");
		if (mainMind == null) return;
		mainMind.process(this);
		
	}


	@Override
	public void processIdea(ThoughtWrapper wrapper) {
		isMainIdea = true;//The first action
		wrapper.action.process(this);
	}


	@Override
	public void processIdea(OpinionWrapper wrapper) {
		
		
	}


	@Override
	public void processIdea(ConditionalWrapper wrapper) {
		
		
	}
	
	
}
