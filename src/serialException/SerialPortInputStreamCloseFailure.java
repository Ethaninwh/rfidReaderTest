package serialException;

/**
 * Created by boy on 2018/4/19.
 */
public class SerialPortInputStreamCloseFailure extends Exception {
    private static final long serialVersionUID = 1L;

    public SerialPortInputStreamCloseFailure() {}

    @Override
    public String toString() {
        return "关闭串口对象输入流出错！";
    }
}
