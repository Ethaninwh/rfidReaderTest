package serialException;

/**
 * Created by boy on 2018/4/19.
 */
public class SendDataToSerialPortFailure extends Exception {
    private static final long serialVersionUID = 1L;

    public SendDataToSerialPortFailure() {}

    @Override
    public String toString() {
        return "向串口发送数据时出错！";
    }
}
