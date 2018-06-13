package controller;

import custom.utils.Trace;
import model.Bin;
import model.Item;
import custom.inserction_strategy.BestFitInserction;
import custom.inserction_strategy.FirstFitInserction;
import custom.inserction_strategy.InserctionStrategy;
import custom.sort_strategy.SortHelp;
import custom.utils.DesignerResponse;

import javax.swing.table.TableRowSorter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GRASP {
    private float capacityBin;
    private List<Item> items = new ArrayList<>();
    private List<Bin> bins;

    private List<Bin> startSolutionBins;
    private List<Bin> bestSolutionAfterPertubationBins;
    private float bestCountBin = 9999999F;

    private float alfa;
    private int numberItem;
    private int numberOfGreedy;
    private int numberOfRamdom;
    private int numberExecution;
    private int sortType;
    private int inserctionType;
    private PrintWriter out;
    private Trace trace = Trace.getInstance();


    public List<Bin> getBestStartSolutionBins() {
        return startSolutionBins;
    }

    public void setStartSolutionBins(List<Bin> startSolutionBins) {
        this.startSolutionBins = startSolutionBins;
    }

    public float getBestCountBin() {
        return bestCountBin;
    }

    public void setBestCountBin(float bestCountBin) {
        this.bestCountBin = bestCountBin;
    }

    public float getCapacityBin() {
        return capacityBin;
    }

    public GRASP setCapacityBin(float capacityBin) {
        this.capacityBin = capacityBin;
        return this;
    }

    public List<Item> getItems() {
        return items;
    }

    public GRASP setItems(List<Item> items) {
        this.items = items;
        //pog
        for(Item item : items){
            item.setWasAdd(false);
        }

        this.numberItem = items.size();
        return this;
    }

    public float getAlfa() {
        return alfa;
    }

    public GRASP setAlfa(float alfa) {
        this.alfa = alfa;
        numberOfGreedy = (int)Math.ceil(numberItem * alfa);
        numberOfRamdom = numberItem - numberOfGreedy;
        return this;
    }

    public int getNumberOfGreedy() {
        return numberOfGreedy;
    }

    public int getNumberOfRamdom() {
        return numberOfRamdom;
    }

    public int getNumberItem() {
        return numberItem;
    }

    public GRASP setOrderType(int order) {
        this.sortType = order;
        return this;
    }

    public GRASP generateStartSolution() {

        // i must use aspact for print  the traces in file
        out = tryCreateTraceFile();
        trace.add("Items:");
        trace.add(DesignerResponse.strutureItemsInString(items));

        trace.add("Configuration:");
        trace.add(buildInserctionStrategy(inserctionType));
        trace.add(buildSortStrategyInString(sortType));
        trace.add("\n\n");

            bins = new ArrayList<>();
            List<Item> greedyList = new ArrayList<>(items.subList(0,numberOfGreedy));

            trace.add("greedyList: ");
            trace.add(DesignerResponse.strutureItemsInString(greedyList));

            List<Item> randomList = new ArrayList<>(items.subList(numberOfGreedy, items.size()));
            trace.add("randomList:");
            trace.add(DesignerResponse.strutureItemsInString(randomList));

            chunkGreedy(greedyList);
            chunkRandom(randomList);

            trace.add("Candidate Solution:");

            trace.add(DesignerResponse.strutureBinsInString(bins));

            if(bins.size() < bestCountBin){
                startSolutionBins = bins;
                bestCountBin = bins.size();
            }

       // }

        return this;
    }

    private PrintWriter tryCreateTraceFile() {

        try {
           return new PrintWriter("trace.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null; // bad action
    }




    private void chunkGreedy(List<Item> greedyList) {

        SortHelp sortHelp = new SortHelp(sortType, greedyList);
        List<Item> greedyListOrdened = sortHelp.getOrdenedList();

        trace.add("greedyListSorted:");

        trace.add(DesignerResponse.strutureItemsInString(greedyListOrdened));


        addItems(greedyListOrdened);
    }

    private void chunkRandom(List<Item> randomList) {

        Collections.shuffle(randomList);

        trace.add("randomList:");
        trace.add(DesignerResponse.strutureItemsInString(randomList));

        addItems(randomList);
    }

    private void addItems(List<Item> items) {

        InserctionStrategy inserctionStrategy = null;

        // improve this \/
        if(inserctionType == InserctionStrategy.FIRST_FIT){
            inserctionStrategy = new FirstFitInserction(bins, items, capacityBin);
        }else if(inserctionType == InserctionStrategy.BEST_FIT){
            inserctionStrategy = new BestFitInserction(bins, items, capacityBin);
        }

        inserctionStrategy.execute();

    }

    public GRASP setNumberExecution(int numberExecution) {
        this.numberExecution = numberExecution;
        return this;
    }

    public GRASP setInserctionType(int inserctionType) {
        this.inserctionType = inserctionType;
        return this;
    }

    public void executePertubation() {

        trace.add("Start Solution:");

        trace.add(DesignerResponse.strutureBinsInString(startSolutionBins));

        bestSolutionAfterPertubationBins = new ArrayList<>();

        for(int numberExecution = 0; numberExecution < this.numberExecution; numberExecution++){
            Bin randomBin1 = getRandomBin();
            Bin randomBin2 = getRandomBin();

            if(randomBin1 != randomBin2){
                Item randomItem = getRandomItem(randomBin1);
                Integer answerAboutAttemptAddItem = randomBin2.tryAddItem(randomItem);

                if(answerAboutAttemptAddItem == Bin.ITEM_ADDED){
                    if(randomBin1.getFilledCapacity() == 0){
                        startSolutionBins.remove(randomBin1);
                    }
                }
            }
        }

        bestSolutionAfterPertubationBins = startSolutionBins;

        trace.add("Best Solution:");

        trace.add(DesignerResponse.strutureBinsInString(bestSolutionAfterPertubationBins));

        trace.add("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><>\n");
        trace.add("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><>\n");
        trace.add("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><>\n");

        out.println(trace.getTraceString());

        out.close();
    }


    public List<Bin> getBestSolutionAfterPertubationBins() {
        return bestSolutionAfterPertubationBins;
    }

    public Bin getRandomBin() {

        int randomIndex = ThreadLocalRandom.current().nextInt(0,startSolutionBins.size()) ;
        return startSolutionBins.get(randomIndex);
    }

    private Item getRandomItem(Bin bin) {
        List<Item> itemsFromBin = bin.getItems();
        int randomIndex = ThreadLocalRandom.current().nextInt(0,itemsFromBin.size()) ;
        return itemsFromBin.get(randomIndex);
    }

    private String buildSortStrategyInString(Integer sortType) {
        String sortStrategy = "";
        if(sortType == SortHelp.CRESCENT_ODER){
            sortStrategy = "CRESCENT_ODER";
        }else if(sortType == SortHelp.DESCRESCENT_ORDER){
            sortStrategy ="DESCRESCENT_ORDER";
        }else if(sortType == SortHelp.RANDOM_ORDER){
            sortStrategy ="RANDOM_ORDER";
        }else {
            sortStrategy ="NONE_ORDER";
        }

        return sortStrategy;
    }

    private String buildInserctionStrategy(Integer inserctionStrategysType) {
        String inserctionStrategy = "";
        if(inserctionStrategysType == InserctionStrategy.FIRST_FIT){
           inserctionStrategy = "FIRST_FIT";
        }else {
            inserctionStrategy = "BEST_FIT";
        }

        return inserctionStrategy+"\n";
    }

}
