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

import kariminf.faris.philosophical.Action;
import kariminf.faris.philosophical.QuantSubstance;
import kariminf.faris.philosophical.Quantity;
import kariminf.faris.philosophical.State;
import kariminf.faris.philosophical.Substance;
import kariminf.faris.process.Generator;
import kariminf.faris.process.ston.FarisParse;


/**
 * The interface class,
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
public class Faris {
	
	public static class FarisWrapper {
		public Faris faris;
		public HashSet<Substance> substances;
		public HashSet<Action> actions;
		public HashMap<String, Mind> minds;
		public HashSet<State> states;
		
		public FarisWrapper(Faris faris){
			this.faris = faris;
		}
		
		public void unsafeAddAll(){
			substances = faris.substances;
			actions = faris.actions;
			minds = faris.minds;
			states = faris.states;
		}
	}
	
	private HashSet<Substance> substances = new HashSet<>();
	
	private HashSet<Action> actions = new HashSet<>();
	
	private HashMap<String, Mind> minds = new HashMap<>();
	
	private HashSet<State> states = new HashSet<>();
	
	
	public Faris() {
		QuantSubstance s = new QuantSubstance(new Substance(0));
		minds.put("$", new Mind("$", s));
	}
	
	/**
	 * 
	 * @param description
	 * @return
	 */
	public boolean addStonDescription(String description){
		FarisWrapper wrapper = new FarisWrapper(this);
		wrapper.unsafeAddAll();
		FarisParse parser = new FarisParse(wrapper);
		parser.parse(description);
		return parser.parsed();
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result;
		result = "Faris minds = ";
		
		result += minds.keySet() + "\n\n";
		
		for (Mind mind: minds.values()){
			result += mind.toString();
		}
		
		return result;
	}
	
	
	public void displayStates(){
		for (State state: states){
			System.out.println("====================");
			System.out.println(state);
		}
	}
	
	
	public void generate(Generator gr){
		Mind mainMind = minds.get("$");
		if (mainMind == null) return;
		mainMind.generate(gr);
	}

}
