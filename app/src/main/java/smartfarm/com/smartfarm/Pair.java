package smartfarm.com.smartfarm;

/**
 * Created by macbook on 13/05/2020.
 */

public class Pair <T1, T2> {
    public T1 first;
    public T2 second;

    public Pair(T1 t1, T2 t2){
        this.first = t1;
        this.second = t2;
    }
}
