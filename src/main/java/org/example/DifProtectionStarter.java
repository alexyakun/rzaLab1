package org.example;

import org.example.iec61850.lodicalNodes.common.LN;
import org.example.iec61850.lodicalNodes.hmi.NHMI;
import org.example.iec61850.lodicalNodes.hmi.NHMIP;
import org.example.iec61850.lodicalNodes.hmi.other.NHMISignal;
import org.example.iec61850.lodicalNodes.measurement.MHAI;
import org.example.iec61850.lodicalNodes.measurement.MMXU;
import org.example.iec61850.lodicalNodes.measurement.RMXU;
import org.example.iec61850.lodicalNodes.protection.PDIF;
import org.example.iec61850.lodicalNodes.protection.PHAR;
import org.example.iec61850.lodicalNodes.protection.PTOC;
import org.example.iec61850.lodicalNodes.protocol.LSVS;

import java.util.ArrayList;
import java.util.List;

public class DifProtectionStarter {
    private static final List<LN> logicalNode = new ArrayList<>();
    private static String path = "D:\\2mag\\algorithm rza\\lr4\\DPB\\4 sections\\";
    private static String name = "KzA";
    private static final double TIME_MULTIPLIER_FOR_FREQ = 1.0 / 1000;
    private static final int NUMBER_OF_CONNECTION = 4;
    private static final int NUMBER_OF_BLOCKED_HARMONIC = 5;
    private static final double PART_OF_HIGH_HARM_IN_BASE_HARM = 0.01;
    private static final double SET_FOR_PTOC = 10;
    public static void main(String[] args) throws Exception {
        LSVS lsvs = new LSVS();
        lsvs.setPath(path);
        lsvs.setFileName(name);
        logicalNode.add(lsvs);

        List<MMXU> mmxuList = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_CONNECTION; i++) {
            MMXU mmxu = new MMXU(TIME_MULTIPLIER_FOR_FREQ);
            mmxu.iaMV = lsvs.getOut().get(0 + i*3);
            mmxu.ibMV = lsvs.getOut().get(1 + i*3);
            mmxu.icMV = lsvs.getOut().get(2 + i*3);
            mmxuList.add(mmxu);
        }
        logicalNode.addAll(mmxuList);

        List<MHAI> mhaiList = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_CONNECTION; i++) {
            MHAI mhai = new MHAI();
            mhai.iaMV = lsvs.getOut().get(0 + i*3);
            mhai.ibMV = lsvs.getOut().get(1 + i*3);
            mhai.icMV = lsvs.getOut().get(2 + i*3);
            mhaiList.add(mhai);
        }
        logicalNode.addAll(mhaiList);

        List<PHAR> pharList = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_CONNECTION; i++) {
            PHAR phar = new PHAR(NUMBER_OF_BLOCKED_HARMONIC, PART_OF_HIGH_HARM_IN_BASE_HARM);
            phar.inputI  = mhaiList.get(i).getHA();
            pharList.add(phar);
        }
        logicalNode.addAll(pharList);

        RMXU rmxu = new RMXU();
        rmxu.mmxuList = mmxuList;
        logicalNode.add(rmxu);

        PTOC ptoc = new PTOC();
        ptoc.getStrVal().getSetMag().getF().setValue(SET_FOR_PTOC);
        ptoc.difA = rmxu.getAmpLocPhsA();
        ptoc.difB = rmxu.getAmpLocPhsB();
        ptoc.difC = rmxu.getAmpLocPhsC();
        logicalNode.add(ptoc);

        NHMI nhmiA = new NHMI();
        for (int i = 0; i < NUMBER_OF_CONNECTION; i++) {
            nhmiA.addSignals("IA"+(i+1), new NHMISignal("ia"+(i+1), mmxuList.get(i).iaMV.getInstMag().getF()));
        }
        logicalNode.add(nhmiA);
        NHMI nhmiB = new NHMI();
        for (int i = 0; i < NUMBER_OF_CONNECTION; i++) {
            nhmiB.addSignals("IB"+(i+1), new NHMISignal("ib"+(i+1), mmxuList.get(i).ibMV.getInstMag().getF()));
        }
        logicalNode.add(nhmiB);
        NHMI nhmiC = new NHMI();
        for (int i = 0; i < NUMBER_OF_CONNECTION; i++) {
            nhmiC.addSignals("IC"+(i+1), new NHMISignal("ic"+(i+1), mmxuList.get(i).icMV.getInstMag().getF()));
        }
        logicalNode.add(nhmiC);

        NHMI nhmiRMSA = new NHMI();
        for (int i = 0; i < NUMBER_OF_CONNECTION; i++) {
            nhmiRMSA.addSignals("RMS_IA"+(i+1), new NHMISignal("ia"+(i+1), mmxuList.get(i).getA().getPhsA().getInstCVal().getMag().getF()));
        }
        logicalNode.add(nhmiRMSA);
        NHMI nhmiRMSB = new NHMI();
        for (int i = 0; i < NUMBER_OF_CONNECTION; i++) {
            nhmiRMSB.addSignals("RMS_IB"+(i+1), new NHMISignal("ib"+(i+1), mmxuList.get(i).getA().getPhsB().getInstCVal().getMag().getF()));
        }
        logicalNode.add(nhmiRMSB);
        NHMI nhmiRMSC = new NHMI();
        for (int i = 0; i < NUMBER_OF_CONNECTION; i++) {
            nhmiRMSC.addSignals("RMS_IC"+(i+1), new NHMISignal("ic"+(i+1), mmxuList.get(i).getA().getPhsC().getInstCVal().getMag().getF()));
        }
        logicalNode.add(nhmiRMSC);

        NHMIP nhmipA = new NHMIP();
//        NHMIP nhmipB = new NHMIP();
//        NHMIP nhmipC = new NHMIP();
        nhmipA.addSignals(new NHMISignal("BUS_A", rmxu.getAmpLocResA().getInstMag().getF(), rmxu.getAmpLocPhsA().getInstMag().getF()));
       // nhmipB.addSignals(new NHMISignal("BUS_B", rmxu.getAmpLocResB().getInstMag().getF(), rmxu.getAmpLocPhsB().getInstMag().getF()));
     //   nhmipC.addSignals(new NHMISignal("BUS_C",rmxu.getAmpLocResC().getInstMag().getF(), rmxu.getAmpLocPhsC().getInstMag().getF()));
        logicalNode.add(nhmipA);
        while (lsvs.hasNext()) {
            logicalNode.forEach(LN::process);
        }
    }
}
