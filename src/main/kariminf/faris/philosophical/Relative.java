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

import java.util.HashSet;

import kariminf.faris.linguistic.Adjective;
import kariminf.faris.process.Generator;
import kariminf.sentrep.univ.types.Relation.Adpositional;

/**
 * Relative or relation (πρός τι, pros ti, toward something). 
 * This is the way one object may be related to another. Examples: double, half, 
 * large, master, knowledge.
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
public class Relative extends Being{
	
	
	//The relative is:
	//  Possession: "son of someone"
	//	Comparison: taller than
	//Issue #9
	public static enum RelativeType {
		OTHER, // other relation defined by the preposition
		PLACE, // a place relation defined by the preposition
		TIME, // a time relation defined by the preposition
		MORE,
		LESS,
		MOST,
		LEAST,
		EQUAL
	}
	
	private static abstract class TheRelation {}
	
	private static class AdpRelation extends TheRelation {
		private Adpositional adpositional; //of
		private AdpRelation(Adpositional adp){
			adpositional = adp;
		}
	}
	
	private static class AdjRelation extends TheRelation {
		private Adjective adjective; //Taller, less tall
		private AdjRelation (Adjective adj){
			adjective = adj;
		}
		
	}
	
	private HashSet<QuantSubstance> owners = new HashSet<>();
	private RelativeType relationType;
	private TheRelation relation;
	private HashSet<QuantSubstance> relatives = new HashSet<>();
		
	protected Relative() {
	}
	
	public static Relative getNew(RelativeType type, Adpositional relation){
		
		if (type.ordinal() > 2) return null;
		
		Relative result = new Relative();
		result.relationType = type;
		
		AdpRelation adpr = new AdpRelation(relation);
		
		result.relation = adpr;
		
		
		return result;
	}
	
	public static Relative getNew(RelativeType type, Adjective relation){
		
		if (type.ordinal() < 3) return null;
		
		Relative result = new Relative();
		result.relationType = type;
		
		AdjRelation adpr = new AdjRelation(relation);
		
		result.relation = adpr;
		
		
		return result;
	}
	
	public void addOwner(QuantSubstance owner){
		owners.add(owner);
	}
	
	public void addRelative(QuantSubstance relative){
		owners.add(relative);
	}

	@Override
	public void generate(Generator gr) {
		// TODO Auto-generated method stub
		
	}
	
	

}
