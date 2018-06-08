package custom;

import Model.Bin;
import Model.Item;

import java.util.List;

public class BestFitInserction extends InserctionStrategy {

    public BestFitInserction(List<Bin> bins, List<Item> items, float capacityBin) {
        super(bins, items, capacityBin);
    }

    @Override
    public void execute() {
        Integer answerAboutOperationAdd;
        Bin binWithBetterFilling;
        float betterFillingValue = 99999999;


        for(Item currentItem : items ){

            binWithBetterFilling = null;
            betterFillingValue = 99999999;

            if(bins.isEmpty()){
               buildBin();
            }

            for(Bin currentBin : bins){
                if(fits(currentItem,currentBin)){
                    float currentRestCapacity = capacityBin - (currentBin.getFilledCapacity()+currentItem.getWeight());
                    if(currentRestCapacity < betterFillingValue){
                        binWithBetterFilling = currentBin;
                        betterFillingValue = currentRestCapacity;
                    }
                }


            }

            if(binWithBetterFilling == null){
               Bin newBin = buildBin();
               newBin.tryAddItem(currentItem);
            }else {

                answerAboutOperationAdd = binWithBetterFilling.tryAddItem(currentItem);

            if(answerAboutOperationAdd == Bin.ITEM_ADDED){
                continue; // pass to next item
            }

            if(answerAboutOperationAdd == Bin.WEIGHT_ITEM_PASS_INITIAL_CAPACITY){
                System.out.println("item with weight above the capacity of the backpack!");
                System.exit(0);
            }
            }

//          for(Bin currentBin: bins){
//              if(bins.size() == 1){
//                  currentBestBin = currentBin;
//
//              } else {
//                  currentBestBin =  getBestBin(currentBestBin, currentBin, currentItem);
//              }
//          }
//          if(currentBestBin == null){
//               Bin newBin = buildBin();
//               answerAboutOperationAdd = newBin.tryAddItem(currentItem);
//          }else {
//              answerAboutOperationAdd = currentBestBin.tryAddItem(currentItem);
//          }
//
//            if(answerAboutOperationAdd == Bin.ITEM_ADDED){
//                break; // pass to next item
//            }
//
//            if (answerAboutOperationAdd == Bin.WEIGHT_ITEM_PASS_REST_CAPACITY){
//                //pass next bin
//                continue;
//            }
//
//            if(answerAboutOperationAdd == Bin.WEIGHT_ITEM_PASS_INITIAL_CAPACITY){
//                System.out.println("item with weight above the capacity of the backpack!");
//                System.exit(0);
//            }








        }
    }

    /*private Bin getBestBin(Bin currentBestBin, Bin currentBin, Item currentItem) {
        Float deltaFromCurrentBestBin = null;
        Float deltaFromCurrentBin = null;

        if(fits(currentItem, currentBestBin) && fits(currentItem, currentBin)){
            deltaFromCurrentBestBin = getDelta(currentBestBin,currentItem);
            deltaFromCurrentBin = getDelta(currentBin,currentItem);
            if(deltaFromCurrentBestBin < deltaFromCurrentBin){
                return currentBestBin;
            }else {
                return currentBin;
            }
        }

        if(fits(currentItem, currentBestBin)){

        }

        if(fits(currentItem, currentBin)){

        }

        if(deltaFromCurrentBestBin < deltaFromCurrentBin){
            return currentBestBin;
        }

    }*/

    private float getDelta(Bin bin, Item item) {
        return capacityBin - (bin.getFilledCapacity() + item.getWeight());
    }

    private Bin getBetterBin(Bin currentBestBin, Bin currentBin, Item currentItem) {
        if(currentBestBin == null){
            return currentBin;
        }

        if(fits(currentItem, currentBin) && fits(currentItem, currentBestBin)){
            if(currentBestBin.getFilledCapacity()+currentItem.getWeight()
                    <= currentBin.getFilledCapacity()+currentItem.getWeight() ){
                return currentBestBin;
            }else {
                return currentBin;
            }
        }else if(fits(currentItem, currentBin)){
            return currentBin;
        }else if(fits(currentItem, currentBestBin)){
            return currentBestBin;
        }

        return null; // bad pratic
    }

    private boolean fits(Item currentItem, Bin currentBin) {
        if(currentItem.getWeight() > capacityBin ) {
            System.out.println("item with weight above the capacity of the backpack!");
            System.exit(0);
        }

        return currentBin.getRestCapacity() >= currentItem.getWeight();
    }


}
