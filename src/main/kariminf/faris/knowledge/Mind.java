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
import kariminf.faris.ston.FarisGenerate;
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

	public Mind(String name, Substance owner) {
		this.name = name;
		this.owner = owner;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean hasOwner(Substance s){
		return owner.equals(s);
	}

	public List<Idea> getIdeas(MentalState ms){
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


	public String getSynSetText(int synSet){
		if (! truthTable.containsKey(MentalState.FACT))
			return "";
		
		return FarisGenerate.getSynsetIdeas(this, synSet);
	}
	
	public String getAllText(){
		return "";
	}

}
