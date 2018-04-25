package serialException;

/**
 * Created by boy on 2018/4/19.
 */
public class NotASerialPort extends Exception {
    private static final long serialVersionUID = 1L;

    public NotASerialPort() {}

    @Override
    public String toString() {
        return "端口指向设备不是串口类型！";
    }
}
