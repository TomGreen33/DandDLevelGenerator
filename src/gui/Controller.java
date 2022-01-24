/* Name:       Thomas Green
 * Student ID: 1048389
 * Email:      tgreen12@uoguelph.ca
 */

package gui;

import tgreen.*;
import java.io.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.scene.control.ListView;
import javafx.stage.Popup;
import javafx.scene.control.TextArea;
import dnd.models.Monster;
import dnd.models.Treasure;


public class Controller {

    /**
     * The gui.
     */
    private Gui myGui;


    /**
     * The Dungeon Generator.
     */
    private DungeonGenerator dungeonGenerator;


    /**
     * The arraylist of spaces.
     */
    private ArrayList<Space> spaces;


    /**
     * The file chooser.
     */
    private FileChooser fileChooser;


    /**
     * The contructor for this controller.
     * @param the gui.
     */
    public Controller(Gui theGui){

        myGui = theGui;
        dungeonGenerator = new DungeonGenerator();
        spaces = dungeonGenerator.getSpacesList();
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Serialization Files", "*.ser"));
    }


    /**
     * populates the spaces list by accessing it from the main application.
     * @param the listview.
     */
    public void populateSpacesList(ListView listView) {

      for(Space s: spaces) {
        listView.getItems().add(s.getSpaceNum());
      }
    }


    /**
     * Populates the doors list by identifying the selected space and adding it's doors.
     * @param spacesList the list of spaces.
     * @param doorsList the list of doors.
     */
    public void populateDoorsList(ListView spacesList, ListView doorsList) {

      int i = 1;

      for(Space s: spaces) {
        if (("[" + s.getSpaceNum() + "]").equals(spacesList.getSelectionModel().getSelectedItems().toString())) {
          doorsList.getItems().clear();
          for (Door d: s.getDoors()) {
            doorsList.getItems().add("Door " + i);
            i++;
          }
        }
      }
    }


    /**
     * Gets the description of the currently selected door.
     * @param spacesList the list of spaces.
     * @param doorsList the list of doors.
     */
    public void getDoorDescription(ListView spacesList, ListView doorsList) {

      int i;

      for(Space s: spaces) {
        i = 1;
        if (("[" + s.getSpaceNum() + "]").equals(spacesList.getSelectionModel().getSelectedItems().toString())) {
          for (Door d: s.getDoors()) {
            if (("[" + "Door " + i + "]").equals(doorsList.getSelectionModel().getSelectedItems().toString())) {
              TextArea textA = new TextArea(d.getDescription());
              myGui.populatePopUp(d.getDescription());
              break;
            }
            i++;
          }
        }
      }
    }


    /**
     * Gets the description of the currently selected space.
     * @param listView the list of spaces.
     */
    public void showSpaceDescripion(ListView listView){

      for(Space s: spaces) {

        if (("[" + s.getSpaceNum() + "]").equals(listView.getSelectionModel().getSelectedItems().toString())) {

          myGui.populateDescriptionArea(s.getDescription());
        }
      }
    }


    /**
     * Adds a monster to a space.
     * @param monsterToAdd the monster to add.
     * @param passageSection the passageSection to add the monster to (if needed).
     * @param spacesList the list of spaces.
     */
    public void addMonster(String monsterToAdd, String passageSection, ListView spacesList) {

      boolean isChamber;
      Chamber chamber;
      Passage passage;
      Monster monster = new Monster();
      int monsterNum;
      int sectionNum;

      try {
        monsterNum = Integer.parseInt(monsterToAdd);
      } catch (NumberFormatException nfe) {
        monsterNum = -1;
      }

      try {
        sectionNum = Integer.parseInt(passageSection);
      } catch (NumberFormatException nfe) {
        sectionNum = -1;
      }


      if (monsterNum >= 0) {
        monster.setType(monsterNum);

        for(Space s: spaces) {

          if (("[" + s.getSpaceNum() + "]").equals(spacesList.getSelectionModel().getSelectedItems().toString())) {
            isChamber = s instanceof Chamber;

            if (isChamber) {
              chamber = (Chamber) s;
              chamber.addMonster(monster);
            } else {
              if (sectionNum >= 0) {
                passage = (Passage) s;
                passage.addMonster(monster, sectionNum);
              }
            }
            myGui.populateDescriptionArea(s.getDescription());
          }
        }
      }
    }


