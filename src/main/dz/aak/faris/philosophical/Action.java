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


package dz.aak.faris.philosophical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dz.aak.faris.linguistic.Adverb;
import dz.aak.faris.linguistic.Verb;

/**
 * Action (poiein, to make or do) — examples: to lance, to heat, to cool (something)
 * Affection, passion (paschein, to suffer or undergo) — examples: to be lanced, 
 * to be heated, to be cooled
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
public class Action {
	
	public static enum ActionRelation {
		IMPLY,
		CAUSE,
		After,
		Before
	}
	
	private static class ConjunctedSubstances extends HashSet<Substance> {
		private static final long serialVersionUID = 1L;
		
		public Set<Substance> getSubstances(){
			Set<Substance> result = new HashSet<Substance>();
			result.addAll(this);
			return result;
		}
		
	}
	
	
	//An action is defined by a verb
	private Verb verb;
	//- A verb has tense, indicating the time that the sentence describes
	//- A verb has mood, indicating whether the sentence describes reality or 
	//expresses a command, a hypothesis, a hope, etc.
	
	//An action is modified by an adverb
	private Adverb adverb;
	
	//Here, we use disjunctions of conjunctions 
	//An Action can have many doers (we can't duplicate a doer)
	private Set<ConjunctedSubstances> subjects = new HashSet<ConjunctedSubstances>(); 
	
	//An Action can affect many predicates 
	private Set<ConjunctedSubstances> objects = new HashSet<ConjunctedSubstances>();  
	
	//An action can have relations with other Actions
	
	private HashMap<Action, ActionRelation> relations = new HashMap<Action, ActionRelation>();
	
	//An action can have locations
	private Set<Place> locations = new HashSet<Place>(); 
	
	//An action can have times
	private Set<Time> times = new HashSet<Time>(); 

	private Action(Verb verb){ 
		
		this.verb = verb;
		//We have to treat the verb before adding it
		//Or we enter the infinitive form, and before that we transform it 
		//to infinitive in the global system
	}
	
	public static Action getNew (Verb verb){
		return new Action(verb);
	}
	
	/**
	 * Adds substances that are separated by the word "and". <br>
	 * @param subjects
	 */
	public void addConjunctSubjects(Set<Substance> subjects){
		ConjunctedSubstances conjunctions = new ConjunctedSubstances();
		conjunctions.addAll(subjects);
		if (conjunctions.size()>0)
			this.subjects.add(conjunctions);
	}
	
	/**
	 * Adds substances that are separated by the word "and". <br>
	 * @param subjects
	 */
	public void addConjunctObjects(Set<Substance> objects){
		ConjunctedSubstances conjunctions = new ConjunctedSubstances();
		conjunctions.addAll(objects);
		if (conjunctions.size()>0)
			this.objects.add(conjunctions);
	}
	
	public boolean hasSubjects(){
		return (subjects.size() > 0);
	}
	
	public boolean hasObjects(){
		return (objects.size() > 0);
	}
	
	/*
	public void addSubject(Substance subject){
		subjects.add(subject);
	}
	
	public void addObject(Substance object){
		objects.add(object);
	}
	
	public void addSubjects(Set<Substance> subjects){
		subjects.addAll(subjects);
	}
	
	public void addObjects(Set<Substance> objects){
		objects.addAll(objects);
	}
	
	
	
	
	public Set<Substance> getSubjectsSet(){
		return subjects;
	}
	
	public Set<Substance> getObjectsSet(){
		return objects;
	}
	
	*/
	
	private Set<Set<Substance>> getDisjunctions(Set<ConjunctedSubstances> disjunctions){
		Set<Set<Substance>> result = new HashSet<Set<Substance>>();
		for (ConjunctedSubstances conjunctions: disjunctions)
			result.add(conjunctions.getSubstances());
		
		return result;
	}
	
	public Set<Set<Substance>> getSubjects(){
		return getDisjunctions(subjects);
	}
	
	public Set<Set<Substance>> getObjects(){
		return getDisjunctions(objects);
	}
	
	public Verb getVerb(){
		return verb;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
