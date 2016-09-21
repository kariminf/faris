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


package kariminf.faris.knowledge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kariminf.faris.linguistic.Verb;
import kariminf.faris.philosophical.Action;
import kariminf.faris.philosophical.Quality;
import kariminf.faris.philosophical.Substance;
import kariminf.sentrep.ston.request.ReqCreator;


/**
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
public class Mind {

	public static enum MentalState {
		THINK,
		BELIEVE,
		HOPE,
		FEAR,
		FACT
	}

	private String name;
	private Substance owner;

	//even conditional have a truth level: "I think if ..., then ... ."
	private HashMap<MentalState, List<Idea>> truthTable = new HashMap<>();

	public Mind(String name) {
		this.name = name;
	}

	private List<Idea> getIdeas(MentalState ms){
		List<Idea> ideas;
		if (truthTable.containsKey(ms)){
			ideas = truthTable.get(ms);
		}
		else{
			ideas = new ArrayList<Idea>();
			truthTable.put(ms, ideas);
		}

		return ideas;
	}

	public void addAction(MentalState ms, Action action){
		//TODO verify if the action exists already, and if other components have to be added

		List<Idea> ideas = getIdeas(ms);

		Thought thought = new Thought(action);

		ideas.add(thought);
	}

	public void addOpinion(MentalState ms, Mind othersThoughts){
		//TODO verify if the action exists already, and if other components have to be added
		//opinions.put(truth, othersThoughts);
	}

	public void addCondition(MentalState ms, Conditional condition){
		List<Idea> ideas = getIdeas(ms);

		ideas.add(condition);
	}

	public String getNoAdjectives(){
		if (! truthTable.containsKey(MentalState.FACT))
			return "";
		ReqCreator rq = new ReqCreator();
		//Affecting a label for each substance: subjects and objects
		int numRoles = 0;
		int numActions = 0;
		HashMap<Substance, String> roles = new HashMap<Substance, String>();

		List<Idea> ideas = getIdeas(MentalState.FACT);

		for (Idea idea : ideas){
			if (! (idea instanceof Thought)) continue;
			Action action = ((Thought) idea).getAction();

			Verb verb = action.getVerb();
			String actionId = "action" + numActions;
			rq.addAction(actionId, verb.getSynSet());
			numActions++;
			
			for (ArrayList<Substance> subjects: action.getSubjects()){
				ArrayList<String> subjectsIDs = new ArrayList<String>();
				for (Substance subject: subjects){


					String roleId = "role-" + numRoles;

					if ( roles.containsKey(subject)){
						roleId = roles.get(subject);
					}
					else{
						roles.put(subject, roleId);
						rq.addRolePlayer(roleId, subject.getNounSynSet());
						numRoles++;
					}

				}
				rq.addAgentConjunctions(actionId, subjectsIDs);
			}

			for (ArrayList<Substance> objects: action.getObjects()){
				ArrayList<String> objectsIDs = new ArrayList<String>();
				for (Substance object: objects){
					String roleId = "role-" + numRoles;

					if ( roles.containsKey(object)){
						roleId = roles.get(object);
					}
					else{
						roles.put(object, roleId);
						rq.addRolePlayer(roleId, object.getNounSynSet());
						numRoles++;
					}
				}
				rq.addThemeConjunctions(actionId, objectsIDs);
				
			}

		}


		return rq.getStructuredRequest();
	}


	public String getSynSetText(int synSet){
		if (! truthTable.containsKey(MentalState.FACT))
			return "";
		ReqCreator rq = new ReqCreator();
		//Affecting a label for each substance: subjects and objects
		int numRoles = 0;
		int numActions = 0;
		HashMap<Substance, String> roles = new HashMap<Substance, String>();
		//HashMap<Action, String> actions = new HashMap<Action, String>();

		//Searching for substances that contain this synSet
		List<Idea> ideas = getIdeas(MentalState.FACT);

		for (Idea idea : ideas){
			//Ideas which are thoughts
			if (! (idea instanceof Thought)) continue;
			Action action = ((Thought) idea).getAction();
			
			//The subjects and the objects
			ArrayList<ArrayList<Substance>> subjects = new ArrayList<ArrayList<Substance>>();
			ArrayList<ArrayList<Substance>> objects = new ArrayList<ArrayList<Substance>>();
			
			//boolean found = false;
			for (ArrayList<Substance> _subjects: action.getSubjects()){
				ArrayList<Substance> conjunctions = new ArrayList<Substance>();
				for (Substance subject: _subjects){
					if (subject.getNounSynSet() != synSet)
						continue;
					//found = true;
					if(! roles.containsKey(subject)){
						String roleId = "srole-" + numRoles;
						roles.put(subject, roleId);
						conjunctions.add(subject);
						rq.addRolePlayer(roleId, subject.getNounSynSet());
						for(Quality quality: subject.getQualities()){
							rq.addAdjective(roleId, quality.getAdjective().getSynSet(), quality.getAdverbsInt());
						}

						numRoles++;
					}
					if (!conjunctions.isEmpty())
						subjects.add(conjunctions);
				}
			}

			for (ArrayList<Substance> _objects: action.getObjects()){
				ArrayList<Substance> conjunctions = new ArrayList<Substance>();
				for(Substance object: _objects){
					if (object.getNounSynSet() != synSet)
						continue;
					//found = true;
					if(! roles.containsKey(object)){
						String roleId = "srole-" + numRoles;
						roles.put(object, roleId);
						conjunctions.add(object);
						rq.addRolePlayer(roleId, object.getNounSynSet());
						for(Quality quality: object.getQualities()){
							rq.addAdjective(roleId, quality.getAdjective().getSynSet(), quality.getAdverbsInt());
						}
						numRoles++;
					}

				}
				if (!conjunctions.isEmpty())
					objects.add(conjunctions);
			}

			if (!(subjects.isEmpty() && objects.isEmpty())){//found
				String actionId = "action-" + numActions;
				//actions.put(action, actionId);
				Verb verb = action.getVerb();
				rq.addAction(actionId, verb.getSynSet());
				numActions++;

				ArrayList<ArrayList<Substance>> substances = 
						(subjects.isEmpty())?action.getSubjects():subjects;

				for (ArrayList<Substance> _subjects: substances){
					ArrayList<String> subjectsIDs = new ArrayList<String>();
					for (Substance subject: _subjects){
						String roleId = "role-" + numRoles;

						if ( roles.containsKey(subject)){
							roleId = roles.get(subject);
						}
						else{
							roles.put(subject, roleId);
							rq.addRolePlayer(roleId, subject.getNounSynSet());
							numRoles++;
						}
					}
					rq.addAgentConjunctions(actionId, subjectsIDs);
				}

				substances = (objects.isEmpty())?action.getObjects():objects;

				for (ArrayList<Substance> _objects: substances){
					ArrayList<String> objectsIDs = new ArrayList<String>();
					for(Substance object: _objects){
						String roleId = "role-" + numRoles;

						if ( roles.containsKey(object)){
							roleId = roles.get(object);
						}
						else{
							roles.put(object, roleId);
							rq.addRolePlayer(roleId, object.getNounSynSet());
							numRoles++;
						}
					}
					rq.addThemeConjunctions(actionId, objectsIDs);
				}


			}


		}


		return rq.getStructuredRequest();

	}

}
