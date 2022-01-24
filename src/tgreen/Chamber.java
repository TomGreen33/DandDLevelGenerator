/* Name:       Thomas Green
    Student ID: 1048389
    Email:        tgreen12@uoguelph.ca */

package tgreen;

import dnd.models.ChamberContents;
import dnd.models.ChamberShape;
import dnd.models.Monster;
import dnd.models.Treasure;
import dnd.models.Exit;
import java.util.ArrayList;
import java.io.Serializable;


public class Chamber extends Space implements java.io.Serializable {


    /* Instance Variables */

    /**
     * The contents of this chamber.
     */
    private ChamberContents myContents;

    /**
     * The shape of this chamber.
     */
    private ChamberShape mySize;

    /**
     * The treasure of this chamber.
     */
    private Treasure treasure;

    /**
     * The monster of this chamber.
     */
    private Monster monster;

    /**
     * The entrance door of this chamber.
     */
    private Door entranceDoor;

    /**
     * The Door that leads to the next passage to be printed.
     */
    private Door exitDoor;

    /**
     * The next passage to be printed.
     */
    private Passage exitPassage;

    /**
     * The doors of this chamber.
     */
    private ArrayList<Door> doors = new ArrayList<Door>();

    /**
     * The exits of this chamber.
     */
    private ArrayList<Exit> exits = new ArrayList<Exit>();

    /**
     * The monsters in this chamber.
     */
    private ArrayList<Monster> monsters = new ArrayList<Monster>();

    /**
     * The treasures in this chamber.
     */
    private ArrayList<Treasure> treasures = new ArrayList<Treasure>();

    /**
     * The passages that this chamber leads to.
     */
    private ArrayList<Passage> passages = new ArrayList<Passage>();

    /**
     * The roll (1-20) for the contents of this chamber.
     */
    private int contentsSelection;

    /**
     * The roll (1-20) for the shape of this chamber.
     */
    private int shapeSelection;

    /**
     * The roll (1-20) for a given treasure in this chamber.
     */
    private int treasureSelection;

    /**
     * The roll (1-20) for a given monster in this chamber.
     */
    private int monsterSelection;

    /**
     * The roll (1-20) for the number of exits in this chamber.
     */
    private int exitSelection;

    /**
     * The roll (1-20) for the container of a given treasure.
     */
    private int containerSelection;

    /**
     * The number of passages that the dungeon previously had.
     */
    private int numPaths;

    /**
     * The number of passages this chamber introduces to the dungeon.
     */
    private int numNewPaths;

    /**
     * The number of exits in this chamber.
     */
    private int exitNum;

    /**
     * The number representing a given passage.
     */
    private int pathNum;

    /**
     * Asserts whether this chamber has a door that is connected to a valid passage.
     */
    private boolean isExitSet = false;


    /**
     * The number used to identifie this chamber.
     */
    private int chamberNum;


    /* Constructors */

    /**
     * The constructor for this chamber when no parameters are provided.
     */
    public Chamber() {


        myContents = new ChamberContents();

        this.contentsSelection = getSelection(1, 20);
        this.shapeSelection = getSelection(1, 20);

        mySize = ChamberShape.selectChamberShape(shapeSelection);
        myContents.chooseContents(contentsSelection);

        initChamber();
    }


    /**
     * The constructor for this Chamber when parameters are provided.
     * @param theShape This chamber's shape.
     * @param theContents This chamber's contents.
     */
    public Chamber(ChamberShape theShape, ChamberContents theContents) {

        mySize = theShape;
        myContents = theContents;

        initChamber();
    }


    /* Methods */

    /**
     * Sets a shape for this chamber.
     * @param theShape This chamber's new shape.
     */
    public void setShape(ChamberShape theShape) {

        mySize = theShape;

        exitSelection = getSelection(1, 20);
        mySize.setNumExits(exitSelection);
    }


    /**
     * Gets the arraylist of doors for this chamber.
     * @return the doors in this chamber.
     */
    public ArrayList<Door> getDoors() {

        return doors;
    }


