/* FARIS : Factual Arrangement and Representation of Ideas in Sentences
 * FAris : Farabi & Aristotle
 * Faris : A knight (in Arabic)
 * --------------------------------------------------------------------
 * Copyright (C) 2015 Abdelkrime Aries (kariminfo0@gmail.com)
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


package kariminf.faris.process.ston;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import kariminf.faris.knowledge.Mind;
import kariminf.faris.knowledge.Mind.MentalState;
import kariminf.faris.linguistic.*;
import kariminf.faris.philosophical.*;
import kariminf.faris.philosophical.Relative.RelativeType;
import kariminf.faris.process.ston.Concepts.PlaceTime;
import kariminf.faris.tools.Search;
import kariminf.sentrep.UnivMap;
import kariminf.sentrep.ston.Parser;
import kariminf.sentrep.ston.Ston2UnivMap;
import kariminf.sentrep.ston.StonLex;
import kariminf.sentrep.univ.types.Pronoun;
import kariminf.sentrep.univ.types.Pronoun.Head;
import kariminf.sentrep.univ.types.Relation;
import kariminf.sentrep.univ.types.Relation.Adpositional;
import kariminf.sentrep.univ.types.Relation.Adverbial;


/**
 * To parse the STON specification and transform it to Faris knowledge format
 * 
 * @author Abdelkrime Aries (kariminfo0@gmail.com)
 *         <br>
 *         Copyright (c) 2015-2017 Abdelkrime Aries
 *         <br><br>
 *         Licensed under the Apache License, Version 2.0 (the "License");
 *         you may not use this file except in compliance with the License.
 *         You may obtain a copy of the License at
 *         <br><br>
 *         http://www.apache.org/licenses/LICENSE-2.0
 *         <br><br>
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *         See the License for the specific language governing permissions and
 *         limitations under the License.
 */
public class FarisParse extends Parser {

	private HashSet<Substance> substances;
	private HashSet<Action> actions;
	private HashMap<String, Mind> minds;

	private QuantSubstance currentPlayer;

	private HashMap<String, QuantSubstance> _players = new HashMap<>();
	
	//pronouns which are pointed to conjunctios of players
	private HashMap<String, List<String>> _pronouns = new HashMap<>();

	private Action currentAction;

	private String currentActionID;

	private MentalState s = MentalState.FACT;

	private HashMap<String, Action> _actions = new HashMap<>();

	private HashMap<String, Mind> _minds = new HashMap<>();

	private ArrayList<List<String>> disj = new ArrayList<>();
	
	private ArrayList<List<String>> RelDisj = null;

	private HashSet<String> mainActionsIDs = new HashSet<>();

	private HashSet<String> mainMindsIDs = new HashSet<>();

	private UnivMap uMap = new Ston2UnivMap();
	
	String proleID = "";
	Pronoun currentPronoun = null;


	/**
	 * Creates a new Faris Parser
	 * @param substances
	 * @param actions
	 * @param minds
	 */
	public FarisParse(HashSet<Substance> substances, HashSet<Action> actions, HashMap<String, Mind> minds){
		this.substances = substances;
		this.actions = actions;
		this.minds = minds;
	}
	
	
	//=====================================================================
	//======================= PRIVATE METHODS =============================
	//=====================================================================
	
	/**
	 * Adds a new mind of the substance if it doesn't exist
	 * @param agent the substance which we want
	 * @return the mind of the substance
	 */
	private Mind addNewMind(QuantSubstance agent){

		for (Mind m: _minds.values()){
			if(m.hasOwner(agent))
				return m;
		}

		String n = agent.getSubstance().getNounSynSet() + "-" + _minds.size();
		Mind m = new Mind(n, agent);

		//Here we put the label of the action as mind

		if (currentActionID != null)
			n = currentActionID;
		_minds.put(n, m);

		return m;

	}//addNewMind
	
	private List<QuantSubstance> getSubstances(List<String> IDs){
		List<QuantSubstance> result = new ArrayList<>();
		for (String roleID: IDs)
			if (_players.containsKey(roleID)){
				QuantSubstance role = _players.get(roleID);
				result.add(role);
			} else if (_pronouns.containsKey(roleID)){
				List<QuantSubstance> result2 =
						getSubstances(_pronouns.get(roleID));
				if (! result2.isEmpty())
					result.addAll(result2);
			}
		return result;
	}//getSubstances

