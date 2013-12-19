import java.awt.Color;
import java.util.*;
import java.lang.reflect.*;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.lang.Math;
import java.lang.Class;

/**
 *  This class is the main class of the game. 
 *  This game is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes and Jonas Dahl
 * @version 2013.12.13
 */

public class Game {
    // Static variables
    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    static final int ROOM_WIDTH = 100;
    static final int ROOM_HEIGHT = 100;
    static final int ROOM_MARGIN = 20;
    static final int ROOM_PADDING = 5;
    static final int OBJECT_WIDTH = 20;
    static final String GAME_NAME = "Save Lil B";

    // Class variables
    private Parser parser;
    private Room currentRoom;
    private Canvas canvas;
    private HashSet<Item> items;
    private Random random;
    private boolean playing;

    /**
     * Create the game and initialise its internal map. Also starts the game. "Aka play()".
     */
    public Game() {
        canvas = new Canvas("Game", WIDTH, HEIGHT, ROOM_WIDTH, ROOM_HEIGHT, 
            ROOM_MARGIN, ROOM_PADDING);
        parser = new Parser();
        items = new HashSet<Item>();
        random = new Random();
        setUpGame();                                // Creates the rooms and other stuff
        play();                                     // Starts game for player
    }

    /**
     * Main method.
     */
    public static void main(String[] args) {
        Game game = new Game();                     // Creates a new ga
    }

