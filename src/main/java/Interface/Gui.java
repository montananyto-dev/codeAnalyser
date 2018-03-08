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
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Gui extends Application {

    private List <LabelFieldFactory.LabelField> LabelFields = new ArrayList <>();

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
    private Alert alertEmptyFile;
    private Alert alertNotSupportedFile;
    private File workfile;


    private GridPane grid;
    private TabPane tabPane;
    private BorderPane mainPane;

    private Button process;
    private Button upload;
    private Button save;
    private Button clear;

    String[] vals = {"Number of Words", "Number of Lines", "Number of Methods",
            "Number of Comments", "Cyclomatic complexity",
            "Halstead Volume", "Halstead Difficulty", "Halstead Effort", "Halstead Time To Code", "Halstead Delivered Bugs"};


    @Override
    public void start(Stage primaryStage) {

        this.window = primaryStage;
        primaryStage.sizeToScene();

        primaryStage.setTitle("Test your code");
        Group root = new Group();
        Scene scene = new Scene(root, 1300, 800, Color.WHITE);
        scene.getStylesheets().add("gui.css");
        setupGrid();

        setupAlert();
        setupAlertEmptyFile();
        setupAlertNotSupportedFile();

        setupTabPane();
        setupLabelFields();
        setupButtons();
        setupTextArea();
        setupTypeField();
        setupMainPane(scene);
        root.getChildren().add(mainPane);
        primaryStage.setScene(scene);
        //primaryStage.setFullScreen(true);
        primaryStage.show();

    }

    private void writeReportFile(Report report) {
        if (_defaultOutputDirectory == null) {
            setDefaultOutputDirectory();
        }
        try {
            FileManager.write(report, _defaultOutputDirectory);
        } catch (OutputDirectoryNotSetException | IOException e) {
            e.printStackTrace();
        }
    }

    private void run(File file) {
        try {
            _report = _processManager.process(file);
            setReportValues(_report);
        } catch (NotSupportedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void run(String[] body, String fileName) {
        try {
            _report = _processManager.process(body, fileName, SupportedLanguages.Java);
            setReportValues(_report);

        } catch (DefinitionNotFoundException | NotSupportedException e) {
            e.printStackTrace();
        }
    }

    private LabelFieldFactory.LabelField findLabelField(String value) {
        for (LabelFieldFactory.LabelField lf : LabelFields) {
            if (lf.Name.contains(value)) return lf;
        }
        return null;
    }

    private String[] getTextArea() {
        return textArea.getText().split("\n");
    }

    private void setTextArea(String[] lines) {
        textArea.clear();
        for (String line : lines) {
            textArea.appendText(line + "\n");
        }
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

    private void setDefaultOutputDirectory() {
        DirectoryChooser chooser = new DirectoryChooser();
        _defaultOutputDirectory = chooser.showDialog(window);
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

    private void setupGrid() {
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setAlignment(Pos.TOP_RIGHT);
        grid.setGridLinesVisible(true);

        int[] widths = {10, 10, 10, 10, 10, 10, 25};
        for (int i : widths) {
            ColumnConstraints constraintCol = new ColumnConstraints();
            constraintCol.setPercentWidth(i);
            grid.getColumnConstraints().add(constraintCol);
        }

        int[] heights = {10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10};
        for (int i : heights) {
            RowConstraints constraintRow = new RowConstraints();
            constraintRow.setPercentHeight(i);
            grid.getRowConstraints().add(constraintRow);
        }

        grid.getStyleClass().add("grid");

        // set lines visible for columns and rows
        // grid.setGridLinesVisible(true);
    }

    private void setupAlert() {
        alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText("Language required");
        alert.setContentText("Please select the language required before pasting or uploading your code");
    }

    private void setupAlertEmptyFile() {
        alertEmptyFile = new Alert(Alert.AlertType.WARNING);
        alertEmptyFile.setTitle("Warning Dialog");
        alertEmptyFile.setHeaderText("The file is empty");
        alertEmptyFile.setContentText("Please select an other file or paste your codes");

    }

    private void setupAlertNotSupportedFile() {
        alertNotSupportedFile = new Alert(Alert.AlertType.WARNING);
        alertNotSupportedFile.setTitle("Warning Dialog");
        alertNotSupportedFile.setHeaderText("This code is not supported");
        alertNotSupportedFile.setContentText("Please select or paste supported codes");

    }

    // run(getTextArea(), "temp");
    //textArea.setText("The file is empty");
    private void setupButtons() {

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
        save.setOnAction(event -> {
            if (_report != null)
                writeReportFile(_report);
        });

        clear = new Button();
        clear.setText("Clear");
        clear.setOnAction(event -> {
            clearTextArea();
        });

        process = new Button();
        process.setText("Process");
        process.setOnAction(even -> {

            if (workfile == null && fileType.getSelectionModel().isEmpty()) {

                alert.show();

            } else if (workfile != null && fileType.getSelectionModel().isEmpty()) {

                alert.show();

            }else if((workfile != null) && !(fileType.getSelectionModel().isEmpty()) && (textArea.getText().isEmpty())){

                alertEmptyFile.show();

            }  else if ((workfile == null) && !(fileType.getSelectionModel().isEmpty())) {


                if (textArea.getText().isEmpty()) {

                    alertEmptyFile.show();

                }else {

                    run(getTextArea(), "temp");
                    run(workfile);
                }

            } else if (!(textArea.getText().isEmpty()) && !(fileType.getSelectionModel().isEmpty() &&(workfile != null))) {

                run(workfile);

            }else if(fileType.getValue().equals("NOTSUPPORTED")){

                alertNotSupportedFile.show();
            }

        });


        grid.add(upload, 0, 19);
        grid.add(process, 3, 19);
        grid.add(save, 4, 19);
        grid.add(clear,5,19);
    }

    private void setupLabelFields() {

        LabelFieldFactory lfFactory = new LabelFieldFactory(grid, 6, 0, Color.WHITE);
        for (String s : vals) {
            LabelFields.add(lfFactory.build(s));
        }
    }

    private void setupTypeField() {
        options = FXCollections.observableArrayList();
        options.addAll(Arrays.asList(_processManager.getSupportedTypeNames()));

        fileType = new ComboBox(options);
        fileType.setMinSize(25,10);

        fileType.valueProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue.toString().equals("Java")) {
                type = SupportedLanguages.Java;

            } else if (newValue.toString().equals("Visual Basic")) {
                type = SupportedLanguages.VisualBasic;
            }
            System.out.println(type.name());
        });

        grid.add(fileType, 1, 19,2,1);
    }

    private void clearTextArea(){

        textArea.clear();
        workfile = null;

        LabelFields.stream().forEach(item->{
            item.Field.setText("");
        });

    }

    private void setupTextArea() {
        textArea = new TextArea();
        textArea.setPrefSize(800, 700);
        textArea.setWrapText(true);

        textArea.textProperty().addListener((observable, oldValue, newValue) -> {

            workfile = null;

            if (fileType.getValue().equals("empty")) {
                alert.show();
            }
        });

        grid.add(textArea, 0, 0, 6, 18);
    }

    private class LabelFieldFactory {

        public class LabelField {
            public final Label Label;
            public final TextField Field;
            public final String Name;

            public LabelField(String name, Label label, TextField field) {
                Name = name;
                Label = label;
                Field = field;
            }
        }

        private GridPane _grid;
        private int _column;
        private int _row;
        private Color _color;

        public LabelFieldFactory(GridPane grid, int column, int row, Color color) {
            _grid = grid;
            _column = column;
            _row = row;
            _color = color;
        }

        public LabelField build(String text) {
            Label label = new Label(text);
            _grid.add(label, _column, _row);
            TextField field = new TextField();
            _grid.add(field, _column + 1, _row);
            _row++;
            label.setTextFill(_color);
            return new LabelField(text, label, field);
        }

    }

    private void setReportValues(Report report) {
        setCyclomaticComplexity(report);
        setNumberOfLines(report);
        setHalstead(report);
        setNumberOfComments(report);
        setNumberOfMethods(report);
        setNumberOfWords(report);
    }

    private void setCyclomaticComplexity(Report report) {
        int sum = 0;
        for (Entry e : report.Entries) {
            if (e.Type == Types.Cyclomatic) sum += (int) e.Values.get("value");
        }
        LabelFieldFactory.LabelField lf = findLabelField("Cyclomatic");
        if (lf != null) lf.Field.setText(Integer.toString(sum));
    }

    private void setNumberOfLines(Report report) {
        for (Entry e : report.Entries) {
            if (e.Type == Types.LinesCount) {
                LabelFieldFactory.LabelField lf = findLabelField("Lines");
                if (lf != null) lf.Field.setText(e.Values.get("value").toString());
                break;
            }
        }
    }

    private void setHalstead(Report report) {
        for (Entry e : report.Entries) {
            if (e.Type == Types.Halstead) {
                for (String key : e.Values.keySet()) {
                    LabelFieldFactory.LabelField lf = findLabelField(key);
                    if (lf != null) lf.Field.setText(new DecimalFormat("0.00").format(e.Values.get(key)));
                }
                break;
            }
        }
    }

    private void setNumberOfComments(Report report){
        for(Entry e:report.Entries){
            if(e.Type == Types.CommentCount){
                LabelFieldFactory.LabelField lf = findLabelField("Comments");
                if(lf != null) lf.Field.setText(e.Values.get("value").toString());
                break;
            }
        }
    }

    private void setNumberOfWords(Report report){
        for(Entry e:report.Entries){
            if(e.Type == Types.WordCount){
                LabelFieldFactory.LabelField lf = findLabelField("Words");
                if(lf != null) lf.Field.setText(e.Values.get("value").toString());
                break;
            }
        }
    }

    private void setNumberOfMethods(Report report){
        for(Entry e:report.Entries){
            if(e.Type == Types.MethodCount){
                LabelFieldFactory.LabelField lf = findLabelField("Methods");
                if(lf != null) lf.Field.setText(e.Values.get("value").toString());
                break;
            }
        }
    }

}


