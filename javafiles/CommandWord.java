/**
 * Representations for all the valid command words for the game
 * along with a string in a particular language.
 * 
 * @author  Michael Kölling and David J. Barnes and Jonas Dahl
 * @version 2013.12.06
 */
public enum CommandWord {
    /*
     * A value for each command word along with its
     * corresponding user interface string. The user interface
     * string is also the name of the method beeing called
     * when user inputs it.
     */
    FIRE("fire"), 
    CHARGE("charge"), 
    PICK("pick"), 
    LOOK("look"), 
    GO("go"), 
    QUIT("quit"), 
    HELP("help"), 
    TALK("talk"), 
    MAP("map"), 
    GIVE("give"), 
    UNKNOWN("unknown");
    
    private String commandString;               // The command string.
    
    /**
     * Initialise with the corresponding command string.
     * @param commandString The command string.
     */
    CommandWord(String commandString) {
        this.commandString = commandString;
    }
    
    /**
     * @return The command word as a string.
     */
    public String toString() {
        return commandString;
    }
}
