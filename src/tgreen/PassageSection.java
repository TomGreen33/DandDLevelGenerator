/* Name:       Thomas Green
    Student ID: 1048389
    Email:        tgreen12@uoguelph.ca */

package tgreen;

import dnd.models.Monster;
import dnd.models.Treasure;
import java.util.ArrayList;
import java.io.Serializable;



public class PassageSection implements java.io.Serializable {

    /* Instance Variables */

    /**
     * The roll (1-20) for a given aspect about this passage section..
     */
    private int roll;

    /**
     * The roll (1-20) for a given monster in this passage section.
     */
    private int monsterSelection;

    /**
     * The roll (1-20) for a given treasure in this passage section.
     */
    private int treasureSelection;

    /**
     * The description of this passage section.
     */
    private String description;

    /**
     * Asserts whether there is a monster in this passage section.
     */
    private boolean isMonster = false;

    /**
     * Asserts whether there is a door in this passage section.
     */
    private boolean isDoor = false;

    /**
     * Asserts whether the door in this passage section is an archway (provided there is one).
     */
    private boolean isArchway = false;


    /**
     * The monster in this passage section.
     */
    private Monster monster;


    /**
     * The treasures in this passage section.
     */
    private ArrayList<Monster> monsters = new ArrayList<Monster>();


    /**
     * The treasures in this passage section.
     */
    private ArrayList<Treasure> treasures = new ArrayList<Treasure>();


    /**
     * The door in this passage section.
     */
    private Door door;


    /* Constructor */

    /**
     * The constructor for this passage section when no paramters are porovided.
     */
    public PassageSection() {

        initPassageSection();
    }


    /* Methods */

    /**
     * The constructor for this passage section when paramters are provided.
     * @param newDescription This passage section's descriptioon.
     */
    public PassageSection(String newDescription) {

        description = newDescription;
    }


    /**
     * Gets the door in this passage section (provided there is one).
     * @return the door in this passage section.
     */
    public Door getDoor() {

        if (isDoor) {
            return door;
        } else {
            return null;
        }
    }


    /**
     * Gets the monster in this passage section (provided there is one).
     * @return the monster in this passage section.
     */
    public Monster getMonster() {

        if (isMonster) {
            return monster;
        } else {
            return null;
        }
    }


    /**
     * Gets the description of this passage section.
     * @return the description of this passage section.
     */
    public String getDescription() {

        return description;
    }


    /* Personal Methods */

    /**
     * Sets the description of this passage section according to table in A2 description.
     * Randomly selects a description when no selection is provided.
     * @param selection the roll used to generate the passage section (if provided).
     */
    public void initPassageSection(int... selection) {

        Monster initMonster;
        Door initDoor;

        /* Roll is used to determine the description/contents of the passage Section.
           One can either be provided for the function, else a random one is generated */

        if (selection.length == 0) {
            roll = getSelection(1, 20);
        } else {
            roll = selection[0];
        }

        switch (roll) {
            case 1: case 2:
                description = "passage goes straight for 10 ft"; break;

            case 3: case 4: case 5:
                description = "passage ends in Door to a Chamber"; isDoor = true; break;

            case 6: case 7:
                description = "archway (door) to right (main passage continues straight for 10 ft)"; isDoor = true; isArchway = true; break;

            case 8: case 9:
                description = "archway (door) to left (main passage continues straight for 10 ft)"; isDoor = true; isArchway = true; break;

            case 10: case 11:
                description = "passage turns to left and continues for 10 ft"; break;

            case 12: case 13:
                description = "passage turns to right and continues for 10 ft"; break;

            case 14: case 15: case 16:
                description = "passage ends in archway (door) to chamber"; isDoor = true; isArchway = true; break;

            case 17:
                description = "Stairs, (passage continues straight for 10 ft)"; break;

            case 18: case 19:
                description = "Dead End"; break;

            case 20:
                description = "Wandering Monster (passage continues straight for 10 ft)";
                isMonster = true;
                setMonster();
                for (Monster m: monsters) {
                  description = description.concat("\n        Monster: ");
                  description = description.concat(m.getDescription());
                }
                break;
            default:
                description = "Error in passage section"; break;

        }

        /* If section contains a door, generate one given the instance
           variable provided for it and add it to the section's description */
        if ((roll >= 3 && roll <= 9) || (roll >= 14 && roll <= 16)) {

            setDoor();
            initDoor = getDoor();

            if (isDoor) {

                description = description.concat("\n        Door: ");
                initDoor.setIsPassageDoor(true);
                description = description.concat(initDoor.getDescription());

            }
        }
    }


    /**
     * Sets the type of monster in this passage section.
     * If no monster is passed, generates a random one.
     * @param monsterToAdd The monster to add (if provided).
     */
    public void setMonster(Monster... monsterToAdd) {

      Monster monster;

      isMonster = true;
        if (monsterToAdd.length == 0) {
            monsterSelection = getSelection(1, 100);
            monster = new Monster();
            monster.setType(monsterSelection);
            monsters.add(monster);
        } else {
            monster = monsterToAdd[0];
            monsters.add(monster);
        }
        description = description.concat("\n        Monster: ");
        description = description.concat(monster.getDescription());
    }


    /**
     * Sets up the door in this passage section, provided there is one.
     */
    private void setDoor() {

        if (isDoor) {

            door = new Door();
            door.setArchway(isArchway);
        }
    }


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
     * Gets the roll for this passage section.
     * @return this passage section's roll.
     */
    public int getRoll() {

        return roll;
    }


    /**
     * Sets the type of treasure in this passage section.
     * If no treasure is passed, generates a random one.
     * @param treasureToAdd The treasure to add (if provided).
     */
    public void setTreasure(Treasure... treasureToAdd) {

      Treasure treasure;
        if (treasureToAdd.length == 0) {
            treasureSelection = getSelection(1, 100);
            treasure = new Treasure();
            treasure.setDescription(treasureSelection);
            treasures.add(treasure);
        } else {
            treasures.add(treasureToAdd[0]);
            treasure = treasureToAdd[0];
        }
        description = description.concat("\n        Treasure: ");
        description = description.concat(treasure.getDescription());
    }


    public void removeTreasure(int index) {

      treasures.remove(index);
      initPassageSection(roll);

      for (Treasure t: treasures) {
        description = description.concat("\n        Treasure: ");
        description = description.concat(t.getDescription());
      }
    }


    public void removeMonster(int index) {

      monsters.remove(index);
      initPassageSection(roll);

      for (Monster m: monsters) {
        description = description.concat("\n        Monster: ");
        description = description.concat(m.getDescription());
      }
    }

}
