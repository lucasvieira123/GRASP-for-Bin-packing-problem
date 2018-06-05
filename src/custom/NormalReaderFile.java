package custom;

import Model.Item;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NormalReaderFile implements ReaderFile {

    private File selectedFile;
    private Float capacityBin;
    private Integer numberOfItems;
    private List<Item> items;


    public NormalReaderFile(File selectedFile) {
        this.selectedFile = selectedFile;
        _readAllSettings();
    }

    @Override
    public void _readAllSettings() {

       BufferedReader br = ReaderHelp.tryGetBufferedReader(selectedFile);

        if (br == null) return;

        String lineWithNumberOfItemsAndCapacityBin = ReaderHelp.tryReadLine(br);
        setNumberOfItemsAndCapacityBin(lineWithNumberOfItemsAndCapacityBin);

        items = new ArrayList<>(numberOfItems);

        String lineWithValuesAboutItems = ReaderHelp.tryReadLine(br);

        while (lineWithValuesAboutItems != null && !lineWithValuesAboutItems.equals("")) {

                readLineWithValuesAboutItemsAndCreateObjectItems(lineWithValuesAboutItems);

                lineWithValuesAboutItems = ReaderHelp.tryReadLine(br);

        }

    }

    private void readLineWithValuesAboutItemsAndCreateObjectItems(String lineWithValuesAboutItems) {
        String[] valuesAboutItemsWeightStrings = lineWithValuesAboutItems.
                trim().split("\\s+");

        float weightItem;
        for (int i = 0; i < valuesAboutItemsWeightStrings.length; i++) {
            weightItem = Float.valueOf(valuesAboutItemsWeightStrings[i]);
            Item item = new Item(weightItem);
            items.add(item);

        }

    }

    @Override
    public float getCapacityBin() {
        return capacityBin;
    }

    @Override
    public int getNumberOfItems() {
        return numberOfItems;
    }

    @Override
    public List<Item> getItems() {
        return items;
    }

    public void setNumberOfItemsAndCapacityBin(String lineWithNumberOfItemsAndCapacityBin) {
        String[] numberOfKnapsackAndNumberOfElementsInStrings = lineWithNumberOfItemsAndCapacityBin.
                trim().split("\\s+");

        numberOfItems = Integer.valueOf(numberOfKnapsackAndNumberOfElementsInStrings[0]);
        capacityBin = Float.valueOf(numberOfKnapsackAndNumberOfElementsInStrings[1]);


    }


}
