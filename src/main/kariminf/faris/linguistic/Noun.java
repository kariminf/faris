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
public class Noun extends POS {


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
	public PosType getPosType() {
		return PosType.NOUN;
	}
	
	public void setAttributs(Gender g, boolean d){
		gender = g;
		defined = d;
	}
	
	public boolean sameAttributs(Gender g, boolean d){
		return (
				gender == g
				&& defined == d
				);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Noun)) return false;
        if (obj == this) return true;
        
        Noun n = (Noun) obj;
        
		return (
				super.equals(obj) 
				&& 
				n.sameAttributs(gender, defined)
				);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Noun [gender=" + gender + ", defined=" + defined + "]";
	}
	
	

}
