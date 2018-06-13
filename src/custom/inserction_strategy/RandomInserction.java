package custom.inserction_strategy;

import model.Bin;
import model.Item;

import java.util.Collections;
import java.util.List;

public class RandomInserction extends InserctionStrategy {

    private final FirstFitInserction firstFitInserction;

    public RandomInserction(List<Bin> bins, List<Item> items, float capacityBin) {
        super(bins, items, capacityBin);

        Collections.shuffle(items);
         firstFitInserction = new FirstFitInserction(bins, items ,capacityBin);
    }

    @Override
    public void execute() {
        firstFitInserction.execute();
    }
}
