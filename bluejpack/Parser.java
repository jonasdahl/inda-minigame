import java.util.Scanner;
import java.util.ArrayList;

/**
 * This class is part of the game.  
 * 
 * This parser reads user input and tries to interpret it as an "Adventure"
 * command. Every time it is called it reads a line from the terminal and
 * tries to interpret the line as a two-word command. It returns the command
 * as an object of class Command.
 *
 * The parser has a set of known command words. It checks user input against
 * the known commands, and if the input is not one of the known commands, it
 * returns a command object that is marked as an unknown command.
 * 
 * @author  Michael Kölling and David J. Barnes and Jonas Dahl
 * @version 2013.12.13
 */
public class Parser {
    private CommandWords commands;  // holds all valid command words
    private Scanner reader;         // source of command input

    /**
     * Create a parser to read from the terminal window.
     */
    public Parser() {
        commands = new CommandWords();
        reader = new Scanner(System.in);
    }

    /**
     * Get a command from the user.
     * @return The next command from the user.
     */
    public Command getCommand() {
        String inputLine;                   // Will hold the full input line
        String word1 = null;
        ArrayList<String> word2 = new ArrayList<String>();

        Output.println();
        Output.print("> ");             // Print prompt

        inputLine = reader.nextLine();

        // Find all words on the line.
        Scanner tokenizer = new Scanner(inputLine);
        if(tokenizer.hasNext())
            word1 = tokenizer.next();
        
        while (tokenizer.hasNext())
            word2.add(tokenizer.next());    // Then we add the other words afterwards
        
        return new Command(commands.getCommandWord(word1), word2);
    }

    /**
     * Print out a list of valid command words.
     */
    public void showCommands() {
        commands.showAll();
    }
}