	private List<Action> getActions(Collection<String> IDs){
		List<Action> result = new ArrayList<>();
		for (String actID: IDs)
			if (_actions.containsKey(actID)){
				Action action = _actions.get(actID);
				result.add(action);
			}
		return result;
	}//getActions

	private List<Mind> getMinds(Collection<String> IDs){
		List<Mind> result = new ArrayList<>();
		for (String actID: IDs)
			if (_minds.containsKey(actID)){
				Mind mind = _minds.get(actID);
				result.add(mind);
			}
		return result;
	}//getMinds
	
	
	
	
	//=====================================================================
	//================== Implementing methods =============================
	//=====================================================================
		
		
	//=====================================================================
	//======================== ACTION METHODS =============================
	//=====================================================================

	@Override
	protected void beginAction(String id, int synSet) {

		//MentalState pastState = s;

		s = Concepts.getMentalState(synSet);

		//We will need the action to save the subjects and the objects
		//even if the sate is not a fact
		Verb verb = new Verb(synSet);
		currentAction = Action.getNew(verb);

		if (s == MentalState.FACT){
			_actions.put(id, currentAction);
			//pastState = MentalState.FACT;
		}

		currentActionID = id;


	}

	@Override
	protected void endAction(String id, int synSet) {
		currentActionID = null;
	}
	
	@Override
	protected boolean actionFailure() {
		return true;
	}

	@Override
	protected void addVerbSpecif(String tense, String modality,
			boolean progressive, boolean perfect, boolean negated) {
		Verb verb = currentAction.getVerb();
		verb.setTense(Ston2FarisLex.getTense(tense));
		//verb.setAspect(Aspect.valueOf(aspect));
	}


	@Override
	protected void addActionAdverb(int advSynSet, List<Integer> advSynSets) {

		Adverb adv = new Adverb(advSynSet);

		switch (Concepts.getAdverbType(advSynSet)) {

		case PLACE:
			Place place = new Place(adv);
			currentAction.addLocation(place);
			break;

		case TIME:
			Time time = new Time(adv);
			currentAction.addTime(time);
			break;

		default:
			currentAction.addAdverb(adv, null);
			break;
		}

	}//addActionAdverb
	
	@Override
	protected boolean adverbFailure() {
		return true;
	}//adverbFailure
	
	@Override
	protected void beginAgents() {
		disj = new ArrayList<>();

	}//beginAgents

	@Override
	protected void endAgents() {

		for (List<String> IDs: disj){
			currentAction.addConjunctSubjects(getSubstances(IDs));
		}

	}//endAgents
	
	@Override
	protected void beginThemes() {

		disj = new ArrayList<>();

	}//beginThemes

	@Override
	protected void endThemes() {

		if (s == MentalState.FACT){
			for (List<String> IDs: disj){
				currentAction.addConjunctObjects(getSubstances(IDs));
			}
			return;
		}

		//TODO he and she **or** me thinks that ...
		for (List<QuantSubstance> agents: currentAction.getAgents()){
			for (QuantSubstance agent: agents){
				Mind m = addNewMind(agent);

				//TODO he thinks that ... and that ... or that ...
				for (List<String> IDs: disj){
					for(Action act: getActions(IDs)){
						m.addAction(s, act);
					}

					for(Mind m2: getMinds(IDs)){
						m.addOpinion(s, m2);
						//System.out.println(m2.getName());
					}
				}

				//The mind is added in the function addNewMind()
				//_minds.put(m.getName(), m);
			}
		}

	}//endThemes
	
