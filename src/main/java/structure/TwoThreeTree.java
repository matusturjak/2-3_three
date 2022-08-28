package structure;

import structure.nodes.AbstractNode;
import structure.nodes.FourNode;
import structure.nodes.TwoThreeNode;

import java.util.*;

/**
 * Trieda reprezentujúca 2-3 strom
 * @author Matúš
 * @param <T> 
 */
public class TwoThreeTree<T extends Comparable<T>> {
    private TwoThreeNode<T> root;
    private int size;
    private boolean founded = false;

    private Comparator<T> comparator;

    /**
     * konštruktor triedy
     */
    public TwoThreeTree() {
        this.root = null;
        this.size = 0;
    }

    /**
     * parametrický konštruktor. Parametrom je komparátor, podľa ktorého chceme porovnávať dáta v štruktúre
     * @param comparator 
     */
    public TwoThreeTree(Comparator<T> comparator) {
        this();
        this.comparator = comparator;
    }

    /**
     * metóda, ktorá vloží prvok do štruktúry
     * metóda bola implementovaná s pomocou textových podkladov a grafických ukážok z prednášky
     * @param data vkladané dáta
     * @return true ak vkladanie prebehlo úspešne, inak false
     */
    public boolean insertData(T data) {
        AbstractNode<T> curr = this.searchNode(data);

        if (this.founded) {
            return false;
        }

        if (curr == null) {
            this.root = new TwoThreeNode<>(data);

            this.incrementSize();
            return true;
        }

        if (curr instanceof TwoThreeNode && ((TwoThreeNode<T>) curr).is2Node()) {
            ((TwoThreeNode<T>) curr).addData(data, this.comparator);

            this.incrementSize();
            return true;
        }

        while (curr != null) {
            TwoThreeNode<T> parent = (TwoThreeNode<T>) curr.getParent();
            if (parent == null) {
                //parent je null -> treba rozdelit koren
                if (curr instanceof TwoThreeNode) {
                    T min = this.getMin(this.getMin(curr.getData1(), curr.getData2()), data);
                    T max = this.getMax(this.getMax(curr.getData1(), curr.getData2()), data);

                    T stred;
                    if (this.compare(data, min) != 0 && this.compare(data, max) != 0)
                        stred = data;
                    else if (this.compare(curr.getData1(), min) != 0 && this.compare(curr.getData1(), max) != 0)
                        stred = curr.getData1();
                    else
                        stred = curr.getData2();

                    TwoThreeNode<T> lMin = new TwoThreeNode<>(min);
                    TwoThreeNode<T> lMax = new TwoThreeNode<>(max);

                    TwoThreeNode<T> newParent = new TwoThreeNode<>(stred);

                    lMin.setParent(newParent);
                    lMax.setParent(newParent);

                    newParent.setLeftSon(lMin);
                    newParent.setRightSon(lMax);

                    this.root = newParent;
                } else {                   
                    FourNode<T> curr4 = (FourNode<T>) curr;
                    TwoThreeNode<T> newRoot = new TwoThreeNode<>(curr4.getData2());

                    TwoThreeNode<T> newLeft = new TwoThreeNode<>(curr4.getData1());
                    TwoThreeNode<T> newRight = new TwoThreeNode<>(curr4.getData3());

                    curr4.getLeftSon().setParent(newLeft);
                    curr4.getLeftMiddleSon().setParent(newLeft);

                    newLeft.setLeftSon(curr4.getLeftSon());
                    newLeft.setRightSon(curr4.getLeftMiddleSon());

                    curr4.getRightMiddleSon().setParent(newRight);
                    curr4.getRightSon().setParent(newRight);

                    newRight.setLeftSon(curr4.getRightMiddleSon());
                    newRight.setRightSon(curr4.getRightSon());

                    newLeft.setParent(newRoot);
                    newRight.setParent(newRoot);

                    newRoot.setLeftSon(newLeft);
                    newRoot.setRightSon(newRight);

                    this.root = newRoot;
                }
                break;
            }
            else if (parent.is2Node()) {
                //parent je dvojvrchol -> vlozim do neho prostredne data(stane sa z neho 3-vrchol) a koncim
                if (curr instanceof TwoThreeNode) {
                    T min = this.getMin(this.getMin(curr.getData1(), curr.getData2()), data);
                    T max = this.getMax(this.getMax(curr.getData1(), curr.getData2()), data);

                    T stred;
                    if (this.compare(data, min) != 0 && this.compare(data, max) != 0)
                        stred = data;
                    else if (this.compare(curr.getData1(), min) != 0 && this.compare(curr.getData1(), max) != 0)
                        stred = curr.getData1();
                    else
                        stred = curr.getData2();

                    TwoThreeNode<T> lMin = new TwoThreeNode<>(min);
                    TwoThreeNode<T> lMax = new TwoThreeNode<>(max);

                    lMin.setParent(parent);
                    lMax.setParent(parent);

                    if (stred != null) {
                        parent.addData(stred, this.comparator);
                        int which = curr.whichSonAmI();
                        if (which == 0) {
                            parent.setLeftSon(lMin);
                            parent.setMiddleSon(lMax);
                        } else if (which == 2) {
                            parent.setMiddleSon(lMin);
                            parent.setRightSon(lMax);
                        }
                    }
                } else {
                    //current je pomocny 4-vrchol
                    FourNode<T> curr4 = (FourNode<T>) curr;
                    T stred = curr4.getData2();

                    parent.addData(stred, this.comparator);

                    int which = curr4.whichSonAmI();
                    if (which == 0) {
                        TwoThreeNode<T> newLeft = new TwoThreeNode<>(curr4.getData1());
                        TwoThreeNode<T> newMiddle = new TwoThreeNode<>(curr4.getData3());

                        curr4.getLeftSon().setParent(newLeft);
                        curr4.getLeftMiddleSon().setParent(newLeft);

                        newLeft.setLeftSon(curr4.getLeftSon());
                        newLeft.setRightSon(curr4.getLeftMiddleSon());

                        curr4.getRightMiddleSon().setParent(newMiddle);
                        curr4.getRightSon().setParent(newMiddle);

                        newMiddle.setLeftSon(curr4.getRightMiddleSon());
                        newMiddle.setRightSon(curr4.getRightSon());

                        newLeft.setParent(parent);
                        newMiddle.setParent(parent);

                        parent.setLeftSon(newLeft);
                        parent.setMiddleSon(newMiddle);
                    } else if (which == 2) {
                        TwoThreeNode<T> newMiddle = new TwoThreeNode<>(curr4.getData1());
                        TwoThreeNode<T> newRight = new TwoThreeNode<>(curr4.getData3());

                        curr4.getLeftSon().setParent(newMiddle);
                        curr4.getLeftMiddleSon().setParent(newMiddle);

                        newMiddle.setLeftSon(curr4.getLeftSon());
                        newMiddle.setRightSon(curr4.getLeftMiddleSon());

                        curr4.getRightMiddleSon().setParent(newRight);
                        curr4.getRightSon().setParent(newRight);

                        newRight.setLeftSon(curr4.getRightMiddleSon());
                        newRight.setRightSon(curr4.getRightSon());

                        newMiddle.setParent(parent);
                        newRight.setParent(parent);

                        parent.setMiddleSon(newMiddle);
                        parent.setRightSon(newRight);
                    }
                }
                break;
            }
            else if (parent.is3Node()) {
                //vytvorim noveho parenta - pomocny 4-vrchol
                FourNode<T> newParent = new FourNode<>(parent);

                if (curr instanceof TwoThreeNode) {
                    
                    FourNode<T> newCurr = new FourNode<>((TwoThreeNode<T>) curr);
                    newCurr.addData(data, this.comparator);

                    newParent.addData(newCurr.getData2(), this.comparator);

                    TwoThreeNode<T> leftNode = new TwoThreeNode<>(newCurr.getData1());
                    TwoThreeNode<T> rightNode = new TwoThreeNode<>(newCurr.getData3());

                    leftNode.setParent(newParent);
                    rightNode.setParent(newParent);

                    int which = curr.whichSonAmI();
                    if (which == 0) {
                        //je lavy
                        newParent.setLeftSon(leftNode);
                        newParent.setLeftMiddleSon(rightNode);

                        newParent.setRightMiddleSon(parent.getMiddleSon());
                        newParent.setRightSon(parent.getRightSon());
                    } else if (which == 1) {
                        //je stredny
                        newParent.setLeftMiddleSon(leftNode);
                        newParent.setRightMiddleSon(rightNode);

                        newParent.setLeftSon(parent.getLeftSon());
                        newParent.setRightSon(parent.getRightSon());
                    } else {
                        //je pravy
                        newParent.setRightMiddleSon(leftNode);
                        newParent.setRightSon(rightNode);

                        newParent.setLeftSon(parent.getLeftSon());
                        newParent.setLeftMiddleSon(parent.getMiddleSon());
                    }
                } else {
                    FourNode<T> curr4 = (FourNode<T>) curr;
                    newParent.addData(curr4.getData2(), this.comparator);

                    TwoThreeNode<T> leftNode = new TwoThreeNode<>(curr4.getData1());
                    TwoThreeNode<T> rightNode = new TwoThreeNode<>(curr4.getData3());

                    //-------
                    curr4.getLeftSon().setParent(leftNode);
                    curr4.getLeftMiddleSon().setParent(leftNode);

                    leftNode.setLeftSon(curr4.getLeftSon());
                    leftNode.setRightSon(curr4.getLeftMiddleSon());

                    //---------
                    curr4.getRightMiddleSon().setParent(rightNode);
                    curr4.getRightSon().setParent(rightNode);

                    rightNode.setLeftSon(curr4.getRightMiddleSon());
                    rightNode.setRightSon(curr4.getRightSon());

                    leftNode.setParent(newParent);
                    rightNode.setParent(newParent);

                    int which = curr4.whichSonAmI();
                    if (which == 0) {
                        newParent.setLeftSon(leftNode);
                        newParent.setLeftMiddleSon(rightNode);

                        newParent.setRightMiddleSon(parent.getMiddleSon());
                        newParent.setRightSon(parent.getRightSon());
                    } else if (which == 1) {
                        newParent.setLeftMiddleSon(leftNode);
                        newParent.setRightMiddleSon(rightNode);

                        newParent.setLeftSon(parent.getLeftSon());
                        newParent.setRightSon(parent.getRightSon());
                    } else {
                        newParent.setRightMiddleSon(leftNode);
                        newParent.setRightSon(rightNode);

                        newParent.setLeftSon(parent.getLeftSon());
                        newParent.setLeftMiddleSon(parent.getMiddleSon());
                    }
                }
                if (parent.getParent() != null) {
                    //treba novemu parentovi(newParent) nastavit parenta
                    TwoThreeNode<T> grandParent = (TwoThreeNode<T>) parent.getParent();
                    if (grandParent != null) {
                        int which = parent.whichSonAmI();

                        if (which == 0) {
                            grandParent.setLeftSon(newParent);
                        } else if (which == 1) {
                            grandParent.setMiddleSon(newParent);
                        } else {
                            grandParent.setRightSon(newParent);
                        }
                    }
                    newParent.setParent(parent.getParent());
                }
                curr = newParent;
            }
        }

        this.incrementSize();
        return true;
    }

