package Model;

public class Item {
    static private Integer _id = 0;
    private Integer id;
    private Float weight;
    private boolean wasAdd = false;

    public Item(Float weight) {
        this.weight = weight;
        id =++_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public boolean isWasAdd() {
        return wasAdd;
    }

    public void setWasAdd(boolean wasAdd) {
        this.wasAdd = wasAdd;
    }

    static public void restart_Id(){
        _id = 0;
    }
}
