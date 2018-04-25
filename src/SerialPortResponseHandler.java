import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yushen on 2018/4/24.
 */
public class SerialPortResponseHandler {
    private static Set<String> epcSet = new HashSet<>();

    public void processData(List<Byte> receivedDate, String cmd) {
        switch (cmd) {
            case "reset":
                break;
            case "realTimeInventory":
                realTimeInventoryResponseHandle(receivedDate);
                break;
            case "setUartBaudrate":
                break;
            case "setReaderAddress":
                break;
            case "setWorkAntenna":
                break;
            case "getWorkAntenna":
                break;
            case "setOutPutPower":
                setOutPutPowerResponseHandle(receivedDate);
                break;
            case "getOutPutPower":
                getOutPutPowerResponseHandle(receivedDate);
                break;
            default:
                break;
        }
    }

    //实时盘存返回数据处理
    public void realTimeInventoryResponseHandle(List<Byte> receivedDate) {
        int size = receivedDate.size();
        if (decodeEndingFlagData(receivedDate.subList(size - 12, size))) {
            decodeBufferData(receivedDate);
            System.out.println("读取标签数量为：" + epcSet.size());
            for (String epc : epcSet) {
                System.out.println("标签为：" + epc);
            }
        }
    }

    //设置读写器功率返回数据处理
    public void setOutPutPowerResponseHandle(List<Byte> receivedDate) {
        List<Byte> response = decodeResponseData(receivedDate);
        if (response == null) {
            System.out.println("设置取功率失败");
        } else {
            System.out.println("设置功率成功，返回码为：" + receivedDate.get(4));
        }
    }

    //获取读写器功率返回数据处理
    public void getOutPutPowerResponseHandle(List<Byte> receivedDate) {
        List<Byte> response = decodeResponseData(receivedDate);
        if (response == null) {
            System.out.println("读取功率失败");
        } else {
            System.out.println("读写器功率为：" + response.get(4));
        }
    }

    //解析串口返回数据
    public List<Byte> decodeResponseData(List<Byte> responseData) {
        List<Byte> checkedData = null;
        for (int i = 0; i < responseData.size(); ) {
            if (responseData.get(i) == (byte) 0xA0) { //判断数据报头
                Byte length = responseData.get(i + 1); //取数据报长
                //截取有效字符串计算校验和
                List<Byte> subList = responseData.subList(i, i + length + 1);
                Byte sumCheck = responseData.get(i + length + 1); //回传校验和
                byte res = SerialHandleUtil.sumCheck(subList); //计算校验和
                if (res == sumCheck) { //验证校验和，如何校验正确循环向后直接向后跳 (length + 2) 位；否则继续向后单步解析
                    checkedData = responseData.subList(i, i + length + 2);
                } else {
                    System.out.println("数据校验和错误");
                }
                return checkedData;
            } else {
                i++;
            }
        }
        return checkedData;
    }


    //下位机回传串口数据解码，盘存场景，用于解码大量携带EPC信息的数据
    public void decodeBufferData(List<Byte> buffer) {
        for (int i = 0; i < buffer.size(); ) {
            if (buffer.get(i) == (byte) 0xA0) { //判断数据报头
                Byte length = buffer.get(i + 1); //取数据报长
                //截取有效字符串计算校验和
                List<Byte> subList = buffer.subList(i, i + length + 1);
                Byte sumCheck = buffer.get(i + length + 1); //回传校验和
                byte res = SerialHandleUtil.sumCheck(subList); //计算校验和

                if (res == sumCheck) { //验证校验和，如何校验正确循环向后直接向后跳 (length + 2) 位；否则继续向后单步解析
                    List<Byte> checkedData = buffer.subList(i, i + length + 2);
                    i = i + length + 2;
                    resolveEpc(checkedData);
                } else {
                    i++;
                    System.out.println("数据校验和错误");
                }
            } else {
                i++;
            }
        }
    }

    //下位机实时盘存回传数据 结束标志包解码，盘存场景，先解码结束标志包。
    public Boolean decodeEndingFlagData(List<Byte> buffer) {
        for (int i = 0; i < buffer.size(); ) {
            if (buffer.get(i) == (byte) 0xA0) { //判断数据报头
                Byte length = buffer.get(i + 1); //取数据报长度
                //截取有效字符串计算校验和
                List<Byte> subList = buffer.subList(i, i + length + 1);
                Byte sumCheck = buffer.get(i + length + 1);
                byte res = SerialHandleUtil.sumCheck(subList);
                if (res == sumCheck) {
                    List<Byte> checkedData = buffer.subList(i, i + length + 2);
                    System.out.println("正确的回传结束包数据报为：" + checkedData);
                    if (buffer.get(i + 3) == (byte) 0x89) {
                        return true;
                    }
                } else {
                    ;
                }
            } else {
                i++;
            }
        }
        return false;
    }


    //解析EPC信息
    public void resolveEpc(List<Byte> bytes) {

        if (bytes.get(1) == 19) { //正确EPC数据包的长度应该为19
            String epc = SerialHandleUtil.epcToHexString(bytes.subList(7, 19)); //EPC信息储存在数据包的7-19位
            epcSet.add(epc);
        } else if (bytes.get(1) == 4) {//错误数据包的长度为4
            System.out.println("操作失败，失败码" + bytes.get(4));
        }
    }
}
