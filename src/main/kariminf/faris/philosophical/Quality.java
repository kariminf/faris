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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kariminf.faris.linguistic.Adjective;
import kariminf.faris.linguistic.Adverb;
import kariminf.faris.process.Generator;


/**
 * Qualification or quality (ποιόν, poion, of what kind or quality). 
 * This determination characterizes the nature of an object. Examples: white, black, 
 * grammatical, hot, sweet, curved, straight.
 * 
 * @author kariminf
 *
 */
public class Quality extends Being{
	
	
	public static final class QualityWrapper {
		public Quality quality;
		public Adjective adjective;
		public Set<Adverb> adverbs;
		
		public QualityWrapper (Quality quality){
			this.quality = quality;	
		}
		
		public void unsafeAddAll(){
			adjective = quality.adjective;
			adverbs = quality.adverbs;
		}
		
	}
	
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
	public void setAdverbs(Set<Adverb> adverbs) {
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

	@SuppressWarnings("rawtypes")
	@Override
	public void generate(Generator gr) {
		QualityWrapper wrapper = new QualityWrapper(this);
		wrapper.unsafeAddAll();
		gr.processQuality(wrapper);
		
	}
	
	
	

}
