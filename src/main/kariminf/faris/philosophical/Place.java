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

import java.util.ArrayList;

import kariminf.faris.linguistic.Adverb;
import kariminf.sentrep.univ.types.Relation.Adpositional;

/**
 * Where or place (ποῦ, pou, where). Position in relation to the surrounding environment. 
 * Examples: in a marketplace, in the Lyceum.
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
public class Place {
	
	private Adverb adv;
	private Adpositional relation;
	private ArrayList<QuantSubstance> places = new ArrayList<>();

	public Place(Adverb adv) {
		this.adv = adv;
	}
	
	public Place(Adpositional prep) {
		this.relation = prep;
	}
	
	public void addLocation(QuantSubstance loc){
		if (relation != null)
			places.add(loc);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		String result = "P:";
		if (adv != null){
			result += adv;
		} else {
			result += relation;
			result += (places.size() > 0)? places: "";
		}
		
		return result;
	}
	
	

}
