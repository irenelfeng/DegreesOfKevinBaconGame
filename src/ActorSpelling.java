import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * A Java implementation of Norvig's spell checker. Norvig's paper and Python spell
 * checker can be found at: http://www.norvig.com/spell-correct.html
 * This Java version from: http://raelcunha.com/spell-correct.php
 *
 * Comments and modifications by Scot Drysdale
 * @author Rael Cunha
 * 
 * Modified even more to correct spelling based off of one certain file: a list of actors.
 * Modified by Irene Feng & Orestis Lykouropoulos
 */
class ActorSpelling {

  private ArrayList<String> actors; //changed the map of strings and frequencies just as an arraylist. 
  									//every actor should have a frequency of one, anyway.

  /** 
   * Constructs a new spell corrector.  Builds up a map of correct words with
   * their frequencies, based on the words in the given file.
   * 
   * Modified the map to be an arraylist, as explained above. 
   * 
   * @param file the text to process
   * @throws IOException
   */
  public ActorSpelling(String file) throws IOException {
    actors = new ArrayList<String>();
    BufferedReader in = new BufferedReader(new FileReader(file));
    
    String line; 
	while ((line = in.readLine()) != null) {
		
		String[] tokens = line.split("\\|");
		String actor = tokens[1]; //name of actor
		actors.add(actor);
	}
	in.close();
  }

  /**
   * Constructs a list of all words within edit distance 1 of the given word
   * 
   * @param word the word to construct the list from
   * @return a list of words with in edit distance 1 of word
   */
  private ArrayList<String> edits(String word) {
    ArrayList<String> result = new ArrayList<String>();
        
    // All deletes of a single letter
    for(int i=0; i < word.length(); ++i) 
      result.add(word.substring(0, i) + word.substring(i+1));
    
    // All swaps of adjacent letters
    for(int i=0; i < word.length()-1; ++i) 
      result.add(word.substring(0, i) + word.substring(i+1, i+2) + 
                 word.substring(i, i+1) + word.substring(i+2));
    
    // All replacements of a letter
    for(int i=0; i < word.length(); ++i) 
      for(char c='a'; c <= 'z'; ++c) 
        result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i+1));
    
    // All insertions of a letter
    for(int i=0; i <= word.length(); ++i) 
      for(char c='a'; c <= 'z'; ++c) 
        result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i));
    
    return result;
  }

  /**
   * Corrects the spelling of a word, if it is within edit distance 2.
   * 
   * Modified to also check if word is capitalized right (does not count towards edit distance), and also to return null
   * For purpose of the baconGame
   *
   * @param word the word to check/correct
   * @return word if correct or too far from any word; corrected word otherwise
   */
  public String correct(String word) {
	  if(actors.contains(word))
		    return null; //returns null if the actor is in the list (the actor in the list is simply not connected to root)
  
	//All mis-capitalizations of the actor
	String[] names = word.split(" ");
	    
	    for(int h=0; h<names.length; h++){
	    	String firstLetter = names[h].substring(0,1).toUpperCase();
	    	String restLetters = names[h].substring(1).toLowerCase();
	    	names[h] = firstLetter + restLetters;
	    }
	    
	   //reconstructs string back to word, with fixed capitalization
	   word = "";
	   for(int i =0; i<names.length -1 ; i++) word += names[i] + " ";
	    	word += names[names.length-1];
	 
	   // If in ActorList now, just return it.
	   if(actors.contains(word))
	    return word;

    ArrayList<String> list = edits(word);  // Everything edit distance 1 from word

    for(String s: list)
    	if(actors.contains(s))
    		return s; //returns the first string contained in actors that is edit distance 1 away
    
    for(String s : list) 
        for(String w : edits(s)) 
        	if(actors.contains(w))
        		return w; //returns the first string contained in actors that is edit distance 2 away 
    
    return null; //also returns null if there are no actors in the actorslist that have an edit distance of 2 or less away from the word.
    	
  }
  
  /**
   * Original version read a single word to correct from the command line.
   * It is commented out below
   * @throws IOException
   */
  
/*
   public static void main(String args[]) throws IOException {
    if(args.length > 0) System.out.println((new Spelling("big.txt")).correct(args[0]));
  }
*/

   public static void main(String args[]) throws IOException {
     ActorSpelling corrector = new ActorSpelling("inputs/actors.txt");
     Scanner input = new Scanner(System.in);  
     
     System.out.println("Enter words to correct");
     String word = input.nextLine();
     
     while(true) {
       System.out.println(word + " is corrected to " + corrector.correct(word));
       word = input.nextLine();
     }
   }
}