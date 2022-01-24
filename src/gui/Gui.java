/* Name:       Thomas Green
 * Student ID: 1048389
 * Email:      tgreen12@uoguelph.ca
 */


package gui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.TextField;


public class Gui<toReturn> extends Application {

    /**
     * The controller.
     */
    private Controller theController;


    /**
     * The root.
     */
    private BorderPane root;


    /**
     * The popup for the doordescription.
     */
    private Popup doorDescriptionPane;


    /**
     * The popup for editing
     */
    private Popup editPane;


    /**
     * The scrollpane used for the spaceslist.
     */
    private ScrollPane spacesPane;


    /**
     * The primary stage.
     */
    private Stage primaryStage;


    /**
     * The list of spaces in this dungeon.
     */
    private ListView spacesList;


    /**
     * The list of doors.
     */
    private ListView doorsList;


    /**
     * The main text area for representing description.
     */
    private TextArea descriptionArea;


    /**
     * The start of the gui.
     * @param assignedStage the assigned stage.
     */
    @Override
    public void start(Stage assignedStage) {

        theController = new Controller(this);
        primaryStage = assignedStage;
        root = setUpRoot();
        root.getStyleClass().add("root");

        doorDescriptionPane = createPopUp(550, 550, "", 20, 20);
        editPane = createPopUp(250, 400, "", 20, 20);

        Scene scene = new Scene(root, 1300,700);

        scene.getStylesheets().add("res/style.css");

        primaryStage.setTitle("Dungeon Generator");
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    /**
     * Used to refresh the gui when file is loaded (does not work).
     */
    public void refresh() {
      root = setUpRoot();
    }


    /**
     * Sets up the root.
     * @return the borderpane.
     */
    private BorderPane setUpRoot() {

        BorderPane temp = new BorderPane();

        Node left = setLeftGraph();
        Node top = setTopGraph();
        Node bottom = setBottomGraph();
        Node center = setCenterGraph();
        Node right = setRightGraph();

        temp.setLeft(left);
        temp.setTop(top);
        temp.setBottom(bottom);
        temp.setCenter(center);
        temp.setRight(right);

        return temp;
    }


    /**
     * Sets up the top graph.
     * @return the top graph.
     */
    private Node setTopGraph() {

      MenuItem menuSave = new MenuItem("Save File");
      menuSave.getStyleClass().add("menu-item");
      MenuItem menuLoad = new MenuItem("Load File");
      menuLoad.getStyleClass().add("menu-item");

      MenuButton menuButton = new MenuButton("File", null, menuSave, menuLoad);
      menuButton.getStyleClass().add("menuButton");

      menuSave.setOnAction((ActionEvent event) -> {
        theController.save(primaryStage);
      });

      menuLoad.setOnAction((ActionEvent event) -> {
        theController.load(primaryStage);
      });

      HBox hBox = new HBox(menuButton);
      hBox.getStyleClass().add("hBox");

      return hBox;
    }


    /**
     * Sets up the bottom graph.
     * @return the bottom graph.
     */
    private Node setBottomGraph() {

      VBox vBox = new VBox();

      vBox.getStyleClass().add("vBox");


      Button editButton = createButton("Edit");

      editButton.setOnAction((ActionEvent event) -> {
          if(!editPane.isShowing()) {
            editButtonClicked();
            editPane.show(primaryStage);
          } else {
            editPane.hide();
          }
      });

      vBox.getChildren().add(editButton);

      return vBox;
    }


    /**
     * Sets up the center graph.
     * @return the center graph.
     */
    private Node setCenterGraph() {

      descriptionArea = new TextArea();
      descriptionArea.setDisable(true);
      descriptionArea.getStyleClass().add("text-area");
      return descriptionArea;
    }


    /**
     * Sets up the right graph.
     * @return the right graph.
     */
    private Node setRightGraph() {

      doorsList = new ListView();
      VBox vBox = new VBox();

      vBox.getStyleClass().add("vBox");

      Button doorDescriptionButton = createButton("Show/Hide Door Description");
      doorDescriptionButton.setOnAction((ActionEvent event) -> {
          if(!doorDescriptionPane.isShowing()) {
            theController.getDoorDescription(spacesList, doorsList);
            doorDescriptionPane.show(primaryStage);
          } else {
            doorDescriptionPane.hide();
          }
      });

      ScrollPane scrollPane = new ScrollPane();

      scrollPane.setContent(doorsList);

      scrollPane.setPrefWidth(150);

      vBox.getChildren().add(scrollPane);
      vBox.getChildren().add(doorDescriptionButton);

      return vBox;
    }


    /**
     * Sets up the left graph.
     * @return the left graph.
     */
    private Node setLeftGraph() {

      spacesList = new ListView();
      theController.populateSpacesList(spacesList);

      VBox vBox = new VBox();

      vBox.getStyleClass().add("vBox");

      Button descriptionButton = createButton("Show Space Description");
      descriptionButton.setOnAction((ActionEvent event) -> {
          theController.showSpaceDescripion(spacesList);
          theController.populateDoorsList(spacesList, doorsList);
      });

      ScrollPane scrollPane = new ScrollPane();

      scrollPane.setContent(spacesList);

      scrollPane.setPrefWidth(150);

      vBox.getChildren().add(scrollPane);
      vBox.getChildren().add(descriptionButton);

      return vBox;
    }

    /**
     * Creates a popup.
     * @return the popup.
     */
    private Popup createPopUp(int x, int y, String text, int w, int h) {

        Popup popup = new Popup();
        popup.setX(x);
        popup.setY(y);
        TextArea textA = new TextArea(text);
        popup.getContent().addAll(textA);
        textA.setStyle(" -fx-background-color: red;");
        textA.setMinWidth(w);
        textA.setMinHeight(h);
        return popup;
    }

    /**
     * Creates a button.
     * @return a button.
     */
    private Button createButton(String text) {

        Button btn = new Button();
        btn.setText(text);
        btn.getStyleClass().add("button");

        return btn;
    }


    public static void main(String[] args) {

        launch(args);
    }

    /**
     * Populates the center description area.
     */
    public void populateDescriptionArea(String description) {

      descriptionArea.setText(description);
    }

    /**
     * Populates a popup.
     * @param description the description.
     */
    public void populatePopUp(String description) {

      TextArea textA = new TextArea(description);
      doorDescriptionPane.getContent().addAll(textA);
    }

    /**
     * Populates the dit popup with various nodes.
     */
    public void editButtonClicked() {

        final TextField monsterToAdd = new TextField();
        final TextField monsterToRemove = new TextField();
        final TextField treasureToAdd = new TextField();
        final TextField treasureToRemove = new TextField();
        final TextField sectionNumber = new TextField();

        final Label labelMonsterAdd = new Label("Monster Type to add (1 - 100):");
        labelMonsterAdd.getStyleClass().add("label");
        final Label labelMonsterRemove = new Label("Monster to remove (index):");
        labelMonsterRemove.getStyleClass().add("label");
        final Label labelTreasureAdd = new Label("Treasure Type to add (1-20):");
        labelTreasureAdd.getStyleClass().add("label");
        final Label labelTreasureRemove = new Label("Treasure to remove (index):");
        labelTreasureRemove.getStyleClass().add("label");
        final Label labelSectionNumber = new Label("Passage Section Number (If Applicable):");
        labelSectionNumber.getStyleClass().add("label");
        final Button saveButton = createButton("Save");
        saveButton.getStyleClass().add("label");

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(10));

        gridPane.add(labelMonsterAdd, 0, 0, 1, 2);
        gridPane.add(monsterToAdd, 2, 0, 1, 1);
        gridPane.add(labelMonsterRemove, 0, 1, 1, 2);
        gridPane.add(monsterToRemove, 2, 1, 1, 1);
        gridPane.add(labelTreasureAdd, 0, 2, 1, 2);
        gridPane.add(treasureToAdd, 2, 2, 1, 1);
        gridPane.add(labelTreasureRemove, 0, 3, 1, 2);
        gridPane.add(treasureToRemove, 2, 3, 1, 1);
        gridPane.add(labelSectionNumber, 0, 4, 1, 2);
        gridPane.add(sectionNumber, 2, 4, 1, 1);

        gridPane.add(saveButton, 0, 5, 2, 1);

        saveButton.setOnAction(action -> {

            theController.addMonster(monsterToAdd.getText(), sectionNumber.getText(), spacesList);
            theController.removeMonster(monsterToRemove.getText(), sectionNumber.getText(), spacesList);
            theController.addTreasure(treasureToAdd.getText(), sectionNumber.getText(), spacesList);
            theController.removeTreasure(treasureToRemove.getText(), sectionNumber.getText(), spacesList);
            editPane.hide();
        });

        gridPane.getStyleClass().add("popup");
        editPane.getContent().addAll(gridPane);
    }
}
