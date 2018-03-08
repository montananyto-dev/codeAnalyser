package Interface;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Gui extends Application {

    private GridPane grid;
    private TabPane tabPane;
    private BorderPane mainPane;
    public Stage window;

    @Override
    public void start(Stage primaryStage) {

        this.window = primaryStage;
        primaryStage.sizeToScene();

        primaryStage.setTitle("Test your code");
        Group root = new Group();
        Scene scene = new Scene(root, 1300, 800, Color.WHITE);
        scene.getStylesheets().add("gui.css");


        setupTabPane();

        setupMainPane(scene);
        root.getChildren().add(mainPane);
        primaryStage.setScene(scene);
        //primaryStage.setFullScreen(true);
        primaryStage.show();

    }

    // setup
    private void setupMainPane(Scene scene) {
        mainPane = new BorderPane();
        mainPane.setCenter(tabPane);
        mainPane.prefHeightProperty().bind(scene.heightProperty());
        mainPane.prefWidthProperty().bind(scene.widthProperty());
    }

    private void setupTabPane() {
        tabPane = new TabPane();
        //Create a tab for upload
        Tab uploadTab = new Tab();
        uploadTab.setText("Upload");
        tabPane.getTabs().add(uploadTab);


        Tab resultTab = new Tab();
        resultTab.setText("Result");
        tabPane.getTabs().add(resultTab);

        //set a Grid for the tab upload independent from result tab
        ParseTabContent pn = new ParseTabContent(this);
        uploadTab.setContent(pn.Grid);

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    }
}