    /**
     *  Main play routine. Loops until end of play.
     */
    public void play() {  
        printWelcome();

        // Enter the main command loop. Here we repeatedly read commands and
        // execute them until the game is over.
        playing = true;
        while (playing) {
            Command command = parser.getCommand();
            processCommand(command);
        }
        Output.println("Thank you for playing. Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome() {
        Output.println();
        Output.println("Welcome to the game " + GAME_NAME + "!");
        Output.println("This game lets you talk to nice people, and you can win!");
        Output.println("Protip: start by talking to Nedo...!");
        Output.println();
        Output.println("Type '" + CommandWord.HELP + "' if you need help (just do it!).");
        Output.println();
        Output.println(currentRoom.getLongDescription());
    }

    /**
     * Create all the rooms, items and characters and link them together.
     */
    private void setUpGame() {
        Room entrance, pub, office, club;
        Character johan, nedo;
        Beamer beamer;
        Animal seal;
        WorldMap map;
        Key officeKey;
        HashSet<String> nedoWords, johanWords;

        // Create items
        officeKey = new Key("OfficeKey");
        beamer = new Beamer("Beamer");
        seal = new Animal("Seal");
        map = new WorldMap("Map");

        // Create rooms
        entrance = new Room("Entrance");
        pub = new Room("Pub");
        club = new Room("Club");
        office = new Room("Office", officeKey);

        // Create characters
        nedo = new Character("Nedo");
        johan = new Character("Johan");
        nedoWords = new HashSet<String>();
        johanWords = new HashSet<String>();

        // Sets exits for the rooms
        entrance.setExit("west", pub);
        entrance.setExit("south", office);
        entrance.setExit("north", club);
        pub.setExit("east", entrance);
        office.setExit("north", entrance);
        club.setExit("south", entrance);

        // Configure characters and other items
        nedoWords.add("help");
        nedoWords.add("win");
        nedoWords.add("code");
        nedoWords.add("word");
        nedoWords.add("what");
        nedoWords.add("johan");
        johanWords.add("code");
        johanWords.add("word");
        johanWords.add("nedo");
        johanWords.add("help");
        johan.setReply(johanWords, "I know the code Nedo wants, but I want a seal to tell it.");
        johan.ifGivenSay(seal, "Thanks!\n   If you tell Nedo the code word: 'LILB', you will win the game.\n   You will be amazed.");
        nedo.setReply(nedoWords, "If you tell me the correct code word you will win. \n   I think Johan knows the code.");
        nedo.setWinningWord("LILB");

        pub.addItem(johan);
        pub.addItem(nedo);
        pub.addItem(beamer);
        pub.addItem(officeKey);
        entrance.addItem(map);
        office.addItem(seal);

        // Set up
        currentRoom = entrance;                     // Start game in entrance 
        Room.calculateRoomCoordinates(currentRoom);
        canvas.drawRoom(currentRoom, true);
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private void processCommand(Command command) {
        CommandWord commandWord = command.getCommandWord();

        for (CommandWord cmd : CommandWord.values()) {
            if (cmd == commandWord) {
                try {
                    Method method = getClass().getMethod(commandWord.toString(), Command.class);
                    method.invoke(this, command);

                    // Update inventory graphics
                    int x = ROOM_PADDING;
                    int y = ROOM_PADDING;

                    for (Item item : items) {
                        item.draw(canvas, x, y, OBJECT_WIDTH);
                        x += OBJECT_WIDTH + ROOM_PADDING;

                        // Wrap the line of items if not space enough
                        if (x > WIDTH - ROOM_PADDING - OBJECT_WIDTH) {
                            x = ROOM_PADDING;
                            y += ROOM_PADDING + OBJECT_WIDTH;
                        }
                    }
                } catch (Exception e) {
                    Output.println("I don't know what you mean...");
                }
                return; 
            }
        }
    }

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    public void help(Command command) {
        Output.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    public void go(Command command) {
        if (!command.hasSecondWords()) {
            // If there is no second word, we don't know where to go...
            Output.println("Go where? (Syntax: go west)");
            return;
        }

        String direction = command.getSecondCommands().get(0).toLowerCase();
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            Output.println("There is no door to the: " + direction + "!");
            return;
        }

        if (nextRoom.isLocked()) {
            // If door is locked, we need to check if player has a key.
            if (nextRoom.unlock(items))
                Output.println("You used your key to unlock the room!");
            else {
                Output.println("This door is locked! You need a key to open it.");
                return;
            }
        }

        // Draw room, erase old room and change room.
        changeRoom(nextRoom);
        Output.println(currentRoom.getLongDescription());
    }

    /** 
     * Fire the beamer.
     */
    public void fire(Command command) {
        if (!command.hasSecondWords()) {
            Output.println("Fire what? (Syntax: fire BeamerName)");
            return;
        }

        String beamerName = command.getSecondCommands().get(0);
        Item beamer = Item.getItem(beamerName, items);
        if (beamer == null) {
            Output.println("You have no beamer with the name '" + beamerName + 
                "'. (Syntax: fire BeamerName)");
            return;
        }

        Room destinationRoom = ((Beamer) beamer).getDestination();
        changeRoom(destinationRoom);
        Output.println("You where teleported.");
    }

    /** 
     * Beams with beamer.
     */
    public void charge(Command command) {
        if (!command.hasSecondWords()) {
            Output.println("Charge what? (Syntax: charge BeamerName)");
            return;
        }

        String beamerName = command.getSecondCommands().get(0);
        Item beamer = Item.getItem(beamerName, items);
        if (beamer == null) {
            Output.println("You have no beamer with the name '" + beamerName + 
                "'. (Syntax: fire BeamerName)");
            return;
        }

        ((Beamer) beamer).charge(currentRoom);
        Output.println("You charged the beamer with the current room!");
    }

    /** 
     * Paints the map.
     */
    public void map(Command command) {
        Item item = Item.getItem(WorldMap.class, items);

        if (item == null) {
            Output.println("You have no map in your inventory, you need to find one!");
            return;
        }

        canvas.drawAllMap(currentRoom, new ArrayList<Room>());
        canvas.drawRoom(currentRoom, true);
        Output.println("Wow, you can now see the whole world!");
    }

    /** 
     * Gives an object to a person.
     */
    public void give(Command command) {
        if (!command.hasSecondWords() || command.getSecondCommands().size() < 2) {
            Output.println("Give what to who? (Syntax: give ItemName CharacterName)");
            return;
        }

        String itemName = command.getSecondCommands().get(0);
        String personName = command.getSecondCommands().get(1);
        Character person = (Character) Item.getItem(personName, currentRoom.getItems()); // Find person
        Item itemToGive = (Item) Item.getItem(itemName, items);                          // Find item to transfer

        if (itemToGive == null || person == null) {
            Output.println("There is no character with The name " + person.getName());
            Output.println("or there is no item with the name " + itemToGive.getName() + ".");
            Output.println("(Syntax: give ItemName CharacterName)");
            return;
        }

        String response = person.isGiven(itemToGive);
        Output.print("<< " + person.getName() + ": " + response);
    }

    /** 
     * Look in the room for items.
     */
    public void look(Command command) {
        if (command.hasSecondWords()) {
            Output.println("Look what? (Syntax: look)");
            return;
        }

        HashSet<Item> items = currentRoom.getItems(); // Try to look in current room.

        for (Item item : items) {
            Output.println(item.getDescription());
        }

        canvas.drawItems(currentRoom);
    }

    /** 
     * Pick up item
     */
    public void pick(Command command) {
        if (!command.hasSecondWords()) {
            Output.println("What item should be picked up?");
            return;
        }

        String itemToPickUp = command.getSecondCommands().get(0);
        HashSet<Item> roomItems = currentRoom.getItems();

        Item item = Item.getItem(itemToPickUp, roomItems);
        if (item.isPickable()) {
            items.add(item);                                // Adds item to player inventory
            roomItems.remove(item);                         // Removes item from room
            Output.println("Picked up " + item.getName() + "!");

            canvas.drawItems(currentRoom);

            String useWord = item.getUseWord();
            if (useWord != null)
                System.out.println("Use it by typing " + useWord + ".");

            return;
        }

        System.out.println("Failed to pick up item '" + itemToPickUp + "'.");
    }

    /** 
     * "talk" was entered. 
     */
    public void talk(Command command) {
        boolean won = false;
        if (!command.hasSecondWords()) {
            Output.println("Talk who?");
        } else {
            String name = command.getSecondCommands().get(0);
            Item person = Item.getItem(name, currentRoom.getItems());
            if (person == null) {
                Output.println("There is no person called " + name + " in this room.");
                return;
            }
            won = ((Character) person).startConversation();
        }

        if (won)
            won();
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    public void quit(Command command) 
    {
        if (command.hasSecondWords()) {
            Output.println("Quit what?");
        } else {
            playing = false;  // signal that we want to quit
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    public void won() {
        Image image = new ImageIcon("nedo.jpg").getImage();
        canvas.drawImage(image, WIDTH / 2 - 306, HEIGHT / 2 - 306);
        Output.println("Congratulations, you won!");
        playing = false;

        for (int i = 0; ; i++) {
            canvas.wait((int)(1 + Math.sin(Math.toRadians(i % 360)) / 2 * 100));
            canvas.setForegroundColor(new Color(40, 40, (i % 255)));
            canvas.setBackgroundColor(new Color(255 - (i % 255), (i + 40) % 255, (i % 255)));
            canvas.setFont(new Font("Arial", 1, 100));
            if (i % 2 == 0)
                canvas.drawString("You WON!!!", 0, HEIGHT / 2);
            else 
                canvas.eraseString("You WON!!!", 0, HEIGHT / 2);
        }
    }

    /**
     * Changes the current room.
     */
    private void changeRoom(Room newRoom) {
        canvas.drawRoom(currentRoom, false);
        canvas.drawRoom(newRoom, true);
        currentRoom = newRoom;
    }
}
