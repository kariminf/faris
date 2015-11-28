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


package dz.aak.faris.knowledge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dz.aak.faris.linguistic.Verb;
import dz.aak.faris.philosophical.Action;
import dz.aak.faris.philosophical.Quality;
import dz.aak.faris.philosophical.Substance;
import dz.aak.sentrep.ston.ReqCreator;

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
public class Mind {

	public static enum Truth {
		THINK,
		BELIEVE,
		HOPE,
		QUOTE, //a quote is a belief that someone said, replace it by saying
		FACT
	}
	
	private String name;
	private Substance owner;
	
	//even conditional have a truth level: "I think if ..., then ... ."
	private HashMap<Truth, List<Idea>> truthTable = new HashMap<Truth, List<Idea>>();
	
	public Mind(String name) {
		this.name = name;
	}
	
	private List<Idea> getIdeas(Truth truth){
		List<Idea> ideas;
		if (truthTable.containsKey(truth)){
			ideas = truthTable.get(truth);
		}
		else{
			ideas = new ArrayList<Idea>();
			truthTable.put(truth, ideas);
		}
		
		return ideas;
	}
	
	public void addAction(Truth truth, Action action){
		//TODO verify if the action exists already, and if other components have to be added
		
		List<Idea> ideas = getIdeas(truth);
		
		Thought thought = new Thought(action);
		
		ideas.add(thought);
	}
	
	public void addOpinion(Truth truth, Mind othersThoughts){
		//TODO verify if the action exists already, and if other components have to be added
		//opinions.put(truth, othersThoughts);
	}
	
	public void addCondition(Truth truth, Conditional condition){
		List<Idea> ideas = getIdeas(truth);
		
		ideas.add(condition);
	}
	
	public String getNoAdjectives(){
		if (! truthTable.containsKey(Truth.FACT))
			return "";
		ReqCreator rq = new ReqCreator();
		//Affecting a label for each substance: subjects and objects
		int numRoles = 0;
		int numActions = 0;
		HashMap<Substance, String> roles = new HashMap<Substance, String>();
		
		List<Idea> ideas = getIdeas(Truth.FACT);
		
		for (Idea idea : ideas){
			if (! (idea instanceof Thought)) continue;
			Action action = ((Thought) idea).getAction();
			
			Verb verb = action.getVerb();
			String actionId = "action" + numActions;
			rq.addAction(actionId, verb.getSynSet());
			numActions++;
			for (Substance subject: action.getSubjects()){
				String roleId = "role-" + numRoles;
				
				if ( roles.containsKey(subject)){
					roleId = roles.get(subject);
				}
				else{
					roles.put(subject, roleId);
					rq.addRolePlayer(roleId, subject.getNounSynSet());
					numRoles++;
				}

				rq.addSubject(actionId, roleId);
				
			}
			
			for (Substance object: action.getObjects()){
				String roleId = "role-" + numRoles;
				
				if ( roles.containsKey(object)){
					roleId = roles.get(object);
				}
				else{
					roles.put(object, roleId);
					rq.addRolePlayer(roleId, object.getNounSynSet());
					numRoles++;
				}

				rq.addObject(actionId, roleId);
				
			}
			
		}
		
		
		return rq.getStructuredRequest();
	}
	
	
	public String getSynSetText(int synSet){
		if (! truthTable.containsKey(Truth.FACT))
			return "";
		ReqCreator rq = new ReqCreator();
		//Affecting a label for each substance: subjects and objects
		int numRoles = 0;
		int numActions = 0;
		HashMap<Substance, String> roles = new HashMap<Substance, String>();
		//HashMap<Action, String> actions = new HashMap<Action, String>();
		
		//Searching for substances that contain this synSet
		List<Idea> ideas = getIdeas(Truth.FACT);
		
		for (Idea idea : ideas){
			if (! (idea instanceof Thought)) continue;
			Action action = ((Thought) idea).getAction();
			HashSet<Substance> subjects = new HashSet<Substance>();
			HashSet<Substance> objects = new HashSet<Substance>();
			//boolean found = false;
			for (Substance subject: action.getSubjects()){
				if (subject.getNounSynSet() != synSet)
					continue;
				//found = true;
				if(! roles.containsKey(subject)){
					String roleId = "srole-" + numRoles;
					roles.put(subject, roleId);
					subjects.add(subject);
					rq.addRolePlayer(roleId, subject.getNounSynSet());
					for(Quality quality: subject.getQualities()){
						rq.addAdjective(roleId, quality.getAdjective().getSynSet(), quality.getAdverbsInt());
					}
					
					numRoles++;
				}
				
			}
			
			for (Substance object: action.getObjects()){
				if (object.getNounSynSet() != synSet)
					continue;
				//found = true;
				if(! roles.containsKey(object)){
					String roleId = "srole-" + numRoles;
					roles.put(object, roleId);
					rq.addRolePlayer(roleId, object.getNounSynSet());
					for(Quality quality: object.getQualities()){
						rq.addAdjective(roleId, quality.getAdjective().getSynSet(), quality.getAdverbsInt());
					}
					numRoles++;
				}
				
			}
			
			if (!(subjects.isEmpty() && objects.isEmpty())){//found
				String actionId = "action-" + numActions;
				//actions.put(action, actionId);
				Verb verb = action.getVerb();
				rq.addAction(actionId, verb.getSynSet());
				numActions++;
				
				Set<Substance> substances = (subjects.isEmpty())?action.getSubjectsSet():subjects;
				
				for (Substance subject: substances){
					String roleId = "role-" + numRoles;
					
					if ( roles.containsKey(subject)){
						roleId = roles.get(subject);
					}
					else{
						roles.put(subject, roleId);
						rq.addRolePlayer(roleId, subject.getNounSynSet());
						numRoles++;
					}

					rq.addSubject(actionId, roleId);
					
				}
				
				substances = (objects.isEmpty())?action.getObjectsSet():objects;
				
				for (Substance object: substances){
					String roleId = "role-" + numRoles;
					
					if ( roles.containsKey(object)){
						roleId = roles.get(object);
					}
					else{
						roles.put(object, roleId);
						rq.addRolePlayer(roleId, object.getNounSynSet());
						numRoles++;
					}

					rq.addObject(actionId, roleId);
					
				}
				
				
			}
			
			
		}
		
		
		return rq.getStructuredRequest();
		
	}

}
