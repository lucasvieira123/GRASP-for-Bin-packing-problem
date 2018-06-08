package custom;

import Model.Bin;
import Model.Item;

import java.util.List;

public class FirstFitInserction extends InserctionStrategy {


    public FirstFitInserction(List<Bin> bins, List<Item> items, float capacityBin) {
        super(bins, items, capacityBin);
    }

    @Override
    public void execute() {
        int answerAboutOperationAdd;

        for(Item currentItem : items ){

            if(bins.size() == 0){
                buildBin();
            }


            for(Bin currentBin : bins){
                answerAboutOperationAdd = currentBin.tryAddItem(currentItem);

                if(answerAboutOperationAdd == Bin.ITEM_ADDED){
                    break; // pass to next item
                }

                if (answerAboutOperationAdd == Bin.WEIGHT_ITEM_PASS_REST_CAPACITY){
                    //pass next bin
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


}
