package Interface;

import Core.Analyzer.Benchmarks.Types;
import Core.Entry;
import Core.FileManager.CsvBuilder;
import Core.Report;
import Interface.Controls.LabelField;
import Interface.Controls.LabelFieldCollection;
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

public class CompareTabContent extends Control {

    private LabelFieldCollection lfCol1;
    private LabelFieldCollection lfCol2;
    private FileChooser fileChooserFirstContent;
    private FileChooser fileChooserSecondContent;
    private Alert alert;
    private Alert alertEmptyFile;
    private Alert alertNotSupportedFile;
    private Alert alertFileFirstContentEmpty;
    private Alert alertFileSecondContentEmpty;
    private Alert alterFileFirstAndSecondEmpty;
    private Gui parent;
    public GridPane gridCompareContent;
    private Button process;
    private Button uploadFirstContent;
    private Button uploadSecondContent;
    private Button clear;
    private Report reportFirstContent;
    private Report reportSecondContent;

    private File fileFirstContent;
    private File fileSecondContent;
    private File defaultInputDirectoryFirstContent = null;
    private File defaultInputDirectorySecondContent = null;

    String[] lfValues = {"Number of Words", "Number of Lines", "Number of Methods",
            "Number of Comments", "Cyclomatic complexity",
            "Halstead Volume", "Halstead Difficulty", "Halstead Effort", "Halstead Time To Code", "Halstead Delivered Bugs"};


    public CompareTabContent(Gui parent) {
        this.parent = parent;
        setupGridCompare();
        setupAlert();
        setupAlertEmptyFile();
        setupAlertNotSupportedFile();
        setupAlertFileFirstContentEmpty();
        setupAlertFileSecondContentEmpty();
        setupAlertFileFirstAndSecondContentEmpty();
        setupLabelFieldsFirstContent();
        setupLabelFieldsSecondContent();
        setupButtons();

    }

    public void setFileChooserFirstContent(Stage window) throws Exception {

        if (defaultInputDirectoryFirstContent == null)
            fileChooserFirstContent = new FileChooser();
        fileChooserFirstContent.setTitle("Open Resource File");
        File file = fileChooserFirstContent.showOpenDialog(window);
        fileChooserFirstContent.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV FILE", "*.csv"));

        if (checkSupportedFile(file)) {
            this.fileFirstContent = file;
            setReportFirstContent(file);
        } else {
            alertNotSupportedFile.show();
        }
    }

    public void setFileChooserSecondContent(Stage window) throws Exception {
        if (defaultInputDirectorySecondContent == null)
            fileChooserSecondContent = new FileChooser();
        fileChooserSecondContent.setTitle("Open Resource File");
        File file = fileChooserSecondContent.showOpenDialog(window);
        fileChooserSecondContent.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV FILE", "*.csv"));

        if (checkSupportedFile(file)) {
            this.fileSecondContent = file;
            setReportSecondContent(file);
        } else {
            alertNotSupportedFile.show();
        }
    }