	@Override
	protected void beginComparison(String type, List<Integer> adjSynSets) {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void endComparison(String type, List<Integer> adjSynSets) {
		// TODO Auto-generated method stub
		
	}
	
	//=====================================================================
	//========================= ROLE METHODS ==============================
	//=====================================================================

	@Override
	protected void beginRole(String id, int synSet) {

		if (_players.containsKey(id)){
			currentPlayer = _players.get(id);
			return;
		}

		Substance sub = Search.getElement(substances, new Substance(synSet));

		currentPlayer = new QuantSubstance(sub);
		_players.put(id, currentPlayer);

	}//beginRole
	
	@Override
	protected void beginRole(String id, int synSet, String pronoun) {
		proleID = id;

		currentPronoun = uMap.mapPronoun(pronoun);


	}//beginRole (pronoun)

	@Override
	protected void endRole(String id, int synSet) {
		//System.out.println(id);
		if (currentPronoun != null){
			switch (currentPronoun.getHead()) {

			case POSSESSIVE:
				//delete id from pronouns
				//TODO add relative OF
				_pronouns.remove(id);
				break;
			case DEMONSTRATIVE:
				//delete id from pronouns
				_pronouns.remove(id);
				break;
			case OBJECTIVE:
				//delete id from pronouns
				_pronouns.remove(id);
				break;
			case SUBJECTIVE:
				
				break;
			default:
				break;
			}
			currentPronoun = null;
			return;
		}

		//Here the role may exists in substances
		Substance sub = Search.getElement(substances, currentPlayer.getSubstance());

		//When the substance if found in the set of substances
		if (sub != currentPlayer.getSubstance()){
			currentPlayer = QuantSubstance.withNewSubstance(currentPlayer, sub);
			_players.put(id, currentPlayer);
		}


	}//endRole
	
	
	@Override
	protected boolean roleFailure() {
		return true;
	}//roleFailure

	@Override
	protected void addRoleSpecif(String name, String def, String quantity) {
		currentPlayer.getSubstance().setNounSpecif(name, def);

		quantity = quantity.toLowerCase();

		if(quantity.endsWith("pl")){
			int len = quantity.length();
			quantity = quantity.substring(0, len-2);
			Quantity farisQuantity = new Quantity();
			currentPlayer.setQuantity(farisQuantity);
		}
		
		if(quantity.length() < 1 || quantity.equals("1")) return;
		
		boolean isOrdinal = false;
		if (quantity.startsWith("o")){
			quantity = quantity.substring(1);
			isOrdinal = true;
		}
		
		double numQuantity = Double.parseDouble(quantity);
		Quantity farisQuantity = new Quantity(numQuantity);
		if(isOrdinal) farisQuantity.setOrdinal();
		currentPlayer.setQuantity(farisQuantity);
	}//addRoleSpecif
	
	@Override
	protected void addAdjective(int synSet, List<Integer> advSynSets) {
		Adjective adj = new Adjective(synSet);
		Quality quality = new Quality(adj);
		quality.setAdverbsInt(advSynSets);
		currentPlayer.getSubstance().addQuality(quality);

	}//addAdjective

	@Override
	protected boolean adjectiveFailure() {
		return true;
	}//adjectiveFailure

	@Override
	protected void beginPRelatives() {
		disj = new ArrayList<>();
	}

	@Override
	protected void endPRelatives() {
		
		
		if(disj.isEmpty() || disj.get(0).isEmpty()) return;
		
		_pronouns.put(proleID, disj.get(0));
		 
		disj = null;
		//TODO when we have a disjunction of players
		

	}
	
	//=====================================================================
	//======================= SENTENCE METHODS ============================
	//=====================================================================

	@Override
	protected void beginSentence(String type) {
		disj = new ArrayList<>();

	}

	@Override
	protected void endSentence(String type) {
		//Verify the main actions
		for (List<String> actIDs: disj){
			for(String actID: actIDs){
				if (_actions.containsKey(actID))
					mainActionsIDs.add(actID);

				if (_minds.containsKey(actID))
					mainMindsIDs.add(actID);
			}
		}

	}

	@Override
	protected void beginActions(boolean mainClause) {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void endActions(boolean mainClause) {
		// TODO Auto-generated method stub

	}//endActions
	
	//=====================================================================
	//======================== SHARED METHODS =============================
	//=====================================================================
	
	@Override
	protected void addConjunctions(List<String> IDs) {
		
		if (RelDisj != null){
			RelDisj.add(IDs);
			return;
		}

		disj.add(IDs);
	}
	
	@Override
	protected void beginRelative(String type) {
		RelDisj = new ArrayList<>();
	}
	
	@Override
	protected void endRelative(String type) {
		
		if (RelDisj.isEmpty() ||
				RelDisj.get(0).isEmpty() ||
				!_players.containsKey(RelDisj.get(0).get(0))) {
			RelDisj = null;
			return;
		}
		
		type = type.toUpperCase();
		
		//System.out.print("EndRelative: " + type);
		
		Adpositional adp = uMap.mapAdposition(type);
		
		int firstSynset = _players.get(RelDisj.get(0).get(0)).getSubstance().getNounSynSet();
		
		PlaceTime adjType = Concepts.getAdjType(adp, firstSynset);
		
		//System.out.println(" which is " + adjType + "." + adp + ".syn:" + firstSynset);

		//The destination is a role
		//============================
		if (StonLex.isPredicateRole(type)){
			
			//The main clause is an action
			//==============================
			//eg. The man is IN the car
			if (currentActionID != null){
				switch (adjType) {
				case PLACE:
					Place p = new Place(adp);
					for (List<String> conj: RelDisj)
						for (String subID: conj)
							if (_players.containsKey(subID)){
								p.addLocation(_players.get(subID));
							}
					currentAction.addLocation(p);
					break;
				case TIME:
					Time t = new Time(adp);
					for (List<String> conj: RelDisj)
						for (String subID: conj)
							if (_players.containsKey(subID)){
								t.addTimeSubstance(_players.get(subID));
							}
					currentAction.addTime(t);
					break;
				default:
					/*
					Relative r = new Relative();
					
					for (List<String> conj: disj)
						for (String subID: conj)
							if (_players.containsKey(subID)){
								p.addLocation(_players.get(subID));
							}
					currentAction.addLocation(p);*/
					break;
				}
				
				RelDisj = null;
				return;
			}
			
			//Here the destination must be defined before
			//The main clause is a role
			//=========================
			//eg. The man IN the car
			Verb toBe = new Verb(2604760);
			Action stateAction = Action.getNew(toBe);//To be
			State state = new State(stateAction);
			switch (adjType) {
			case PLACE:
				break;
			case TIME:
				break;
				
			case OTHER:
				if (adp == Adpositional.POSSESSION){
					//System.out.println("==> OF");
					for (List<String> conj: RelDisj)
						for (String subID: conj)
							if (_players.containsKey(subID)){
								QuantSubstance relative = _players.get(subID);
								Relative.affectRelative(currentPlayer, relative);
							}
				}
				break;
				
			default:
				break;
			}
			
			
			RelDisj = null;
			return;
		}

		//The destination is an action

		//TODO the action can refer to an action:
		//He is Where I can see him
		if (currentActionID != null){
			//from action to action (adverbials)
			Adverbial adv = uMap.mapAdverbial(type);
			
			switch (adv) {
			case AFTER:
				break;
			case BEFORE:
				break;
			case CONDITION:
				break;
			case CONSESSION:
				break;
			case CONTINUUM:
				break;
			case MANNER:
				break;
			case PLACE:
				break;
			case PURPOSE:
				break;
			case REASON:
				break;
			case TIME:
				break;
			default:
				break;
			}
			
			//System.out.println("Adverbial: " + adv);
			RelDisj = null;
			return;
		}

		//The main clause is a role
		//eg. The man who is driving
		Relation.Relative rel = uMap.mapRelative(type);
		
		//TODO a list of states must be retained
		//This can't be handled here, because we didn't create the relative action yet
		
		
		RelDisj = null;
		
	}//endRelative
	
	@Override
	protected boolean relativeFailure() {
		return true;

	}
	
	//=====================================================================
	//========================= PARSE METHODS =============================
	//=====================================================================
	
	@Override
	protected void parseSuccess() {

		for(QuantSubstance sub : _players.values()){	
			substances.add(sub.getSubstance());
		}


		HashSet<Action> _mainactions = new HashSet<>();
		for(String id: _actions.keySet()){
			//If the action exists, we update the information 
			Action action = _actions.get(id);
			Action act = Search.getElement(actions, action);
			act.update(action);
			actions.add(act);

			if (mainActionsIDs.contains(id)){
				_mainactions.add(act);
			}


		}

		Mind defaultMind = minds.get("$");
		for(Action action: _mainactions){
			defaultMind.addAction(MentalState.FACT, action);
		}


		for(String mindID: mainMindsIDs){
			Mind mind = _minds.get(mindID);
			minds.put(mind.getName(), mind);
		}


	}//parseSuccess



	@Override
	protected void parseFailure() {
		// TODO Auto-generated method stub

	}

}
