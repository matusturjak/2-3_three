package sk.matusturjak.nodes;

import java.util.Comparator;

/**
 * Abstraktný predok vrcholov 2-3 stromu
 * @author Matúš
 * @param <T> 
 */
public abstract class AbstractNode<T extends Comparable<T>> {
    protected T data1;
    protected T data2;

    protected AbstractNode<T> parent;

    /**
     * 
     * @return prvé dáta vrchola
     */
    public T getData1() {
        return data1;
    }

    /**
     * 
     * @return druhé dáta vrchola
     */
    public T getData2() {
        return data2;
    }

    /**
     * 
     * @return otec vrchola 
     */
    public AbstractNode<T> getParent() {
        return parent;
    }

    /**
     * nastaví prvé dáta vrchola
     * @param data1 
     */
    public void setData1(T data1) {
        this.data1 = data1;
    }

    /**
     * nastaví druhé dáta vrchola
     * @param data2 
     */
    public void setData2(T data2) {
        this.data2 = data2;
    }

    /**
     * nastaví otca vrchola
     * @param parent 
     */
    public void setParent(AbstractNode<T> parent) {
        this.parent = parent;
    }

    /**
     * @return 0 ak je vrchol ľavým synom, 1 ak je vrchol stredným synom, 2 ak je vrchol pravým synom 
     */
    public int whichSonAmI() {
        TwoThreeNode<T> parent = (TwoThreeNode<T>) this.parent;
        if (parent.getLeftSon() == this)
            return 0;
        else if (parent.getMiddleSon() == this)
            return 1;
        else if (parent.getRightSon() == this)
            return 2;
        else
            return -1;
    }

    /**
     * metóda slúžiaca na porovnávanie prvkov vo vrchole 2-3 stromu
     * @param data1
     * @param data2
     * @param comparator
     * @return 1 ak data1 sú väčšie ako dataľ, -1 ak data1 sú menšie ako data2, inak 0
     */
    public int compare(T data1, T data2, Comparator<T> comparator) {
        return comparator == null ? data1.compareTo(data2) : comparator.compare(data1, data2);
    }
}
