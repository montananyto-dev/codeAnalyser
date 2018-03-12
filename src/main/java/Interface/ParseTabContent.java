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
import Interface.Controls.LabelField;
import Interface.Controls.LabelFieldCollection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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

import static java.lang.System.out;

public class ParseTabContent extends Control {

    private LabelFieldCollection LabelFields;
    private ProcessManager _processManager = new ProcessManager();
    private Report _report;
    private File _defaultOutputDirectory = null;
    private File _defaultInputDirectory = null;
    private SupportedLanguages type;
    private TextArea textArea;
    private FileChooser fileChooser;
    private ObservableList<String> options;
    private ComboBox fileType;
    private Alert alert;
    private Alert alertEmptyFile;
    private Alert alertNotSupportedFile;
    private File workFile;
    private Gui parent;
    public GridPane gridParseContent;
    private Button process;
    private Button upload;
    private Button save;
    private Button clear;

    private String[] values = {"Number of Words", "Number of Lines", "Number of Methods",
                     "Number of Comments", "Cyclomatic complexity",
                     "Halstead Volume", "Halstead Difficulty", "Halstead Effort", "Halstead Time To Code", "Halstead Delivered Bugs"};

    public ParseTabContent(Gui parent) {
        this.parent = parent;
        setupGridUpload();
        setupAlert();
        setupAlertEmptyFile();
        setupAlertNotSupportedFile();
        setupLabelFields();
        setupButtons();
        setupTextArea();
        setupTypeField();
        out.print(gridParseContent.isVisible());
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
        workFile = file;
    }

    private void setDefaultOutputDirectory() {
        DirectoryChooser chooser = new DirectoryChooser();
        _defaultOutputDirectory = chooser.showDialog(parent.window);
    }
    // setup
    private void setupGridUpload() {
        gridParseContent = new GridPane();
        gridParseContent.setHgap(10);
        gridParseContent.setVgap(10);
        gridParseContent.setPadding(new Insets(20, 20, 20, 20));
        gridParseContent.setAlignment(Pos.TOP_RIGHT);
        //gridParseContent.setGridLinesVisible(true);

        int[] widths = {10, 10, 10, 10, 10, 10, 10,10,10,10};
        for (int i : widths) {
            ColumnConstraints constraintCol = new ColumnConstraints();
            constraintCol.setPercentWidth(i);
            gridParseContent.getColumnConstraints().add(constraintCol);
        }

        int[] heights = {10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10};
        for (int i : heights) {
            RowConstraints constraintRow = new RowConstraints();
            constraintRow.setPercentHeight(i);
            gridParseContent.getRowConstraints().add(constraintRow);
        }
        gridParseContent.getStyleClass().addAll("grid","label","button");
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

    private void setupButtons() {

        upload = new Button();
        upload.setText("Upload File");
        upload.setOnAction(event -> {
            try {
                setFileChooser(parent.window);
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

            if (workFile == null && fileType.getSelectionModel().isEmpty()) {
                alert.show();
            } else if (workFile != null && fileType.getSelectionModel().isEmpty()) {
                alert.show();
            }else if((workFile != null) && !(fileType.getSelectionModel().isEmpty()) && (textArea.getText().isEmpty())){
                alertEmptyFile.show();
            }  else if ((workFile == null) && !(fileType.getSelectionModel().isEmpty())) {
                if (textArea.getText().isEmpty()) {
                    alertEmptyFile.show();
                }else {
                    run(getTextArea(), "temp");
                    run(workFile);
                }

            } else if (!(textArea.getText().isEmpty()) && !(fileType.getSelectionModel().isEmpty() &&(workFile != null))) {
                run(workFile);
            }else if(fileType.getValue().equals("NOTSUPPORTED")){
                alertNotSupportedFile.show();
            }
        });
        gridParseContent.add(upload, 0, 19);
        gridParseContent.add(process, 3, 19);
        gridParseContent.add(save, 4, 19);
        gridParseContent.add(clear, 5, 19);
    }

    private void setupLabelFields() {

        LabelFields = new LabelFieldCollection(gridParseContent, 6, 0,2,1, Color.WHITE);
        for (String s : values) {
            LabelFields.add(s);
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
        });

        gridParseContent.add(fileType, 1, 19, 2, 1);
    }

    private void clearTextArea(){

        textArea.clear();
        workFile = null;

        LabelFields.LabelFields.stream().forEach(item->{
            item.Field.setText("");
        });

    }

    private void setupTextArea() {
        textArea = new TextArea();
        textArea.setPrefSize(800, 700);
        textArea.setWrapText(true);

        textArea.textProperty().addListener((observable, oldValue, newValue) -> {

            workFile = null;

            if (fileType.getValue().equals("empty")) {
                alert.show();
            }
        });

        gridParseContent.add(textArea, 0, 0, 6, 18);
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
        LabelField lf = LabelFields.find("Cyclomatic");
        if (lf != null) lf.Field.setText(Integer.toString(sum));
    }

    private void setNumberOfLines(Report report) {
        for (Entry e : report.Entries) {
            if (e.Type == Types.LinesCount) {
                LabelField lf = LabelFields.find("Lines");
                if (lf != null) lf.Field.setText(e.Values.get("value").toString());
                break;
            }
        }
    }

    private void setHalstead(Report report) {
        for (Entry e : report.Entries) {
            if (e.Type == Types.Halstead) {
                for (String key : e.Values.keySet()) {
                    LabelField lf = LabelFields.find(key);
                    if (lf != null) lf.Field.setText(new DecimalFormat("0.00").format(e.Values.get(key)));
                }
                break;
            }
        }
    }

    private void setNumberOfComments(Report report){
        for(Entry e:report.Entries){
            if(e.Type == Types.CommentCount){
                LabelField lf = LabelFields.find("Comments");
                if(lf != null) lf.Field.setText(e.Values.get("value").toString());
                break;
            }
        }
    }

    private void setNumberOfWords(Report report){
        for(Entry e:report.Entries){
            if(e.Type == Types.WordCount){
                LabelField lf = LabelFields.find("Words");
                if(lf != null) lf.Field.setText(e.Values.get("value").toString());
                break;
            }
        }
    }

    private void setNumberOfMethods(Report report){
        for(Entry e:report.Entries){
            if(e.Type == Types.MethodCount){
                LabelField lf = LabelFields.find("Methods");
                if(lf != null) lf.Field.setText(e.Values.get("value").toString());
                break;
            }
        }
    }

}
