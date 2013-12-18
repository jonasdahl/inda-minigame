Project: game
Authors: Michael Kölling and David J. Barnes and Jonas Dahl

This is the best game ever, LIL-B. To play, run the main method.

Some old good and not longer used methods are:


/**
 * Determines wether a room is connected to another room
 */
private boolean roomsAreConnected(ArrayList<Room> roomsTested, Room nowRoom, Room destinationRoom) {
    roomsTested.add(nowRoom);

    if (nowRoom == destinationRoom)
        return true;

    HashMap<String, Room> roomsNearby = nowRoom.getExits();
    for(Room room : roomsNearby.values()) {
        if (!roomsTested.contains(room) && roomsAreConnected(roomsTested, room, destinationRoom))
            return true;
    }

    return false;
}





/**
 * Gets path to go from one room to another. (Uses recursive help method)
 */
public ArrayList<String> getPath(Room fromRoom, Room toRoom) {
    return getPathPrivate(fromRoom, toRoom, new ArrayList<String>(), new ArrayList<Room>());
}

/**
 * Help method for getPath(...)
 */
private ArrayList<String> getPathPrivate(Room fromRoom, Room toRoom, ArrayList<String> pathWalked, ArrayList<Room> roomsTested) {
    if (fromRoom == toRoom) {
        ArrayList<String> array = new ArrayList<String>();
        return pathWalked;
    }

    roomsTested.add(fromRoom);

    HashMap<String, Room> roomsNearby = fromRoom.getExits();

    for (Map.Entry<String, Room> entry : roomsNearby.entrySet()) {
        String direction = entry.getKey();
        Room room = entry.getValue();
        if (!roomsTested.contains(room) && getPathPrivate(room, toRoom, pathWalked, roomsTested) != null) {
            pathWalked.add(direction);
            return pathWalked;
        }
    }

    return null;
}







/**
 * A method that inverses the string direction, ie "south" becomes "north".
 */
private String inverseDirection(String direction) {
    String inverseDirection = "";
    if (direction.equals("north"))
        inverseDirection = "south";
    else if (direction.equals("west"))
        inverseDirection = "east";
    else if (direction.equals("south"))
        inverseDirection = "north";
    else if (direction.equals("east"))
        inverseDirection = "west";

    return inverseDirection;
}