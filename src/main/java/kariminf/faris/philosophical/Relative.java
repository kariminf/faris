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

import kariminf.faris.linguistic.Adjective;
import kariminf.faris.process.Generator;
import kariminf.sentrep.types.Comparison;

/**
 * Relative or relation (πρός τι, pros ti, toward something). 
 * This is the way one object may be related to another. Examples: double, half, 
 * large, master, knowledge.
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
public class Relative extends Being{
	
	
	public static final class RelativeWrapper {
		public Relative relative;
		public QuantSubstance owner;
		public Action actOwner;
		public RelativeType relationType;
		public QuantSubstance relSubstance;
		public Adjective adjective;
		
		public RelativeWrapper (Relative relative){
			this.relative = relative;
		}
		
		public void unsafeAddAll(){
			owner = relative.owner;
			actOwner = relative.actOwner;
			relationType = relative.relationType;
			relSubstance = relative.relSubstance;
			adjective = relative.adjective;
		}
	}
	//The relative is:
	//  Possession: "son of someone"
	//	Comparison: taller than
	//Issue #9
	public static enum RelativeType {
		OF, // other relation defined by the preposition
		MORE,
		LESS,
		MOST,
		LEAST,
		EQUAL;
		
		public static RelativeType fromComparison(Comparison cmp){
			return RelativeType.valueOf(cmp.name());
		}
		
		public static Comparison toComparison(RelativeType rel){
			if (rel == RelativeType.OF) return null;
			
			return Comparison.valueOf(rel.name());
		}
	}
	
	//The owner can be a substance: the man is taller than the boy
	private QuantSubstance owner;
	//The owner can be an action: Karim worked harder than his colleague.
	private Action actOwner;
	
	private RelativeType relationType;
	private QuantSubstance relSubstance;
	
	//He works more than Me.
	private Adjective adjective;
	
		
	private Relative(RelativeType type, Adjective relation, QuantSubstance relSubstance){
		this.relationType = type;
		this.adjective = relation;
		this.relSubstance = relSubstance;
	}
	
	/**
	 * Creates a Relative OF. For example: the mother of the child. 
	 * Then affect it to the owner
	 * @param owner the owner of the relation; in the example: the mother
	 * @param relative the relative; in the example: the child
	 */
	public static void affectRelative(QuantSubstance owner, QuantSubstance relative){

		if (owner == null) return;
		if (relative == null) return;
		
		Relative result = new Relative(RelativeType.OF, null, relative);
		result.owner = owner;
		owner.addRelative(result);
	}
	

	/**
	 * Creates a relative and affect it to the owner
	 * @param type
	 * @param relation
	 * @param owner
	 * @param relative
	 */
	public static void affectRelative (RelativeType type, Adjective relation, 
			Action owner, QuantSubstance relative){
		
		//This type of relative is for comparison
		if (type == RelativeType.OF) return;
		if (owner == null) return;
		
		
		Relative result = new Relative(type, relation, relative);
		result.actOwner = owner;
		
		owner.addRelative(result);
	}
	
	
	/**
	 * Comparison relative: He is taller than me
	 * @param type
	 * @param relation
	 * @return
	 */
	public static Relative getNew(RelativeType type, Adjective relation, 
			QuantSubstance owner, QuantSubstance relative){
		
		//This type of relative is for comparison
		if (type == RelativeType.OF) return null;
		if (owner == null) return null;
		if (relative == null) return null;
		//Here, we must have an adjective
		if (relation == null) return null;
		
		
		Relative result = new Relative(type, relation, relative);
		result.owner = owner;
		
		return result;
	}
	
	public Action getOwnerAction(){
		return actOwner;
	}
	
	public QuantSubstance getOwnerSubstance(){
		return owner;
	}
	
	public QuantSubstance getRelative(){
		return owner;
	}
	
	public RelativeType getRelativeType(){
		return relationType;
	}
	
	public Adjective getAdjective(){
		return adjective;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		String result = "R:";
		
		if (relationType == RelativeType.OF){
			result += "OF{" + relSubstance + "}";
			return result;
		} 
		
		result += relationType + "{";
		result += (adjective == null)? "": adjective;
		result += "}." + relSubstance;
		
		return result;
	}
	

	@SuppressWarnings("rawtypes")
	@Override
	public void generate(Generator gr) {
		
		RelativeWrapper wrapper = new RelativeWrapper(this);
		wrapper.unsafeAddAll();
		gr.processRelative(wrapper);
		
	}
	
	

}
