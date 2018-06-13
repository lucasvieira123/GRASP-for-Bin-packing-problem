package custom.sort_strategy;

import model.Item;

import java.util.*;

public class SortHelp {

    public static final int CRESCENT_ODER = 0;
    public static final int DESCRESCENT_ORDER = 1;
    public static final int RANDOM_ORDER = 2;
    public static final int NONE_ORDER = 3;

    private int orderStyle;
    private List<Item> weightsItems = new ArrayList<>();

    public SortHelp(int orderStyle, List<Item>  weightsItems) {
        this.orderStyle = orderStyle;
        this.weightsItems = weightsItems;


    }

    public List<Item> getOrdenedList() {

        if(orderStyle == CRESCENT_ODER){ return  buildAscendingOrderList();}
        if (orderStyle == DESCRESCENT_ORDER) { return buildReverseOrderList();}
        if(orderStyle == RANDOM_ORDER) { return buildRandomOrderList();}

        return weightsItems;
        }

    private List<Item> buildRandomOrderList() {
        Collections.shuffle(weightsItems);
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
