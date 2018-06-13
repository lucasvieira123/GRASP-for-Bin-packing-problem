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
        struturedAnswer = struturedAnswer.concat("totalRestCapacity = "+totalRestCapacity+"\n\n");


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




}
