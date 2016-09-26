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
import java.util.Set;

import kariminf.faris.knowledge.Mind;
import kariminf.faris.knowledge.Mind.MentalState;
import kariminf.faris.linguistic.Adjective;
import kariminf.faris.linguistic.Verb;
import kariminf.faris.linguistic.Verb.Aspect;
import kariminf.faris.linguistic.Verb.Tense;
import kariminf.faris.philosophical.Action;
import kariminf.faris.philosophical.Quality;
import kariminf.faris.philosophical.Substance;
import kariminf.sentrep.ston.Parser;


/**
 * 
 * @author Abdelkrime Aries (kariminfo0@gmail.com)
 *         <br>
 *         Copyright (c) 2015 Abdelkrime Aries
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
	
	private Substance currentPlayer;
	//private String currentPlayerID;
	private HashMap<String, Substance> _players = new HashMap<>();
	
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
	 * @param s the substance which we want
	 * @return the mind of the substance
	 */
	private Mind addNewMind(Substance s){
		for (Mind m: _minds.values()){
			if(m.hasOwner(s))
				return m;
		}
		
		String n = s.getNounSynSet() + "-" + _minds.size();
		Mind m = new Mind(n, s);
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
		
		currentPlayer = (_players.containsKey(id))? _players.get(id): new Substance(synSet);
		_players.put(id, currentPlayer);
		
	}

	@Override
	protected void addAdjective(int synSet, List<Integer> advSynSets) {
		Adjective adj = new Adjective(synSet);
		Quality quality = new Quality(adj);
		quality.setAdverbsInt(advSynSets);
		currentPlayer.addQuality(quality);
		
	}

	@Override
	protected void adjectiveFail() {
	}

	@Override
	protected void roleFail() {
		
	}

	@Override
	protected void parseSuccess() {
		
		for(Substance sub : _players.values()){
			substances.add(sub);
		}
		
		for(Action action: _actions.values()){
			actions.add(action);
		}
		
		Mind defaultMind = minds.get("Default");
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
	
	private List<Substance> getSubstances(List<String> IDs){
		List<Substance> result = new ArrayList<>();
		for (String roleID: IDs)
			if (_players.containsKey(roleID)){
				Substance role = _players.get(roleID);
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
		// TODO Auto-generated method stub
		
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
		for (List<Substance> agents: currentAction.getSubjects()){
			for (Substance agent: agents){
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
		// TODO Auto-generated method stub
		
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
			
		//TODO where we have a lot of players
		
		
	}

}