    /**
     * metóda, ktorá slúži na vyhľadanie vrcholu v ktorom sú hľadané dáta.
     * Ak sa hľadané dáta v štruktúre nenájdu, vráti sa posledný prvok na ktory sa narazilo.
     * metóda bola implementovaná s pomocou podkladov z prednášky
     * @param data hľadané dáta
     * @return vrchol s hľadanými dátami, inak null
     */
    public AbstractNode<T> searchNode(T data) {
        this.founded = false;
        
        if (data == null) {
            return null;
        }

        TwoThreeNode<T> curr = this.root;

        if (curr == null) {
            return null;
        }

        while (curr != null) {
            if (curr.is3Node()) {
                if (this.compare(curr.getData1(), data) == 0 || this.compare(curr.getData2(), data) == 0) {
                    this.founded = true;
                    return curr;
                } else if (this.compare(data, curr.getData1()) < 0) {
                    if (curr.getLeftSon() == null) {
                        this.founded = false;
                        return curr;
                    }
                    curr = (TwoThreeNode<T>) curr.getLeftSon();
                } else if (this.compare(data, curr.getData1()) > 0 && this.compare(data, curr.getData2()) < 0) {
                    if (curr.getMiddleSon() == null) {
                        this.founded = false;
                        return curr;
                    }
                    curr = (TwoThreeNode<T>) curr.getMiddleSon();
                } else if (this.compare(data, curr.getData2()) > 0) {
                    if (curr.getRightSon() == null) {
                        this.founded = false;
                        return curr;
                    }
                    curr = (TwoThreeNode<T>) curr.getRightSon();
                }
            } else if (curr.is2Node()) {
                if (this.compare(data, curr.getData1()) == 0) {
                    this.founded = true;
                    return curr;
                } else if (this.compare(data, curr.getData1()) < 0) {
                    if (curr.getLeftSon() == null) {
                        this.founded = false;
                        return curr;
                    }
                    curr = (TwoThreeNode<T>) curr.getLeftSon();
                } else if (this.compare(data, curr.getData1()) > 0) {
                    if (curr.getRightSon() == null) {
                        this.founded = false;
                        return curr;
                    }
                    curr = (TwoThreeNode<T>) curr.getRightSon();
                }
            }
        }
        return null;
    }
    