    public Boolean checkSupportedFile(File file) {

        if (file.isFile() && file.exists() && file.getName().contains("Project")) {
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

    private void setupAlertFileFirstContentEmpty() {
        alertFileFirstContentEmpty = new Alert(Alert.AlertType.WARNING);
        alertFileFirstContentEmpty.setTitle("Warning Dialog");
        alertFileFirstContentEmpty.setHeaderText("The File 1 is not uploaded");
        alertFileFirstContentEmpty.setContentText("Please select the file 1");

    }

    private void setupAlertFileSecondContentEmpty() {
        alertFileSecondContentEmpty = new Alert(Alert.AlertType.WARNING);
        alertFileSecondContentEmpty.setTitle("Warning Dialog");
        alertFileSecondContentEmpty.setHeaderText("The File 2 is not uploaded");
        alertFileSecondContentEmpty.setContentText("Please select the file 2");

    }

    private void setupAlertFileFirstAndSecondContentEmpty() {
        alterFileFirstAndSecondEmpty = new Alert(Alert.AlertType.WARNING);
        alterFileFirstAndSecondEmpty.setTitle("Warning Dialog");
        alterFileFirstAndSecondEmpty.setHeaderText("The File 1 and the File 2 are not uploaded");
        alterFileFirstAndSecondEmpty.setContentText("Please select the File 1 and File 2");

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

            if (fileFirstContent == null && fileSecondContent == null) {
                alterFileFirstAndSecondEmpty.show();
            } else if (fileFirstContent == null) {

                alertFileFirstContentEmpty.show();
            } else if (fileSecondContent == null) {

                alertFileSecondContentEmpty.show();
            } else if (fileFirstContent != null && fileSecondContent != null) {
                setReportValues(reportFirstContent, lfCol1);
                setReportValues(reportSecondContent, lfCol2);

                compareNumberOfWords();
                compareNumberOfLines();
                compareNumberOfMethods();
                compareNumberOfComments();
                compareCyclomaticComplexity();
                compareHalsted();

            }
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

    private void clearField() {

        fileFirstContent = null;
        fileSecondContent = null;
        defaultInputDirectoryFirstContent = null;
        defaultInputDirectorySecondContent = null;

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

    private void setReportSecondContent(File file) throws Exception {

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

    private String setNumberOfWords(Report report, LabelFieldCollection collection) {

        String value = "";
        for (Entry e : report.Entries) {
            if (e.Type == Types.WordCount) {
                LabelField lf = collection.find("Words");
                if (lf != null) {
                    value = e.Values.get("value").toString();
                    lf.Field.setText(value);
                }
                break;
            }
        }
        return value;
    }

    private String setNumberOfLines(Report report, LabelFieldCollection collection) {

        String value = "";
        for (Entry e : report.Entries) {
            if (e.Type == Types.LinesCount) {
                LabelField lf = collection.find("Lines");

                if (lf != null) {
                    value = e.Values.get("value").toString();
                    lf.Field.setText(value);
                }
                break;
            }
        }
        return value;
    }

    private String setNumberOfMethods(Report report, LabelFieldCollection collection) {

        String value = "";
        for (Entry e : report.Entries) {
            if (e.Type == Types.MethodCount) {
                LabelField lf = collection.find("Methods");
                if (lf != null) {
                    value = e.Values.get("value").toString();
                    lf.Field.setText(value);
                }
                break;
            }
        }
        return value;
    }

    private String setNumberOfComments(Report report, LabelFieldCollection collection) {

        String value = "";
        for (Entry e : report.Entries) {
            if (e.Type == Types.CommentCount) {
                LabelField lf = collection.find("Comments");

                if (lf != null) {
                    value = e.Values.get("value").toString();
                    lf.Field.setText(value);
                }
                break;
            }
        }
        return value;
    }

    private String setCyclomaticComplexity(Report report, LabelFieldCollection collection) {

        String value = "";

        int sum = 0;
        for (Entry e : report.Entries) {
            if (e.Type == Types.Cyclomatic) sum += (long) e.Values.get("value");
        }
        LabelField lf = collection.find("Cyclomatic");
        if (lf != null) {
            value = Long.toString(sum);
            lf.Field.setText(Long.toString(sum));
        }
        return value;
    }

    private String[] setHalstead(Report report, LabelFieldCollection collection) {
        int count = 0;
        String[] value = new String[5];
        for (Entry e : report.Entries) {
            if (e.Type == Types.Halstead) {
                for (String key : e.Values.keySet()) {
                    LabelField lf = collection.find(key);
                    if (lf != null) {
                        lf.Field.setText(new DecimalFormat("0.00").format(e.Values.get(key)));
                        value[count] = new DecimalFormat("0.00").format(e.Values.get(key));
                        count ++;
                    }
                }
                break;
            }
        }return value;
    }



    private void compareNumberOfWords() {

        int numberOfWordsFirstContent = Integer.parseInt(setNumberOfWords(reportFirstContent, lfCol1));
        int numberOfWordsSecondContent = Integer.parseInt(setNumberOfWords(reportSecondContent, lfCol2));

        LabelField fieldOne = lfCol1.find("Words");
        LabelField fieldTwo = lfCol2.find("Words");

        if (numberOfWordsFirstContent > numberOfWordsSecondContent) {
            setColorIfGreater(fieldOne, fieldTwo);
        } else if (numberOfWordsFirstContent < numberOfWordsSecondContent) {
            setColorIfSmaller(fieldOne, fieldTwo);
        } else {
            setColorIfEquals(fieldOne, fieldTwo);
        }
    }

    private void compareNumberOfLines() {

        int numberOfLinesFirstContent = Integer.parseInt(setNumberOfLines(reportFirstContent, lfCol1));
        int numberOfLinesSecondContent = Integer.parseInt(setNumberOfLines(reportSecondContent, lfCol2));

        LabelField fieldOne = lfCol1.find("Lines");
        LabelField fieldTwo = lfCol2.find("Lines");

        if (numberOfLinesFirstContent > numberOfLinesSecondContent) {
            setColorIfGreater(fieldOne, fieldTwo);
        } else if (numberOfLinesFirstContent < numberOfLinesSecondContent) {
            setColorIfSmaller(fieldOne, fieldTwo);
        } else if (numberOfLinesFirstContent == numberOfLinesSecondContent) {
            setColorIfEquals(fieldOne, fieldTwo);
        }
    }

    private void compareNumberOfComments() {

        int numberOfCommentsFirstContent = Integer.parseInt(setNumberOfComments(reportFirstContent, lfCol1));
        int numberOfCommentsSecondContent = Integer.parseInt(setNumberOfComments(reportSecondContent, lfCol2));

        LabelField fieldOne = lfCol1.find("Comments");
        LabelField fieldTwo = lfCol2.find("Comments");

        if (numberOfCommentsFirstContent > numberOfCommentsSecondContent) {
            setColorIfGreater(fieldOne, fieldTwo);
        } else if (numberOfCommentsFirstContent < numberOfCommentsSecondContent) {
            setColorIfSmaller(fieldOne, fieldTwo);
        } else if (numberOfCommentsFirstContent == numberOfCommentsSecondContent) {
            setColorIfEquals(fieldOne, fieldTwo);
        }
    }

    private void compareNumberOfMethods() {

        int numberOfMethodsFirstContent = Integer.parseInt(setNumberOfMethods(reportFirstContent, lfCol1));
        int numberOfMethodsSecondContent = Integer.parseInt(setNumberOfMethods(reportSecondContent, lfCol2));

        LabelField fieldOne = lfCol1.find("Methods");
        LabelField fieldTwo = lfCol2.find("Methods");

        if (numberOfMethodsFirstContent > numberOfMethodsSecondContent) {
            setColorIfGreater(fieldOne, fieldTwo);
        } else if (numberOfMethodsFirstContent < numberOfMethodsSecondContent) {
            setColorIfSmaller(fieldOne, fieldTwo);
        } else if (numberOfMethodsFirstContent == numberOfMethodsSecondContent) {
            setColorIfEquals(fieldOne, fieldTwo);
        }
    }

    private void compareCyclomaticComplexity() {

        int cyclomaticFirstContent = Integer.parseInt(setNumberOfMethods(reportFirstContent, lfCol1));
        int cyclomaticSecondContent = Integer.parseInt(setNumberOfMethods(reportSecondContent, lfCol2));

        LabelField fieldOne = lfCol1.find("Cyclomatic");
        LabelField fieldTwo = lfCol2.find("Cyclomatic");

        if (cyclomaticFirstContent > cyclomaticSecondContent) {
            setColorIfGreater(fieldOne, fieldTwo);
        } else if (cyclomaticFirstContent < cyclomaticSecondContent) {
            setColorIfSmaller(fieldOne, fieldTwo);
        } else if (cyclomaticFirstContent == cyclomaticSecondContent) {
            setColorIfEquals(fieldOne, fieldTwo);
        }

    }

    private void compareHalsted() {

        String[] halsteadFirstContent = setHalstead(reportFirstContent,lfCol1);
        String[] halsteadSecondContent = setHalstead(reportSecondContent,lfCol2);

        double volumeFirstContent = Double.parseDouble(halsteadFirstContent[0]);
        double volumeSecondContent = Double.parseDouble(halsteadSecondContent[0]);

        double difficultyFirstContent = Double.parseDouble(halsteadFirstContent[1]);
        double difficultySecondContent = Double.parseDouble(halsteadSecondContent[1]);

        double effortFirstContent = Double.parseDouble(halsteadFirstContent[2]);
        double effortSecondContent = Double.parseDouble(halsteadSecondContent[2]);

        double timeToCodeFirstContent = Double.parseDouble(halsteadFirstContent[3]);
        double timeToCodeSecondContent =  Double.parseDouble(halsteadSecondContent[3]);

        double deliveryBugsFirstContent = Double.parseDouble(halsteadFirstContent[4]);
        double deliveryBugsSecondContent =  Double.parseDouble(halsteadSecondContent[4]);


        LabelField fieldOneVolume = lfCol1.find("Halstead Volume");
        LabelField fieldTwoVolume = lfCol2.find("Halstead Volume");

        LabelField fieldOneDifficulty = lfCol1.find("Halstead Difficulty");
        LabelField fieldTwoDifficulty =  lfCol2.find("Halstead Difficulty");

        LabelField fieldOneEffort = lfCol1.find("Halstead Effort");
        LabelField fieldTwoEffort = lfCol2.find("Halstead Effort");

        LabelField fieldOneTimeToCode = lfCol1.find("Halstead Time To Code");
        LabelField fieldTwoTimeToCode = lfCol2.find("Halstead Time To Code");

        LabelField fieldOneDeliveryBugs = lfCol1.find("Halstead Delivered Bugs");
        LabelField fieldTwoDeliveryBugs = lfCol2.find("Halstead Delivered Bugs");


        if(volumeFirstContent > volumeSecondContent){
            setColorIfGreater(fieldOneVolume,fieldTwoVolume);
        }else if (volumeFirstContent < volumeSecondContent){
            setColorIfSmaller(fieldOneVolume,fieldTwoVolume);
        }else if (volumeFirstContent == volumeSecondContent){
            setColorIfEquals(fieldOneVolume,fieldTwoVolume);
        }

        if(difficultyFirstContent > difficultySecondContent){
            setColorIfGreater(fieldOneDifficulty,fieldTwoDifficulty);
        }else if (difficultyFirstContent < difficultySecondContent){
            setColorIfSmaller(fieldOneDifficulty,fieldTwoDifficulty);
        }else if (difficultyFirstContent == difficultySecondContent){
            setColorIfEquals(fieldOneDifficulty,fieldTwoDifficulty);
        }

        if(effortFirstContent > effortSecondContent){
            setColorIfGreater(fieldOneEffort,fieldTwoEffort);
        }else if (effortFirstContent < effortSecondContent){
            setColorIfSmaller(fieldOneEffort,fieldTwoEffort);
        }else if (effortFirstContent == effortSecondContent){
            setColorIfEquals(fieldOneEffort,fieldTwoEffort);
        }

        if(timeToCodeFirstContent > timeToCodeSecondContent){
            setColorIfGreater(fieldOneTimeToCode,fieldTwoTimeToCode);
        }else if (timeToCodeFirstContent < timeToCodeSecondContent){
            setColorIfSmaller(fieldOneTimeToCode,fieldTwoTimeToCode);
        }else if (timeToCodeFirstContent == timeToCodeSecondContent){
            setColorIfEquals(fieldOneTimeToCode,fieldTwoTimeToCode);
        }

        if(deliveryBugsFirstContent > deliveryBugsSecondContent){
            setColorIfGreater(fieldOneDeliveryBugs,fieldTwoDeliveryBugs);
        }else if (deliveryBugsFirstContent < deliveryBugsSecondContent){
            setColorIfSmaller(fieldOneDeliveryBugs,fieldTwoDeliveryBugs);
        }else if (deliveryBugsFirstContent == deliveryBugsSecondContent){
            setColorIfEquals(fieldOneDeliveryBugs,fieldTwoDeliveryBugs);
        }


    }

    private void setColorIfGreater(LabelField one, LabelField two) {

        one.Field.setStyle("-fx-background-color: red;-fx-text-inner-color:white");
        two.Field.setStyle("-fx-background-color:green;-fx-text-inner-color:white");
    }

    private void setColorIfSmaller(LabelField one, LabelField two) {

        one.Field.setStyle("-fx-background-color: green;-fx-text-inner-color:white");
        two.Field.setStyle("-fx-background-color:red;-fx-text-inner-color:white");

    }

    private void setColorIfEquals(LabelField one, LabelField two) {

        one.Field.setStyle("-fx-background-color: blue;-fx-text-inner-color:white");
        two.Field.setStyle("-fx-background-color:blue;-fx-text-inner-color:white");

    }


}
