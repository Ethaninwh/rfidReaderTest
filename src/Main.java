import gnu.io.*;
import serialException.*;

import java.util.*;

public class Main {


    public static void main(String[] args) {
        SerialPortController controller = new SerialPortController();
        SerialPort serialPort = controller.openPortAddListener();
//        controller.setOutPutPower(serialPort);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        controller.getOutPutPower(serialPort);

        controller.realTimeInventory();

    }
}
