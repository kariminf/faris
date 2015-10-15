/* Farest : Facts representation of sentences
 * ------------------------------------------
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

import java.util.HashSet;
import java.util.Set;

import dz.aak.faris.linguistic.Adjective;
import dz.aak.faris.linguistic.Adverb;

/**
 * Qualification or quality (ποιόν, poion, of what kind or quality). 
 * This determination characterizes the nature of an object. Examples: white, black, 
 * grammatical, hot, sweet, curved, straight.
 * 
 * @author kariminf
 *
 */
public class Quality {
	
	private Adjective adjective;
	private Set<Adverb> adverbs = new HashSet<Adverb>();

	public Quality(Adjective adjective) {
		this.adjective = adjective;
	}

	/**
	 * @return the adjective
	 */
	public Adjective getAdjective() {
		return adjective;
	}

	/**
	 * @return the adverbs
	 */
	public Set<Adverb> getAdverbs() {
		//Alert: Security problem
		return adverbs;
	}

	/**
	 * @param adverbs the adverbs to set
	 */
	public void setAdverbs(Set<Adverb> adverbs) {
		//Alert: Security problem
		this.adverbs = adverbs;
	}
	
	/**
	 * @param adverbs the adverbs to set
	 */
	public void setAdverbsInt(Set<Integer> advSynSets) {
		if (advSynSets == null) return;
		
		for(int synSet: advSynSets){
			Adverb adverb = new Adverb(synSet);
			adverbs.add(adverb);
		}
	}
	
	
	

}
