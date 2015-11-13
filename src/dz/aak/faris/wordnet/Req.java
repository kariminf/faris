package dz.aak.faris.wordnet;

import java.io.File;
import java.io.IOException;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.WordID;

public class Req {
	
	private static File file =new File("dict/");
	
	public static String getWord(int synset, POS pos){
		IDictionary dict = new Dictionary(file);
		try {
			dict.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IWordID wordID1 = new WordID(synset,pos,1);
		IWord word1 = dict.getWord(wordID1);
		
		String lemma = word1.getLemma();
		
		dict.close();
		
		return lemma;
	}

}
