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
import java.util.Set;

import kariminf.faris.linguistic.Noun;
import kariminf.faris.linguistic.ProperNoun;


/**
 * Substance (οὐσία, ousia, essence or substance).
 * Substance is that which cannot be predicated of anything or be said to be in anything. 
 * Hence, this particular man or that particular tree are substances. Later in the text, 
 * Aristotle calls these particulars “primary substances”, to distinguish them from 
 * secondary substances, which are universals and can be predicated. Hence, Socrates is 
 * a primary substance, while man is a secondary substance. Man is predicated of Socrates, 
 * and therefore all that is predicated of man is predicated of Socrates.
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
public class Substance {

	//a substance is a noun
	private Noun noun;
	
	//Qualities
	private Set<Quality> qualities = new HashSet<Quality>();	
	
	
	public Substance(int nounSynSet) {
		noun = Noun.getNew(nounSynSet);
	}
	
	public void setNounSpecif(String name, String def){
		noun.setAttributs(Noun.Gender.COMMON, def.equals("Y"));
		if (name.trim().length() > 0)
			noun = new ProperNoun(noun, name);
	}
	
	
	public void addQuality(Quality quality){
		qualities.add(quality);
	}
	
	
	public int getNounSynSet(){
		return noun.getSynSet();
	}
	
	

	public Set<Quality> getQualities(){
		HashSet<Quality> result = new HashSet<>();
		result.addAll(qualities);
		return result;
	}
	
	public boolean hasQuality(Quality q){
		return qualities.contains(q);
	}
	
	public boolean hasNoun(Noun n){
		return this.noun.equals(n);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (! (o instanceof Substance)) return false;
		if (o == this) return true;
		Substance os = (Substance) o;
		if (! os.hasNoun(this.noun)) return false;
		for (Quality q: qualities)
			if (! os.hasQuality(q)) return false;
		return true;
	}
	
	
	//Probably won't be used, but let it here for now
	/**
	 * This function will assure the existence of one instance of the same substance
	 * @param sub2 the other substance
	 */
	public Substance getOne(Substance sub2){
		if(equals(sub2)) return this;
		return sub2;
	}
	
	//TODO update a substance
	public void update(Substance sub2){
		
	}
	
	public Noun getNoun(){
		return Noun.getNew(noun);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "";
		result += noun;
		result += (qualities.isEmpty())? "": "-Q:" + qualities;
		return result;
	}
	
	

}
