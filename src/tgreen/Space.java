/* Name:       Thomas Green
    Student ID: 1048389
    Email:        tgreen12@uoguelph.ca */

package tgreen;
import java.util.ArrayList;
import java.io.Serializable;


public abstract class Space implements java.io.Serializable  {

    /**
     * Gets the description of this space.
     * @return the space's description.
     */
    public abstract  String getDescription();


    /**
     * Sets a door to this space.
     * @param theDoor the door to set.
     */
    public abstract void setDoor(Door theDoor);

    /**
     * Gets the number representing this space.
     * @return the number representing this space.
     */
    public abstract String getSpaceNum();


    public abstract ArrayList<Door> getDoors();

}
