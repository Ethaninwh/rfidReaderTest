package serialException;

/**
 * Created by boy on 2018/4/19.
 */
public class TooManyListeners extends Exception {
    private static final long serialVersionUID = 1L;

    public TooManyListeners() {}

    @Override
    public String toString() {
        return "监听类对象过多！";
    }
}
