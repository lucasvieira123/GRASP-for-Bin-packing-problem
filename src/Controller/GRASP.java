package Controller;

import Model.Bin;
import Model.Item;
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
    private int order;


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

    public GRASP setOrder(int order) {
        this.order = order;
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

        SortHelp sortHelp = new SortHelp(order, greedyList);
        List<Item> greedyListOrdened = sortHelp.getOrdenedList();

        addItems(greedyListOrdened);
    }

    private void chunkRandom(List<Item> randomList) {
        Collections.shuffle(randomList);
        addItems(randomList);
    }

    private void addItems(List<Item> list) {
        Integer answerAboutOperationAdd;

        for(Item currentItem : list){

            if(bins.size() == 0){
                buildBin();
            }

            for(Bin currentBin : bins){
                answerAboutOperationAdd = currentBin.tryAddItem(currentItem);

                if(answerAboutOperationAdd == Bin.ITEM_ADDED){
                    break; // pass to next item
                }

                if (answerAboutOperationAdd == Bin.WEIGHT_ITEM_PASS_REST_CAPACITY){
                    continue;
                }

                if(answerAboutOperationAdd == Bin.WEIGHT_ITEM_PASS_INITIAL_CAPACITY){
                    System.out.println("item with weight above the capacity of the backpack!");
                    System.exit(0);
                }
            }

            if(!currentItem.isWasAdd()){
                Bin currentBinTmp = buildBin();
                answerAboutOperationAdd = currentBinTmp.tryAddItem(currentItem);

                if(answerAboutOperationAdd != Bin.ITEM_ADDED){
                    System.out.println("ANALISAR ESSE CASO QUE DÁ PROBLEMA!!!");
                    System.exit(0);
                }else {
                    continue; // pass to next item
                }
            }
        }
    }




    private Bin buildBin() {
        Bin bin = new Bin(capacityBin);
        bins.add(bin);
        return bin;
    }


    public GRASP setNumberExecution(int numberExecution) {
        this.numberExecution = numberExecution;
        return this;
    }
}