    /**
     * metóda, ktorá slúži na vyhľadanie dát
     * @param data hľadané dáta
     * @return dáta
     */
    public T searchData(T data) {
        TwoThreeNode<T> node = (TwoThreeNode<T>) this.searchNode(data);
        if (this.founded) {
            if (node.is2Node()) {
                return node.getData1();
            } else {
                return this.compare(data, node.getData1()) == 0 ? node.getData1() : node.getData2();
            }
        }
        return null;
    }

    /**
     * metóda pomocou ktorej sa zo štruktúry odoberú zadané dáta
     * metóda bola implementovaná s pomocou textových podkladov a grafických ukážok z prednášky
     * @param data
     * @return true ak sa mazanie podarilo, inak false
     */
    public boolean deleteData(T data) {
        TwoThreeNode<T> foundedNode = (TwoThreeNode<T>) this.searchNode(data);

        if (!this.founded) {
            return false;
        }
        TwoThreeNode<T> v = null;
        //odober prvok
        if (foundedNode.isLeaf()) {
            foundedNode.deleteData(data, this.comparator);

            v = foundedNode;
        } else {
            TwoThreeNode<T> successor = this.getInOrderSuccessor(foundedNode, this.compare(foundedNode.getData1(), data) == 0); //nasledovnik je v tomto pripade vzdy list
            //nahrad data
            T dataOfSuccessor = successor.getData1();

            if (this.compare(foundedNode.getData1(), data) == 0) {
                foundedNode.setData1(dataOfSuccessor);
            } else {
                foundedNode.setData2(dataOfSuccessor);
            }

            successor.deleteData(successor.getData1(), this.comparator);
            v = successor;
        }

        while (true) {
            //v aktualnom v je prvok - mozem skoncit
            if (!v.isEmpty()) {
                this.decrementSize();
                return true;
            }

            TwoThreeNode<T> parent = (TwoThreeNode<T>) v.getParent();
            if (parent == null) {
                //parent je null - nastavim ako root jeho syna
                if (v.getLeftSon() != null) {
                    v.getLeftSon().setParent(null);
                    this.root = (TwoThreeNode<T>) v.getLeftSon();
                } else if (v.getMiddleSon() != null) {
                    v.getMiddleSon().setParent(null);
                    this.root = (TwoThreeNode<T>) v.getMiddleSon();
                } else if (v.getRightSon() != null) {
                    v.getRightSon().setParent(null);
                    this.root = (TwoThreeNode<T>) v.getRightSon();
                } else {
                    this.root = null;
                }
                this.decrementSize();
                return true;
            }

            if (parent.is2Node()) {
                int which = v.whichSonAmI();

                if (which == 0) { //lavy
                    TwoThreeNode<T> rightSonOfParent = (TwoThreeNode<T>) parent.getRightSon();
                    if (rightSonOfParent.is3Node()) { //z praveho brata moze ist prvok do otca
                        T dataKo = parent.getData1();

                        v.setData1(dataKo);
                        parent.setData1(rightSonOfParent.getData1());
                        rightSonOfParent.deleteData(rightSonOfParent.getData1(), this.comparator);

                        if (v.getRightSon() != null) {
                            v.setLeftSon(v.getRightSon());
                        }

                        if (rightSonOfParent.getLeftSon() != null) {
                            rightSonOfParent.getLeftSon().setParent(v);
                        }
                        v.setRightSon(rightSonOfParent.getLeftSon());
                        rightSonOfParent.setLeftSon(rightSonOfParent.getMiddleSon());
                        rightSonOfParent.setRightSon(rightSonOfParent.getRightSon());
                        rightSonOfParent.setMiddleSon(null);
                        break;
                    } else {
                        //vlozim data z parenta do praveho syna, parent bude prazdny
                        rightSonOfParent.addData(parent.getData1(), this.comparator);
                        rightSonOfParent.setMiddleSon(rightSonOfParent.getLeftSon());
                        rightSonOfParent.setRightSon(rightSonOfParent.getRightSon());

                        parent.deleteData(parent.getData1(), this.comparator);
                        if (v.getLeftSon() != null) {
                            v.getLeftSon().setParent(rightSonOfParent);
                            rightSonOfParent.setLeftSon(v.getLeftSon());
                            v.setLeftSon(null);
                        } else if (v.getRightSon() != null) {
                            v.getRightSon().setParent(rightSonOfParent);
                            rightSonOfParent.setLeftSon(v.getRightSon());
                            v.setRightSon(null);
                        }
                        parent.setLeftSon(null);
                        v = parent;
                    }
                }  else { //pravy
                    TwoThreeNode<T> leftSonOfParent = (TwoThreeNode<T>) parent.getLeftSon();

                    if (leftSonOfParent.is3Node()) { // z laveho brata moze ist druhy prvok do otca
                        T dataKo = parent.getData1();

                        v.setData1(dataKo);
                        parent.setData1(leftSonOfParent.getData2());
                        leftSonOfParent.deleteData(leftSonOfParent.getData2(), this.comparator);

                        if (v.getLeftSon() != null) {
                            v.setRightSon(v.getLeftSon());
                        }

                        if (leftSonOfParent.getRightSon() != null) {
                            leftSonOfParent.getRightSon().setParent(v);
                        }
                        v.setLeftSon(leftSonOfParent.getRightSon());
                        leftSonOfParent.setRightSon(leftSonOfParent.getMiddleSon());
                        leftSonOfParent.setLeftSon(leftSonOfParent.getLeftSon());
                        leftSonOfParent.setMiddleSon(null);
                        break;
                    } else {
                        //vlozim data z parenta do laveho syna, parent bude prazdny
                        leftSonOfParent.addData(parent.getData1(), this.comparator);
                        leftSonOfParent.setMiddleSon(leftSonOfParent.getRightSon());
                        leftSonOfParent.setLeftSon(leftSonOfParent.getLeftSon());

                        parent.deleteData(parent.getData1(), this.comparator);
                        if (v.getLeftSon() != null) {
                            v.getLeftSon().setParent(leftSonOfParent);
                            leftSonOfParent.setRightSon(v.getLeftSon());
                            v.setLeftSon(null);
                        } else if (v.getRightSon() != null) {
                            v.getRightSon().setParent(leftSonOfParent);
                            leftSonOfParent.setRightSon(v.getRightSon());
                            v.setRightSon(null);
                        }
                        parent.setRightSon(null);
                        v = parent;
                    }
                }
            } else { //parent.is3Node()
                int which = v.whichSonAmI();

                if (which == 0) { //lavy
                    TwoThreeNode<T> middleSonOfParent = (TwoThreeNode<T>) parent.getMiddleSon();

                    if (middleSonOfParent.is3Node()) {
                        T dataKo = parent.getData1();

                        v.setData1(dataKo);
                        parent.setData1(middleSonOfParent.getData1());
                        middleSonOfParent.deleteData(middleSonOfParent.getData1(), this.comparator);

                        if (v.getRightSon() != null) {
                            v.setLeftSon(v.getRightSon());
                        }

                        if (middleSonOfParent.getLeftSon() != null) {
                            middleSonOfParent.getLeftSon().setParent(v);
                        }
                        v.setRightSon(middleSonOfParent.getLeftSon());
                        middleSonOfParent.setLeftSon(middleSonOfParent.getMiddleSon());
                        middleSonOfParent.setRightSon(middleSonOfParent.getRightSon());
                        middleSonOfParent.setMiddleSon(null);
                        break;
                    } else { //pravy brat je dvojvrchol - data z parenta vlozim do praveho brata
                        middleSonOfParent.addData(parent.getData1(), this.comparator);
                        parent.deleteData(parent.getData1(), this.comparator);

                        middleSonOfParent.setMiddleSon(middleSonOfParent.getLeftSon());
                        middleSonOfParent.setRightSon(middleSonOfParent.getRightSon());

                        if (v.getLeftSon() != null) {
                            v.getLeftSon().setParent(middleSonOfParent);
                            middleSonOfParent.setLeftSon(v.getLeftSon());
                            v.setLeftSon(null);
                        } else if (v.getRightSon() != null) {
                            v.getRightSon().setParent(middleSonOfParent);
                            middleSonOfParent.setLeftSon(v.getRightSon());
                            v.setRightSon(null);
                        }
                        parent.setLeftSon(middleSonOfParent);
                        parent.setRightSon(parent.getRightSon());
                        parent.setMiddleSon(null);
                        break;
                    }
                } else if (which == 1) { //stredny
                    TwoThreeNode<T> leftSonOfParent = (TwoThreeNode<T>) parent.getLeftSon();
                    TwoThreeNode<T> rightSonOfParent = (TwoThreeNode<T>) parent.getRightSon();

                    if (leftSonOfParent.is3Node()) {
                        T dataKo = parent.getData1();

                        v.setData1(dataKo);
                        parent.setData1(leftSonOfParent.getData2());
                        leftSonOfParent.deleteData(leftSonOfParent.getData2(), this.comparator);

                        if (v.getLeftSon() != null) {
                            v.setRightSon(v.getLeftSon());
                        }

                        if (leftSonOfParent.getRightSon() != null) {
                            leftSonOfParent.getRightSon().setParent(v);
                        }
                        v.setLeftSon(leftSonOfParent.getRightSon());
                        leftSonOfParent.setRightSon(leftSonOfParent.getMiddleSon());
                        leftSonOfParent.setLeftSon(leftSonOfParent.getLeftSon());
                        leftSonOfParent.setMiddleSon(null);
                        break;
                    } else if (rightSonOfParent.is3Node()) {
                        T dataKo = parent.getData2();

                        v.setData1(dataKo);
                        parent.setData2(rightSonOfParent.getData1());
                        rightSonOfParent.deleteData(rightSonOfParent.getData1(), this.comparator);

                        if (v.getRightSon() != null) {
                            v.setLeftSon(v.getRightSon());
                        }

                        if (rightSonOfParent.getLeftSon() != null) {
                            rightSonOfParent.getLeftSon().setParent(v);
                        }
                        v.setRightSon(rightSonOfParent.getLeftSon());
                        rightSonOfParent.setLeftSon(rightSonOfParent.getMiddleSon());
                        rightSonOfParent.setRightSon(rightSonOfParent.getRightSon());
                        rightSonOfParent.setMiddleSon(null);
                        break;
                    } else {
                        //operujem s pravym
                        rightSonOfParent.addData(parent.getData2(), this.comparator);
                        parent.deleteData(parent.getData2(), this.comparator);

                        rightSonOfParent.setMiddleSon(rightSonOfParent.getLeftSon());
                        rightSonOfParent.setRightSon(rightSonOfParent.getRightSon());

                        if (v.getLeftSon() != null) {
                            v.getLeftSon().setParent(rightSonOfParent);
                            rightSonOfParent.setLeftSon(v.getLeftSon());
                            v.setLeftSon(null);
                        } else if (v.getRightSon() != null) {
                            v.getRightSon().setParent(rightSonOfParent);
                            rightSonOfParent.setLeftSon(v.getRightSon());
                            v.setRightSon(null);
                        }

                        parent.setLeftSon(parent.getLeftSon());
                        parent.setRightSon(rightSonOfParent);
                        parent.setMiddleSon(null);
                        break;
                    }

                } else { //pravy
                    TwoThreeNode<T> middleSonOfParent = (TwoThreeNode<T>) parent.getMiddleSon();

                    if (middleSonOfParent.is3Node()) {
                        T dataKo = parent.getData2();

                        v.setData1(dataKo);
                        parent.setData2(middleSonOfParent.getData2());
                        middleSonOfParent.deleteData(middleSonOfParent.getData2(), this.comparator);

                        if (v.getLeftSon() != null) {
                            v.setRightSon(v.getLeftSon());
                        }

                        if (middleSonOfParent.getRightSon() != null) {
                            middleSonOfParent.getRightSon().setParent(v);
                        }
                        v.setLeftSon(middleSonOfParent.getRightSon());
                        middleSonOfParent.setRightSon(middleSonOfParent.getMiddleSon());
                        middleSonOfParent.setLeftSon(middleSonOfParent.getLeftSon());
                        middleSonOfParent.setMiddleSon(null);
                        break;
                    } else {
                        middleSonOfParent.addData(parent.getData2(), this.comparator);
                        parent.deleteData(parent.getData2(), this.comparator);

                        middleSonOfParent.setLeftSon(middleSonOfParent.getLeftSon());
                        middleSonOfParent.setMiddleSon(middleSonOfParent.getRightSon());

                        if (v.getLeftSon() != null) {
                            v.getLeftSon().setParent(middleSonOfParent);
                            middleSonOfParent.setRightSon(v.getLeftSon());
                            v.setLeftSon(null);
                        } else if (v.getRightSon() != null) {
                            v.getRightSon().setParent(middleSonOfParent);
                            middleSonOfParent.setRightSon(v.getRightSon());
                            v.setRightSon(null);
                        }

                        parent.setLeftSon(parent.getLeftSon());
                        parent.setRightSon(middleSonOfParent);
                        parent.setMiddleSon(null);
                        break;
                    }
                }
            }
        }
        this.decrementSize();
        return true;
    }
    
