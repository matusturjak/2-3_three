package structure.nodes;

import java.util.Comparator;

/**
 * Trieda reprezentujúca vrchol 2-3 stromu
 * @author Matúš
 * @param <T> 
 */
public class TwoThreeNode<T extends Comparable<T>> extends AbstractNode<T> {
    private AbstractNode<T> leftSon;
    private AbstractNode<T> middleSon;
    private AbstractNode<T> rightSon;

    /**
     * parametrický konštruktor triedy
     * @param data1
     * @param data2 
     */
    public TwoThreeNode(T data1, T data2) {
        this.data1 = data1;
        this.data2 = data2;

        this.parent = null;

        this.leftSon = null;
        this.middleSon = null;
        this.rightSon = null;
    }

    /**
     * parametrický konštruktor triedy
     * @param data 
     */
    public TwoThreeNode(T data) {
        this.data1 = data;

        this.parent = null;

        this.leftSon = null;
        this.middleSon = null;
        this.rightSon = null;
    }

    /**
     * 
     * @return ľavý syn vrcholu 
     */
    public AbstractNode<T> getLeftSon() {
        return leftSon;
    }

    public void setLeftSon(AbstractNode<T> leftSon) {
        this.leftSon = leftSon;
    }

    /**
     * 
     * @return stredný syn vrcholu 
     */
    public AbstractNode<T> getMiddleSon() {
        return middleSon;
    }

    public void setMiddleSon(AbstractNode<T> middleSon) {
        this.middleSon = middleSon;
    }

    /**
     * 
     * @return pravý syn vrcholu 
     */
    public AbstractNode<T> getRightSon() {
        return rightSon;
    }

    public void setRightSon(AbstractNode<T> rightSon) {
        this.rightSon = rightSon;
    }

    /**
     * 
     * @return true ak je vrchol 2-vrchol, inak false
     */
    public boolean is2Node() {
        return this.data1 != null && this.data2 == null;
    }

    /**
     * 
     * @return true, ak je vrchol 3-vrchol, inak false
     */
    public boolean is3Node() {
        return this.data1 != null && this.data2 != null;
    }

    /**
     * metóda, pomocou ktorej sa dáta vložia do vrcholu na správnu pozíciu
     * @param data
     * @param comparator 
     */
    public void addData(T data, Comparator<T> comparator) {
        if (this.compare(data, this.data1, comparator) < 0) {
            T pomData = this.data1;
            this.data1 = data;
            this.data2 = pomData;
        } else {
            this.data2 = data;
        }
    }

    /**
     * 
     * @return true ak je vrchol list, inak false 
     */
    public boolean isLeaf() {
        return this.leftSon == null && this.rightSon == null && this.middleSon == null;
    }

    /**
     * 
     * @return true ak neobsahuje žiadne dáta, inak false
     */
    public boolean isEmpty() {
        return this.data1 == null && this.data2 == null;
    }

    /**
     * 
     * @return true ak je vrchol ľavým synom, inak false
     */
    public boolean isLeftSon() {
        TwoThreeNode<T> parent = (TwoThreeNode<T>) this.parent;
        return parent != null && parent.leftSon == this;
    }

    /**
     * 
     * @return true ak je vrchol stredným synom, inak false 
     */
    public boolean isMiddleSon() {
        TwoThreeNode<T> parent = (TwoThreeNode<T>) this.parent;
        return parent != null && parent.middleSon == this;
    }

    /**
     * 
     * @return true ak je vrchol pravým synom, inak false 
     */
    public boolean isRightSon() {
        TwoThreeNode<T> parent = (TwoThreeNode<T>) this.parent;
        return parent != null && parent.rightSon == this;
    }

    /**
     * metóda, ktorá vymaže zadané dáta z vrcholu
     * @param data
     * @param comparator 
     */
    public void deleteData(T data, Comparator<T> comparator) {
        if (this.compare(this.data1, data, comparator) == 0) {
            this.data1 = this.data2;

            this.data2 = null;
        } else {
            this.data2 = null;
        }
    }

    @Override
    public String toString() {
        return "TwoThreeNode{" +
                "data1=" + data1 +
                ", data2=" + data2 +
                '}';
    }
}
