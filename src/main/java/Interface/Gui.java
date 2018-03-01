package Interface;

import Core.Analyzer.Benchmarks.Types;
import Core.Definitions.SupportedLanguages;
import Core.Entry;
import Core.Exceptions.DefinitionNotFoundException;
import Core.Exceptions.NotSupportedException;
import Core.FileManager.Exceptions.OutputDirectoryNotSetException;
import Core.FileManager.FileManager;
import Core.ProcessManager;
import Core.Report;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Gui extends Application {

    private List<LabelFieldFactory.LabelField> LabelFields = new ArrayList<>();

    private ProcessManager _processManager = new ProcessManager();
    private Report _report;
    private SupportedLanguages type;

    private File _defaultOutputDirectory = null;
    private File _defaultInputDirectory = null;

    private TextArea textArea;
    private FileChooser fileChooser;
    private Stage window;
    private ObservableList <String> options;
    private ComboBox fileType;
    private Alert alert;
    private File workfile;

    private GridPane grid;
    private TabPane tabPane;
    private BorderPane mainPane;
    private Button process;
    private Button upload;
    private Button save;


    @Override
    public void start(Stage primaryStage) {

        this.window = primaryStage;

        primaryStage.setTitle("Welcome");
        Group root = new Group();
        Scene scene = new Scene(root, 1300, 900, Color.WHITE);
        scene.getStylesheets().add("gui.css");

        setupGrid();
        setupAlert();
        setupTabPane();
        setupLabelFields();
        setupButtons();
        setupTextArea();
        setupTypeField();
        setupMainPane(scene);
        root.getChildren().add(mainPane);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void setFileChooser(Stage window) throws IOException {
        if (_defaultInputDirectory == null)
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(window);
        fileType.setValue(_processManager.determineLanguage(file).name());
        setTextArea(FileManager.read(file));
        workfile = file;
    }

    private void writeReportFile(Report report) {
        if (_defaultOutputDirectory == null){
            setDefaultOutputDirectory();
        }
        try {
            FileManager.write(report, _defaultOutputDirectory);
        } catch (OutputDirectoryNotSetException | IOException e) {
            e.printStackTrace();
        }
    }

    private void setDefaultOutputDirectory(){
        DirectoryChooser chooser = new DirectoryChooser();
        _defaultOutputDirectory = chooser.showDialog(window);
    }

    private void run(File file){
        try {
            _report = _processManager.process(file);
            setReportValues(_report);
        } catch (NotSupportedException | IOException e) {
            e.printStackTrace();
        }
    }
    private void run(String[] body, String fileName)  {
        try {
            _report = _processManager.process(body, fileName, SupportedLanguages.Java);
            setReportValues(_report);

        } catch (DefinitionNotFoundException | NotSupportedException e) {
            e.printStackTrace();
        }

    }

    private void setReportValues(Report report){
        setCyclomaticComplexity(report);
        setNumberOfLines(report);
        setHalstead(report);
    }

    private void setCyclomaticComplexity(Report report){
        int sum = 0;
        for (Entry e : report.Entries){
            if (e.Type == Types.Cyclomatic) sum += (int) e.Values.get("value");
        }
        LabelFieldFactory.LabelField lf = findLabelField("Cyclomatic");
        if (lf != null) lf.Field.setText(Integer.toString(sum));
    }

    private void setNumberOfLines(Report report){
        for (Entry e : report.Entries){
            if (e.Type == Types.Lines){
                LabelFieldFactory.LabelField lf = findLabelField("lines");
                if (lf != null) lf.Field.setText(e.Values.get("value").toString());
                break;
            }
        }
    }

    private void setHalstead(Report report){
        for (Entry e : report.Entries){
            if (e.Type == Types.Halstead){
                for(String key : e.Values.keySet()){
                    LabelFieldFactory.LabelField lf = findLabelField(key);
                    if (lf != null) lf.Field.setText(e.Values.get(key).toString());
                }
                break;
            }
        }
    }

    private LabelFieldFactory.LabelField findLabelField(String value){
        for (LabelFieldFactory.LabelField lf : LabelFields) {
            if (lf.Name.contains(value)) return lf;
        }
        return null;
    }

    private String[] getTextArea(){
        return textArea.getText().split("\n");
    }

    private void setTextArea(String[] lines){
        textArea.clear();
        for(String line : lines){
            textArea.appendText(line + "\n");
        }
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

        //set a grid for the tab upload independent from result tab
        uploadTab.setContent(grid);

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    }

    private void setupGrid(){
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(30, 30, 30, 30));
        grid.setAlignment(Pos.TOP_RIGHT);


        int[] widths = {10,10,10,10,10,10,25};
        for (int i : widths){
            ColumnConstraints constraint = new ColumnConstraints();
            constraint.setPercentWidth(i);
            grid.getColumnConstraints().add(constraint);
        }

        grid.getStyleClass().add("grid");

        // set lines visible for columns and rows
        // grid.setGridLinesVisible(true);
    }

    private void setupAlert(){
        alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText("Language required");
        alert.setContentText("Please select the language required before pasting or uploading your code");
    }

    private void setupButtons(){
        process = new Button();
        process.setText("Process");
        process.setOnAction(even->{

            if (textArea.getText().length() > 0) {
                if (workfile == null) {
                    if (fileType.getValue().equals("")) alert.show();
                    run(getTextArea(), "temp");
                }
                else run(workfile);

            } else {
                textArea.setText("The file is empty");
            }

        });

        upload = new Button();
        upload.setText("Upload File");
        upload.setOnAction(event -> {

            try {
                setFileChooser(window);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        save = new Button();
        save.setText("Save Report");
        save.setOnAction(event ->{
            if (_report != null)
                writeReportFile(_report);
        });


        grid.add(upload, 0, 50);
        grid.add(process,2,50);
        grid.add(save, 3, 50);
    }

    private void setupLabelFields(){
        String[] vals = {"Number of words", "Number of lines","Number of Classes","Number of Methods",
                         "Number of Comments", "Cyclomatic complexity",
                         "Halstead Volume","Halstead Difficulty","Halstead Effort","Halstead Time To Code","Halstead Delivered Bugs"};
        LabelFieldFactory lfFactory = new LabelFieldFactory(grid, 6,0, Color.WHITE);
        for(String s: vals){
            LabelFields.add(lfFactory.build(s));
        }
    }

    private void setupTypeField(){
        options = FXCollections.observableArrayList("");
        options.addAll(Arrays.asList(_processManager.getSupportedTypeNames()));

        fileType = new ComboBox(options);

        fileType.valueProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue.toString().equals("Java")) {
                type = SupportedLanguages.Java;

            } else if (newValue.toString().equals("Visual Basic")) {
                type = SupportedLanguages.VisualBasic;
            }
            System.out.println(type.name());
        });

        grid.add(fileType, 1, 50);
    }

    private void setupTextArea(){
        textArea = new TextArea();
        textArea.setPrefSize(900, 900);
        textArea.setWrapText(true);

        textArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {

                workfile = null;

                System.out.println(fileType.valueProperty());

                if(fileType.getValue().equals("empty")) {
                    alert.show();

                }
            }
        });

        grid.add(textArea, 0, 0, 5, 48);
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