    /**
     * @return list všetkých vrcholov štruktúry zoradených v in order poradí podľa kľúča
     */
    public List<TwoThreeNode<T>> inOrderNodes() {
        TwoThreeNode<T> curr = this.root;

        Stack<TwoThreeNode<T>> stack = new Stack<>();
        List<TwoThreeNode<T>> list = new ArrayList<>();

        while (curr != null || stack.size() > 0)
        {
            while (curr !=  null)
            {
                stack.push(curr);
                curr = (TwoThreeNode<T>) curr.getLeftSon();
            }

            curr = stack.pop();

            if (curr.isLeaf()) {
                list.add(curr);
                curr = (TwoThreeNode<T>) curr.getRightSon();
            } else if (curr.is3Node()) {
                T lastData = list.get(list.size() - 1).getData1();
                if (this.compare(lastData, curr.getData1()) < 0) {
                    stack.push(curr);
                    list.add(curr);
                    curr = (TwoThreeNode<T>) curr.getMiddleSon();
                } else {
                    list.add(curr);
                    curr = (TwoThreeNode<T>) curr.getRightSon();
                }       
            } else {
                list.add(curr);
                curr = (TwoThreeNode<T>) curr.getRightSon();
            }
        }
        return list;
    }

    /**
     * @return list všetkých dát štruktúry zoradených v in order poradí podľa kľúča
     */
    public List<T> inOrderData() {
        TwoThreeNode<T> curr = this.root;

        Stack<TwoThreeNode<T>> stack = new Stack<>();
        List<T> list = new ArrayList<>();

        while (curr != null || stack.size() > 0)
        {
            while (curr !=  null)
            {
                stack.push(curr);
                curr = (TwoThreeNode<T>) curr.getLeftSon();
            }

            curr = stack.pop();

            if (curr.isLeaf()) {
                if (curr.is3Node()) {
                    list.add(curr.getData1());
                    list.add(curr.getData2());
                } else {
                    list.add(curr.getData1());
                }
                curr = (TwoThreeNode<T>) curr.getRightSon();
            } else if (curr.is3Node()) {
                if (this.compare(list.get(list.size() - 1), curr.getData1()) < 0) {
                    list.add(curr.getData1());
                    stack.push(curr);
                    curr = (TwoThreeNode<T>) curr.getMiddleSon();
                } else {
                    list.add(curr.getData2());
                    curr = (TwoThreeNode<T>) curr.getRightSon();
                }
            } else {
                list.add(curr.getData1());
                curr = (TwoThreeNode<T>) curr.getRightSon();
            }
        }
        return list;
    }
    
