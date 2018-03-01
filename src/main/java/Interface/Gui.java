package Interface;

import Core.Analyzer.Benchmarks.Types;
import Core.Entry;
import Core.Exceptions.DefinitionNotFoundException;
import Core.Exceptions.NotSupportedException;
import Core.Parser.Models.ParserObjects.Value;
import Core.ProcessManager;
import Core.Report;
import com.sun.deploy.util.StringUtils;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.out;


public class Gui extends Application {

    private String descriptionFileType = "empty";
    private String extensionFile = "";
    private List<LabelFieldFactory.LabelField> LabelFields = new ArrayList<>();
    TextArea textArea;
    FileChooser fileChooser;
    String fileName;
    String[] body;
    Stage window;
    ObservableList <String> options;
    ComboBox fileType;
    Alert alert;
    File file;


    @Override
    public void start(Stage primaryStage) throws Exception {

        this.window = primaryStage;

        primaryStage.setTitle("Welcome");
        Group root = new Group();
        Scene scene = new Scene(root, 1300, 900, Color.WHITE);

        scene.getStylesheets().add("gui.css");

        TabPane tabPane = new TabPane();
        BorderPane mainPane = new BorderPane();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(30, 30, 30, 30));
        grid.setAlignment(Pos.TOP_RIGHT);


        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(10);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(10);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(10);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPercentWidth(10);
        ColumnConstraints col5 = new ColumnConstraints();
        col5.setPercentWidth(10);
        ColumnConstraints col6 = new ColumnConstraints();
        col6.setPercentWidth(10);
        ColumnConstraints col7 = new ColumnConstraints();
        col7.setPercentWidth(25);


        alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText("Language required");
        alert.setContentText("Please select the language required before pasting or uploading your code");


        grid.getColumnConstraints().addAll( col1, col2, col3, col4, col5, col6, col7);


        //Create a tab for upload
        Tab uploadTab = new Tab();
        uploadTab.setText("Upload");
        tabPane.getTabs().add(uploadTab);


        Tab resultTab = new Tab();
        resultTab.setText("Result");
        tabPane.getTabs().add(resultTab);

        //set a grid for the tab upload independent from result tab
        uploadTab.setContent(grid);

        LabelFieldFactory lfFactory = new LabelFieldFactory(grid, 6,0, Color.WHITE);
        LabelFields.add(lfFactory.build("Number of words"));
        LabelFields.add(lfFactory.build("Number of lines"));
        LabelFields.add(lfFactory.build("Number of Classes"));
        LabelFields.add(lfFactory.build("Number of Methods"));
        LabelFields.add(lfFactory.build("Number of Comments"));
        LabelFields.add(lfFactory.build("Halstead complexity"));
        LabelFields.add(lfFactory.build("Cyclomatic complexity"));

        Button process = new Button();
        process.setText("Process");
        process.setOnAction(even->{

            if (body.length > 0) {
                run(body, fileName);

            } else {
                textArea.setText("The file is empty");
            }

        });

        Button upload = new Button();
        upload.setText("Upload File");
        upload.setOnAction(event -> {

            try {
                if(fileType.getValue() == null){

                    alert.show();
                }else{
                    setFileChooser(window);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        });


        grid.add(upload, 0, 50);
        grid.add(process,2,50);

        textArea = new TextArea();
        textArea.setPrefSize(900, 900);

        textArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {

                System.out.println(descriptionFileType);

                if(descriptionFileType == "empty" || descriptionFileType != "Java" || descriptionFileType != "Visual Basic") {

                    System.out.println(descriptionFileType);

                    alert.show();

                }
            }
        });

        grid.add(textArea, 0, 0, 5, 48);

        options = FXCollections.observableArrayList("Java", "Visual Basic");
        fileType = new ComboBox(options);

        fileType.valueProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue.toString().equals("Java")) {
                descriptionFileType = "JAVA files (*.java)";
                extensionFile = "*.java";
                System.out.println(descriptionFileType);

            } else if (newValue.toString().equals("Visual Basic")) {
                descriptionFileType = "VISUAL BASIC files (*.vb)";
                extensionFile = "*.vb";
                System.out.println(descriptionFileType);

            }
        });

        grid.add(fileType, 1, 50);
        grid.getStyleClass().add("grid");

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // set lines visible for columns and rows
       // grid.setGridLinesVisible(true);


        mainPane.setCenter(tabPane);
        mainPane.prefHeightProperty().bind(scene.heightProperty());
        mainPane.prefWidthProperty().bind(scene.widthProperty());


        root.getChildren().add(mainPane);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void setFileChooser(Stage window) throws IOException {

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(descriptionFileType, extensionFile);
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Open Resource File");
        file = fileChooser.showOpenDialog(window);
        body = readFile(file);
        writeFile(body);
        fileName = file.getName();

    }


    private String[] readFile(File file) throws IOException {

        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List <String> lines = new ArrayList <>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();

        return lines.toArray(new String[lines.size()]);
    }

    private void writeFile(String[] contentFile) {
        for (String data : contentFile) {
            textArea.appendText("\n" + data);
        }
    }

    private void run(String[] body, String fileName)  {

        ProcessManager manager = new ProcessManager();
        try {
            Report report = manager.process(body, fileName);
            for (Entry e : report.Entries) {
                out.println(StringUtils.join(Arrays.asList(e.Path), ".") + ":" + e.Name + ": " + e.Type.name() + ":" + e.Value);
            }
            setCyclomaticComplexity(report);
            setNumberOfLines(report);
        } catch (DefinitionNotFoundException e) {
            e.printStackTrace();
        } catch (NotSupportedException e) {
            e.printStackTrace();
        }

    }

    private LabelFieldFactory.LabelField findLabelField(String value){
        for (LabelFieldFactory.LabelField lf : LabelFields) {
            if (lf.Name.contains(value)) return lf;
        }
        return null;
    }

    private void setCyclomaticComplexity(Report report){
        int sum = 0;
        for (Entry e : report.Entries){
            if (e.Type == Types.Cyclomatic) sum += e.Value;
        }
        LabelFieldFactory.LabelField lf = findLabelField("Cyclomatic");
        if (lf != null) lf.Field.setText(Integer.toString(sum));
    }

    private void setNumberOfLines(Report report){
        for (Entry e : report.Entries){
            if (e.Type == Types.Lines){
                LabelFieldFactory.LabelField lf = findLabelField("lines");
                if (lf != null) lf.Field.setText(Integer.toString(e.Value));
                break;
            }
        }
    }



    private class LabelFieldFactory{

        public class LabelField{
            public final Label Label;
            public final TextField Field;
            public final String Name;

            public LabelField(String name, Label label, TextField field){
                Name = name;
                Label = label;
                Field = field;
            }
        }
        private GridPane _grid;
        private int _column;
        private int _row;
        private Color _color;

        public LabelFieldFactory(GridPane grid, int column, int row, Color color){
            _grid = grid;
            _column = column;
            _row = row;
            _color = color;
        }

        public LabelField build(String text){
            Label label = new Label(text);
            _grid.add(label, _column, _row);
            TextField field = new TextField();
            _grid.add(field, _column+1, _row);
            _row++;
            label.setTextFill(_color);
            return new LabelField(text, label, field);
        }

    }
}


