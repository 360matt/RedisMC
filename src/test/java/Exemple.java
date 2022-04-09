import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

public class Exemple implements Serializable {

    public int a;
    public HashMap<String, String> b;
    public HashSet<String> c;

    public Exemple(String id) {
        this.a = 10;
        this.b = new HashMap<String, String>();
        this.c = new HashSet<String>();
    }
}