    /**
     * metóda, ktorá slúži na intervalové vyhľadávanie dát v štruktúre
     * @param from
     * @param to
     * @return list dát, ktoré patria do zadaného intervalu
     */
    public List<T> searchData(T from, T to) {
        TwoThreeNode<T> searched = (TwoThreeNode<T>) this.searchNode(from);
        List<T> list = new ArrayList<>();
        
        if (searched == null) {
            return new ArrayList<>();
        }
        
        T lastData = null;
        if (searched.isLeaf()) {
            if (searched.is3Node()) {
                if (this.compare(searched.getData1(), from) >= 0 && this.compare(searched.getData1(), to) <= 0) {
                    list.add(searched.getData1());
                }

                if (this.compare(searched.getData2(), from) >= 0 && this.compare(searched.getData2(), to) <= 0) {
                    list.add(searched.getData2());
                }
                lastData = searched.getData2();
            } else {
                if (this.compare(searched.getData1(), from) >= 0 && this.compare(searched.getData1(), to) <= 0) {
                    list.add(searched.getData1());
                }
                lastData = searched.getData1();
            }
        } else {
            if (this.compare(searched.getData1(), from) >= 0 && this.compare(searched.getData1(), to) <= 0) {
                list.add(searched.getData1());
            }
            lastData = searched.getData1();
        }
                
        TwoThreeNode<T> curr = searched;
        boolean middle = false;
        
        while (this.compare(lastData, to) < 0) {
                     
            TwoThreeNode<T> successor;
            if (curr.is3Node()) {
                if (this.compare(lastData, curr.getData1()) <= 0) {
                    middle = true;
                    successor = this.getInOrderSuccessor(curr, middle);
                } else {
                    middle = false;
                    successor = this.getInOrderSuccessor(curr, middle);
                }
                
                lastData = this.addDataFromSuccessorToList(list, successor, lastData, from, to);
                if (lastData == null) {
                    break;
                }
            } else {
                successor = this.getInOrderSuccessor(curr, middle);

                lastData = this.addDataFromSuccessorToList(list, successor, lastData, from, to);
                if (lastData == null) {
                    break;
                }
            }      
            curr = successor;       
        }
        return list;
    }
    
