package org.example;

import org.example.iec61850.lodicalNodes.common.LN;
import org.example.iec61850.lodicalNodes.control.XCBR;
import org.example.iec61850.lodicalNodes.hmi.NHMI;
import org.example.iec61850.lodicalNodes.hmi.other.NHMISignal;
import org.example.iec61850.lodicalNodes.measurement.MMXU;
import org.example.iec61850.lodicalNodes.protection.PTOC;
import org.example.iec61850.lodicalNodes.control.CSWI;
import org.example.iec61850.lodicalNodes.protocol.LSVS;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final List<LN> logicalNode = new ArrayList<>();
    private static String path = "D:\\2mag\\algorithm rza\\Начало линии\\";
//    private static String path = "D:\\2mag\\algorithm rza\\Конец линии\\";

    /**
     * Начало линии
     */
    private static String name = "PhB20";
//    private static String name = "PhBC20";
    /**
     * Конец линии
     */
//    private static String name = "PhABC20";
//    private static String name = "PhC20";
    public static void main(String[] args) throws Exception {
        LSVS lsvs = new LSVS();
        lsvs.setPath(path);
        lsvs.setFileName(name);
        logicalNode.add(lsvs);

        MMXU mmxu = new MMXU();
        mmxu.iaMV = lsvs.getOut().get(0);
        mmxu.ibMV = lsvs.getOut().get(1);
        mmxu.icMV = lsvs.getOut().get(2);
        logicalNode.add(mmxu);

        //1 я ступень
        PTOC ptoc1 = new PTOC();
        ptoc1.setA(mmxu.getA());
        ptoc1.getStrVal().getSetMag().getF().setValue(1700.0); //уставка по току
        ptoc1.getOpDlOpTmms().getSetVal().setValue(0); //выдержка времени
        ptoc1.getTmMult().getSetMag().getF().setValue(1.0);
        logicalNode.add(ptoc1);

        //2 я ступень
        PTOC ptoc2 = new PTOC();
        ptoc2.setA(mmxu.getA());
        ptoc2.getStrVal().getSetMag().getF().setValue(600.0); //уставка по току
        ptoc2.getOpDlOpTmms().getSetVal().setValue(300); //выдержки времени
        ptoc2.getTmMult().getSetMag().getF().setValue(1.0);
        logicalNode.add(ptoc2);

        //3 я ступень
        PTOC ptoc3 = new PTOC();
        ptoc3.setA(mmxu.getA());
        ptoc3.getStrVal().getSetMag().getF().setValue(355.0); //уставкf по току
        ptoc3.getOpDlOpTmms().getSetVal().setValue(600); //выдержки времени
        ptoc3.getTmMult().getSetMag().getF().setValue(1.0);
        logicalNode.add(ptoc3);

        CSWI cswi = new CSWI();
        cswi.getOpOpnList().add(ptoc1.getOp());
        cswi.getOpOpnList().add(ptoc2.getOp());
        cswi.getOpOpnList().add(ptoc3.getOp());
        logicalNode.add(cswi);

        XCBR xcbr = new XCBR();
        xcbr.setPos(cswi.getPos());
        logicalNode.add(xcbr);

        NHMI nhmi = new NHMI();
        nhmi.addSignals("IA", new NHMISignal("ia", mmxu.iaMV.getInstMag().getF()));
        nhmi.addSignals("IB", new NHMISignal("ib", mmxu.ibMV.getInstMag().getF()));
        nhmi.addSignals("IC", new NHMISignal("ic", mmxu.icMV.getInstMag().getF()));
        logicalNode.add(nhmi);
        NHMI nhmiPTOC = new NHMI();

        nhmiPTOC.addSignals("PhsA",
                new NHMISignal("Phase_A", mmxu.getA().getPhsA().getCVal().getMag().getF()),
                new NHMISignal("ptoc_1", ptoc1.getStrVal().getSetMag().getF()),
                new NHMISignal("ptoc_2", ptoc2.getStrVal().getSetMag().getF()),
                new NHMISignal("ptoc_3", ptoc3.getStrVal().getSetMag().getF()));

        nhmiPTOC.addSignals("PhsB",
                new NHMISignal("Phase_B", mmxu.getA().getPhsB().getCVal().getMag().getF()),
                new NHMISignal("ptoc_1", ptoc1.getStrVal().getSetMag().getF()),
                new NHMISignal("ptoc_2", ptoc2.getStrVal().getSetMag().getF()),
                new NHMISignal("ptoc_3", ptoc3.getStrVal().getSetMag().getF()));
        nhmiPTOC.addSignals("PhsC",
                new NHMISignal("Phase_C", mmxu.getA().getPhsC().getCVal().getMag().getF()),
                new NHMISignal("ptoc_1", ptoc1.getStrVal().getSetMag().getF()),
                new NHMISignal("ptoc_2", ptoc2.getStrVal().getSetMag().getF()),
                new NHMISignal("ptoc_3", ptoc3.getStrVal().getSetMag().getF()));
        logicalNode.add(nhmiPTOC);

        NHMI nhmiDigitalSignal = new NHMI();
        nhmiDigitalSignal.addSignals("Discrete I STR", new NHMISignal("prot_1str", ptoc1.getStr().getGeneral()));
        nhmiDigitalSignal.addSignals("Discrete I OP", new NHMISignal("prot_1op", ptoc1.getOp().getGeneral()));
        nhmiDigitalSignal.addSignals("Discrete II STR", new NHMISignal("prot_2str", ptoc2.getStr().getGeneral()));
        nhmiDigitalSignal.addSignals("Discrete II OP", new NHMISignal("prot_2op", ptoc2.getOp().getGeneral()));
        nhmiDigitalSignal.addSignals("Discrete III STR", new NHMISignal("prot_3str", ptoc3.getStr().getGeneral()));
        nhmiDigitalSignal.addSignals("Discrete III OP", new NHMISignal("prot_3op", ptoc3.getOp().getGeneral()));
        logicalNode.add(nhmiDigitalSignal);
        while (lsvs.hasNext()) {
            logicalNode.forEach(LN::process);
            System.out.println();
        }

    }
}