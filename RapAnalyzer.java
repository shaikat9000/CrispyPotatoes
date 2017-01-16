
import java.io.*;
import cs1.Keyboard;
import color.Text;
import java.util.*;


//the objective of this program is to create basic rhyme checker. also necessary is a tool to check for number of multis in a verse, assonance, consonance, and other factors
//step 1: create a dictionary (done) (uses ArrayList instead of a LinkedHashMap but oh well)
//step 2: basic rhyme checker (done)
//step 3: assonance (done)
//step 4: rhyme checker that uses phenomes (on a word by word basis) (done)
//step 5: multi checker that uses JUST PHENOMES (not done yet)
 

public class RapAnalyzer  {

    //instance variables
    // private static Map<String, String> dictionary;
    private static ArrayList<String> dictionary;
    private static ArrayList<String> sentence;
    private static ArrayList<String> multi;
    private static ArrayList<String> multi2;
    private static ArrayList<String> phenome1;
    private static ArrayList<String> phenome2;
    private static ArrayList<Integer> colorindex;
    private static ArrayList<String> colors;
    private static ArrayList<Integer> colorindex1;

    //stats
    private static double confidencerhyme; //one word rhymes
    private static double notstrict; //alliteration
    private static double strict; //alliteration
    private static int cRhyme; //number of multisyllabic rhyming words in two lyrics ("multis")
    

    
    //constructor for LinkedHashMap
    public RapAnalyzer(){
	//	dictionary = new LinkedHashMap<String, String>();
	dictionary= new ArrayList<String>();
	sentence = new ArrayList<String>();
	multi = new ArrayList<String>();
	multi2 = new ArrayList<String>();
	phenome1 = new ArrayList<String>();
	phenome2 = new ArrayList<String>();
	colorindex = new ArrayList<Integer>();
       	colorindex1 = new ArrayList<Integer>();
	colors = new ArrayList<String>();
	confidencerhyme = 0.0;
	strict = 0.0;
	notstrict = 0.0;
	cRhyme = 0;
	
    }

