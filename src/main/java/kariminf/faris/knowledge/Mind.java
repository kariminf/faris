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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import kariminf.faris.philosophical.Action;
import kariminf.faris.philosophical.QuantSubstance;
import kariminf.faris.process.Processor;
import kariminf.faris.tools.Search;


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
	
	public static final class MindWrapper {
		public Mind mind;
		public String name;
		public QuantSubstance owner;
		
		public HashMap<MentalState, Set<Thought>> thoughts = new HashMap<>();
		public HashMap<MentalState, Set<Opinion>> opinions = new HashMap<>();
		//even conditional have a truth level: "I think if ..., then ... ."
		public HashMap<MentalState, Set<Conditional>> conditions = new HashMap<>();
		
		public HashSet<MentalState> mentalStates = new HashSet<>();
		
		public MindWrapper(Mind mind){
			this.mind = mind;
		}
		
		public void unsafeAddAll(){
			name = mind.name;
			owner = mind.owner;
			thoughts = mind.thoughts;
			opinions = mind.opinions;
			conditions = mind.conditions;
			mentalStates = mind.mentalStates;
		}
	}
	
	protected String name;
	private QuantSubstance owner;
	
	private HashMap<MentalState, Set<Thought>> thoughts = new HashMap<>();
	private HashMap<MentalState, Set<Opinion>> opinions = new HashMap<>();
	//even conditional have a truth level: "I think if ..., then ... ."
	private HashMap<MentalState, Set<Conditional>> conditions = new HashMap<>();
	
	private HashSet<MentalState> mentalStates = new HashSet<>();
	
	
	/**
	 * 
	 * @param name
	 * @param owner
	 */
	public Mind(String name, QuantSubstance owner) {
		this.name = name;
		this.owner = owner;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * 
	 * @param agent
	 * @return
	 */
	public boolean hasOwner(QuantSubstance agent){
		return owner.equals(agent);
	}
	
	/**
	 * 
	 * @param ms
	 * @return
	 */
	public Set<Thought> getThoughts(MentalState ms){

		return getIdeas(ms, thoughts);
	}
	
	/**
	 * 
	 * @param ms
	 * @param truthTable
	 * @return
	 */
	private <E> Set<E> getIdeas(MentalState ms, HashMap<MentalState, Set<E>> truthTable){
		Set<E> ideas;
		if (truthTable.containsKey(ms)){
			ideas = truthTable.get(ms);
		}
		else{
			ideas = new HashSet<E>();
			truthTable.put(ms, ideas);
		}

		return ideas;
	}

	/**
	 * 
	 * @param ms
	 * @param action
	 */
	public void addAction(MentalState ms, Action action){

		Set<Thought> ideas = getIdeas(ms, thoughts);
		
		Thought newIdea = new Thought(action);
		
		Thought thought = Search.getElement(ideas, newIdea);
		thought.update(newIdea);

		ideas.add(thought);
		mentalStates.add(ms);
	}

	/**
	 * 
	 * @param ms
	 * @param other
	 * @return
	 */
	public Mind addOpinion(MentalState ms, QuantSubstance other){
		
		Set<Opinion> ideas = getIdeas(ms, opinions);
		
		Opinion newIdea = new Opinion(name, other);
		
		Opinion opinion = Search.getElement(ideas, newIdea);
		
		mentalStates.add(ms);
		
		ideas.add(opinion);
		
		return opinion.getMind();
	}
	
	/**
	 * 
	 * @param ms
	 * @param other
	 * @return
	 */
	public Mind addOpinion(MentalState ms, Mind other){
		
		Set<Opinion> ideas = getIdeas(ms, opinions);
		
		Opinion newIdea = new Opinion(name, other);
		
		Opinion opinion = Search.getElement(ideas, newIdea);
		
		mentalStates.add(ms);
		
		ideas.add(opinion);
		
		return opinion.getMind();
	}

	/**
	 * 
	 * @param ms
	 * @param condition
	 */
	public void addCondition(MentalState ms, Conditional condition){
		Set<Conditional> ideas = getIdeas(ms, conditions);

		ideas.add(condition);
		mentalStates.add(ms);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "==================\n";
		result += "Name: " + name + "\n";
		result += "Owner = " + owner + "\n";
		
		for (MentalState ms: MentalState.values()){
			
			if (! mentalStates.contains(ms)) continue;
			
			result += ms + "\n";
			
			if (thoughts.containsKey(ms))
				for (Idea i : thoughts.get(ms)){
					result += i;
				}

			if (conditions.containsKey(ms))
				for (Idea i : conditions.get(ms)){
					result += i;
				}

			if (opinions.containsKey(ms))
				for (Idea i : opinions.get(ms)){
					result += i;
				}
		}
		return result;
	}
	
	public void process(Processor pr){
		MindWrapper wrapper = new MindWrapper(this);
		wrapper.unsafeAddAll();
		pr.processMind(wrapper);
	}

}
