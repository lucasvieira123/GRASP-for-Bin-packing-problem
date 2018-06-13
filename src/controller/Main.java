package controller;

import model.Bin;
import model.Item;
import custom.*;
import custom.inserction_strategy.InserctionStrategy;
import custom.sort_strategy.SortHelp;
import custom.utils.DesignerResponse;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class Main extends Application {

    private Stage primaryStage;
    private AnchorPane mainLayout;

    @FXML
    private TextField pathFileTxtFild;

    @FXML
    private TextField alfaTxtField;

    @FXML
    private TextField greedyTxtField;

    @FXML
    private TextField randomTxtField;
    
    @FXML
    private Button startBtn;

    @FXML
    private Button chooseBtn;

    @FXML
    private RadioButton crescentSortRadioBt;
    @FXML
    private RadioButton descrescentSortRadioBt;
    @FXML
    private RadioButton noneSortRadioBt;
    @FXML
    private RadioButton randomSortRadioBt;
    @FXML
    private RadioButton firstFitSortRadioBt;
    @FXML
    private RadioButton bestFitSortRadioBt;

    @FXML
    private TextField numberExecutionTxtField;

    @FXML
    private TextArea answerTextArea;

    @FXML
    private CheckBox automaticCheckBox;

    @FXML
    private Label insertionCriterionLabel;
    @FXML
    private Label sortTheGreedyLabel;

    ToggleGroup insertionCriterionToggleGruop = new ToggleGroup();

    ToggleGroup orderToggleGruop = new ToggleGroup();




    private int selectedInsertionType = InserctionStrategy.BEST_FIT;
    private int selectedSortType = SortHelp.NONE_ORDER;


    FileChooser fileChooser = new FileChooser();
    private File selectedFile;
    private float capacityBin;
    private List<Item> items;
    private Float alfa;
    private int executionNumber;
    private String valueDefaultAlfa = "1.0";
    private String valueDefaultNumberExecution = "1";

    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Bin-Packing with Grasp");
        configureFileChooser();
        initRootLayout();

    }

    private void configureFileChooser() {
        fileChooser.setTitle("Choose text file with settings (bbb_formato.txt)");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files","*.txt"));

    }

    private void initRootLayout() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/MainLayout.fxml"));
        try {
            mainLayout = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    @FXML
    private void initialize() {

        configureInsertionCriterionAndOrderRadioButtons();
        applyAutomaticConfiguration();


        chooseBtn.setOnAction(event -> showChooserAndSetPathAction());

        alfaTxtField.setOnMouseClicked(clearField(alfaTxtField));
        numberExecutionTxtField.setOnMouseClicked(clearField(numberExecutionTxtField));

        automaticCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(automaticCheckBoxSelected(newValue)){
                applyAutomaticConfiguration();

            }else {
                applyManualConfiguration();
            }
        });
        
        startBtn.setOnAction(event -> {

            Bin.restart_Id();
            Item.restart_Id();

            alfa = Float.valueOf(alfaTxtField.getText());

            if(alfa == null || alfaTxtField.getText().isEmpty()){
                alfaTxtField.setText(valueDefaultAlfa);
            }




            if(numberExecutionTxtField.getText().isEmpty()){
                numberExecutionTxtField.setText(valueDefaultNumberExecution);
            }

            float executionNumberInFloat = Float.valueOf(numberExecutionTxtField.getText());
            executionNumber = buildNatureValue(executionNumberInFloat);
            numberExecutionTxtField.setText(String.valueOf(executionNumber));

            // restrictions to fields
            if(!alfaIsValid(alfa)){
                alfaTxtField.setText("1.0");

            }

            if(!checkExistFile()){
               pathFileTxtFild.setText("ERRO PATH!");
                return;
            }

            readFile();

            int numberItem = readerFile.getNumberOfItems();
            int numberOfGreedy = (int)Math.ceil(numberItem * alfa);
            int numberOfRamdom = numberItem - numberOfGreedy;

            greedyTxtField.setText(String.valueOf(numberOfGreedy));
            randomTxtField.setText(String.valueOf(numberOfRamdom));
            boolean automaticGrasp = automaticCheckBox.isSelected();


            if(automaticGrasp){
                startAutomaticGrasp();
            }else {
                startManualGrasp();
            }




        });




    }

    private void applyManualConfiguration() {
        crescentSortRadioBt.setDisable(false);
        descrescentSortRadioBt.setDisable(false);
        noneSortRadioBt.setDisable(false);
        randomSortRadioBt.setDisable(false);
        firstFitSortRadioBt.setDisable(false);
        bestFitSortRadioBt.setDisable(false);
        sortTheGreedyLabel.setDisable(false);
        insertionCriterionLabel.setDisable(false);
    }

    private void applyAutomaticConfiguration() {
        crescentSortRadioBt.setDisable(true);
        descrescentSortRadioBt.setDisable(true);
        noneSortRadioBt.setDisable(true);
        randomSortRadioBt.setDisable(true);
        firstFitSortRadioBt.setDisable(true);
        bestFitSortRadioBt.setDisable(true);
        sortTheGreedyLabel.setDisable(true);
        insertionCriterionLabel.setDisable(true);
    }


    private boolean automaticCheckBoxSelected(Boolean newValue) {
        return newValue;
    }

    private int buildNatureValue(float floatNumber) {
        return Math.abs((int)floatNumber);
    }

    private EventHandler<? super MouseEvent> clearField(TextField textField) {
        return event -> textField.setText("");
    }

    private void configureInsertionCriterionAndOrderRadioButtons() {
        firstFitSortRadioBt.setToggleGroup(insertionCriterionToggleGruop);
        firstFitSortRadioBt.setUserData(InserctionStrategy.FIRST_FIT);

        bestFitSortRadioBt.setToggleGroup(insertionCriterionToggleGruop);
        bestFitSortRadioBt.setUserData(InserctionStrategy.BEST_FIT);

        crescentSortRadioBt.setToggleGroup(orderToggleGruop);
        crescentSortRadioBt.setUserData(SortHelp.CRESCENT_ODER);

        descrescentSortRadioBt.setToggleGroup(orderToggleGruop);
        descrescentSortRadioBt.setUserData(SortHelp.DESCRESCENT_ORDER);

        randomSortRadioBt.setToggleGroup(orderToggleGruop);
        randomSortRadioBt.setUserData(SortHelp.RANDOM_ORDER);

        noneSortRadioBt.setToggleGroup(orderToggleGruop);
        noneSortRadioBt.setUserData(SortHelp.NONE_ORDER);

        insertionCriterionToggleGruop.selectedToggleProperty()
                .addListener((observable, oldValue, newValue)
                        -> selectedInsertionType = (int) newValue.getUserData());

       orderToggleGruop.selectedToggleProperty()
               .addListener((observable, oldValue, newValue)
                       -> selectedSortType = (int) newValue.getUserData());

    }

    private void showChooserAndSetPathAction() {
        selectedFile = openChooser();
        pathFileTxtFild.setText(selectedFile.getPath());
    }

    private File openChooser() {
       return fileChooser.showOpenDialog(primaryStage);
    }



    private void startManualGrasp() {
        GRASP grasp = new GRASP();

        grasp.setNumberExecution(executionNumber)
                .setInserctionType(selectedInsertionType)
                .setOrderType(selectedSortType)
                .setCapacityBin(capacityBin)
                .setItems(items)
                .setAlfa(alfa)
                .generateStartSolution();
        String answer = DesignerResponse.strutureBinsInString(grasp.getBestStartSolutionBins());

        answerTextArea.setText(answer);
    }

    private void startAutomaticGrasp() {
        //bad code because it is very coupled. I must use reflection
        List<Integer> inserctionStrategysTypes = new ArrayList<>(2);
        inserctionStrategysTypes.add(InserctionStrategy.FIRST_FIT);
        inserctionStrategysTypes.add(InserctionStrategy.BEST_FIT);

        //bad code because it is very coupled. I must use reflection
        List<Integer> sortTypes = new ArrayList<>(3);
        sortTypes.add(SortHelp.CRESCENT_ODER);
        sortTypes.add(SortHelp.DESCRESCENT_ORDER);
        sortTypes.add(SortHelp.RANDOM_ORDER);
        sortTypes.add(SortHelp.NONE_ORDER);

        GRASP grasp = new GRASP();
//        Collections.shuffle(inserctionStrategysTypes);
//        Collections.shuffle(sortTypes);

        for(Integer inserctionStrategysType: inserctionStrategysTypes){
            for(Integer sortType: sortTypes){

                grasp.setNumberExecution(executionNumber)
                        .setInserctionType(inserctionStrategysType)
                        .setOrderType(sortType)
                        .setCapacityBin(capacityBin)
                        .setItems(items)
                        .setAlfa(alfa)
                        .generateStartSolution();


            }


        }

        grasp.executePertubation();
        String answer = DesignerResponse.strutureBinsInString(grasp.getBestSolutionAfterPertubationBins());

        answerTextArea.setText(answer);





    }






    private boolean alfaIsValid(float alfa) {
        return alfa>=0 && alfa<=1;


    }

    private void readFile() {
        readerFile = new NormalReaderFile(selectedFile);
        capacityBin = readerFile.getCapacityBin();
        items = readerFile.getItems();
    }

    ReaderFile readerFile;



    private boolean checkExistFile() {
        return selectedFile != null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
