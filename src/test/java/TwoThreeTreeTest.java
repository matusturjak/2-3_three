import org.junit.Test;
import sk.matusturjak.tree.TwoThreeTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import sk.matusturjak.tree.nodes.TwoThreeNode;

public class TwoThreeTreeTest {
    
    private Random seed = new Random();
    private Random random = new Random();
    
    @Test
    public void iterateTest() throws Exception {
        for (int i = 0; i < 10; i++) {
            searchDataTest();
        }
    }

    @Test
    public void insertDataAndThenDeleteRoot() throws Exception {
        this.random.setSeed(this.seed.nextInt());
        
        TwoThreeTree<TwoThreeObject> tree = new TwoThreeTree<>();

        int pom = 0;
        while (pom != 1000000) {
            int rand = random.nextInt(2100000000);

            TwoThreeObject obj = new TwoThreeObject(rand);
            if (tree.insertData(obj)) {
                pom++;
            }
        }

        while (tree.getSize() != 0) {
            if (!tree.deleteData(tree.getRoot().getData1())) {
                throw new Exception("mazanie sa nepodarilo");
            }
        }

        System.out.println("Tree size - " + tree.getSize());
        System.out.println("root - " + tree.getRoot());
    }

    @Test
    public void firstInsertAndThenDeleteTest() throws Exception {
        this.random.setSeed(this.seed.nextInt());
        
        TwoThreeTree<TwoThreeObject> tree = new TwoThreeTree<>();

        List<Integer> list = new ArrayList<>();

        int pom = 0;
        while (pom != 100000) {
            Integer randNum = random.nextInt(1000000000);
            TwoThreeObject obj = new TwoThreeObject(randNum);

            if (tree.insertData(obj)) {
                list.add(randNum);
                pom++;
            }
        }

        int size = list.size();
        for (int i = 0; i < size; i++) {
            int randNum = random.nextInt(list.size());
            Integer prvok = list.remove(randNum);

            TwoThreeObject obj = new TwoThreeObject(prvok);
            if (!tree.deleteData(obj)) {
                throw new Exception("nepodarilo sa vymazat");
            }
        }
        System.out.println("root - " + tree.getRoot());
        System.out.println("Tree size - " + tree.getSize());
        System.out.println("List size - " + list.size());
    }

    @Test
    public void randomOperationsTest() throws Exception {
        this.random.setSeed(this.seed.nextInt());
        
        TwoThreeTree<TwoThreeObject> tree = new TwoThreeTree<>();
        List<Integer> list = new ArrayList<>(1000000);

        int pom = 0;
        while (pom != 100000) {
            double rand = random.nextDouble();
            if (rand < 0.55) {
                int num = random.nextInt(2000000000);
                TwoThreeObject obj = new TwoThreeObject(num);
                
                if (tree.insertData(obj)) {
                    list.add(num);
                }
            } else {
                if (list.size() > 0) {
                    Integer num = list.remove(random.nextInt(list.size()));
                    if (!tree.deleteData(new TwoThreeObject(num))) {
                        throw new Exception("dany objekt sa v strome nenachadza");
                    }
                }
            }
            pom++;
        }
        System.out.println("Pocet prvkov v strome po teste - " + tree.getSize());
    }
    
    @Test
    public void numOfParentsForAllLeafsTest() throws Exception {
        this.random.setSeed(this.seed.nextInt());
        
        TwoThreeTree<TwoThreeObject> tree = new TwoThreeTree<>();
        List<Integer> keys = new ArrayList<>();
        int numOfData = 100000;
        
        int count = 0;
        while (count != numOfData) {
            double rand = random.nextDouble();
            if (rand < 0.6) {
                int key = random.nextInt(10000000);
                if (tree.insertData(new TwoThreeObject(key))) {
                    count++;
                    keys.add(key);
                }   
            } else {
                if (tree.getSize() > 0) {
                    int key = keys.remove(random.nextInt(keys.size()));
                    if (tree.deleteData(new TwoThreeObject(key))) {
                        count--;
                    } else {
                        throw new Exception("nenasiel sa prvok pri mazani");
                    }
                }
            }       
        }
        
        List<TwoThreeNode<TwoThreeObject>> inOrder = tree.inOrderNodes();
        
        int lastNumOfParents = 0;
        for (int i = 0; i < inOrder.size(); i++) {
            if (inOrder.get(i).isLeaf()) {
                int numOfParents = 0;
                TwoThreeNode<TwoThreeObject> parent = (TwoThreeNode<TwoThreeObject>) inOrder.get(i).getParent();
                while (parent != null) {
                    numOfParents++;
                    parent = (TwoThreeNode<TwoThreeObject>) parent.getParent();           
                }
                
                if (i != 0) {
                    if (lastNumOfParents == numOfParents) {
                        lastNumOfParents = numOfParents;
                    } else {
                        throw new Exception("LIST JE NA INEJ UROVNI AKO OSTATNE");
                    }                   
                } else {
                    lastNumOfParents = numOfParents;
                }
            }
        }
        System.out.println("log2(" + numOfData + ") = " + (double) Math.log(numOfData) / (double) Math.log(2));
        System.out.println("log3(" + numOfData + ") = " + (double) Math.log(numOfData) / (double) Math.log(3));
        System.out.println("Uroven vsetkych listov = " + lastNumOfParents);
    }
    
