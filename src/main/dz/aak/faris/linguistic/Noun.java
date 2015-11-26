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


//TODO redo this once again
public class Noun extends PartOfSpeach {

	public enum Gender {
		COMMON, //we use it with things 
		MASCULINE,
		FEMININE
	}
	
	private Gender gender;
	
	//gender, number, person, case
	
	
	private boolean defined;
	
	
	
	protected Noun(int nounSynSet) {
		super(nounSynSet);
	}
	
	public static Noun getNew(int nounSynSet){
		return new Noun(nounSynSet);
	}

	@Override
	public POS getPOS() {
		return POS.NOUN;
	}

}