    /**
     * Removes a monster from a space.
     * @param monsterToAdd the index of the monster to remove.
     * @param passageSection the passageSection to remove the monster from (if needed).
     * @param spacesList the list of spaces.
     */
    public void removeMonster(String monsterToRemove, String passageSection, ListView spacesList) {

      boolean isChamber;
      Chamber chamber;
      Passage passage;
      int monsterIndex;
      int sectionNum;

      try {
        monsterIndex = Integer.parseInt(monsterToRemove);
      } catch (NumberFormatException nfe) {
        monsterIndex = -1;
      }

      try {
        sectionNum = Integer.parseInt(passageSection);
      } catch (NumberFormatException nfe) {
        sectionNum = -1;
      }


      if (monsterIndex >= 0) {


        for(Space s: spaces) {

          if (("[" + s.getSpaceNum() + "]").equals(spacesList.getSelectionModel().getSelectedItems().toString())) {
            isChamber = s instanceof Chamber;

            if (isChamber) {
              chamber = (Chamber) s;
              chamber.removeMonster(monsterIndex);
            } else {
              if (sectionNum >= 0) {
                passage = (Passage) s;
                passage.removeMonster(sectionNum, monsterIndex);
              }
            }
            myGui.populateDescriptionArea(s.getDescription());
          }
        }
      }
    }


    /**
     * Adds a treasure to a space.
     * @param treasureToAdd the treasure to add.
     * @param passageSection the passageSection to add the treasure to (if needed).
     * @param spacesList the list of spaces.
     */
    public void addTreasure(String treasureToAdd, String passageSection, ListView spacesList) {

      boolean isChamber;
      Chamber chamber;
      Passage passage;
      Treasure treasure;
      int treasureNum;
      int sectionNum;

      try {
        treasureNum = Integer.parseInt(treasureToAdd);
      } catch (NumberFormatException nfe) {
        treasureNum = -1;
      }

      try {
        sectionNum = Integer.parseInt(passageSection);
      } catch (NumberFormatException nfe) {
        sectionNum = -1;
      }


      if (treasureNum >= 0) {
        treasure = new Treasure();
        treasure.setDescription(treasureNum);

        for(Space s: spaces) {

          if (("[" + s.getSpaceNum() + "]").equals(spacesList.getSelectionModel().getSelectedItems().toString())) {
            isChamber = s instanceof Chamber;

            if (isChamber) {
              chamber = (Chamber) s;
              chamber.addTreasure(treasure);
            } else {
              if (sectionNum >= 0) {
                passage = (Passage) s;
                passage.addTreasure(treasure, sectionNum);
              }

            }
            myGui.populateDescriptionArea(s.getDescription());
          }
        }
      }

    }


    /**
     * Removes a treasure from a space.
     * @param treasureToAdd the index of the treasure to remove.
     * @param passageSection the passageSection to remove the treasure from (if needed).
     * @param spacesList the list of spaces.
     */
    public void removeTreasure(String treasureToRemove, String passageSection, ListView spacesList) {

      boolean isChamber;
      Chamber chamber;
      Passage passage;
      int treasureIndex;
      int sectionNum;

      try {
        treasureIndex = Integer.parseInt(treasureToRemove);
      } catch (NumberFormatException nfe) {
        treasureIndex = -1;
      }

      try {
        sectionNum = Integer.parseInt(passageSection);
      } catch (NumberFormatException nfe) {
        sectionNum = -1;
      }


      if (treasureIndex >= 0) {


        for(Space s: spaces) {

          if (("[" + s.getSpaceNum() + "]").equals(spacesList.getSelectionModel().getSelectedItems().toString())) {
            isChamber = s instanceof Chamber;

            if (isChamber) {
              chamber = (Chamber) s;
              chamber.removeTreasure(treasureIndex);
            } else {
              if (sectionNum >= 0) {
                passage = (Passage) s;
                passage.removeTreasure(sectionNum, treasureIndex);
              }
            }
            myGui.populateDescriptionArea(s.getDescription());
          }
        }
      }
    }


    /**
     * Serializes the spaces arraylist in a specified directory.
     * @param stage the primary stage
     */
    public void save(Stage stage) {

      File selectedFile = fileChooser.showSaveDialog(stage);

      try {

        FileOutputStream fileOutputStream = new FileOutputStream(selectedFile);

        ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);

        out.writeObject(spaces);

        out.close();

        fileOutputStream.close();
      } catch (IOException i) {
        i.printStackTrace();
      }
    }


    /**
     * Deserializes the spaces arraylist from a specified file.
     * @param stage the primary stage
     */
    public void load(Stage stage) {

      File selectedFile = fileChooser.showOpenDialog(stage);

      spaces = null;

      try {
        FileInputStream fileIn = new FileInputStream(selectedFile);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        spaces = (ArrayList<Space>) in.readObject();
        in.close();
        fileIn.close();
      } catch (IOException i) {
        i.printStackTrace();
        return;
      } catch (ClassNotFoundException c) {
        System.out.println("Space class not found");
        c.printStackTrace();
        return;
      }

      myGui.refresh();
    }
}
