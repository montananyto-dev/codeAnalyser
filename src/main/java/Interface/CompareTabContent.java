package Interface;

import Core.Analyzer.Benchmarks.Types;
import Core.Definitions.SupportedLanguages;
import Core.Entry;
import Core.FileManager.CsvBuilder;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;


import static java.lang.System.out;

public class CompareTabContent extends Control {

    private LabelFieldCollection lfCol1;
    private LabelFieldCollection lfCol2;
    private ProcessManager _processManager = new ProcessManager();
    private SupportedLanguages type;
    private FileChooser fileChooserFirstContent;
    private FileChooser fileChooserSecondContent;
    private ObservableList <String> options;
    private ComboBox fileType;
    private Alert alert;
    private Alert alertEmptyFile;
    private Alert alertNotSupportedFile;
    private File workfile;
    private Gui parent;
    public GridPane gridCompareContent;
    private Button process;
    private Button uploadFirstContent;
    private Button uploadSecondContent;
    private Button clear;
    private Report reportFirstContent;
    private Report reportSecondContent;

    private File fileFirstContent;
    private File fileSecontContent;

    private File defaultInputDirectoryFirstContent = null;
    private File getDefaultInputDirectorySecondContent = null;

    String[] lfValues = {"Number of Words", "Number of Lines", "Number of Methods",
                         "Number of Comments", "Cyclomatic complexity",
                         "Halstead Volume", "Halstead Difficulty", "Halstead Effort", "Halstead Time To Code", "Halstead Delivered Bugs"};


    public CompareTabContent(Gui parent) {
        this.parent = parent;
        setupGridCompare();
        setupAlert();
        setupAlertEmptyFile();
        setupAlertNotSupportedFile();
        setupLabelFieldsFirstContent();
        setupLabelFieldsSecondContent();
        setupButtons();
        setupTypeField();
    }

    public void setFileChooserFirstContent(Stage window) throws Exception {

        if(defaultInputDirectoryFirstContent == null)
        fileChooserFirstContent = new FileChooser();
        fileChooserFirstContent.setTitle("Open Resource File");
        File file = fileChooserFirstContent.showOpenDialog(window);
        fileChooserFirstContent.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV FILE", "*.csv"));

        if (checkSupportedFile(file)) {
            fileFirstContent = file;
            setReportFirstContent(file);
        } else {
            alertNotSupportedFile.show();
        }
    }

    public void setFileChooserSecondContent(Stage window) throws Exception {
        if(getDefaultInputDirectorySecondContent==null)
        fileChooserSecondContent = new FileChooser();
        fileChooserSecondContent.setTitle("Open Resource File");
        File file = fileChooserSecondContent.showOpenDialog(window);
        fileChooserSecondContent.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV FILE", "*.csv"));
        fileSecontContent = file;
        if (checkSupportedFile(file)) {
            fileSecontContent = file;
            setReportSecondContent(file);
        } else {
            alertNotSupportedFile.show();
        }
    }

    public Boolean checkSupportedFile(File file) {

        if (file.isFile() && file.getName().contains("Project")) {
            return true;
        } else {
            return false;
        }
    }

    private void setupGridCompare() {

        gridCompareContent = new GridPane();
        gridCompareContent.setHgap(10);
        gridCompareContent.setVgap(10);
        gridCompareContent.setPadding(new Insets(20, 20, 20, 20));
        gridCompareContent.setAlignment(Pos.TOP_RIGHT);
        //gridCompareContent.setGridLinesVisible(true);

        int[] widths = {10, 10, 10, 10, 10, 10, 10, 10, 10, 10};
        for (int i : widths) {
            ColumnConstraints constraintCol = new ColumnConstraints();
            constraintCol.setPercentWidth(i);
            gridCompareContent.getColumnConstraints().add(constraintCol);
        }
        int[] heights = {10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10};
        for (int i : heights) {
            RowConstraints constraintRow = new RowConstraints();
            constraintRow.setPercentHeight(i);
            gridCompareContent.getRowConstraints().add(constraintRow);
        }

        gridCompareContent.getStyleClass().addAll("grid", "label", "button");
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
        alertNotSupportedFile.setHeaderText("Only csv file are supported");
        alertNotSupportedFile.setContentText("Please select a csv file");

    }

