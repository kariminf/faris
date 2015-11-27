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

import java.util.HashMap;
import java.util.HashSet;

import dz.aak.faris.knowledge.Mind.Truth;
import dz.aak.faris.linguistic.Adjective;
import dz.aak.faris.linguistic.Verb;
import dz.aak.faris.linguistic.Verb.Aspect;
import dz.aak.faris.linguistic.Verb.Tense;
import dz.aak.faris.philosophical.Action;
import dz.aak.faris.philosophical.Quality;
import dz.aak.faris.philosophical.Substance;
import dz.aak.faris.ston.FarisParse;
import dz.aak.sentrep.ston.Parser;
import dz.aak.sentrep.ston.ReqAction;
import dz.aak.sentrep.ston.ReqAdjective;
import dz.aak.sentrep.ston.ReqParser;
import dz.aak.sentrep.ston.ReqRolePlayer;

/**
 * The interface class,
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
public class Faris {

	private HashSet<Substance> substances = new HashSet<Substance>();
	private HashSet<Action> actions = new HashSet<Action>();
	private HashMap<String, Mind> minds = new HashMap<String, Mind>();
	
	
	public Faris() {
		minds.put("Default", new Mind("Default"));
	}
	
	public boolean addStonDescription(String description){
		FarisParse parser = new FarisParse(substances, actions, minds);
		parser.parse(description);
		return parser.parsed();
	}
	
	
	public String getNoAdjectives(String mindLabel){
		
		if (! minds.containsKey(mindLabel)) return "";
		
		Mind mind = minds.get(mindLabel);
		
		return mind.getNoAdjectives();
	}
	
	/**
	 * 
	 * @param mindLabel
	 * @param synSet
	 * @return
	 */
	public String getSynSetText (String mindLabel, int synSet){
		if (! minds.containsKey(mindLabel)) return "";
		Mind mind = minds.get(mindLabel);
		
		return mind.getSynSetText(synSet);
	}
	
	/**
	 * 
	 * @return
	 */
	public String info(){
		
		String result = "Faris:\n";
		result += "There are " + minds.size() + " pricipal mind(s)\n";
		result += "There are " + substances.size() + " different substances\n";
		result += "There are " + actions.size() + " different actions\n";
		
		return result;
	}


}
