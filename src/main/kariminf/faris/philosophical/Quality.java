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
import java.util.List;
import kariminf.faris.linguistic.Adjective;
import kariminf.faris.linguistic.Adverb;


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
	private ArrayList<Adverb> adverbs = new ArrayList<Adverb>();

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
	public ArrayList<Adverb> getAdverbs() {
		//Alert: Security problem
		return adverbs;
	}
	
	public ArrayList<Integer> getAdverbsInt() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(Adverb adverb: adverbs)
			result.add(adverb.getSynSet());
		//Alert: Security problem
		return result;
	}

	/**
	 * @param adverbs the adverbs to set
	 */
	public void setAdverbs(ArrayList<Adverb> adverbs) {
		//Alert: Security problem
		this.adverbs = adverbs;
	}
	
	/**
	 * @param adverbs the adverbs to set
	 */
	public void setAdverbsInt(List<Integer> advSynSets) {
		if (advSynSets == null) return;
		
		for(int synSet: advSynSets){
			Adverb adverb = new Adverb(synSet);
			adverbs.add(adverb);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "";
		result += adjective;
		result += (adverbs.size() > 0)? adverbs: "";
		return result;
	}
	
	
	

}
