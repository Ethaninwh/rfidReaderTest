/**
 * Created by yushen on 2018/4/24.
 */
public class SerialCmdGenerate {
    private static SerialCmdGenerate serialCmdGenerate;
    private byte address = 0x01;

    private SerialCmdGenerate() {
    }

    public static SerialCmdGenerate getInstance() {
        if (serialCmdGenerate == null) {
            serialCmdGenerate = new SerialCmdGenerate();
        }
        return serialCmdGenerate;
    }


    public byte getAddress() {
        return address;
    }

    public void setAddress(byte address) {
        this.address = address;
    }

    /**
     * 复位读写器
     */
    public byte[] reset() {
        byte[] reset = {(byte) 0xA0, 0x03, this.address, 0x70, 0x00};
        byte sumCheck = SerialHandleUtil.sumCheck(reset);
        reset[reset.length - 1] = sumCheck;
        return reset;
    }

    /**
     * 实时盘存
     */
    public byte[] realTimeInventory() {
        byte[] realTimeInventory = {(byte) 0xA0, 0x04, this.address, (byte) 0x89, 0x01, 0x00};
        byte sumCheck = SerialHandleUtil.sumCheck(realTimeInventory);
        realTimeInventory[realTimeInventory.length - 1] = sumCheck;
        return realTimeInventory;
    }

    /**
     * 设置波特率
     *
     * @param baudrate 波特率: 0x30 -> 38400bps ; 0x40 -> 115200bps
     */
    public byte[] setUartBaudrate(byte baudrate) {
        byte[] setUartBaudrate = {(byte) 0xA0, 0x04, this.address, (byte) 0x71, baudrate, 0x00};
        byte sumCheck = SerialHandleUtil.sumCheck(setUartBaudrate);
        setUartBaudrate[setUartBaudrate.length - 1] = sumCheck;
        return setUartBaudrate;
    }


    /**
     * 设置读写器地址
     *
     * @param address 读写器地址0-254: 0x00 - 0xFE
     */
    public byte[] setReaderAddress(byte address) {
        byte[] setReaderAddress = {(byte) 0xA0, 0x04, this.address, (byte) 0x73, address, 0x00};
        byte sumCheck = SerialHandleUtil.sumCheck(setReaderAddress);
        setReaderAddress[setReaderAddress.length - 1] = sumCheck;
        return setReaderAddress;
    }

    /**
     * 设置工作天线
     *
     * @param antennaId 天线序号1-4：0x00 - 0x03
     */
    public byte[] setWorkAntenna(byte antennaId) {
        byte[] setWorkAntenna = {(byte) 0xA0, 0x04, this.address, (byte) 0x74, antennaId, 0x00};
        byte sumCheck = SerialHandleUtil.sumCheck(setWorkAntenna);
        setWorkAntenna[setWorkAntenna.length - 1] = sumCheck;
        return setWorkAntenna;
    }

    /**
     * 读工作天线
     */
    public byte[] getWorkAntenna() {
        byte[] getWorkAntenna = {(byte) 0xA0, 0x03, this.address, (byte) 0x75, 0x00};
        byte sumCheck = SerialHandleUtil.sumCheck(getWorkAntenna);
        getWorkAntenna[getWorkAntenna.length - 1] = sumCheck;
        return getWorkAntenna;
    }

    /**
     * 设置所有天线的发射功率
     *
     * @param rfPower 功率0-33dbm: 0x00 - 0x21
     */
    public byte[] setOutPutPower(byte rfPower) {
        byte[] setOutPutPower = {(byte) 0xA0, 0x04, this.address, (byte) 0x76, rfPower, 0x00};
        byte sumCheck = SerialHandleUtil.sumCheck(setOutPutPower);
        setOutPutPower[setOutPutPower.length - 1] = sumCheck;
        return setOutPutPower;
    }

    /**
     * 按天线设置发射功率
     *
     * @param rfPower1 功率0-33dbm: 0x00 - 0x21
     * @param rfPower2 功率0-33dbm: 0x00 - 0x21
     * @param rfPower3 功率0-33dbm: 0x00 - 0x21
     * @param rfPower4 功率0-33dbm: 0x00 - 0x21
     */
    public byte[] setOutPutPower(byte rfPower1, byte rfPower2, byte rfPower3, byte rfPower4) {
        byte[] setOutPutPower = {(byte) 0xA0, 0x07, this.address, (byte) 0x76, rfPower1, rfPower2, rfPower3, rfPower4, 0x00};
        byte sumCheck = SerialHandleUtil.sumCheck(setOutPutPower);
        setOutPutPower[setOutPutPower.length - 1] = sumCheck;
        return setOutPutPower;
    }


    /**
     * 读天线功率
     */
    public byte[] getOutPutPower() {
        byte[] getOutPutPower = {(byte) 0xA0, 0x03, this.address, (byte) 0x77, 0x00};
        byte sumCheck = SerialHandleUtil.sumCheck(getOutPutPower);
        getOutPutPower[getOutPutPower.length - 1] = sumCheck;
        return getOutPutPower;
    }
}