    /**
     * metóda, ktorá do zoznamu vloží dáta inorder nasledovníka ak sú zo zadaného intervalu
     * @param list
     * @param successor
     * @param lastData
     * @param from
     * @param to
     * @return ak je succesor 2-vrchol vráti jeho dáta, ak je 3-vrchol vráti jeho druhé dáta, null ak sú dáta v nasledovníkovy
     * väčšie ako je horná hranica
     */
    private T addDataFromSuccessorToList(List<T> list, TwoThreeNode<T> successor, T lastData, T from, T to) {
        if (successor == null)
            return null;
                
        if (this.compare(lastData, successor.getData1()) < 0) {
            if (successor.is3Node()) {
                if (this.compare(successor.getData2(), lastData) < 0) {
                    return null;
                }
                //ak je list a aj 3-vrchol mozem pridat obidve data vrchola
                if (successor.isLeaf()) {
                    if (this.compare(successor.getData1(), from) >= 0 && this.compare(successor.getData1(), to) <= 0) {
                        list.add(successor.getData1());
                    }

                    if (this.compare(successor.getData2(), from) >= 0 && this.compare(successor.getData2(), to) <= 0) {
                        list.add(successor.getData2());    
                    }
                    lastData = successor.getData2();
                } else {
                    if (this.compare(successor.getData1(), from) >= 0 && this.compare(successor.getData1(), to) <= 0) {
                        list.add(successor.getData1());
                    }
                    lastData = successor.getData1();
                }

            } else {
                if (this.compare(successor.getData1(), lastData) < 0) {
                    return null;
                }
                if (this.compare(successor.getData1(), from) >= 0 && this.compare(successor.getData1(), to) <= 0) {
                    list.add(successor.getData1());
                }
                lastData = successor.getData1();
            }

        } else {
            if (successor.is3Node()) {
                if (this.compare(successor.getData2(), lastData) < 0) {
                    return null;
                }
            } else {
                if (this.compare(successor.getData1(), lastData) < 0) {
                    return null;
                }
            }

            if (this.compare(successor.getData2(), from) >= 0 && this.compare(successor.getData2(), to) <= 0) {
                list.add(successor.getData2());
            }

            lastData = successor.getData2();
        }
        return lastData;
    }
    
