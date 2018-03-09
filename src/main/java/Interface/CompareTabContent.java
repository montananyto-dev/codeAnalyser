package Interface;

import Core.Definitions.SupportedLanguages;
import Core.FileManager.FileManager;
import Core.ProcessManager;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static java.lang.System.out;

public class CompareTabContent extends Control {

    private List<LabelFieldFactory.LabelField> LabelFields = new ArrayList<>();
    private ProcessManager _processManager = new ProcessManager();
    private SupportedLanguages type;
    private FileChooser fileChooserFirstContent;
    private FileChooser fileChooserSecondContent;
    private ObservableList<String> options;
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

    String[] valuesFirstContent = {"Number of Words ", "Number of Lines ", "Number of Methods ",
            "Number of Comments ", "Cyclomatic complexity ",
            "Halstead Volume ", "Halstead Difficulty ", "Halstead Effort ", "Halstead Time To Code ", "Halstead Delivered Bugs "};

    String[] valuesSecondContent = {"Number of Words ", "Number of Lines ", "Number of Methods ",
            "Number of Comments ", "Cyclomatic complexity ",
            "Halstead Volume ", "Halstead Difficulty ", "Halstead Effort ", "Halstead Time To Code ", "Halstead Delivered Bugs "};


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

    public void setFileChooserFirstContent(Stage window) throws IOException {
        fileChooserFirstContent = new FileChooser();
        fileChooserFirstContent.setTitle("Open Resource File");
        File file = fileChooserFirstContent.showOpenDialog(window);
        fileChooserFirstContent.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV FILE","*.csv"));
        if(checkSupportedFile(file)){
            String[] firstContent = readFile(file);
        }else{
            alertNotSupportedFile.show();
        }
    }

    public void setFileChooserSecondContent(Stage window) throws IOException {
        fileChooserSecondContent = new FileChooser();
        fileChooserSecondContent.setTitle("Open Resource File");
        File file = fileChooserSecondContent.showOpenDialog(window);
        fileChooserSecondContent.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV FILE","*.csv"));
        if(checkSupportedFile(file)){
            String[] secondContent = readFile(file);
        }else{
            alertNotSupportedFile.show();
        }
    }

    public Boolean checkSupportedFile(File file){

        if(file.isFile() && file.getName().contains("MyProject") ){
            return true;
        }else{
            return false;
        }
    }
    // setup
    private void setupGridCompare() {

        gridCompareContent = new GridPane();
        gridCompareContent.setHgap(10);
        gridCompareContent.setVgap(10);
        gridCompareContent.setPadding(new Insets(20, 20, 20, 20));
        gridCompareContent.setAlignment(Pos.TOP_RIGHT);
        //gridCompareContent.setGridLinesVisible(true);

        int[] widths = {10, 10, 10, 10, 10, 10, 10,10,10,10};
        for (int i : widths) {
            ColumnConstraints constraintCol = new ColumnConstraints();
            constraintCol.setPercentWidth(i);
            gridCompareContent.getColumnConstraints().add(constraintCol);
        }
        int[] heights = {10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10};
        for (int i : heights) {
            RowConstraints constraintRow = new RowConstraints();
            constraintRow.setPercentHeight(i);
            gridCompareContent.getRowConstraints().add(constraintRow);
        }

        gridCompareContent.getStyleClass().addAll("grid","label","button");
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
            }
        });
        uploadSecondContent = new Button();
        uploadSecondContent.setText("Upload File 2");
        uploadSecondContent.setOnAction(event -> {
            try {
                setFileChooserSecondContent(parent.window);
            } catch (IOException e) {
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
        });
        gridCompareContent.add(uploadFirstContent, 0, 19);
        gridCompareContent.add(process, 9, 18);
        gridCompareContent.add(uploadSecondContent, 5, 19);
        gridCompareContent.add(clear, 9, 19);
    }

    private void setupLabelFieldsFirstContent(){

        LabelFieldFactory lfFactory = new LabelFieldFactory(gridCompareContent, 0, 2,2,1, Color.WHITE);
        for (String s : valuesFirstContent) {
            LabelFields.add(lfFactory.build(s));
        }
        Label fileOne = new Label();
        fileOne.setText("File 1");
        fileOne.setTextFill(Color.WHITE);
        gridCompareContent.add(fileOne,0,0);
    }

    private void setupLabelFieldsSecondContent() {

        LabelFieldFactory lfFactory = new LabelFieldFactory(gridCompareContent, 5, 2,2,1, Color.WHITE);
        for (String s : valuesSecondContent) {
            LabelFields.add(lfFactory.build(s));
        }
        Label fileTwo = new Label();
        fileTwo.setText("File 2");
        fileTwo.setTextFill(Color.WHITE);
        gridCompareContent.add(fileTwo,5,0);

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
            out.println(type.name());
        });

        gridCompareContent.add(fileType, 9, 17, 2, 1);
    }

    private void clearField(){

        workfile = null;
        LabelFields.stream().forEach(item->{
            item.Field.setText("");
        });
    }

    private String[] readFile(File file) throws IOException {

        String[] csvFile =  FileManager.read(file);

        for (String s: csvFile){
            System.out.println(s);
        }
        return csvFile;
    }

}
