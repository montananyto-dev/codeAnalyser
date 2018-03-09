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

        primaryStage.setTitle("Code Analyser");
        Group root = new Group();
        Scene scene = new Scene(root, 1300, 800, Color.WHITE);
        scene.getStylesheets().addAll("grid.css");

        setupTabPane();
        setupMainPane(scene);
        root.getChildren().add(mainPane);
        primaryStage.setScene(scene);
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

        //Create a tab for compare
        Tab resultTab = new Tab();
        resultTab.setText("Compare");
        tabPane.getTabs().add(resultTab);

        //set a Grid for the tab upload independent from compare tab
        ParseTabContent ptc = new ParseTabContent(this);
        uploadTab.setContent(ptc.gridParseContent);

        //set a Grid for the tab compare independent from upload
        CompareTabContent ctc = new CompareTabContent(this);
        resultTab.setContent(ctc.gridCompareContent);

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    }
}


