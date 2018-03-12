package Interface;

import Core.Analyzer.Benchmarks.Types;
import Core.Definitions.SupportedLanguages;
import Core.Entry;
import Core.FileManager.CsvBuilder;
import Core.FileManager.FileManager;
import Core.ProcessManager;
import Core.Report;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static java.lang.System.out;

public class CompareTabContent extends Control {

    private List <LabelFieldFactory.LabelField> LabelFields = new ArrayList <>();
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

    String[] valuesFirstContent = {"Number of Words 1", "Number of Lines 1", "Number of Methods 1",
            "Number of Comments 1", "Cyclomatic complexity 1",
            "Halstead Volume 1", "Halstead Difficulty 1", "Halstead Effort 1", "Halstead Time To Code 1", "Halstead Delivered Bugs 1"};

    String[] valuesSecondContent = {"Number of Words 2", "Number of Lines 2", "Number of Methods 2",
            "Number of Comments 2", "Cyclomatic complexity 2",
            "Halstead Volume 2", "Halstead Difficulty 2", "Halstead Effort 2", "Halstead Time To Code 2", "Halstead Delivered Bugs 2"};


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

    private LabelFieldFactory.LabelField findLabelField(String value) {
        for (LabelFieldFactory.LabelField lf : LabelFields) {
            if (lf.Name.contains(value)) return lf;
        }
        return null;
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

            setReportValues(reportSecondContent);
        });
        gridCompareContent.add(uploadFirstContent, 0, 19);
        gridCompareContent.add(process, 9, 18);
        gridCompareContent.add(uploadSecondContent, 5, 19);
        gridCompareContent.add(clear, 9, 19);
    }

    private void setupLabelFieldsFirstContent() {

        LabelFieldFactory lfFactory = new LabelFieldFactory(gridCompareContent, 0, 2, 2, 1, Color.WHITE);
        for (String s : valuesFirstContent) {
            LabelFields.add(lfFactory.build(s));
        }
        Label fileOne = new Label();
        fileOne.setText("File 1");
        fileOne.setTextFill(Color.WHITE);
        gridCompareContent.add(fileOne, 0, 0);
    }

    private void setupLabelFieldsSecondContent() {

        LabelFieldFactory lfFactory = new LabelFieldFactory(gridCompareContent, 5, 2, 2, 1, Color.WHITE);
        for (String s : valuesSecondContent) {
            LabelFields.add(lfFactory.build(s));
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
        LabelFields.stream().forEach(item -> {
            item.Field.setText("");
        });
    }

//    private String parseString(String line) {
//
//        String value = line.substring((line.lastIndexOf(",") + 1), line.lastIndexOf('"'));
//
//        return value;
//    }
//
//    private void parseHalstead(String line){
//
//        String [] halstead;
//        if(line.contains("Volume")){
//            String volume = line.substring((line.lastIndexOf("Volume,")));
//            System.out.println(volume);
//        }
//
//        System.out.println(line);
//
//    }
//
//    private void readFile(File file) throws Exception {
//
//        Report report = CsvBuilder.read(file);
//
//
//        try {
//
//            BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()));
//
//            String sCurrentLine;
//
//            while ((sCurrentLine = br.readLine()) != null) {
//
//                if (sCurrentLine.contains("CommentCount")) {
//                    LabelFieldFactory.LabelField lf = findLabelField("Number of Comments 1");
//                    if (lf != null) lf.Field.setText(parseString(sCurrentLine));
//                } else if (sCurrentLine.contains("LinesCount")) {
//                    LabelFieldFactory.LabelField lf = findLabelField("Number of Lines 1");
//                    if (lf != null) lf.Field.setText(parseString(sCurrentLine));
//                } else if (sCurrentLine.contains("MethodCount")) {
//                    LabelFieldFactory.LabelField lf = findLabelField("Number of Methods 1");
//                    if (lf != null) lf.Field.setText(parseString(sCurrentLine));
//                } else if (sCurrentLine.contains("WordCount")) {
//                    LabelFieldFactory.LabelField lf = findLabelField("Number of Words 1");
//                    if (lf != null) lf.Field.setText(parseString(sCurrentLine));
//                }else if (sCurrentLine.contains("Cyclomatic")) {
//                    LabelFieldFactory.LabelField lf = findLabelField("Cyclomatic complexity 1");
//                    if (lf != null) lf.Field.setText(parseString(sCurrentLine));
//                }else if (sCurrentLine.contains("Halstead")) {
//
//                    parseHalstead(sCurrentLine);
//
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void setReportFirstContent(File file) throws Exception {

        reportFirstContent = CsvBuilder.read(file);
    }

    private void setReportSecondContent(File file) throws Exception{

        reportSecondContent = CsvBuilder.read(file);
    }

    private void setReportValues(Report report) {
       // setCyclomaticComplexity(report);
        //setNumberOfLines(report);
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
