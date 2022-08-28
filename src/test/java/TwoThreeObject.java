/**
 * Objekt slúžiaci na testovacie účely
 * @author Matúš
 */
public class TwoThreeObject implements Comparable<TwoThreeObject> {

    private Integer key;
    
    public TwoThreeObject(Integer key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "TwoTreeObject{" +
                "key=" + key +
                '}';
    }

    @Override
    public int compareTo(TwoThreeObject o) {
        return this.key.compareTo(o.key);
    }
    
    public Integer getKey() {
        return key;
    }
}
