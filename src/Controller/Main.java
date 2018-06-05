package Controller;

import Model.Bin;
import Model.Item;
import custom.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    private TextField numberExecutionTxtField;

    @FXML
    private TextArea answerTextArea;

    private int sort = SortHelp.NONE_ORDER;



    FileChooser fileChooser = new FileChooser();
    private File selectedFile;
    private float capacityBin;
    private List<Item> items;
    private Float alfa;
    private int executionNumber;
    private String valueDefaulAlfa = "1.0";
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

        chooseBtn.setOnAction(event -> showChooserAndSetPathAction());

        alfaTxtField.setOnMouseClicked(event -> alfaTxtField.setText(""));

        coordinateEventSelectRadioButton(); // improve this method (desing Pattern)

        numberExecutionTxtField.setOnMouseClicked(event->numberExecutionTxtField.setText(""));
        
        startBtn.setOnAction(event -> {
            if(alfa == null || alfaTxtField.getText().isEmpty()){
                alfaTxtField.setText(valueDefaulAlfa);
            }

            alfa = Float.valueOf(alfaTxtField.getText());

            if(numberExecutionTxtField.getText().isEmpty()){
                numberExecutionTxtField.setText(valueDefaultNumberExecution);
            }

            float executionNumberInFloat = Float.valueOf(numberExecutionTxtField.getText());
            executionNumber = Math.abs((int)executionNumberInFloat);
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

            alfa = Float.valueOf(alfaTxtField.getText());

            int numberOfGreedy = (int)Math.ceil(numberItem * alfa);
            int numberOfRamdom = numberItem - numberOfGreedy;

            greedyTxtField.setText(String.valueOf(numberOfGreedy));
            randomTxtField.setText(String.valueOf(numberOfRamdom));

            startGrasp();


            
           
        });




    }

    private void showChooserAndSetPathAction() {
        selectedFile = openChooser();
        pathFileTxtFild.setText(selectedFile.getPath());
    }

    private File openChooser() {
       return fileChooser.showOpenDialog(primaryStage);
    }

    private void coordinateEventSelectRadioButton() {
        crescentSortRadioBt.setOnAction(event -> {
            sort = SortHelp.CRESCENT_ODER;
            crescentSortRadioBt.setSelected(true);
            descrescentSortRadioBt.setSelected(false);
            noneSortRadioBt.setSelected(false);
        });


        descrescentSortRadioBt.setOnAction(event -> {
            sort = SortHelp.DESCRESCENT_ODER;
            descrescentSortRadioBt.setSelected(true);
            crescentSortRadioBt.setSelected(false);
            noneSortRadioBt.setSelected(false);
        });

        noneSortRadioBt.setOnAction(event -> {
            sort = SortHelp.NONE_ORDER;
            noneSortRadioBt.setSelected(true);
            descrescentSortRadioBt.setSelected(false);
            crescentSortRadioBt.setSelected(false);
        });

    }

    private void startGrasp() {
        GRASP grasp = new GRASP();
        grasp.setNumberExecution(executionNumber)
                .setOrder(sort).setCapacityBin(capacityBin)
                .setItems(items).setAlfa(alfa).execute();
        String answer = strutureAnswer(grasp.getBestBinsList());

        answerTextArea.setText(answer);
    }

    private String strutureAnswer(List<Bin> bestBinsList) {
        String struturedAnswer = "Count Bins: "+bestBinsList.size()+ "\n";

        for(Bin bin : bestBinsList){
            struturedAnswer = struturedAnswer.concat("[Bin-"+bin.getId()+" Fill_Capacity="
                    +bin.getFilledCapacity()+" Rest_Fill_Capacity="+bin.getRestCapacity()+"]\n");

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
