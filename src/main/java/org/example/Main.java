package org.example;

import org.example.iec61850.lodicalNodes.LN;
import org.example.iec61850.lodicalNodes.hmi.NHMI;
import org.example.iec61850.lodicalNodes.hmi.other.NHMISignal;
import org.example.iec61850.lodicalNodes.measurement.MMXU;
import org.example.iec61850.lodicalNodes.protection.PTOC;
import org.example.iec61850.lodicalNodes.supervisory_control.CSWI;
import org.example.iec61850.lodicalNodes.system_logic_nodes.LSVS;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final List<LN> logicalNode = new ArrayList<>();
    private static String path = "D:\\2mag\\algorithm rza\\Начало линии\\";

//    private static String path = "C:\\Users\\Aglomiras\\Изображения\\Рабочий стол\\AlgoritmRZAProgrammRealize\\Конец линии\\";

    /**
     * Начало линии
     */
//    private static String name = "PhAB80";
//    private static String name = "PhA80";
    private static String name = "PhB20";
//    private static String name = "PhBC20";

    /**
     * Конец линии
     */
//    private static String name = "PhABC20";
//    private static String name = "PhABC80";
//    private static String name = "PhB80";
//    private static String name = "PhC20";
    public static void main(String[] args) throws Exception {
        LSVS lsvs = new LSVS(); //Создаем узел LSVS
        lsvs.setPath(path);
        lsvs.setFileName(name);
        logicalNode.add(lsvs); //Добавляем узел в лист узлов

        MMXU mmxu = new MMXU(); //Создаем узел MMXU
        mmxu.IaInst = lsvs.getOut().get(0);
        mmxu.IbInst = lsvs.getOut().get(1);
        mmxu.IcInst = lsvs.getOut().get(2);
        logicalNode.add(mmxu); //Добавляем узел в лист узлов

        /**I ступень*/
        PTOC ptoc1 = new PTOC();
        ptoc1.setA(mmxu.getA());
        ptoc1.getStrVal().getSetMag().getF().setValue(2544.0); //Задание уставки по току
        ptoc1.getOpDlOpTmms().getSetVal().setValue(0); //Задание выдержки времени
        ptoc1.getTmMult().getSetMag().getF().setValue(0.02 / 80);
        logicalNode.add(ptoc1);

        /**II ступень*/
        PTOC ptoc2 = new PTOC();
        ptoc2.setA(mmxu.getA());
        ptoc2.getStrVal().getSetMag().getF().setValue(770.0); //Задание уставки по току
        ptoc2.getOpDlOpTmms().getSetVal().setValue(1); //Задание выдержки времени
        ptoc2.getTmMult().getSetMag().getF().setValue(0.02 / 80);
        logicalNode.add(ptoc2);

        /**III ступень*/
        PTOC ptoc3 = new PTOC();
        ptoc3.setA(mmxu.getA());
        ptoc3.getStrVal().getSetMag().getF().setValue(420.0); //Задание уставки по току
        ptoc3.getOpDlOpTmms().getSetVal().setValue(2); //Задание выдержки времени
        ptoc3.getTmMult().getSetMag().getF().setValue(0.02 / 80);
        logicalNode.add(ptoc3);

        CSWI cswi = new CSWI();
        /**Добавляем информацию о сигнала на отключение оборудования от защит*/
        cswi.getOpOpnList().add(ptoc1.getOp());
        cswi.getOpOpnList().add(ptoc2.getOp());
        cswi.getOpOpnList().add(ptoc3.getOp());
        logicalNode.add(cswi);

//        XCBR xcbr = new XCBR();
//
//        logicalNode.add(xcbr);

        NHMI nhmi = new NHMI();
        nhmi.addSignals("SignalIA", new NHMISignal("ia", mmxu.IaInst.getInstMag().getF()));
        nhmi.addSignals("SignalIB", new NHMISignal("ib", mmxu.IbInst.getInstMag().getF()));
        nhmi.addSignals("SignalIC", new NHMISignal("ic", mmxu.IcInst.getInstMag().getF()));
        logicalNode.add(nhmi);

        while (lsvs.hasNext()) {
            logicalNode.forEach(LN::process);
            System.out.println();
        }

    }
}