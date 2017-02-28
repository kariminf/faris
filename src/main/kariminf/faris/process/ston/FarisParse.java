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

import kariminf.faris.knowledge.Faris.FarisWrapper;
import kariminf.faris.knowledge.Mind;
import kariminf.faris.knowledge.Mind.MentalState;
import kariminf.faris.linguistic.*;
import kariminf.faris.linguistic.Verb.Aspect;
import kariminf.faris.philosophical.*;
import kariminf.faris.philosophical.Relative.RelativeType;
import kariminf.faris.process.ston.Concepts.PlaceTime;
import kariminf.faris.tools.Search;
import kariminf.sentrep.UnivMap;
import kariminf.sentrep.ston.Parser;
import kariminf.sentrep.ston.Ston2UnivMap;
import kariminf.sentrep.ston.StonLex;
import kariminf.sentrep.univ.types.Comparison;
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

	private FarisWrapper wrapper;


	private static class TmpRelative {
		Relation.Relative rel;
		ArrayList<List<String>> RelDisj;
		//we dont affect the state, but it is helpful to add main actions
		State state = new State(); 
		QuantSubstance substance;
	}
	private HashMap<String, List<TmpRelative>> subsRel = new HashMap<>();

	private QuantSubstance currentPlayer;

	private HashMap<String, QuantSubstance> _players = new HashMap<>();

	//pronouns which are pointed to conjunctions of players
	private HashMap<String, List<String>> _pronouns = new HashMap<>();

	private Action currentAction;

	private String currentActionID;

	private String currentPlayerID;

	private HashMap<String, List<State>> _states = new HashMap<>();

	private MentalState s = MentalState.FACT;

	private HashMap<String, Action> _actions = new HashMap<>();

	private HashMap<String, Mind> _minds = new HashMap<>();

	private ArrayList<List<String>> disj = new ArrayList<>();

	private ArrayList<List<String>> mainActDisj;
	private ArrayList<List<String>> secActDisj;

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
	public FarisParse(FarisWrapper wrapper){
		this.wrapper = wrapper;
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

	/**
	 * 
	 * @param IDs
	 * @return
	 */
	private List<QuantSubstance> getSubstances(List<String> IDs){
		List<QuantSubstance> result = new ArrayList<>();
		for (String roleID: IDs)
			if (_players.containsKey(roleID)){
				QuantSubstance role = _players.get(roleID);
				result.add(role);
				if (_states.containsKey(roleID)){
					List<State> states = _states.get(roleID);
					for (State state: states)
						state.addMainAction(currentAction);
				} else if (subsRel.containsKey(roleID))
					for (TmpRelative tmpRel: subsRel.get(roleID))
						tmpRel.state.addMainAction(currentAction);
			} else if (_pronouns.containsKey(roleID)){
				List<QuantSubstance> result2 =
						getSubstances(_pronouns.get(roleID));
				if (! result2.isEmpty())
					result.addAll(result2);
			}
		return result;
	}//getSubstances


	/**
	 * Get the available actions from a collection (List, Set, etc.) of their IDs
	 * @param IDs The IDs of the actions we want to retreive
	 * @return The actions already created having these IDs
	 */
	private List<Action> getActions(Collection<String> IDs){
		List<Action> result = new ArrayList<>();
		for (String actID: IDs)
			if (_actions.containsKey(actID)){
				Action action = _actions.get(actID);
				result.add(action);
			}
		return result;
	}//getActions

	/**
	 * Get a list of minds from a collection of IDs
	 * @param IDs The IDs of the actions already created within this parser
	 * @return A list of existing minds having the input IDs
	 */
	private List<Mind> getMinds(Collection<String> IDs){
		List<Mind> result = new ArrayList<>();
		for (String actID: IDs)
			if (_minds.containsKey(actID)){
				Mind mind = _minds.get(actID);
				result.add(mind);
			}
		return result;
	}//getMinds



	private void role2roleRelative(Adpositional adp){
		//Here the destination must be defined before
		//The main clause is a role
		//=========================
		//eg. The mother of the child
		//eg. The man IN the car

		if (currentPlayer == null || currentPlayerID == null){
			RelDisj = null;
			return;
		}

		//The mother OF the child
		if (adp == Adpositional.POSSESSION){
			//System.out.println("==> OF");
			for (List<String> conj: RelDisj)
				for (String subID: conj)
					if (_players.containsKey(subID)){
						QuantSubstance relative = _players.get(subID);
						Relative.affectRelative(currentPlayer, relative);
					}
			RelDisj = null;
			return;
		}

		//The man in the car
		//This can be handled as "The man which is being in the car"
		//As a state of a man <subject>, being in the car <no objects>

		int firstSynset = _players.get(RelDisj.get(0).get(0)).getSubstance().getNounSynSet();

		PlaceTime adjType = Concepts.getAdjType(adp, firstSynset);

		Verb toBe = new Verb(2604760);
		Action stateAction = Action.getNew(toBe);//To be

		switch (adjType) {
		case PLACE:{
			Place p = new Place(adp);
			for (List<String> conj: RelDisj)
				for (String subID: conj)
					if (_players.containsKey(subID)){
						p.addLocation(_players.get(subID));
					}
			stateAction.addLocation(p);
			break;
		}

		case TIME:{
			Time t = new Time(adp);
			for (List<String> conj: RelDisj)
				for (String subID: conj)
					if (_players.containsKey(subID)){
						t.addTimeSubstance(_players.get(subID));
					}
			stateAction.addTime(t);
			break;
		}

		default:
			RelDisj = null;
			return;
		}

		State state = new State();
		state.affectState(stateAction, currentPlayer, null, Relation.Relative.SUBJECT);

		List<State> states = new ArrayList<State>();
		states.add(state);
		_states.put(currentPlayerID, states);


		RelDisj = null;
	}

	/**
	 * Called inside endRelative to process the case where an action is having 
	 * some roles as relatives
	 * @param adp The adposition (preposition) describing the relation
	 */
	private void action2roleRelative(Adpositional adp){

		int firstSynset = _players.get(RelDisj.get(0).get(0)).getSubstance().getNounSynSet();

		PlaceTime adjType = Concepts.getAdjType(adp, firstSynset);

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
			//System.out.println(currentActionID + ": " + adp);
			//TODO think about these situations
			/*
			 * madefamous: ROLE 
			 * began_writing_again: ACCOMPANY
			 * began_writing_again: EXIST 
			 * concealed: SITUATION 
			 * employed: ROLE
			 * made: DESTINATION 
			 * regarded: ROLE 
			 * mentioned: BETWEEN 
			 * is_inevit: BETWEEN
			 * 
			 */
			/*
			 * Relative r = new Relative();
			 * 
			 * for (List<String> conj: disj) for (String subID: conj) if
			 * (_players.containsKey(subID)){
			 * p.addLocation(_players.get(subID)); }
			 * currentAction.addLocation(p);
			 */
			break;
		}

		RelDisj = null;
	}//action2roleRelative


	/**
	 * Called inside endRelative to process the case where an action is having 
	 * some actions as relatives
	 * @param adv the adverb describing the relation
	 */
	private void action2actionRelative(Adverbial adv){
		//TODO the action can refer to an action
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
	}//action2actionRelative


	/**
	 * Called inside endRelative to process the case where a role is having 
	 * some actions as relatives
	 * @param rel
	 */
	private void role2actionRelative(Relation.Relative rel){

		//because we didn't create the relative action yet, 
		//a list of states of each role must be retained
		//The states will be processed in "parseSuccess"
		List<TmpRelative> listRel = new ArrayList<TmpRelative>();
		subsRel.put(currentPlayerID, listRel);
		
		//System.out.println(rel);
		
		//These lists are composed of some objects, containing information
		//about the elements used to create a state
		TmpRelative tr = new TmpRelative();
		tr.rel = rel;
		tr.RelDisj = RelDisj;
		tr.substance = currentPlayer;

		listRel.add(tr);
		
	}//role2actionRelative
	

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
		//even if the state is not a fact
		Verb verb = new Verb(synSet);
		currentAction = Action.getNew(verb);


		if (s == MentalState.FACT){
			_actions.put(id, currentAction);
			//pastState = MentalState.FACT;
		}

		currentActionID = id;


	}//beginAction

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

		if (progressive && perfect) verb.setAspect(Aspect.PROGRESSIVEPERFECT);
		else if (progressive) verb.setAspect(Aspect.PROGRESSIVE);
		else if (perfect) verb.setAspect(Aspect.PERFECT);

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
		disj = new ArrayList<>();

	}

	@Override
	protected void endComparison(String type, List<Integer> adjSynSets) {

		if (currentAction == null) return;

		Comparison cmp = uMap.mapComparison(type);
		for (List<String> conj: disj)
			for (String subID: conj)
				if (_players.containsKey(subID)){
					QuantSubstance relative = _players.get(subID);
					for (int adjSynSet: adjSynSets){
						Adjective adj = new Adjective(adjSynSet);
						Relative.affectRelative (RelativeType.fromComparison(cmp), 
								adj, currentAction, relative);
					}

				}
		disj = null;

	}//endComparison

	//=====================================================================
	//========================= ROLE METHODS ==============================
	//=====================================================================

	@Override
	protected void beginRole(String id, int synSet) {
		//System.out.println("role: " + id);
		if (_players.containsKey(id)){
			currentPlayer = _players.get(id);
			return;
		}

		Substance sub = Search.getElement(wrapper.substances, new Substance(synSet));

		currentPlayer = new QuantSubstance(sub);
		_players.put(id, currentPlayer);

		currentPlayerID = id;

	}//beginRole

	@Override
	protected void beginRole(String id, int synSet, String pronoun) {
		//System.out.println("role: " + id);
		if (synSet > 0) beginRole(id, synSet);

		proleID = id;

		currentPronoun = uMap.mapPronoun(pronoun);

		currentPlayerID = id;

	}//beginRole (pronoun)

	@Override
	protected void endRole(String id, int synSet) {
		//System.out.println(id);
		currentPlayerID = null;

		if (currentPronoun != null){
			switch (currentPronoun.getHead()) {

			case POSSESSIVE:

				if (synSet == 0 ){
					return;
				}
				//delete id from pronouns
				//System.out.println("OF pronoun");
				if (_pronouns.containsKey(id)){
					for (String relID: _pronouns.get(id)){
						if (_players.containsKey(relID)){
							Relative.affectRelative(currentPlayer, _players.get(relID));
						}
					}
					//_pronouns.remove(id);
				}

				break;
			case DEMONSTRATIVE:
				//delete id from pronouns
				//_pronouns.remove(id);
				break;
			case OBJECTIVE:
				//delete id from pronouns
				//_pronouns.remove(id);
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
		Substance sub = Search.getElement(wrapper.substances, currentPlayer.getSubstance());

		//When the substance is found in the set of substances
		if (sub != currentPlayer.getSubstance()){
			currentPlayer = QuantSubstance.withNewSubstance(currentPlayer, sub);
			_players.put(id, currentPlayer);
		}

		currentPlayer = null;

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
		
		if (!isOrdinal && numQuantity==1) return;
		
		//System.out.println(numQuantity);
		Quantity farisQuantity = new Quantity(numQuantity);
		//System.out.println(numQuantity + ": " + farisQuantity.isPlural());
		if(isOrdinal) farisQuantity.setOrdinal();
		currentPlayer.setQuantity(farisQuantity);
		//System.out.println(currentPlayer + ": " + currentPlayer.getNbrQuanty());
	}//addRoleSpecif


	@Override
	protected void addAdjective(int synSet, List<Integer> advSynSets) {
		Adjective adj = new Adjective(synSet);
		Quality quality = new Quality(adj);
		quality.setAdverbsInt(advSynSets);

		if (currentPlayer != null){
			currentPlayer.getSubstance().addQuality(quality);
			return;
		}
		
		for (String relID: _pronouns.get(currentPlayerID)){
			if (_players.containsKey(relID)){
				_players.get(relID).getSubstance().addQuality(quality);
			}
		}

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
		mainActDisj = null;
		secActDisj = null;
	}

	@Override
	protected void endSentence(String type) {
		//Verify the main actions
		for (List<String> actIDs: mainActDisj){
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
		disj = new ArrayList<>();
	}

	@Override
	protected void endActions(boolean mainClause) {
		if (mainClause) mainActDisj = disj;
		else secActDisj = disj;
		disj = null;
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

		if (RelDisj.isEmpty() || RelDisj.get(0).isEmpty()) {
			RelDisj = null;
			return;
		}

		type = type.toUpperCase();

		//System.out.println("EndRelative: " + type);

		//The destination is a role
		//==========================
		if (StonLex.isPredicateRole(type)){

			Adpositional adp = uMap.mapAdposition(type);

			//The main clause is an action (Action-Role)
			//==========================================
			//eg. The man is IN the car
			if (currentActionID != null){
				action2roleRelative(adp);
				RelDisj = null;
				return;
			}

			//The main clause is a role (Role-Role)
			//=====================================
			//eg. The mother of the child
			//eg. The man IN the car
			role2roleRelative(adp);
			RelDisj = null;
			return;
		}//End destination Role

		//The destination is an action
		//================================


		//The main clause is an action (Action-Action)
		//============================================
		//He is Where I can see him
		if (currentActionID != null){
			Adverbial adv = uMap.mapAdverbial(type);
			action2actionRelative(adv);
			RelDisj = null;
			return;
		}


		//The main clause is a role (Role-Action)
		//========================================
		//eg. The man who is driving

		if (currentPlayer == null || currentPlayerID == null){
			RelDisj = null;
			return;
		}

		Relation.Relative rel = uMap.mapRelative(type);
		role2actionRelative(rel);

		RelDisj = null;

	}//endRelative

	@Override
	protected boolean relativeFailure() {
		return true;

	}//relativeFailure

	//=====================================================================
	//========================= PARSE METHODS =============================
	//=====================================================================

	@Override
	protected void parseSuccess() {

		for(QuantSubstance sub : _players.values()){	
			wrapper.substances.add(sub.getSubstance());
		}


		HashSet<Action> _mainactions = new HashSet<>();
		for(String id: _actions.keySet()){
			//If the action exists, we update the information 
			Action action = _actions.get(id);
			Action act = Search.getElement(wrapper.actions, action);
			act.update(action);
			wrapper.actions.add(act);

			if (mainActionsIDs.contains(id)){
				_mainactions.add(act);
			}
		}

		Mind defaultMind = wrapper.minds.get("$");
		for(Action action: _mainactions){
			defaultMind.addAction(MentalState.FACT, action);
		}


		for(String mindID: mainMindsIDs){
			Mind mind = _minds.get(mindID);
			wrapper.minds.put(mind.getName(), mind);
		}

		for(List<State> states: _states.values()){
			wrapper.states.addAll(states);
		}


		//Handle relatives Role-action
		for (String subID: subsRel.keySet()){
			for(TmpRelative tmpRel: subsRel.get(subID)){

				for(List<String> conj: tmpRel.RelDisj){
					for(String stateActID: conj)
						if (_actions.containsKey(stateActID)){
							Action stateAction = _actions.get(stateActID);
							State state = tmpRel.state;
							state.affectState(stateAction, tmpRel.substance, tmpRel.rel);
							wrapper.states.add(state);

						}		
				}
			}
		}

	}//parseSuccess



	@Override
	protected void parseFailure() {
		// TODO Auto-generated method stub

	}

}
