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
package dz.aak.faris.linguistic;

import edu.mit.jwi.item.POS;

public class Verb extends PartOfSpeach {

	public static enum Tense {
		PAST,
		PRESENT,
		FUTURE
	}
	
	public static enum Aspect {
		SIMPLE, //“I see” (simple aspect) expresses the act of seeing as a simple fact;
		PROGRESSIVE, //“I am seeing” (progressive aspect) represents the action as continuous and ongoing; 
		PERFECT //“I have seen” (perfect aspect) represents the present situation as the result of past action. 
	}
	
	private Tense tense;
	
	private Aspect aspect;
	
	//the mood (imperative, indicative, subjective)
	//we don't need person, number, voice
	
	public Verb(int verbSynSet) {
		super(verbSynSet);
	}

	@Override
	public POS getPOS() {
		return POS.VERB;
	}

}
