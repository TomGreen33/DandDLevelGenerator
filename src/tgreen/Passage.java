/* Name:       Thomas Green
    Student ID: 1048389
    Email:        tgreen12@uoguelph.ca */


package tgreen;


import dnd.models.Monster;
import dnd.models.Treasure;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;



public class Passage extends Space implements java.io.Serializable {

    /* Instance Variables */

    /**
     * The passage sections in this passage.
     */
    private ArrayList<PassageSection> thePassage;

    /**
     * The doors inside this passage's passage sections.
     */
    private ArrayList<Door> sectionDoors = new ArrayList<Door>();

    /**
     * The doors that enter and exit this passage.
     */
    private ArrayList<Door> mainDoors = new ArrayList<Door>();

    /**
     * The doors of this passage and their assossiated passage section.
     */
    private HashMap<Door, PassageSection> doorMap = new HashMap<Door, PassageSection>();


    /**
     * The door that enters this passage.
     */
    private Door entranceDoor;

    /**
     * The door that exits this passage.
     */
    private Door exitDoor;


    /**
     * The number used to identifie this passage.
     */
    private int passageNum;


    /* Constructor */

    /**
     * The constructor for this passage when no paramters are given.
     */
    public Passage() {

        thePassage = new ArrayList<PassageSection>();
    }

    /* Methods */

    /**
     * Gets the door from a specified passage section.
     * @param i The passage section from which the desired door can be found.
     * @return the door.
     */
    public Door getDoor(int i) {

        Door door;

        door = sectionDoors.get(i);

        return door;
    }


    /**
     * Gets the doors in this passage.
     * @return the doors in this passage.
     */
    public ArrayList<Door> getDoors() {

        return mainDoors;
    }


    /**
     * Adds a monster to a specified passage section.
     * @param theMonster The monster to add.
     * @param i the passage section in which the monster can be added to.
     */
    public void addMonster(Monster theMonster, int i) {

        thePassage.get(i).setMonster(theMonster);

    }


    /**
    * Gets the monster from a specified passage section.
    * @param i The passage section from which the desired monster can be found.
    * @return the monster.
     */
    public Monster getMonster(int i) {

        Monster monster;
        monster = thePassage.get(i).getMonster();

        return monster;
    }


    /**
     * Adds a passage section to this passage.
     * @param toAdd The passage section to add.
     */
    public void addPassageSection(PassageSection toAdd) {

        thePassage.add(toAdd);

    }


    /**
     * Sets the entrance/exiting door of this passage.
     * @param newDoor the exiting/enterting door.
     */
    @Override
    public void setDoor(Door newDoor) {

        mainDoors.add(newDoor);
    }


    /**
     * Gets the description of this passage.
     * @return the description of this passage.
     */
    @Override
    public String getDescription() {

        int section;
        String description = "";

        for (section = 0; section < thePassage.size(); section++) {
            description = description.concat("    ");
            description = description.concat(thePassage.get(section).getDescription());
            description = description.concat("\n");
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
     * Generates a passage. If the passage leads to a door attatched to a chamber,
     * the door is returned. If it does not, function returns null.
     * @param forceEnd Asserts whether the passage must lead to a chamber.
     * @return the exiting door of this passage (provided there is one).
      */
    public Door generatePassage(boolean forceEnd) {

        int numSections;
        int section;
        int curRoll;
        String sectionDescription;
        Boolean isArchway;

        PassageSection curSection;
        PassageSection forcedPassageEnd;
        Door door;
        Door endDoor;
        Monster monster;

        section = 0;

        while ((section < 10 && !forceEnd) || forceEnd) {

            /* Forces the end of a passage into a chamber */
            if (section == 9 && forceEnd) {
                forcedPassageEnd = new PassageSection();
                forcedPassageEnd.initPassageSection(4);
                thePassage.add(forcedPassageEnd);
                curRoll = thePassage.get(section).getRoll();
            } else { /* Adds a section to the passage */
                curSection = new PassageSection();
                thePassage.add(curSection);
                curRoll = thePassage.get(section).getRoll();
            }

            /* If section is a dead end, but function is required to force end into a chamber,
               override with a section that ends with a door to a chamber */
            if ((curRoll ==  18 || curRoll ==  19) && forceEnd) {

                forcedPassageEnd = new PassageSection();
                forcedPassageEnd.initPassageSection(4);
                thePassage.remove(section);
                thePassage.add(forcedPassageEnd);
                curRoll = thePassage.get(section).getRoll();

            }

            /* If the section contains a door */
            if ((curRoll >= 3 && curRoll <= 9) || (curRoll >= 14 && curRoll <= 16)) {

                door = new Door();

                /* If it is an archway */
                if ((curRoll >= 6 && curRoll <= 9) || (curRoll >= 14 && curRoll <= 16)) {
                    isArchway = true;
                } else {
                    isArchway = false;
                }

                door.setArchway(isArchway);
                doorMap.put(door, thePassage.get(section));

                /* If that door leads to a chamber */
                if ((curRoll >= 3 && curRoll <= 5) || (curRoll >= 14 && curRoll <= 16)) {

                    endDoor = door;
                    exitDoor = door;
                    return endDoor;
                }
            }

            /* If it is a deadend */
            if (curRoll ==  18 || curRoll ==  19) {
                return null;
            }

            section++;
        }

        return null;
    }


    /**
     * Gets the exit door of this passage.
     * @return the exit door.
     */
    public Door getExitDoor() {

            return exitDoor;
    }


    /**
         * Sets the number used to identifie this passage.
         */
        public void setPassageNum(int num) {

          passageNum = num;
        }


    /**
     * Gets the number used to identifie this passage.
     * @return the number.
     */
    @Override
    public String getSpaceNum() {

      String toReturn;

      toReturn = "Passage " + passageNum;

      return toReturn;
    }


    public void addTreasure(Treasure theTreasure, int i) {

        thePassage.get(i).setTreasure(theTreasure);
    }


    public void removeTreasure(int section, int index) {

      thePassage.get(section).removeTreasure(index);
    }


    public void removeMonster(int section, int index) {

      thePassage.get(section).removeMonster(index);
    }


}