    //basic reading of the file and construction of the dictionary
    public static void dictionary() {
	int counterD = 0;
	File file = new File("c06d.txt");
        
	FileInputStream fis = null;
	BufferedInputStream bis = null;
	DataInputStream dis = null;
 
	try {
	    fis = new FileInputStream(file);
 
	    // Here BufferedInputStream is added for fast reading.
	    bis = new BufferedInputStream(fis);
	    dis = new DataInputStream(bis);
	    Scanner sc = new Scanner(dis);
	    sc.useDelimiter("  |\\n");//use two delimiters: double space and newline
	    
	    while(sc.hasNext()){
		counterD++;
		dictionary.add(sc.next());
	    }
	    
	    //test
	    // System.out.println(counter); //testing that all elements from the sc got into temp
	    // System.out.println(dictionary);
	   

	    /* broken code (might be able to fix?)
	    //for loop to populate the dictionary (this is kind of messed up for some reason)
	    for (int i = 0; i < temp.size() - 1; i+=2){//have to add two to make sure duplicate entries dont get messed up
		String key = temp.get(i);
		String value = temp.get(i+1);
		dictionary.put(key, value);	       
	    }
	    */
 
	    // dispose all the resources after using them.
	    fis.close();
	    bis.close();
	    dis.close();
	    sc.close();
 
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }//end dictionary

    //binarysearch method

    //a rudimentary rhyme checker
    public static boolean rhymeornot(String wrd1, String wrd2){
	int counterR = 0;
	//	System.out.println("Input a word.");
	//String input1 = Keyboard.readString().toUpperCase();
	//System.out.println("Input another word.");
	//String input2 = Keyboard.readString().toUpperCase();
	String input1 = wrd1;
	String input2 = wrd2;
        int x = dictionary.indexOf(input1);
        if ( x == -1){
	    System.out.println("\"" + input1 + "\"" + " is  not a word by this program's standards. Sorry. Try again.");
	    System.exit(0);
	}
	int y = dictionary.indexOf(input2);
        if ( y == -1){
	    System.out.println("\"" + input2 + "\"" + " is  not a word by this program's standards. Sorry. Try again.");
	    System.exit(0);
	}
	String p1 = dictionary.get(x+1);
	String p2 = dictionary.get(y+1);
	//System.out.println(x);
	//System.out.println(y);
	String one = new StringBuilder(p1).reverse().toString().replaceAll(" ", "");
	String two = new StringBuilder(p2).reverse().toString().replaceAll(" ", "");
	
	if(one.length() > two.length()){
	    for (int i = 0 ; i < two.length(); i++){
		if(one.charAt(i) == two.charAt(i)){
		    counterR ++;
		}
	    }
	}
	else{
	    for (int i = 0; i < one.length(); i++){
		if(one.charAt(i) == two.charAt(i)){
		    counterR++;
		}
	    }
	}
	
	//some semblance of statistical analysis
	float avg = (one.length() + two.length())/2;
	confidencerhyme = counterR/avg * 100;
	if (Objects.equals(one, two)){
	    System.out.println(Text.stringSUPER("You can't rhyme a word with the same word.", "default", "red", "underline"));
	    System.out.print(Text.stringSUPER("", "default", "default", "default"));
	    return false;
	}
        else if(counterR > 0 && (confidencerhyme > 35.0)){
	    System.out.println(Text.stringSUPER("Confidence Level: " + confidencerhyme + "%", "default", "cyan", "bold"));
	    System.out.print(Text.stringSUPER("", "default", "default", "default"));
	    System.out.println(Text.stringSUPER("I suppose those two words rhyme.", "default", "blue", "underline"));
	    System.out.print(Text.stringSUPER("", "default", "default", "default"));	    
	    return true;
	}
	else{
	    System.out.println(Text.stringSUPER("Confidence Level: " + confidencerhyme + "%", "default", "red", "bold"));
	    System.out.print(Text.stringSUPER("", "default", "default", "default"));
	    System.out.println(Text.stringSUPER("Seems like these two words do not rhyme.","default","red", "underline"));
            System.out.print(Text.stringSUPER("", "default", "default", "default"));
	    return false;
			     
	}
    }

    public static double sentenceanalyzer(String s1){
	int counter = 0;
	String input = s1;
	Scanner sc1 = new Scanner(input);
	while (sc1.hasNext()){
	    sentence.add(sc1.next());
	}
	//strict consonance checking
	for (int i = 0; i < sentence.size() - 1; i++){
	    if(sentence.get(i).charAt(0) == sentence.get(i+1).charAt(0)){
		counter++;
	    }
	}

	//less strict consonance checking
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < sentence.size() ; i++){
	    sb.append(sentence.get(i).charAt(0));
	}
	
	//a lot of copypaste here :)/:(
	int cA = sb.toString().length() - sb.toString().replace("A", "").length();
	int cB= sb.toString().length() - sb.toString().replace("B", "").length();
	int cC = sb.toString().length() - sb.toString().replace("C", "").length();
	int cD = sb.toString().length() - sb.toString().replace("D", "").length();
	int cE = sb.toString().length() - sb.toString().replace("E", "").length();
	int cF = sb.toString().length() - sb.toString().replace("F", "").length();
	int cG = sb.toString().length() - sb.toString().replace("G", "").length();
	int cH = sb.toString().length() - sb.toString().replace("H", "").length();
	int cI = sb.toString().length() - sb.toString().replace("I", "").length();
	int cJ = sb.toString().length() - sb.toString().replace("J", "").length();
	int cK = sb.toString().length() - sb.toString().replace("K", "").length();
	int cL = sb.toString().length() - sb.toString().replace("L", "").length();
	int cM = sb.toString().length() - sb.toString().replace("M", "").length();
	int cN = sb.toString().length() - sb.toString().replace("N", "").length();
	int cO = sb.toString().length() - sb.toString().replace("O", "").length();
	int cP = sb.toString().length() - sb.toString().replace("P", "").length();
	int cQ = sb.toString().length() - sb.toString().replace("Q", "").length();
	int cR = sb.toString().length() - sb.toString().replace("R", "").length();
	int cS = sb.toString().length() - sb.toString().replace("S", "").length();
	int cT = sb.toString().length() - sb.toString().replace("T", "").length();
	int cU = sb.toString().length() - sb.toString().replace("U", "").length();
	int cV = sb.toString().length() - sb.toString().replace("V", "").length();
	int cW = sb.toString().length() - sb.toString().replace("W", "").length();
	int cX = sb.toString().length() - sb.toString().replace("X", "").length();
	int cY = sb.toString().length() - sb.toString().replace("Y", "").length();
	int cZ = sb.toString().length() - sb.toString().replace("Z", "").length();

	ArrayList<Integer> consonance = new ArrayList<Integer>();
	consonance.add(cA);
	consonance.add(cB);
	consonance.add(cC);
	consonance.add(cD);
	consonance.add(cE);
	consonance.add(cF);	
	consonance.add(cG);
	consonance.add(cH);
	consonance.add(cI);
	consonance.add(cJ);
	consonance.add(cK);
	consonance.add(cL);
	consonance.add(cM);
	consonance.add(cN);
	consonance.add(cO);
	consonance.add(cP);	
	consonance.add(cQ);
	consonance.add(cR);
	consonance.add(cS);
	consonance.add(cT);
	consonance.add(cU);
	consonance.add(cV);
	consonance.add(cW);
	consonance.add(cX);
	consonance.add(cY);
	consonance.add(cZ);
	
	char[] alphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	System.out.println(Text.Rcolorbold("\nResults: "));
        System.out.print(Text.stringSUPER("", "default", "default", "default"));
	System.out.println(Text.Rcolorbold("Your Sentence/Lyric: " + "\"" + input + "\""));
        System.out.print(Text.stringSUPER("", "default", "default", "default"));

	
	
	//where the magic happens
	for(int i = 0; i < consonance.size() - 1; i ++){
	    if (consonance.get(i) != 0){
		System.out.println(Text.Rcolorbold("The letter " + "\"" + alphabet[i] + "\"" + " appeared " + consonance.get(i) + " time(s)."));
	        System.out.print(Text.stringSUPER("", "default", "default", "default"));

	    }
	}
	//one problem: if two letters have the same occurrences, the following line of code returns the indice of the letter that is alphabetically lower than the other letter
	int maxEl = Collections.max(consonance);
	int maxElement = consonance.indexOf(Collections.max(consonance));
	
	System.out.println(Text.stringSUPER("\nYou seem to be fond of the letter" + " " + "\"" + alphabet[maxElement] +"\"" +  ".", "default", "blue", "bold"));
	System.out.print(Text.stringSUPER("", "default", "default", "default"));
	

	String strictmeasure = "Strict Measure of Assonance:"; //num of consonant  words next to each other over the num of words times 100
	strict = (float)counter/(float)sentence.size() * 100;
	String exp = "(Only counts if consonant words are next to each other)";

	String notstrictmeasure = "The Not Strict Measure of Assonance:";
	String exp2 = "(Based on occurrence of same-lettered words in a sentence, regardless of position)";
	//takes the max element over the num of words times 100;
	int s = sentence.size();
	notstrict = (float)maxEl/(float)s * 100;
	
	if (maxEl != 1){
	    System.out.println(Text.Rcolorbold("\nMore statistics:"));
	    System.out.print(Text.stringSUPER("", "default", "default", "default"));
	    System.out.println(strictmeasure + " " + strict + "% " + exp);
	    System.out.println("");
	    System.out.println(notstrictmeasure + " " + notstrict + "% " + exp2);
	}
	else{
	    System.out.println(Text.stringSUPER("Your sentence is not consonant.", "default", "red", "bold"));
	    System.out.print(Text.stringSUPER("", "default", "default", "default"));

	    System.out.println(notstrictmeasure + " " + 0 + "% " + exp2);
	    notstrict = 0;
	}
	    
        return notstrict;
    }

   

