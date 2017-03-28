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


package kariminf.faris.linguistic;

import kariminf.sentrep.types.VerbTense;

/**
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
public class Verb extends POS {
	
	private VerbTense tense = VerbTense.PRESENT;
	
	private boolean perfect = false;
	private boolean progressive = false;
	
	/**
	 * @return the tense
	 */
	public VerbTense getTense() {
		return tense;
	}

	/**
	 * @param tense the tense to set
	 */
	public void setTense(VerbTense tense) {
		this.tense = tense;
	}


	public boolean isPerfect() {
		return perfect;
	}
	
	public boolean isProgressive() {
		return progressive;
	}


	public void setPerfect() {
		this.perfect = true;
	}
	
	public void setProgressive() {
		this.progressive = true;
	}
	
	//the mood (imperative, indicative, subjective)
	//we don't need person, number, voice
	
	public Verb(int verbSynSet) {
		super(verbSynSet);
	}

	@Override
	public PosType getPosType() {
		return PosType.VERB;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "V@" + getSynSet() +  "." + tense;
		result += (perfect)? ".PRF": "";
		result += (progressive)? ".PROG": "";
		return result ;
	}
	
	

}
