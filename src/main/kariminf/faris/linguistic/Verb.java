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

	public static enum Tense {
		PAST,
		PRESENT,
		FUTURE;
		
		/*public static Tense getTense (int number){
			switch (number){
			case 0: return PAST;
			case 1: return PRESENT;
			case 2: return FUTURE;
			}
			
			return PRESENT;
		}*/
	}
	
	public static enum Aspect {
		SIMPLE, //“I see” (simple aspect) expresses the act of seeing as a simple fact;
		PROGRESSIVE, //“I am seeing” (progressive aspect) represents the action as continuous and ongoing; 
		PERFECT, //“I have seen” (perfect aspect) represents the present situation as the result of past action.
		PROGRESSIVEPERFECT;
	
		/*public static Aspect getAspect (int number){
			switch (number){
			case 0: return SIMPLE;
			case 1: return PROGRESSIVE;
			case 2: return PERFECT;
			}
			
			return SIMPLE;
		}*/
	}
	
	/**
	 * @return the tense
	 */
	public Tense getTense() {
		return tense;
	}

	/**
	 * @param tense the tense to set
	 */
	public void setTense(Tense tense) {
		this.tense = tense;
	}

	/**
	 * @return the aspect
	 */
	public Aspect getAspect() {
		return aspect;
	}

	/**
	 * @param aspect the aspect to set
	 */
	public void setAspect(Aspect aspect) {
		this.aspect = aspect;
	}

	private Tense tense = Tense.PRESENT;
	
	private Aspect aspect = Aspect.SIMPLE;
	
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
		return "V@" + getSynSet() +  "." + tense + "." + aspect ;
	}
	
	

}
