import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import serialException.ReadDataFromSerialPortFailure;
import serialException.SerialPortInputStreamCloseFailure;

import java.util.*;

/**
 * Created by yushen on 2018/4/19.
 */
public class SerialPortController {

    private static final long serialVersionUID = 1L;
    private SerialPort serialPort = null;    //保存串口对象
    private SerialListener listener = null;  //串口监听
    private static List<Byte> receivedDate = new ArrayList<>();  //从串口接收数据


    public SerialPort openPortAddListener() {
        try {
            ArrayList<String> ports = SerialTool.findPort();
            System.out.println("扫描到的串口为" + ports);
            //获取指定端口名及波特率的串口对象
            serialPort = SerialTool.openPort(ports.get(0), 38400);

            listener = new SerialListener();
            //在串口对象上添加监听器
            SerialTool.addListener(serialPort, listener);
            //监听成功进行提示
            System.out.println(serialPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serialPort;
    }


    public void realTimeInventory() {
        try {
            listener.setCmd("realTimeInventory"); //告诉监听者，当前的指令，监听者获取数据后根据发送指令解码
            SerialCmdGenerate serialCmdGenerate = SerialCmdGenerate.getInstance();
            byte[] realTimeInventory = serialCmdGenerate.realTimeInventory();
            String serialCmd = SerialHandleUtil.epcToHexString(realTimeInventory);
            System.out.println("realTimeInventory 的串口指令为：" + serialCmd);
            while (true) {
                Thread.sleep(500);
                receivedDate.clear();
                SerialTool.sendToPort(serialPort, realTimeInventory);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        try {
            listener.setCmd("reset");
            SerialCmdGenerate serialCmdGenerate = SerialCmdGenerate.getInstance();
            byte[] reset = serialCmdGenerate.reset();
            String serialCmd = SerialHandleUtil.epcToHexString(reset);
            System.out.println("reset 的串口指令为：" + serialCmd);
            receivedDate.clear();
            SerialTool.sendToPort(serialPort, reset);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUartBaudrate() {
        try {
            listener.setCmd("setUartBaudrate");
            SerialCmdGenerate serialCmdGenerate = SerialCmdGenerate.getInstance();
            byte[] setUartBaudrate = serialCmdGenerate.setUartBaudrate((byte) 0x30);
            String serialCmd = SerialHandleUtil.epcToHexString(setUartBaudrate);
            System.out.println("setUartBaudrate 的串口指令为：" + serialCmd);
            receivedDate.clear();
            SerialTool.sendToPort(serialPort, setUartBaudrate);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setReaderAddress() {
        try {
            listener.setCmd("setReaderAddress");
            SerialCmdGenerate serialCmdGenerate = SerialCmdGenerate.getInstance();
            byte[] setReaderAddress = serialCmdGenerate.setReaderAddress((byte) 0x01);
            String serialCmd = SerialHandleUtil.epcToHexString(setReaderAddress);
            System.out.println("setReaderAddress 的串口指令为：" + serialCmd);
            receivedDate.clear();
            SerialTool.sendToPort(serialPort, setReaderAddress);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setWorkAntenna() {
        try {
            listener.setCmd("setWorkAntenna");
            SerialCmdGenerate serialCmdGenerate = SerialCmdGenerate.getInstance();
            byte[] setWorkAntenna = serialCmdGenerate.setWorkAntenna((byte) 0x00);
            String serialCmd = SerialHandleUtil.epcToHexString(setWorkAntenna);
            System.out.println("setWorkAntenna 的串口指令为：" + serialCmd);
            receivedDate.clear();
            SerialTool.sendToPort(serialPort, setWorkAntenna);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getWorkAntenna() {
        try {
            listener.setCmd("getWorkAntenna");
            SerialCmdGenerate serialCmdGenerate = SerialCmdGenerate.getInstance();
            byte[] getWorkAntenna = serialCmdGenerate.getWorkAntenna();
            String serialCmd = SerialHandleUtil.epcToHexString(getWorkAntenna);
            System.out.println("getWorkAntenna 的串口指令为：" + serialCmd);
            receivedDate.clear();
            SerialTool.sendToPort(serialPort, getWorkAntenna);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOutPutPower(SerialPort serialPort) {
        try {
            listener.setCmd("setOutPutPower");
            SerialCmdGenerate serialCmdGenerate = SerialCmdGenerate.getInstance();
            byte[] setOutPutPower = serialCmdGenerate.setOutPutPower((byte) 0x00);
            String serialCmd = SerialHandleUtil.epcToHexString(setOutPutPower);
            System.out.println("setOutPutPower 的串口指令为：" + serialCmd);
            receivedDate.clear();
            SerialTool.sendToPort(serialPort, setOutPutPower);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getOutPutPower(SerialPort serialPort) {
        try {
            listener.setCmd("getOutPutPower");
            SerialCmdGenerate serialCmdGenerate = SerialCmdGenerate.getInstance();
            byte[] getOutPutPower = serialCmdGenerate.getOutPutPower();
            String serialCmd = SerialHandleUtil.epcToHexString(getOutPutPower);
            System.out.println("getOutPutPower 的串口指令为：" + serialCmd);
            receivedDate.clear();
            SerialTool.sendToPort(serialPort, getOutPutPower);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class SerialListener implements SerialPortEventListener {
        private String cmd;  //串口发送指令时设置，指示当前发送的指令，用来处理数据时解码。
        private SerialPortResponseHandler responseHandler = new SerialPortResponseHandler();

        public String getCmd() {
            return cmd;
        }
        public void setCmd(String cmd) {
            this.cmd = cmd;
        }

        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {
            switch (serialPortEvent.getEventType()) {

                case SerialPortEvent.BI: // 10 通讯中断
                    System.out.println("与串口设备通讯中断");
                    break;
                case SerialPortEvent.OE: // 7 溢位（溢出）错误
                    System.out.println("溢位（溢出）错误");
                case SerialPortEvent.FE: // 9 帧错误
                    System.out.println("帧错误");
                case SerialPortEvent.PE: // 8 奇偶校验错误
                    System.out.println("奇偶校验错误");
                case SerialPortEvent.CD: // 6 载波检测
                    System.out.println("载波检测");
                case SerialPortEvent.CTS: // 3 清除待发送数据
                    System.out.println("清除待发送数据");
                case SerialPortEvent.DSR: // 4 待发送数据准备好了
                    System.out.println("待发送数据准备好了");
                case SerialPortEvent.RI: // 5 振铃指示
                    System.out.println("振铃指示");
                case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 输出缓冲区已清空
                    System.out.println("输出缓冲区已清空");
                    break;

                case SerialPortEvent.DATA_AVAILABLE: // 1 串口存在可用数据
//                    System.out.println("串口存在可用数据");
                    byte[] data = null;

                    try {
                        if (serialPort == null) {
                            System.out.println("串口对象为空！监听失败！");
                        } else {
                            data = SerialTool.readFromPort(serialPort);    //读取数据，存入字节数组
//                            System.out.println("读取数据为：" + Arrays.toString(data));

                            if (data == null || data.length < 1) {    //检查数据是否读取正确
                                System.out.println("读取数据过程中未获取到有效数据！请检查设备或程序！");
                                System.exit(0);
                            } else {
                                for (byte b : data) {
                                    receivedDate.add(b);
                                }
                                responseHandler.processData(receivedDate, cmd);
                            }
                        }

                    } catch (ReadDataFromSerialPortFailure | SerialPortInputStreamCloseFailure e) {
                        e.printStackTrace();
                        System.exit(0);    //发生读取错误时显示错误信息后退出系统
                    }
                    break;
            }

        }
    }
}
