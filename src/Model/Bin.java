package Model;

import java.util.ArrayList;
import java.util.List;

public class Bin {

    static private Integer _id = 0;
    private Integer id;
    private Float initialCapacity;
    private Float filledCapacity;
    private Float restCapacity;
    private List<Item> items = new ArrayList<>();

    public static final int ITEM_ADDED = 0;
    public static final int WEIGHT_ITEM_PASS_REST_CAPACITY = 2;
    public static final int WEIGHT_ITEM_PASS_INITIAL_CAPACITY = 3;


    public Bin(Float initialCapacity) {
        id = ++_id;
        this.initialCapacity = initialCapacity;
        this.filledCapacity =0F;
        this.restCapacity = initialCapacity;
    }

    public Integer getId() {
        return id;
    }

    public List<Item> getItems() {
        return items;
    }

    public Float getInitialCapacity() {
        return initialCapacity;
    }

    public Float getFilledCapacity() {
        return filledCapacity;
    }

    public void setFilledCapacity(Float filledCapacity) {
        this.filledCapacity = filledCapacity;
    }

    public Float getRestCapacity() {
        return restCapacity;
    }


    public int tryAddItem(Item item ){
        if(item.getWeight()> initialCapacity){
            return WEIGHT_ITEM_PASS_INITIAL_CAPACITY;
        }

        if( item.getWeight() > restCapacity){
            return WEIGHT_ITEM_PASS_REST_CAPACITY;
        }else {
            filledCapacity = filledCapacity + item.getWeight();
            restCapacity = initialCapacity - filledCapacity;
            item.setWasAdd(true);
            items.add(item);
            return ITEM_ADDED;



        }
    }
}
