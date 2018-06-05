package custom;

import Model.Item;

import java.util.*;

public class SortHelp {

    public static final int CRESCENT_ODER = 0;
    public static final int DESCRESCENT_ODER = 1;
    public static final int NONE_ORDER = 2;
    private int order;
    private List<Item> weightsItems = new ArrayList<>();

    public SortHelp(int order, List<Item>  weightsItems) {
        this.order = order;
        this.weightsItems = weightsItems;


    }

    public List<Item> getOrdenedList() {

        if(order == CRESCENT_ODER){ return  buildAscendingOrderList();}
        if (order == DESCRESCENT_ODER) { return buildReverseOrderList();}

        return weightsItems;
        }



    private List<Item> buildReverseOrderList() {
        List<Item> sortedList = new ArrayList<>(weightsItems);

        sortedList.sort((e1, e2) -> e2.getWeight().compareTo(e1.getWeight()));

        return sortedList;

    }

    private  List<Item> buildAscendingOrderList() {
        List<Item> sortedList = new ArrayList<>(weightsItems);

        sortedList.sort(Comparator.comparing(Item::getWeight));

        return sortedList;
    }
}
