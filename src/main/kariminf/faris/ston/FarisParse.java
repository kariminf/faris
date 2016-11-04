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


package kariminf.faris.ston;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import kariminf.faris.knowledge.Mind;
import kariminf.faris.knowledge.Mind.MentalState;
import kariminf.faris.linguistic.*;
import kariminf.faris.philosophical.*;
import kariminf.faris.tools.Search;
import kariminf.sentrep.ston.Parser;


/**
 * To parse the STON specification and transform it to Faris knowledge format
 * 
 * @author Abdelkrime Aries (kariminfo0@gmail.com)
 *         <br>
 *         Copyright (c) 2015-2016 Abdelkrime Aries
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
	//private String currentPlayerID;
	private HashMap<String, QuantSubstance> _players = new HashMap<>();
	
	private Action currentAction;

	//private String currentPlayerID;
	
	
	MentalState s = MentalState.FACT;
			
	private HashMap<String, Action> _actions = new HashMap<>();
	
	private HashMap<String, Mind> _minds = new HashMap<>();
	
	private ArrayList<List<String>> disj = new ArrayList<>();
	
	private List<String> mainActionsIDs = new ArrayList<>();
	
	
	public FarisParse(HashSet<Substance> substances, HashSet<Action> actions, HashMap<String, Mind> minds){
		this.substances = substances;
		this.actions = actions;
		this.minds = minds;
	}

	@Override
	protected void addAction(String id, int synSet) {
		
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
		
		
	}
	
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
		minds.put(n, m);
		
		return m;
		
	}
	
	@Override
	protected void endAction(String id, int synSet) {
		//
	}

	@Override
	protected void addVerbSpecif(String tense, String modality,
			boolean progressive, boolean negated) {
		Verb verb = currentAction.getVerb();
		verb.setTense(Ston2FarisLex.getTense(tense));
		//verb.setAspect(Aspect.valueOf(aspect));
	}

	

	@Override
	protected void actionFail() {
	}

	@Override
	protected void addRole(String id, int synSet) {
		
		if (_players.containsKey(id)){
			currentPlayer = _players.get(id);
			return;
		}
		
		Substance sub = Search.getSubstance(substances, new Substance(synSet));
		
		currentPlayer = new QuantSubstance(sub, new Quantity(1.0));
		_players.put(id, currentPlayer);
		
	}
	
	@Override
	protected void endRole(String id) {
		
		//Here the role may exists in substances
		Substance sub = Search.getSubstance(substances, currentPlayer.getSubstance());
		
		if (sub != currentPlayer.getSubstance()){
			//TODO here we have to update the information
			currentPlayer = new QuantSubstance(sub, currentPlayer.getQuantity());
			
			_players.put(id, currentPlayer);
		}
		
		
	}

	@Override
	protected void addAdjective(int synSet, List<Integer> advSynSets) {
		Adjective adj = new Adjective(synSet);
		Quality quality = new Quality(adj);
		quality.setAdverbsInt(advSynSets);
		currentPlayer.getSubstance().addQuality(quality);
		
	}

	@Override
	protected void adjectiveFail() {
	}

	@Override
	protected void roleFail() {
		
	}

	@Override
	protected void parseSuccess() {
		
		for(QuantSubstance sub : _players.values()){	
			substances.add(sub.getSubstance());
		}
		
		for(Action action: _actions.values()){
			actions.add(action);
		}
		
		Mind defaultMind = minds.get("$");
		for(Action action: getActions(mainActionsIDs)){
			defaultMind.addAction(MentalState.FACT, action);
		}
		
		for(Mind mind: _minds.values()){
			minds.put(mind.getName(), mind);
		}
		
		
	}

	@Override
	protected void endActions(boolean mainClause) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void addConjunctions(List<String> IDs) {
		
		disj.add(IDs);
	}


	@Override
	protected void beginAgents() {
		disj = new ArrayList<>();
		
	}
	
	private List<QuantSubstance> getSubstances(List<String> IDs){
		List<QuantSubstance> result = new ArrayList<>();
		for (String roleID: IDs)
			if (_players.containsKey(roleID)){
				QuantSubstance role = _players.get(roleID);
				result.add(role);
			}
		return result;
	}
	
	private List<Action> getActions(List<String> IDs){
		List<Action> result = new ArrayList<>();
		for (String actID: IDs)
			if (_actions.containsKey(actID)){
				Action action = _actions.get(actID);
				result.add(action);
			}
		return result;
	}

	@Override
	protected void endAgents() {
		
		for (List<String> IDs: disj){
			currentAction.addConjunctSubjects(getSubstances(IDs));
		}
		
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
		
	}

	@Override
	protected void adverbFail() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void beginThemes() {
		
		disj = new ArrayList<>();
		
	}

	@Override
	protected void endThemes() {
		if (s == MentalState.FACT){
			for (List<String> IDs: disj){
				currentAction.addConjunctObjects(getSubstances(IDs));
			}
			return;
		}
		
		//TODO he and she **or** me thinks that ...
		for (List<QuantSubstance> agents: currentAction.getSubjects()){
			for (QuantSubstance agent: agents){
				Mind m = addNewMind(agent);
				
				//TODO he thinks that ... and that ... or that ...
				for (List<String> IDs: disj){
					for(Action act: getActions(IDs)){
						m.addAction(s, act);
					}
				}
				
				_minds.put(m.getName(), m);
			}
		}
		
	}

	@Override
	protected void addRoleSpecif(String name, String def, String quantity) {
		currentPlayer.getSubstance().setNounSpecif(name, def);
		
		quantity = quantity.toLowerCase();
		
		if(quantity.length() < 1 || quantity.equals("1")) return;
		
		if (quantity.startsWith("o")){
			quantity = quantity.substring(1);
			
			//quantity = getOrdinal(quantity);
		}
		
		
		if(quantity.equals("pl")) return;
		
		//np.addPreModifier(quantity);
		
	}

	@Override
	protected void parseFail() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void relativeFail() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addRelative(String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addComparison(String type, List<Integer> adjSynSets) {
		// TODO Auto-generated method stub
		
	}

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
			}
		}
		
	}

	@Override
	protected void beginActions(boolean mainClause) {
		// TODO Auto-generated method stub
		
	}


	String proleID = "";
	
	@Override
	protected void addPRole(String id, String pronoun) {
		proleID = id;
		
	}

	@Override
	protected void beginPRelatives() {
		disj = new ArrayList<>();
	}

	@Override
	protected void endPRelatives() {
		if(disj.size() == 1){
			currentPlayer = getSubstances(disj.get(0)).get(0);
			_players.put(proleID, currentPlayer);
			return;
		}
			
		//TODO when we have a lot of players
		
		
	}

}