    /**
     * Adds a monster to the arrayList of monsters for this chamber.
     * @param theMonster The monster to be added.
     */
    public void addMonster(Monster theMonster) {

        monsters.add(theMonster);
    }


    /**
     * Gets the arrayList containing the monsters in this chamber.
     * @return the monsters in this chamber.
     */
    public ArrayList<Monster> getMonsters() {

        return monsters;
    }


    /**
     * Adds a treasure to this chamber.
     * @param theTreasure The treasure to be added.
     */
    public void addTreasure(Treasure theTreasure) {

        treasures.add(theTreasure);
    }


    /**
     * Gets the arrayList containing the treasures in this chamber.
     * @return the treasures in this chamber.
     */
    public ArrayList<Treasure> getTreasureList() {

        return treasures;
    }


    /**
     * Gets the description of this chamber.
     * @return the description of this chamber.
     */
    @Override
    public String getDescription() {

        String description;

        description = String.join(" ", "Chamber size:",
                mySize.getShape(), "| Chamber Contents:", myContents.getDescription(),
                getTreasureListString(), getMonsterListString(), getExitsListString());

        return description;
    }


    /**
     * Sets the door that exits this chamber.
     * @param newDoor the door that exits the chamber.
     */
    @Override
    public void setDoor(Door newDoor) {

        entranceDoor = newDoor;
    }


    /* Personal Methods */


    /**
     * Called by constructors (common between the two).
     */
    public void initChamber() {

        initNumExits();

        initTreasure();

        initMonster();

        setDoors();
    }


    /**
     * Initially sets the number of exits for a given chamber.
     */
    private void initNumExits() {

        exitSelection = getSelection(1, 20);
        mySize.setNumExits(exitSelection);
        numNewPaths = mySize.getNumExits(); //Used for numbering passages

        /* If chamber has no exits, reset its number of exits until it has atleast 1 */
        if (mySize.getNumExits() < 1) {
            while (mySize.getNumExits() < 1) {
                exitSelection = getSelection(1, 20);
                mySize.setNumExits(exitSelection);
                numNewPaths = mySize.getNumExits();
            }
        }
    }


    /**
     * Initially sets the treasure and treasure container for a given chamber.
     */
    private void initTreasure() {

        /* In the case of treasure */
        if (contentsSelection == 15 || contentsSelection == 16
                || contentsSelection == 17 || contentsSelection == 20) {

            treasure = new Treasure();

            treasureSelection = getSelection(1, 20);
            treasure.setDescription(treasureSelection);

            containerSelection = getSelection(1, 20);
            treasure.setContainer(containerSelection);

            addTreasure(treasure);
        }
    }


    /**
     * Initially sets the monster for a given chamber.
     */
    private void initMonster() {

        /* In the case that chamber contents allows for monster */
        if (contentsSelection >= 13 && contentsSelection <= 17) {

            monster = new Monster();

            monsterSelection = getSelection(1, 100);
            monster.setType(monsterSelection);

            addMonster(monster);
        }
    }


    /**
    * Sets up a door/passage for each exit. First exit that leads
    * to a passage attatched to a chamer gets stored as exitDoor.
    */
    public void setDoors() {

        int numExits;
        int i;
        Passage curPassage;
        Passage forceEnd;
        Exit forceEndExit;
        Door forceEndDoor;

        numExits = mySize.getNumExits();

        for (i = 0; i < numExits; i++) {

            /* Create a door/passage for current exit. Set spaces */
            exits.add(new Exit());
            doors.add(new Door(exits.get(i)));
            passages.add(new Passage());
            doors.get(i).setSpaces(this, passages.get(i));

            /* If the last exit still dosen't lead to a chamber, replace the
               Passage that the door is attatched to to one that does */
            if ((i == numExits - 1) && !isExitSet) {

                forceEnd = new Passage();
                forceEndExit = new Exit();
                forceEndDoor = new Door(forceEndExit);

                forceEndDoor.setSpaces(this, forceEnd);

                forceEnd.generatePassage(true);

                exitDoor = forceEndDoor;

                isExitSet = true;
                exitNum = i + 1;
            }

            /* If cuurent exit leads to another chamber, store it as exitDoor */
            if ((passages.get(i).generatePassage(false) != null) && !isExitSet) {
                doors.get(i).setSpaces(this, passages.get(i));
                exitDoor = doors.get(i);
                isExitSet = true;
                exitNum = i + 1;
            }

        }
    }


