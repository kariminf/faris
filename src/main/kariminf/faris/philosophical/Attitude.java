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


package kariminf.faris.philosophical;

import java.util.HashMap;
import java.util.HashSet;

import kariminf.faris.linguistic.Verb;
import kariminf.faris.process.Generator;

/**
 * Being-in-a-position, posture, attitude (κεῖσθαι, keisthai, to lie). 
 * The examples Aristotle gives indicate that he meant a condition of rest resulting 
 * from an action: ‘Lying’, ‘sitting’, ‘standing’. Thus position may be taken as the 
 * end point for the corresponding action. The term is, however, frequently taken to 
 * mean the relative position of the parts of an object (usually a living object), 
 * given that the position of the parts is inseparable from the state of rest implied.
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
public class Attitude extends Being{

	//In this category, represent the -ing verbs describing a substance
	//it is an adjective derived from the verb in a continuous state with another action.
	
	private HashMap<QuantSubstance, HashSet<Action>> owners = new HashMap<>();
	
	private Verb posture;
	
	/**
	 * 
	 * @param ingVerb the -ing verb; or the action which describes a substance while another action happens
	 */
	public Attitude(Verb ingVerb) {
		this.posture = ingVerb;
	}
	
	public void addAttitudeOwner(QuantSubstance player, Action inAction){
		
		if (owners.containsKey(player)){
			owners.get(player).add(inAction);
			return;
		}
		
		HashSet<Action> actions = new HashSet<Action>();
		actions.add(inAction);
		owners.put(player, actions);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "";
		result += posture.getSynSet();
		result += "ING";
		return result;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void generate(Generator gr) {
		// TODO Auto-generated method stub
		
	}
	
	

}
