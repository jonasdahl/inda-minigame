import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

/**
 * Class Canvas - a class to allow for simple graphical 
 * game drawing on a canvas.
 * 
 * @author Jonas Dahl
 * @version 2013.12.04
 */

public class Canvas {
    private int roomWidth, roomHeight, roomMargin, roomPadding;
    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Color backgroundColor;
    private Image canvasImage;

    /**
     * Create a Canvas with default background color (white).
     * @param title  title to appear in Canvas Frame
     * @param width  the desired width for the canvas
     * @param height  the desired height for the canvas
     */
    public Canvas(String title, int width, int height, int roomWidth, 
                  int roomHeight, int roomMargin, int roomPadding) {
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(width, height));
        backgroundColor = Color.WHITE;
        frame.pack();
        setVisible(true);
        this.roomWidth = roomWidth;
        this.roomHeight = roomHeight;
        this.roomPadding = roomPadding;
        this.roomMargin = roomMargin;
    }

    /**
     * Set the canvas visibility and brings canvas to the front of screen
     * when made visible. This method can also be used to bring an already
     * visible canvas to the front of other windows.
     * @param visible  boolean value representing the desired visibility of
     * the canvas (true or false) 
     */
    public void setVisible(boolean visible) {
        if(graphic == null) {
            // first time: instantiate the offscreen image and fill it with
            // the background color
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D)canvasImage.getGraphics();
            graphic.setColor(backgroundColor);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
        }
        frame.setVisible(true);
    }

    /**
     * Provide information on visibility of the Canvas.
     * @return  true if canvas is visible, false otherwise
     */
    public boolean isVisible() {
        return frame.isVisible();
    }

    /**
     * Draw the outline of a given shape onto the canvas.
     * @param  shape  the shape object to be drawn on the canvas
     */
    public void draw(Shape shape) {
        graphic.draw(shape);
        canvas.repaint();
    }
 
    /**
     * Fill the internal dimensions of a given shape with the current 
     * foreground color of the canvas.
     * @param  shape  the shape object to be filled 
     */
    public void fill(Shape shape) {
        graphic.fill(shape);
        canvas.repaint();
    }

    /**
     * Fill the internal dimensions of the given circle with the current 
     * foreground color of the canvas.
     * @param  xPos  The x-coordinate of the circle center point
     * @param  yPos  The y-coordinate of the circle center point
     * @param  diameter  The diameter of the circle to be drawn
     */
    public void fillCircle(int xPos, int yPos, int diameter) {
        Ellipse2D.Double circle = new Ellipse2D.Double(xPos, yPos, diameter, diameter);
        fill(circle);
    }

    /**
     * Fill the internal dimensions of the given rectangle with the current 
     * foreground color of the canvas. This is a convenience method. A similar 
     * effect can be achieved with the "fill" method.
     */
    public void fillRectangle(int xPos, int yPos, int width, int height) {
        fill(new Rectangle(xPos, yPos, width, height));
    }

    /**
     * Erase the whole canvas.
     */
    public void erase() {
        Color original = graphic.getColor();
        graphic.setColor(backgroundColor);
        Dimension size = canvas.getSize();
        graphic.fill(new Rectangle(0, 0, size.width, size.height));
        graphic.setColor(original);
        canvas.repaint();
    }

    /**
     * Erase the internal dimensions of the given circle. This is a 
     * convenience method. A similar effect can be achieved with
     * the "erase" method.
     */
    public void eraseCircle(int xPos, int yPos, int diameter) {
        Ellipse2D.Double circle = new Ellipse2D.Double(xPos, yPos, diameter, diameter);
        erase(circle);
    }

    /**
     * Erase the internal dimensions of the given rectangle. This is a 
     * convenience method. A similar effect can be achieved with
     * the "erase" method.
     */
    public void eraseRectangle(int xPos, int yPos, int width, int height) {
        erase(new Rectangle(xPos, yPos, width, height));
    }

    /**
     * Erase a given shape's interior on the screen.
     * @param  shape  the shape object to be erased 
     */
    public void erase(Shape shape) {
        Color original = graphic.getColor();
        graphic.setColor(backgroundColor);
        graphic.fill(shape);              // erase by filling background color
        graphic.setColor(original);
        canvas.repaint();
    }

    /**
     * Erases a given shape's outline on the screen.
     * @param  shape  the shape object to be erased 
     */
    public void eraseOutline(Shape shape) {
        Color original = graphic.getColor();
        graphic.setColor(backgroundColor);
        graphic.draw(shape);  // erase by drawing background color
        graphic.setColor(original);
        canvas.repaint();
    }

    /**
     * Draws an image onto the canvas.
     * @param  image   the Image object to be displayed 
     * @param  x       x co-ordinate for Image placement 
     * @param  y       y co-ordinate for Image placement 
     * @return  returns boolean value representing whether the image was 
     *          completely loaded 
     */
    public boolean drawImage(Image image, int x, int y) {
        boolean result = graphic.drawImage(image, x, y, null);
        canvas.repaint();
        return result;
    }

    /**
     * Draws a String on the Canvas.
     * @param  text   the String to be displayed 
     * @param  x      x co-ordinate for text placement 
     * @param  y      y co-ordinate for text placement
     */
    public void drawString(String text, int x, int y) {
        graphic.drawString(text, x, y);   
        canvas.repaint();
    }

    /**
     * Erases a String on the Canvas.
     * @param  text     the String to be displayed 
     * @param  x        x co-ordinate for text placement 
     * @param  y        y co-ordinate for text placement
     */
    public void eraseString(String text, int x, int y) {
        Color original = graphic.getColor();
        graphic.setColor(backgroundColor);
        graphic.drawString(text, x, y);   
        graphic.setColor(original);
        canvas.repaint();
    }

    /**
     * Draws a line on the Canvas.
     * @param  x1   x co-ordinate of start of line 
     * @param  y1   y co-ordinate of start of line 
     * @param  x2   x co-ordinate of end of line 
     * @param  y2   y co-ordinate of end of line 
     */
    public void drawLine(int x1, int y1, int x2, int y2) {
        graphic.drawLine(x1, y1, x2, y2);   
        canvas.repaint();
    }

    /**
     * Sets the foreground color of the Canvas.
     * @param  newColor   the new color for the foreground of the Canvas 
     */
    public void setForegroundColor(Color newColor) {
        graphic.setColor(newColor);
    }

    /**
     * Returns the current color of the foreground.
     * @return   the color of the foreground of the Canvas 
     */
    public Color getForegroundColor() {
        return graphic.getColor();
    }

    /**
     * Sets the background color of the Canvas.
     * @param  newColor   the new color for the background of the Canvas 
     */
    public void setBackgroundColor(Color newColor) {
        backgroundColor = newColor;   
        graphic.setBackground(newColor);
    }

    /**
     * Returns the current color of the background
     * @return   the color of the background of the Canvas 
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * changes the current Font used on the Canvas
     * @param  newFont   new font to be used for String output
     */
    public void setFont(Font newFont) {
        graphic.setFont(newFont);
    }

    /**
     * Returns the current font of the canvas.
     * @return     the font currently in use
     **/
    public Font getFont() {
        return graphic.getFont();
    }

    /**
     * Sets the size of the canvas.
     * @param  width    new width 
     * @param  height   new height 
     */
    public void setSize(int width, int height) {
        canvas.setPreferredSize(new Dimension(width, height));
        Image oldImage = canvasImage;
        canvasImage = canvas.createImage(width, height);
        graphic = (Graphics2D)canvasImage.getGraphics();
        graphic.setColor(backgroundColor);
        graphic.fillRect(0, 0, width, height);
        graphic.drawImage(oldImage, 0, 0, null);
        frame.pack();
    }

    /**
     * Returns the size of the canvas.
     * @return     The current dimension of the canvas
     */
    public Dimension getSize() {
        return canvas.getSize();
    }

    /**
     * Waits for a specified number of milliseconds before finishing.
     * This provides an easy way to specify a small delay which can be
     * used when producing animations.
     * @param  milliseconds  the number 
     */
    public void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } 
        catch (InterruptedException e) {
            // ignoring exception at the moment
        }
    }

    /************************************************************************
     * Inner class CanvasPane - the actual canvas component contained in the
     * Canvas frame. This is essentially a JPanel with added capability to
     * refresh the image drawn on it.
     */
    private class CanvasPane extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(canvasImage, 0, 0, null);
        }
    }
    
    /**
     * Draws a room on canvas.
     * direction is where we came from
     */
    public void drawRoom(Room room, boolean newRoom) {
        setForegroundColor(Color.GRAY);
        
        if (newRoom)
            setForegroundColor(room.getColor());
            
        int xpos = room.getXPos();
        int ypos = room.getYPos();
        
        fillRectangle(xpos, ypos, roomWidth, roomHeight);
        setForegroundColor(Color.WHITE);
        drawString(room.getShortDescription(), xpos + roomPadding, ypos + roomHeight - roomPadding);
        
        drawExits(room);
    }
    
    public void drawExits(Room room) {
        setForegroundColor(Color.GRAY);
        HashMap<String, Room> exits = room.getExits();
        
        int xpos = room.getXPos();
        int ypos = room.getYPos();
        
        Set<String> keys = exits.keySet();
        for (String direction : keys) {
            int y = 0, x = 0;
            if (direction.equalsIgnoreCase("west")) {
                x = xpos - roomMargin;
                y = ypos + (roomHeight - roomMargin) / 2;
            } else if (direction.equalsIgnoreCase("north")) {
                x = xpos + (roomWidth - roomMargin) / 2;
                y = ypos - roomMargin;
            } else if (direction.equalsIgnoreCase("east")) {
                x = xpos + roomWidth;
                y = ypos + (roomHeight - roomMargin) / 2;
            } else if (direction.equalsIgnoreCase("south")) {
                x = xpos + (roomWidth - roomMargin) / 2;
                y = ypos + roomHeight;
            }
                
            fillRectangle(x, y, roomMargin, roomMargin);
        }
    }
    
    public void drawAllMap(Room actualRoom, ArrayList<Room> roomsDrawn) {
        drawRoom(actualRoom, false);
        
        if (roomsDrawn.contains(actualRoom))
            return;

        roomsDrawn.add(actualRoom);
        HashMap<String, Room> roomsNearby = actualRoom.getExits();

        for (Map.Entry<String, Room> entry : roomsNearby.entrySet()) {
            String direction = entry.getKey();
            Room room = entry.getValue();
            int x = actualRoom.getXPos();
            int y = actualRoom.getYPos();
            
            if (roomsDrawn.contains(room))
                continue;
            
            if (direction.equalsIgnoreCase("north"))
                y -= roomHeight + roomMargin;
            else if (direction.equalsIgnoreCase("west"))
                x -= roomWidth + roomMargin;
            else if (direction.equalsIgnoreCase("south"))
                y += roomHeight + roomMargin;
            else if (direction.equalsIgnoreCase("east"))
                x += roomWidth + roomMargin;
            
            drawAllMap(room, roomsDrawn);
        }
    }
    
    public void drawItems(Room room) {
        drawRoom(room, true); // Erase earlier items from screen
        HashSet<Item> items = room.getItems(); // Try to look in current room.
        int x = room.getXPos() + Game.ROOM_PADDING;
        int y = room.getYPos() + Game.ROOM_PADDING;

        if (items == null || items.isEmpty()) {
            Output.println("There are no items in this room!");
            return;
        }
        
        for (Item item : items) {
            Output.println(item.getDescription());

            item.draw(this, x, y, Game.OBJECT_WIDTH);
            x += Game.OBJECT_WIDTH + Game.ROOM_PADDING;

            // Wrap the line of items if not space enough
            if (x > room.getXPos() + Game.ROOM_WIDTH - Game.ROOM_PADDING - Game.OBJECT_WIDTH) {
                x = room.getXPos() + Game.ROOM_PADDING;
                y += Game.ROOM_PADDING + Game.OBJECT_WIDTH;
            }
        }
    }
}