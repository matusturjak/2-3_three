package sk.matusturjak.tree.nodes;

import java.util.Comparator;

/**
 * Trieda reprezentujúca pomocný 4-vrchol 2-3 stromu. Využíva sa pri vkladaní do štruktúry
 * @author Matúš
 * @param <T> 
 */
public class FourNode<T extends Comparable<T>> extends AbstractNode<T> {
    private T data3;

    private AbstractNode<T> leftSon;
    private AbstractNode<T> leftMiddleSon;
    private AbstractNode<T> rightMiddleSon;
    private AbstractNode<T> rightSon;

    /**
     * parametrický konštruktor triedy
     * @param node 
     */
    public FourNode(TwoThreeNode<T> node) {
        this.data1 = node.getData1();
        this.data2 = node.getData2();

        this.leftSon = node.getLeftSon();
        this.leftMiddleSon = node.getMiddleSon();
    }

    /**
     * metóda, pomocou ktorej sa dáta vložia do vrcholu na správnu pozíciu
     * @param data
     * @param comparator 
     */
    public void addData(T data, Comparator<T> comparator) {
        T currData1 = this.data1;
        T currData2 = this.data2;

        if (this.compare(data, this.data1, comparator) < 0) {
            //vlavo
            this.data1 = data;
            this.data2 = currData1;
            this.data3 = currData2;
        } else if (this.compare(data, this.data1, comparator) > 0 && this.compare(data, this.data2, comparator) < 0) {
            //stred
            this.data2 = data;
            this.data3 = currData2;
        } else {
            this.data3 = data;
        }
    }

    /**
     * 
     * @return tretí prvok vrcholu 
     */
    public T getData3() {
        return data3;
    }

    /**
     * nastaví tretí prvok vrcholu
     * @param data3 
     */
    public void setData3(T data3) {
        this.data3 = data3;
    }

    /**
     * 
     * @return ľavý syna vrchola
     */
    public AbstractNode<T> getLeftSon() {
        return leftSon;
    }

    /**
     * nastaví ľavého syna vrchola
     * @param leftSon 
     */
    public void setLeftSon(AbstractNode<T> leftSon) {
        this.leftSon = leftSon;
    }

    /**
     * 
     * @return stredno-ľavý syn vrchola
     */
    public AbstractNode<T> getLeftMiddleSon() {
        return leftMiddleSon;
    }

    /**
     * nastaví stredno-ľavého syna vrchola
     * @param leftMiddleSon 
     */
    public void setLeftMiddleSon(AbstractNode<T> leftMiddleSon) {
        this.leftMiddleSon = leftMiddleSon;
    }

    /**
     * 
     * @return stredno-pravý syn vrchola 
     */
    public AbstractNode<T> getRightMiddleSon() {
        return rightMiddleSon;
    }

    /**
     * nastaví stredno-pravého syna vrchola
     * @param rightMiddleSon 
     */
    public void setRightMiddleSon(AbstractNode<T> rightMiddleSon) {
        this.rightMiddleSon = rightMiddleSon;
    }

    /**
     * 
     * @return pravý syn vrchola 
     */
    public AbstractNode<T> getRightSon() {
        return rightSon;
    }

    /**
     * nastaví pravého syna vrchola
     * @param rightSon 
     */
    public void setRightSon(AbstractNode<T> rightSon) {
        this.rightSon = rightSon;
    }
}