    //multisyllable rhymes (needs tuning)
    public static double multi(String sent1,String sent2){
	
	int counterP = 0; //used to check if phenomes match; for every letter add 1;
	//original arraylists that hold the parsed words from the users inputted strings
	Scanner sc2 = new Scanner(sent1);
        sc2.useDelimiter("[/*#.,\\s]");
	while (sc2.hasNext()){
	    multi.add(sc2.next().toUpperCase());
	}

	Scanner sc3 = new Scanner(sent2);
        sc3.useDelimiter("[/*#.,\\s]");
	while(sc3.hasNext()){
	    multi2.add(sc3.next().toUpperCase());
	}

	//populate an arraylist with the reversed phenomes from the users sentence
	//if they don't appear, just use empty string as placeholder
       
	for (String s: multi){
	    int x = dictionary.indexOf(s);
	    if(x == -1){
		phenome1.add("empty");
	    }
	    else{
		String ph1 = new StringBuilder(dictionary.get(x+1)).reverse().toString().replaceAll(" ", "");
		phenome1.add(ph1);
	    }
	}

      	for (String s: multi2){
	    int x = dictionary.indexOf(s);
	    if(x == -1){
		phenome2.add("empty");
	    }
	    else{
		String ph2 = new StringBuilder(dictionary.get(x+1)).reverse().toString().replaceAll(" ","");
		phenome2.add(ph2);
		
	    }
	}

	//check for multis, return a double based on how fire the two sentences are
	//return sentence with rhyming words colored

	//multiple cases; a lot of this is based on the method rhymeornot

	if (phenome2.size() > phenome1.size()){
	    for(int i = phenome1.size()-1; i >= 0; i--){
		if (Objects.equals(phenome2.get(i), phenome1.get(i))){
		    counterP+= 0;
		}
		
		else if(phenome2.get(i).length() > phenome1.get(i).length()){
      		    float avgR = (phenome2.get(i).length() + phenome1.get(i).length())/2;
		    int counterm = 0;
		    for (int x = 0 ; x < phenome1.get(i).length(); x++){
			if(phenome1.get(i).charAt(x) == phenome2.get(i).charAt(x)){
			    counterP ++;
			    counterm ++;
			}
			
		    }
		    float confidenceL = (float)counterm/avgR * 100;
		    if (counterm > 0 && confidenceL > 40.0){
			cRhyme ++;
			colorindex.add(i);
		    }	
		    counterm = 0;
		}
		else{
      		    float avgR = (phenome2.get(i).length() + phenome1.get(i).length())/2;
		    int counterm = 0;		    
		    for (int y = 0; y < phenome2.get(i).length(); y++){
			if(phenome1.get(i).charAt(y) == phenome2.get(i).charAt(y)){
			    counterP++;
			    counterm++;
			}
		    }
		    float confidenceL = (float)counterm/avgR * 100;
		    if (counterm > 0 && confidenceL > 40.0){
			cRhyme ++;
			colorindex.add(i);
		    }	
		    counterm = 0;		    
		}
	    }
	}
	else{
	    for(int i = phenome2.size()-1; i >= 0; i--){
		if (Objects.equals(phenome2.get(i), phenome1.get(i))){
		    counterP+= 0;
		}
		else if(phenome2.get(i).length() > phenome1.get(i).length()){
      		    float avgR = (phenome2.get(i).length() + phenome1.get(i).length())/2;
		    int counterm = 0;		    
		    for (int x = 0 ; x < phenome1.get(i).length(); x++){
			if(phenome1.get(i).charAt(x) == phenome2.get(i).charAt(x)){
			    counterP ++;
			    counterm++;
			}
		    }
		    float confidenceL = (float)counterm/avgR * 100;
		    if (counterm > 0 && confidenceL > 40.0){
			cRhyme ++;
			colorindex.add(i);
		    }	
		    counterm = 0;		    		    
		}
		else{
      		    float avgR = (phenome2.get(i).length() + phenome1.get(i).length())/2;
		    int counterm = 0;		    
		    for (int y = 0; y < phenome2.get(i).length(); y++){
			if(phenome1.get(i).charAt(y) == phenome2.get(i).charAt(y)){
			    counterP ++;
			    counterm++;
			}
		    }
		    float confidenceL = (float)counterm/avgR * 100;  
		    if (counterm > 0 && confidenceL > 40.0){
			cRhyme ++;
			colorindex.add(i);
		    }	
		    counterm = 0;		    
		}
	    }
	}

	System.out.println(multi);
	System.out.println(multi2);
	System.out.println(phenome1);
	System.out.println(phenome2);
	System.out.println("Your lyrics, analyzed");
	// a color match analysis of the sentence (mad cool)
	Collections.reverse(colorindex);
	System.out.println(colorindex);

	
	// just some ui stuff
	//the reason this is so long is in order to repeat colors once lyrics get lengthy
	colors.add("black");
	colors.add("red");
	colors.add("green");
	colors.add("yellow");
	colors.add("blue");
	colors.add("purple");
	colors.add("cyan");
	colors.add("white");
       	colors.add("black");
	colors.add("red");
	colors.add("green");
	colors.add("yellow");
	colors.add("blue");
	colors.add("purple");
	colors.add("cyan");
	colors.add("white");
	colors.add("black");
	colors.add("red");
	colors.add("green");
	colors.add("yellow");
	colors.add("blue");
	colors.add("purple");
	colors.add("cyan");
	colors.add("white");
	colors.add("black");
	colors.add("red");
	colors.add("green");
	colors.add("yellow");
	colors.add("blue");
	colors.add("purple");
	colors.add("cyan");
	colors.add("white");
	colors.add("black");
	colors.add("red");
	colors.add("green");
	colors.add("yellow");
	colors.add("blue");
	colors.add("purple");
	colors.add("cyan");
	colors.add("white");

	String lyric1 = "\"";
	for (int i = 0; i < multi.size(); i++){
	    if(colorindex.contains(i)){
		lyric1 += Text.stringSUPER(multi.get(i), "default", colors.get(i), "bold");
		lyric1 += " ";
	    }
	    else{
		System.out.print(Text.stringSUPER("", "default", "default", "default"));	    
		lyric1 += multi.get(i);
		lyric1 += " ";
	    }
	}
	lyric1 += "\"";

	
	String lyric2 = "\"";
	for (int i = 0; i < multi2.size(); i++){
	    if(colorindex.contains(i)){
		lyric2 += Text.stringSUPER(multi2.get(i), "default", colors.get(i), "bold");
		lyric2 += " ";
	    }
	    else{
		System.out.print(Text.stringSUPER("", "default", "default", "default"));	    
		lyric2 += multi2.get(i);
		lyric2 += " ";
	    }
	}
	lyric2+= "\"";
	
	System.out.println(lyric1);
       	System.out.print(Text.stringSUPER("", "default", "default", "default"));
       	System.out.println("");
	System.out.println(lyric2);
       	System.out.print(Text.stringSUPER("", "default", "default", "default"));

       	System.out.println("");

        System.out.println("Rhyming words: " + cRhyme);
	System.out.println("Similar phenomes: " + counterP);


	return 0.0;

    }

	
	    
	
	
    
      
    
}//end class
	
	