    /**
     * Returns a random int between min and max (same functionality as a die).
     * @param min the minimum value that the random value can be.
     * @param max the maximum value that the random value can be.
     * @return the random number.
     */
    private int getSelection(int min, int max) {

        int number;

        number = (int) (Math.random() * ((max - min) + min) + min);

        return number;
    }


    /**
     * Takes treasure list array and turns it into a string to be printed.
     * @return the description of the treasures in this chamber.
     */
    private String getTreasureListString() {

        String treasureListString = "";
        String treasureString;
        String containerString;
        Treasure t;
        int i;

        for (i = 0; i < treasures.size(); i++) {

            t = treasures.get(i);

            treasureString = t.getDescription();
            containerString = t.getContainer();

            treasureListString = treasureListString.concat(
                "\n    Chamber Treasure: " + treasureString + " | Contained in: " + containerString);
        }
        return treasureListString;
    }


    /**
     * Returns the string describing any monsters in the chamber.
     * Used by method getDescription.
     * @return the description of the monsters in this chamber.
     */
    private String getMonsterListString() {

        String monsterListString = "";
        String monsterString;
        Monster m;
        int i;

        for (i = 0; i < monsters.size(); i++) {
            m = monsters.get(i);
            monsterString = m.getDescription();

            monsterListString = monsterListString.concat("\n    Chamber Monster: ");
            monsterListString = monsterListString.concat(monsterString);
        }

        return monsterListString;
    }


    /**
     * Returns the string describing each door (exit) in the room.
     * @return the description of the doors (exits) in this chamber.
     */
    private String getExitsListString() {

        int numExits;
        int i;
        String exitsListString;

        exitsListString = "";
        numExits = mySize.getNumExits();

        for (i = 0; i < numExits; i++) {

            exitsListString = exitsListString.concat("\n    Exit " + (i + 1) + ": "
            + doors.get(i).getDescription() + "\n            Leads to: Passage " + (numPaths + (i + 1)));
        }

        exitsListString = exitsListString.concat("\n");

        return exitsListString;
    }


    /**
     * Gets the number of current passages. Uses that to store
     * the passage number of the passage to be printed.
     * @param currentNumPaths the current number of paths.
     */
    public void getNumPaths(int currentNumPaths) {

        numPaths = currentNumPaths;
        pathNum = exitNum + currentNumPaths;
    }


    /**
     * Gets the chamber's exit door.
     * @return the exiting door.
     */
    public Door getExitDoor() {

            return exitDoor;
        }


    /**
     * Gets the number of new passages (dictated by num of exits).
     * @return the number of new passages.
     */
    public int getNumNewPaths() {

        return numNewPaths;
    }

    /**
     * Gets the number representing the chamber's exiting door.
     * @return the exiting door's number.
     */
    public int getExitNum() {

        return exitNum;
    }


    /**
     * Gets the chamber's exit door.
     * @return the exiting door.
     */
    public int getPathNum() {

        return pathNum;
    }


    /**
     * Sets the number used to identifie this chamber.
     */
    public void setChamberNum(int num) {

      chamberNum = num;
    }


    /**
     * Gets the number used to identifie this chamber.
     * @return the number.
     */
    @Override
    public String getSpaceNum() {

      String toReturn;

      toReturn = "Chamber " + chamberNum;

      return toReturn;
    }


    public void removeTreasure(int index) {

      treasures.remove(index);
    }


    public void removeMonster(int index) {

      monsters.remove(index);
    }

}