    @Test
    public void searchDataTest() throws Exception {
        this.random.setSeed(this.seed.nextLong());
        
        TwoThreeTree<TwoThreeObject> tree = new TwoThreeTree<>();
        List<TwoThreeObject> list = new ArrayList<>();

        int count = 0;
        while (count != 1000000) {
            int rand = random.nextInt(10000000);
            if (tree.insertData(new TwoThreeObject(rand))) {
                list.add(new TwoThreeObject(rand));
                count++;
            }
        }

        TwoThreeObject lower = new TwoThreeObject(random.nextInt(100));
        TwoThreeObject upper = new TwoThreeObject(random.nextInt(1000000));
        

        List<TwoThreeObject> founded = null;

        if (lower.compareTo(upper) < 0) {
            founded = tree.searchData(lower, upper);
        } else {
            lower = new TwoThreeObject(upper.getKey());
            upper = new TwoThreeObject(lower.getKey());
            founded = tree.searchData(lower, upper);
        }
        
        List<TwoThreeObject> sublist = new ArrayList<>();
        for (TwoThreeObject obj : list) {
            if (obj.compareTo(lower) >= 0 && obj.compareTo(upper) <= 0) {
                sublist.add(obj);
            }
        }
        
        if (sublist.size() != founded.size()) {
            list.forEach(obj -> System.out.println(obj.getKey()));
            System.out.println("lower " + lower.getKey());
            System.out.println("upper " + upper.getKey());
            
            throw new Exception("velkosti listov sa nerovnaju");
        }
        
        Collections.sort(sublist);
        
        for (int i = 0; i < sublist.size(); i++) {
            if (sublist.get(i).compareTo(founded.get(i)) != 0) {
                throw new Exception("INTERVALOVE VYHLADAVANIE NIE JE SPRAVNE");
            }
        }

        for (int i = 0; i < founded.size() - 1; i++) {
            if (founded.get(i).compareTo(founded.get(i + 1)) >= 0) {
                throw new Exception("nespravne poradie");
            } else {
                if (founded.get(i).compareTo(lower) < 0 && founded.get(i).compareTo(upper) > 0) {
                    throw new Exception("najdeny prvok nie je z rozsahu");
                }
            }
        }
    }

    @Test
    public void testInOrder() throws Exception {
        this.random.setSeed(this.seed.nextLong());
        TwoThreeTree<TwoThreeObject> tree = new TwoThreeTree<>();

        int pom = 0;
        while (pom != 50000) {
            TwoThreeObject obj = new TwoThreeObject(random.nextInt(500000000));
            if (tree.insertData(obj)) {
                pom++;
            }
        }

        List<TwoThreeObject> inorder = tree.inOrderData();
        for (int j = 0; j < inorder.size() - 1; j++) {

            for (int k = j + 1; k < inorder.size(); k++) {
                if (inorder.get(j).compareTo(inorder.get(k)) >= 0) {
                    throw new Exception("nespravne poradie prvkov");
                }
            }        
        }        
    }
    
    @Test
    public void testInOrder2() throws Exception {
        this.random.setSeed(this.seed.nextLong());
        TwoThreeTree<TwoThreeObject> tree = new TwoThreeTree<>();

        int pom = 0;
        while (pom != 1000000) {

            TwoThreeObject obj = new TwoThreeObject(random.nextInt(500000000));
            if (tree.insertData(obj)) {
                pom++;
            }
        }

        List<TwoThreeObject> inorder = tree.inOrderData();
        List<TwoThreeObject> inOrder2 = tree.searchData(new TwoThreeObject(Integer.MIN_VALUE), new TwoThreeObject(Integer.MAX_VALUE));
        
        if (inorder.size() != inOrder2.size()) {
            throw new Exception("zoznamy sa lisia"); 
        }
        
        for (int i = 0; i < inorder.size(); i++) {
            if (inorder.get(i).compareTo(inOrder2.get(i)) != 0) {
                throw new Exception("zoznamy sa lisia"); 
            }
        }
        
        for (int j = 0; j < inorder.size() - 1; j++) {           
            if (inorder.get(j).compareTo(inorder.get(j + 1)) >= 0) {
                throw new Exception("nespravne poradie prvkov");     
            }
        }
    }
    