    private void setupButtons() {

        uploadFirstContent = new Button();
        uploadFirstContent.setText("Upload File 1");
        uploadFirstContent.setOnAction(event -> {
            try {
                setFileChooserFirstContent(parent.window);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        uploadSecondContent = new Button();
        uploadSecondContent.setText("Upload File 2");
        uploadSecondContent.setOnAction(event -> {
            try {
                setFileChooserSecondContent(parent.window);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        clear = new Button();
        clear.setText("Clear");
        clear.setOnAction(event -> {
            clearField();
        });
        process = new Button();
        process.setText("Process");
        process.setOnAction(even -> {
            setReportValues(reportFirstContent, lfCol1);
            setReportValues(reportSecondContent, lfCol2);
        });
        gridCompareContent.add(uploadFirstContent, 0, 19);
        gridCompareContent.add(process, 9, 18);
        gridCompareContent.add(uploadSecondContent, 5, 19);
        gridCompareContent.add(clear, 9, 19);
    }

    private void setupLabelFieldsFirstContent() {

        lfCol1 = new LabelFieldCollection(gridCompareContent, 0, 2, 2, 1, Color.WHITE);
        for (String s : lfValues) {
            lfCol1.add(s);
        }
        Label fileOne = new Label();
        fileOne.setText("File 1");
        fileOne.setTextFill(Color.WHITE);
        gridCompareContent.add(fileOne, 0, 0);
    }

    private void setupLabelFieldsSecondContent() {

        lfCol2 = new LabelFieldCollection(gridCompareContent, 5, 2, 2, 1, Color.WHITE);
        for (String s : lfValues) {
            lfCol2.add(s);
        }
        Label fileTwo = new Label();
        fileTwo.setText("File 2");
        fileTwo.setTextFill(Color.WHITE);
        gridCompareContent.add(fileTwo, 5, 0);

    }

    private void setupTypeField() {
        options = FXCollections.observableArrayList();
        options.addAll(Arrays.asList(_processManager.getSupportedTypeNames()));

        fileType = new ComboBox(options);
        fileType.setMinSize(25, 10);


        fileType.valueProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue.toString().equals("Java")) {
                type = SupportedLanguages.Java;

            } else if (newValue.toString().equals("Visual Basic")) {
                type = SupportedLanguages.VisualBasic;
            }
            out.println(type.name());
        });

        gridCompareContent.add(fileType, 9, 17, 2, 1);
    }

    private void clearField() {

        workfile = null;
        lfCol1.LabelFields.forEach(item -> {
            item.Field.setText("");
        });
        lfCol2.LabelFields.forEach(item -> {
            item.Field.setText("");
        });
    }

    private void setReportFirstContent(File file) throws Exception {

        reportFirstContent = CsvBuilder.read(file);
    }

    private void setReportSecondContent(File file) throws Exception{

        reportSecondContent = CsvBuilder.read(file);
    }

    private void setReportValues(Report report, LabelFieldCollection collection) {
        setCyclomaticComplexity(report, collection);
        setNumberOfLines(report, collection);
        setHalstead(report, collection);
        setNumberOfComments(report, collection);
        setNumberOfMethods(report, collection);
        setNumberOfWords(report, collection);
    }

    private void setCyclomaticComplexity(Report report, LabelFieldCollection collection) {
        int sum = 0;
        for (Entry e : report.Entries) {
            if (e.Type == Types.Cyclomatic) sum += (int) e.Values.get("value");
        }
        LabelField lf = collection.find("Cyclomatic");
        if (lf != null) lf.Field.setText(Integer.toString(sum));
    }

    private void setNumberOfLines(Report report, LabelFieldCollection collection) {
        for (Entry e : report.Entries) {
            if (e.Type == Types.LinesCount) {
                LabelField lf = collection.find("Lines");
                if (lf != null) lf.Field.setText(e.Values.get("value").toString());
                break;
            }
        }
    }

    private void setHalstead(Report report, LabelFieldCollection collection) {
        for (Entry e : report.Entries) {
            if (e.Type == Types.Halstead) {
                for (String key : e.Values.keySet()) {
                    LabelField lf = collection.find(key);
                    if (lf != null) lf.Field.setText(new DecimalFormat("0.00").format(e.Values.get(key)));
                }
                break;
            }
        }
    }

    private void setNumberOfComments(Report report, LabelFieldCollection collection){
        for(Entry e:report.Entries){
            if(e.Type == Types.CommentCount){
                LabelField lf = collection.find("Comments");
                if(lf != null) lf.Field.setText(e.Values.get("value").toString());
                break;
            }
        }
    }

    private void setNumberOfWords(Report report, LabelFieldCollection collection){
        for(Entry e:report.Entries){
            if(e.Type == Types.WordCount){
                LabelField lf = collection.find("Words");
                if(lf != null) lf.Field.setText(e.Values.get("value").toString());
                break;
            }
        }
    }

    private void setNumberOfMethods(Report report, LabelFieldCollection collection){
        for(Entry e:report.Entries){
            if(e.Type == Types.MethodCount){
                LabelField lf = collection.find("Methods");
                if(lf != null) lf.Field.setText(e.Values.get("value").toString());
                break;
            }
        }
    }

}
