package serialException;

/**
 * Created by boy on 2018/4/19.
 */
public class SerialPortOutputStreamCloseFailure extends Exception {
    private static final long serialVersionUID = 1L;

    public SerialPortOutputStreamCloseFailure() {}

    @Override
    public String toString() {
        return "关闭串口对象的输出流出错！";
    }
}