    @Test
    public void testInOrder3() throws Exception {
        this.random.setSeed(this.seed.nextLong());
        TwoThreeTree<TwoThreeObject> tree = new TwoThreeTree<>();
        List<TwoThreeObject> list = new ArrayList<>();
        
        int pom = 0;
        while (pom != 1000000) {
            int rand = random.nextInt(Integer.MAX_VALUE);
            if (tree.insertData(new TwoThreeObject(rand))) {
                list.add(new TwoThreeObject(rand));
                pom++;
            }
        }
        Collections.sort(list);
        List<TwoThreeObject> inOrder = tree.inOrderData();
        
        if (inOrder.size() == list.size()) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).compareTo(inOrder.get(i)) != 0) {
                    throw new Exception("listy nemaju rovnake data");
                }
            }
        } else  {
            throw new Exception("velkost listov nie je rovnaka");
        }
    }

    @Test
    public void debuggingMethod() {
        TwoThreeTree<TwoThreeObject> tree = new TwoThreeTree<>();
        List<TwoThreeObject> list = new ArrayList<>();

        tree.insertData(new TwoThreeObject(351));
        tree.insertData(new TwoThreeObject(611));
        tree.insertData(new TwoThreeObject(440));
        tree.insertData(new TwoThreeObject(642));
        tree.insertData(new TwoThreeObject(186));
        tree.insertData(new TwoThreeObject(36));
        tree.insertData(new TwoThreeObject(509));
        tree.insertData(new TwoThreeObject(989));
        tree.insertData(new TwoThreeObject(819));
        tree.insertData(new TwoThreeObject(311));
        tree.insertData(new TwoThreeObject(98));
        tree.insertData(new TwoThreeObject(766));
        tree.insertData(new TwoThreeObject(653));
        tree.insertData(new TwoThreeObject(479));
        tree.insertData(new TwoThreeObject(349));
        tree.insertData(new TwoThreeObject(898));
        tree.insertData(new TwoThreeObject(551));
        tree.insertData(new TwoThreeObject(44));
        tree.insertData(new TwoThreeObject(671));
        tree.insertData(new TwoThreeObject(288));
        tree.insertData(new TwoThreeObject(40));
        tree.insertData(new TwoThreeObject(42));
        tree.insertData(new TwoThreeObject(20));
        tree.insertData(new TwoThreeObject(15));
        tree.insertData(new TwoThreeObject(10));
        tree.insertData(new TwoThreeObject(9));

        tree.searchData(new TwoThreeObject(41), new TwoThreeObject(643)).forEach(obj -> System.out.println(obj.getKey()));
    }
    
    @Test
    public void debuggingMethod2() {
        TwoThreeTree<TwoThreeObject> tree = new TwoThreeTree<>();
        List<TwoThreeObject> list = new ArrayList<>();

        tree.insertData(new TwoThreeObject(605));
        tree.insertData(new TwoThreeObject(290));
        tree.insertData(new TwoThreeObject(795));
        tree.insertData(new TwoThreeObject(47));
        tree.insertData(new TwoThreeObject(849));
        tree.insertData(new TwoThreeObject(260));
        tree.insertData(new TwoThreeObject(627));
        tree.insertData(new TwoThreeObject(198));
        tree.insertData(new TwoThreeObject(509));
        tree.insertData(new TwoThreeObject(807));

        tree.searchData(new TwoThreeObject(311), new TwoThreeObject(311)).forEach(obj -> System.out.println(obj.getKey()));
    }

    @Test
    public void testSearchOperation() {
        this.random.setSeed(this.seed.nextLong());

        TwoThreeTree<TwoThreeObject> tree = new TwoThreeTree<>();

        int count = 0;
        int notInserted = 0;
        while (count != 30000000) {
            if (tree.insertData(new TwoThreeObject(count))) {
                if (count % 1000000 == 0) {
                    System.out.println(count);
                }
                count++;
            } else {
                notInserted++;
                if (notInserted % 10000 == 0) {
                    System.out.println("Not inserted" + notInserted);
                }
            }
        }

        System.out.println("data inserted");
    }

    @Test
    public void testIntervalSearchTree() {
        this.random.setSeed(this.seed.nextLong());

        TwoThreeTree<TwoThreeObject> tree = new TwoThreeTree<>();

        int count = 0;
        while (count != 30000000) {
            int rand = random.nextInt(1000000000);
            if (tree.insertData(new TwoThreeObject(rand))) {
                count++;
            }
        }

        System.out.println(tree.getSize());

        TwoThreeObject lower = new TwoThreeObject(5000);
        TwoThreeObject upper = new TwoThreeObject(50000);
        long start = System.currentTimeMillis();
        List<TwoThreeObject> founded = tree.searchData(lower,upper);
        long end = System.currentTimeMillis();

        System.out.println("Duration - " + (end - start));
        System.out.println(founded.size());
    }

    @Test
    public void testIntervalSearchArrayList() {
        this.random.setSeed(this.seed.nextLong());
        List<TwoThreeObject> list = new ArrayList<>();

        int count = 0;
        while (count != 20000000) {
            int rand = random.nextInt(100000000);
            list.add(new TwoThreeObject(rand));
            count++;
        }

        TwoThreeObject lower = new TwoThreeObject(500);
        TwoThreeObject upper = new TwoThreeObject(50000);
        List<TwoThreeObject> founded = new ArrayList<>();

        long start = System.currentTimeMillis();
        list.forEach(twoThreeObject -> {
            if (twoThreeObject.compareTo(lower) >= 0 && twoThreeObject.compareTo(upper) < 0) {
                founded.add(twoThreeObject);
            }
        });
        long end = System.currentTimeMillis();
        System.out.println("Duration - " + (end - start));
        System.out.println(founded.size());
    }
}
