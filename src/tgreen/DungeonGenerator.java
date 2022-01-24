/* Name:       Thomas Green
    Student ID: 1048389
    Email:        tgreen12@uoguelph.ca */

package tgreen;

import java.util.ArrayList;

public class DungeonGenerator implements java.io.Serializable {



  private ArrayList<Space> spacesList;


  public DungeonGenerator() {

    spacesList = new ArrayList<>();
    GenerateDungeon();
  }


    public void GenerateDungeon() {

        /* Object ArrayLists */
        ArrayList<Passage> mainPassages = new ArrayList<Passage>();
        ArrayList<Door> mainDoors = new ArrayList<Door>();
        ArrayList<Chamber> mainChambers = new ArrayList<Chamber>();
        ArrayList<String> passageDescriptions = new ArrayList<String>();
        ArrayList<String> chamberDescriptions = new ArrayList<String>();

        /* Counters */
        int i;
        int numPaths = 1;
        int pathNum;

        /* Beginning of level */
        mainPassages.add(new Passage());
        mainPassages.get(0).setPassageNum(1);

        mainDoors.add(mainPassages.get(0).generatePassage(true));

        passageDescriptions.add(mainPassages.get(0).getDescription());

        spacesList.add(mainPassages.get(0));


        /* Main Dungeon Generator Algorithm */

        for (i = 0; i < 5; i++) {
            /* Set current door to current passage and a new dungeon */
            mainDoors.get(i * 2).setSpaces(mainPassages.get(i), new Chamber());

            /* Add the new chamber to it's array list */
            mainChambers.add((Chamber) mainDoors.get(i * 2).getSpace(1));

            /* gives the chamber object the num of current passages */
            mainChambers.get(i).getNumPaths(numPaths);

            /* Store the chamber's description in it's array list */
            chamberDescriptions.add(mainChambers.get(i).getDescription());

            numPaths += mainChambers.get(i).getNumNewPaths();

            pathNum = mainChambers.get(i).getPathNum();

            mainChambers.get(i).setChamberNum(i + 1);

            spacesList.add(mainChambers.get(i));

            mainDoors.add(mainChambers.get(i).getExitDoor());

            Passage passage = (Passage) mainDoors.get(i * 2 + 1).getSpace(1);
            mainPassages.add(passage);

            passageDescriptions.add(mainPassages.get(i + 1).getDescription());

            if (i != 4) {
                mainPassages.get(i + 1).setPassageNum(pathNum);
                spacesList.add(mainPassages.get(i + 1));

            }

            passage = (Passage) mainChambers.get(i).getExitDoor().getSpace(1);
            mainDoors.add(passage.getExitDoor());

        }
    }


    public ArrayList<Space> getSpacesList() {

      return spacesList;
    }
}
