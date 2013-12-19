import java.awt.Color;
import java.util.*;

/**
 * Write a description of class Character here.
 * 
 * @author Jonas Dahl
 * @version 2013.12.05
 */
public class Character extends Item {
    private static final Color CHARACTER_COLOR = new Color(255, 228, 181);
    Room room;
    String winningWord;
    HashMap<HashSet<String>, String> replies;
    HashMap<Item, String> itemReplies;

    /**
     * Constructor for objects of class Character.
     * @param name  name of the Character.
     */
    public Character(String name) {
        super(name);
        winningWord = null;
        setColor(CHARACTER_COLOR);
        replies = new HashMap<HashSet<String>, String>();
        itemReplies = new HashMap<Item, String>();
        setPickable(false);
        setUseWord("'talk " + super.getName() + "'");
    }

    /**
     * Sets room of the Character so it knows where it is.
     * @param room   the room to be.
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * Sets the winning word of character.
     * If this word is said when talking to this character,
     * tand tested with isWinningWord(), it may finish the game.
     * @param winningWord the winning word to be set
     */
    public void setWinningWord(String winningWord) {
        this.winningWord = winningWord;
    }

    /**
     * Gets the winning word of this person.
     * @return the winning word - null if nothing
     */
    public String getWinningWord() {
        return winningWord;
    }

    /**
     * Checks if input string if winning word.
     * @param stringToTest the string to test if is winning word
     * @return returns true if stringToTest is winning word - false elsewise
     */
    public boolean isWinningWord(String stringToTest) {
        return (stringToTest.equalsIgnoreCase(winningWord) ? true : false);
    }

    /**
     * Handles conversation with this person. If returning true, handle game-winning situation.
     * @return true if player has mentioned the winning word - the game should be finished
     */
    public boolean startConversation() {
        String name = getName();
        Output.println("You are now talking to " + name + ". Exit by typing 'bye'.");
        Output.println("<< " + name + ": Hello");
        Output.print(">> ");

        for (;;) {
            Scanner reader = new Scanner(System.in);
            String inputLine = reader.nextLine();
            if (inputLine.equalsIgnoreCase("bye"))
                break;

            Scanner tokenizer = new Scanner(inputLine);
            ArrayList<String> line = new ArrayList<String>();
            while (tokenizer.hasNext()) {
                line.add(tokenizer.next().trim());
            }
            
            if (winningWord != null) {
                for (String word : line) {
                    if (word.equalsIgnoreCase(winningWord))
                        return true;
                }
            }
            Output.println("<< " + name + ": " + getReply(line));
            Output.print(">> ");
        }
        Output.println("<< Thank you for talking to me!");
        return false;
    }

    /**
     * Sets reply for this person.
     * @param words the words for which the second parameter should be used.
     * @param reply the reply for the given words in "words" parameter
     */
    public void setReply(HashSet<String> words, String reply) {
        replies.put(words, reply);
    }

    /**
     * Returns the output from "input" as a String.
     * @param input an ArrayList of input words
     * @return a String - the reply from the person
     */
    public String getReply(ArrayList<String> input) {
        String returnString = null;
        
        for (String word : input) {
            Set<Map.Entry<HashSet<String>, String>> set = replies.entrySet();

            for (Map.Entry<HashSet<String>, String> matching : set) {
                for (String inTheList : matching.getKey()) {
                    if (inTheList.trim().equalsIgnoreCase(word.trim())) {
                        returnString = matching.getValue();
                        break;
                    }
                }
            }
        }

        if (returnString == null) {
            returnString = "Maybe you should try to say something about '" + getRandomControlWord() + "'.";
        }

        return returnString;
    }

    /**
     * Gets a random control word for which the person will answer something relevant.
     */
    public String getRandomControlWord() {
        Random random = new Random();
        List<HashSet<String>> words = new ArrayList<HashSet<String>>(replies.keySet());
        HashSet<String> pool = words.get(random.nextInt(words.size()));

        int size = pool.size();
        int item = new Random().nextInt(size);
        int i = 0;
        for (String obj : pool) {
            if (i == item)
                return obj;
            i++;
        }

        return null;
    }
    
    /**
     * Adds a reply for given item.
     */
    public void ifGivenSay(Item item, String answer) {
        itemReplies.put(item, answer);
    }
    
    /**
     * Returns the reply from character if item was given.
     */
    public String isGiven(Item item) {
        ArrayList<Item> items = new ArrayList<Item>(itemReplies.keySet());
        ArrayList<String> values = new ArrayList<String>(itemReplies.values());
       
        int i = 0;
        for (Item tempItem : items) {
            if (item == tempItem)
                return values.get(i);
            i++;
        }
        return "Thank you, but what am I going to do with a " + item.getName() + "?";
    }

    /**
     * Gets description of this Character.
     */
    public String getDescription() {
        return this.getName() + " is in here! Talk to him by typing 'talk " + this.getName() + "'";
    }
}