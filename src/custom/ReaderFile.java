package custom;


import model.Item;

import java.util.List;

public interface ReaderFile {

    void _readAllSettings();
    float getCapacityBin();
    int getNumberOfItems();
    List<Item> getItems();

}
