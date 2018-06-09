package Controller;

import Model.Bin;
import Model.Item;
import custom.*;
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

    ToggleGroup insertionCriterionToggleGruop = new ToggleGroup();

    ToggleGroup orderToggleGruop = new ToggleGroup();




    private int selectedInsertionStyle = InserctionStrategy.BEST_FIT;
    private int selectedSortStyle = SortHelp.NONE_ORDER;


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
        loader.setLocation(Main.class.getResource("/View/MainLayout.fxml"));
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

        chooseBtn.setOnAction(event -> showChooserAndSetPathAction());

        alfaTxtField.setOnMouseClicked(clearField(alfaTxtField));
        numberExecutionTxtField.setOnMouseClicked(clearField(numberExecutionTxtField));
        
        startBtn.setOnAction(event -> {

            Bin.restart_Id();
            Item.restart_Id();

            if(alfa == null || alfaTxtField.getText().isEmpty()){
                alfaTxtField.setText(valueDefaultAlfa);
            }

            alfa = Float.valueOf(alfaTxtField.getText());


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

            startGrasp();

        });




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
                        -> selectedInsertionStyle = (int) newValue.getUserData());

       orderToggleGruop.selectedToggleProperty()
               .addListener((observable, oldValue, newValue)
                       -> selectedSortStyle = (int) newValue.getUserData());

    }

    private void showChooserAndSetPathAction() {
        selectedFile = openChooser();
        pathFileTxtFild.setText(selectedFile.getPath());
    }

    private File openChooser() {
       return fileChooser.showOpenDialog(primaryStage);
    }



    private void startGrasp() {
        GRASP grasp = new GRASP();

        grasp.setNumberExecution(executionNumber)
                .setInserctionType(selectedInsertionStyle)
                .setOrderType(selectedSortStyle)
                .setCapacityBin(capacityBin)
                .setItems(items)
                .setAlfa(alfa)
                .execute();

        String answer = strutureAnswer(grasp.getBestBinsList());

        answerTextArea.setText(answer);
    }

    private String strutureAnswer(List<Bin> bestBinsList) {
        String struturedAnswer = "Count Bins: "+bestBinsList.size()+ "\n\n";

        for(Bin bin : bestBinsList){
            struturedAnswer = struturedAnswer.concat("[Bin-"+bin.getId()+" Fill_Capacity="
                    +bin.getFilledCapacity()+" Rest_Capacity="+bin.getRestCapacity()+"]\n");

            for(Item item : bin.getItems()){
                struturedAnswer = struturedAnswer.concat(item.getWeight().toString()+"  ");
            }

            struturedAnswer = struturedAnswer.concat("\n\n");
        }

        return struturedAnswer;

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