    /**
     * metóda, ktorá pre zadaný vrchol vráti in order nasledovníka
     * @param node
     * @param middle
     * @return in order nasledovník
     */
    public TwoThreeNode<T> getInOrderSuccessor(TwoThreeNode<T> node, boolean middle) {
        TwoThreeNode<T> curr = node;
        if (curr.isLeaf()) {
            if (curr == this.root) {
                return null;
            }
            while (curr != this.root) {
                TwoThreeNode<T> parent = (TwoThreeNode<T>) curr.getParent();
                if (curr.whichSonAmI() == 0) {
                    return parent;
                } else if (curr.whichSonAmI() == 1) {
                    return parent;
                } else {
                    curr = parent;
                }
            }
            return curr;
        } else {
            if (curr.is3Node()) {
                if (middle) {
                    curr = (TwoThreeNode<T>) curr.getMiddleSon();              
                } else {
                    curr = (TwoThreeNode<T>) curr.getRightSon();
                }
            } else {
                curr = (TwoThreeNode<T>) curr.getRightSon();
            }
            while (curr.getLeftSon() != null) {
                curr = (TwoThreeNode<T>) curr.getLeftSon();
            }
            return curr;
        }
    }

    /**
     * 
     * @return koreň 
     */
    public TwoThreeNode<T> getRoot() {
        return root;
    }

    /**
     * inkrementuje veľkosť stromu
     */
    public void incrementSize() {
        this.size++;
    }

    /**
     * dekrementuje veľkosť stromu
     */
    public void decrementSize() {
        this.size--;
    }

    /**
     * 
     * @param data1
     * @param data2
     * @return minimum z dvoch prvkov
     */
    private T getMin(T data1, T data2) {
        return this.compare(data1, data2) < 0 ? data1 : data2;
    }

    /**
     * 
     * @param data1
     * @param data2
     * @return maximum z dvoch prvkov
     */
    private T getMax(T data1, T data2) {
        return this.compare(data1, data2) > 0 ? data1 : data2;
    }

    /**
     * 
     * @return veľkosť stromu
     */
    public int getSize() {
        return size;
    }

    public boolean isFounded() {
        return founded;
    }

    /**
     * metóda, ktorá slúži na porovnávanie dvoch dát
     * @param data1
     * @param data2
     * @return 1 ak data1 sú väčšie ako dataľ, -1 ak data1 sú menšie ako dataľ, inak é
     */
    private int compare(T data1, T data2) {
        return this.comparator == null ? data1.compareTo(data2) : this.comparator.compare(data1, data2);
    }
}
