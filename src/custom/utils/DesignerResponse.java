package custom.utils;

import model.Bin;
import model.Item;

import java.util.List;

public class DesignerResponse {

    public static String strutureBinsInString(List<Bin> bins){
        float totalRestCapacity = 0;

        for(Bin bin : bins){
            totalRestCapacity = totalRestCapacity+ bin.getRestCapacity();
        }

        String struturedAnswer = "Count Bins: "+bins.size()+ "\n";
        struturedAnswer =  struturedAnswer.concat("Initial Capacity = "+bins.get(0).getInitialCapacity()+"\n");
        struturedAnswer = struturedAnswer.concat("Total Rest Capacity by Solution = "+totalRestCapacity+"\n");
        struturedAnswer = struturedAnswer.concat("Filled Capacity by Bin:\n");
        struturedAnswer = struturedAnswer.concat(DesignerResponse.structureFilledCapacitys(bins)+"\n");
        struturedAnswer = struturedAnswer.concat("Rest Capacity by Bin:\n");
        struturedAnswer = struturedAnswer.concat(DesignerResponse.structureRestCapacitys(bins)+"\n\n");


        for(Bin bin : bins){
            struturedAnswer = struturedAnswer.concat("[Bin-"+bin.getId()+" Fill_Capacity="
                    +bin.getFilledCapacity()+" Rest_Capacity="+bin.getRestCapacity()+"]\n");

            struturedAnswer = struturedAnswer.concat(strutureBinInString(bin));

            struturedAnswer = struturedAnswer.concat("\n\n");
        }

        return struturedAnswer+"\n\n";
    }



    public static String strutureBinInString(Bin bin){
        String struturedAnswer = "";
        for(Item item : bin.getItems()){
            struturedAnswer = struturedAnswer.concat(item.getWeight().toString()+"  ");
        }
        return struturedAnswer;
    }

    public static String strutureItemsInString(List<Item> listWithItems) {
        String weights = "";
        for(Item item : listWithItems){
            weights = weights.concat(item.getWeight()+"  ");
        }
        return weights+"\n";
    }


    public static String structureRestCapacitys(List<Bin> bins) {
        String restCapacitysInString = "";
        for(Bin bin : bins){
           restCapacitysInString = restCapacitysInString.concat(bin.getRestCapacity()+" ");
        }
        return restCapacitysInString;
    }

    private static String structureFilledCapacitys(List<Bin> bins) {
        String filledCapacitysInString = "";
        for(Bin bin : bins){
            filledCapacitysInString = filledCapacitysInString.concat(bin.getFilledCapacity()+" ");
        }

        return filledCapacitysInString;
    }
    }

