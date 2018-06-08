package Controller;

import Model.Bin;
import Model.Item;
import custom.BestFitInserction;
import custom.FirstFitInserction;
import custom.InserctionStrategy;
import custom.SortHelp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GRASP {
    private float capacityBin;
    private List<Item> items;
    private List<Bin> bins;

    private List<Bin> bestBinsList;
    private float bestCountBin = 9999999F;

    private float alfa;
    private int numberItem;
    private int numberOfGreedy;
    private int numberOfRamdom;
    private int numberExecution;
    private int orderStyle;
    private int inserctionType;


    public List<Bin> getBestBinsList() {
        return bestBinsList;
    }

    public void setBestBinsList(List<Bin> bestBinsList) {
        this.bestBinsList = bestBinsList;
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
        this.orderStyle = order;
        return this;
    }

    public void execute() {

        for(int ne = 0; ne < numberExecution; ne ++){
            bins = new ArrayList<>();
            List<Item> greedyList = new ArrayList<>(items.subList(0,numberOfGreedy));
            List<Item> randomList = new ArrayList<>(items.subList(numberOfGreedy, items.size()));

            chunkGreedy(greedyList);
            chunkRandom(randomList);

            if(bins.size() < bestCountBin){
                bestBinsList = bins;
            }

        }
    }


    private void chunkGreedy(List<Item> greedyList) {

        SortHelp sortHelp = new SortHelp(orderStyle, greedyList);
        List<Item> greedyListOrdened = sortHelp.getOrdenedList();

        addItems(greedyListOrdened);
    }

    private void chunkRandom(List<Item> randomList) {
        Collections.shuffle(randomList);
        addItems(randomList);
    }

    private void addItems(List<Item> itemList) {

        InserctionStrategy inserctionStrategy = null;

        // improve this \/
        if(inserctionType == InserctionStrategy.FIRST_FIT){
            inserctionStrategy = new FirstFitInserction(bins,itemList,capacityBin);
        }else if(inserctionType == InserctionStrategy.BEST_FIT){
            inserctionStrategy = new BestFitInserction(bins,itemList,capacityBin);
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
}
