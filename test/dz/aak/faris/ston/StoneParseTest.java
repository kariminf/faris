package dz.aak.faris.ston;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import dz.aak.faris.ston.Parser;

public class StoneParseTest {

	static String testFile = "test.ston";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String specif = readFile(testFile);
		
		//specif = "{ jjjjj }";
		System.out.println(specif + "\n----------\n");
		Parser parser = new Parser(specif);

	}


	public static String readFile (String f) {
		try {
			String contents = "";

			BufferedReader input = new BufferedReader(new FileReader(f));

			
			for(String line = input.readLine(); line != null; line = input.readLine()) {
				contents += line + "\n";
			}
			input.close();

			return contents;

		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		} 
	}

}
