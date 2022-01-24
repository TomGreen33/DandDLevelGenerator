/* Name:       Thomas Green
    Student ID: 1048389
    Email:        tgreen12@uoguelph.ca */

package tgreen;
import dnd.models.Exit;
import dnd.models.Trap;
import java.util.ArrayList;
import java.io.Serializable;


public class Door implements java.io.Serializable {

    /* Instance Variables */

    /**
     * Asserts whether this door is trapped.
     */
    private boolean isTrappedVar;

    /**
     * Asserts whether this door is open.
     */
    private boolean isOpenVar;

    /**
     * Asserts whether this door is locked.
     */
    private boolean isLockedVar;

    /**
     * Asserts whether this door is an archway.
     */
    private boolean isArchwayVar;

    /**
     * Asserts whether this door is inside a passage.
     */
    private boolean isPassageDoor = false; //Used to print passage doors

    /**
     * The loctation of this door.
     */
    private String location;

    /**
     * The dirrections to this door.
     */
    private String direction;

    /**
     * The roll (1-20) for the trap of this door.
     */
    private int trapSelection;

    /**
     * The roll (1-20) for a given aspect about this door.
     */
    private int chance;

    /**
     * The trap of this door.
     */
    private Trap trap;

    /**
     * The spaces connected to this door.
     */
    private ArrayList<Space> spaces = new ArrayList<Space>();


    /* Constructors */

    /**
     * The constructor for this door when no paramters are given.
     */
    public Door() {

        spaces = new ArrayList<Space>();
        init();

    }


    /**
     * The constructor for this door when parameters are given.
     * @param theExit the exit describing this door.
     */
    public Door(Exit theExit) {
        //sets up the door based on the Exit from the tables
        init();
        location = theExit.getLocation();
        direction = theExit.getDirection();
    }


    /* Methods */

    /**
     * Adds a trap to this door if specified to do so.
     * Randomly selects a trap when provided with no roll.
     * @param flag The existence of a trap in this door.
     * @param roll The roll used to generate the trap (if provided).
     */
    public void setTrapped(boolean flag, int... roll) {

        isTrappedVar = flag;

        /* If no argument for roll was passed, randomly generate the trap selection */
        if (roll.length == 0) {
            trapSelection = getSelection(1, 20);
        } else {
            trapSelection = roll[0];
        }

        if (isTrappedVar) {
            trap = new Trap();
            trap.setDescription(trapSelection);
        }
    }


    /**
     * Sets this door to open (provided that flag is true).
     * @param flag Asserts whether this door is open.
     */
    public void setOpen(boolean flag) {

        if (!isArchwayVar) {
            isOpenVar = flag;
        }
    }


    /**
     * Sets this door to an archway (provided that flag is true).
     * @param flag Asserts whether this door is an archway.
     */
    public void setArchway(boolean flag) {
        isArchwayVar = flag;
        isOpenVar = true;
        isLockedVar = false;
    }


    /**
     * Asserts whether this door is trapped.
     * @return whether this door is trapped.
     */
    public boolean isTrapped() {

        return isTrappedVar;
    }


    /**
     * Asserts whether this door is open.
     * @return whether this door is open.
     */
    public boolean isOpen() {

        return isOpenVar;
    }


    /**
     * Asserts whether this door is an archway.
     * @return whether this door is an archway.
     */
    public boolean isArchway() {

        return isArchwayVar;
    }


    /**
     * Gets the description of thsi door.
     * @return the description of this door.
     */
    public String getTrapDescription() {
        return trap.getDescription();
    }


    /**
     * Gets the spaces attatched to this door.
     * @return the spaces that are attatched to this door.
     */
    public ArrayList<Space> getSpaces() {
        return spaces;
    }


    /**
     * Sets the spaces attatched to this door.
     * @param spaceOne The first space attatched to this door.
     * @param spaceTwo The second space attatched to this door.
     */
    public void setSpaces(Space spaceOne, Space spaceTwo) {

        spaces.add(spaceOne);
        spaces.add(spaceTwo);
        spaces.get(0).setDoor(this);
        spaces.get(1).setDoor(this);
    }


    /**
     * Gets the description of this door.
     * @return the description of this door.
     */
    public String getDescription() {

    String description;

        /* Passage door descriptions and exit door descriptions differ in the sense that
           chamber exits contain locations and dirrections, passage doors do not */
        if (isPassageDoor) {
            description = String.join(" ",  "istrapped:", String.valueOf(isTrappedVar), "| isLocked:",
            String.valueOf(isLockedVar), "| isArchway:", String.valueOf(isArchwayVar));
        } else {
            description = String.join(" ",  "Location:", location, "| direction:", direction, "| istrapped:",
            String.valueOf(isTrappedVar), "| isLocked:", String.valueOf(isLockedVar), "| isArchway:", String.valueOf(isArchwayVar));
        }

        return description;
    }


    /* Personal Methods */

    /**
    * Returns a random int between min and max (same functionality as a die).
    * @param min the minimum value that the random value can be.
    * @param max the maximum value that the random value can be.
    * @return the random number.
    */
    static int getSelection(int min, int max) {

        int number;

        number = (int) (Math.random() * ((max - min) + min) + min);

        return number;
    }


    /**
     * Initiates the instance variables of this door (called by constructors).
     */
    private void init() {

        initIsTrapped();

        initIsLocked();

        initIsArchway();
    }


    /**
     * Initially sets the isTrapped variable for a given Door.
     */
    private void initIsTrapped() {

        int roll;

        /* 1/20 chance it will be a trapped door */
        roll = getSelection(1, 20);

        if (roll == 1) {
            isTrappedVar = true;
        } else {
            isTrappedVar = false;
        }
    }

    /**
     * Initially sets the isTrapped variable for a given Door.
     */
    private void initIsLocked() {

        int roll;

        /* 1/6 chance it will be locked */
        roll = getSelection(1, 6);

        if  (roll == 1) {
            isLockedVar = true;
        } else {
            isLockedVar = false;
        }
    }


    /**
     * Initially sets the isTrapped variable for a given Door.
     */
    private void initIsArchway() {

        int roll;

        /* 1/20 chance it will be an archway */
        roll = getSelection(1, 10);

        if (roll == 1) {
            isArchwayVar = true;
            isOpenVar = true;
            isLockedVar = false;
        } else {
            isArchwayVar = false;
        }
    }


    /**
     * Gets the specified space attatched to this door.
     * @param i the space to return.
     * @return the specified space attatched to this door.
     */
    public Space getSpace(int i) {
        return spaces.get(i);
    }

    /**
     * Sets this door as a passage door given if the flag is true.
     * @param flag Deciffers whether this door is a passage door.
     */
    public void setIsPassageDoor(boolean flag) {

        isPassageDoor = flag;
    }

}
