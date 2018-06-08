package custom;

import Model.Bin;
import Model.Item;

import java.util.List;

public abstract class InserctionStrategy {
    protected final List<Bin> bins;
    protected final List<Item> items;
    protected final float capacityBin;

    public static final int FIRST_FIT = 0;
    public static final int BEST_FIT = 1;

    public InserctionStrategy(List<Bin> bins, List<Item> items, float capacityBin) {
        this.bins = bins;
        this.items = items;
        this.capacityBin = capacityBin;
    }

    public abstract void execute();

    protected Bin buildBin() {
        Bin bin = new Bin(capacityBin);
        bins.add(bin);
        return bin;
    }


}
