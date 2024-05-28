package org.example;

import org.example.iec61850.lodicalNodes.common.LN;
import org.example.iec61850.lodicalNodes.hmi.NHMI;
import org.example.iec61850.lodicalNodes.hmi.NHMIP;
import org.example.iec61850.lodicalNodes.hmi.other.NHMIPoint;
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
    private static String name = "Vkl";
    private static final double TIME_MULTIPLIER_FOR_FREQ = 1.0 / 1000;
    private static final int NUMBER_OF_CONNECTION = 4;
    private static final int NUMBER_OF_BLOCKED_HARMONIC = 5;
    private static final double PART_OF_HIGH_HARM_IN_BASE_HARM = 0.02;
    private static final double SET_FOR_PTOC = 5000;
    private static final double SET_MIN_DIFF = 1200;
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

        PDIF pdif = new PDIF(SET_MIN_DIFF);
        pdif.difA = rmxu.getAmpLocPhsA();
        pdif.difB = rmxu.getAmpLocPhsB();
        pdif.difC = rmxu.getAmpLocPhsC();
        pdif.rstA = rmxu.getAmpLocResA();
        pdif.rstB = rmxu.getAmpLocResB();
        pdif.rstC = rmxu.getAmpLocResC();
        pdif.pharList = pharList;
        logicalNode.add(pdif);

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
        NHMI nhmiDigitalSignal = new NHMI();
        nhmiDigitalSignal.addSignals("BUS_A", new NHMISignal("dif_A", pdif.getOperate().getPhsA()));
        nhmiDigitalSignal.addSignals("BUS_B", new NHMISignal("dif_B", pdif.getOperate().getPhsB()));
        nhmiDigitalSignal.addSignals("BUS_C", new NHMISignal("dif_C", pdif.getOperate().getPhsC()));
        nhmiDigitalSignal.addSignals("BLK_A", new NHMISignal("BLK_A", pdif.block.getPhsA()));
        nhmiDigitalSignal.addSignals("BLK_B", new NHMISignal("BLK_B", pdif.block.getPhsB()));
        nhmiDigitalSignal.addSignals("BLK_C", new NHMISignal("BLK_C", pdif.block.getPhsC()));
        logicalNode.add(nhmiDigitalSignal);

        NHMI diffCurrent = new NHMI();
        diffCurrent.addSignals("BUS_A", new NHMISignal("dif_cur_A", pdif.difA.getInstMag().getF()));
        diffCurrent.addSignals("BUS_B", new NHMISignal("dif_cur_B",pdif.difB.getInstMag().getF()));
        diffCurrent.addSignals("BUS_C", new NHMISignal("dif_cur_C", pdif.difC.getInstMag().getF()));
        logicalNode.add(diffCurrent);
        NHMI ptocOper = new NHMI();
        ptocOper.addSignals("BUS_A", new NHMISignal("ptoc_A", ptoc.getOp().getPhsA()));
        ptocOper.addSignals("BUS_B", new NHMISignal("ptoc_B",ptoc.getOp().getPhsB()));
        ptocOper.addSignals("BUS_C", new NHMISignal("ptoc_C", ptoc.getOp().getPhsC()));
        logicalNode.add(ptocOper);

        NHMIP nhmipA = new NHMIP();
        NHMIP nhmipB = new NHMIP();
//        NHMIP nhmipC = new NHMIP();
        nhmipA.drawCharacteristic("1 ступени",
                makeCharacteristic(0.53, SET_MIN_DIFF));
        nhmipB.drawCharacteristic("1 ступени",
                makeCharacteristic(0.53, SET_MIN_DIFF));
        nhmipA.addSignals(new NHMISignal("BUS_A", rmxu.getAmpLocResA().getInstMag().getF(), rmxu.getAmpLocPhsA().getInstMag().getF()));
        nhmipB.addSignals(new NHMISignal("BUS_B", rmxu.getAmpLocResB().getInstMag().getF(), rmxu.getAmpLocPhsB().getInstMag().getF()));
     //   nhmipC.addSignals(new NHMISignal("BUS_C",rmxu.getAmpLocResC().getInstMag().getF(), rmxu.getAmpLocPhsC().getInstMag().getF()));
        logicalNode.add(nhmipA);
        while (lsvs.hasNext()) {
            logicalNode.forEach(LN::process);
        }
    }
    private static List<NHMIPoint<Double, Double>> makeCharacteristic(double s, double difMin) {
        List<NHMIPoint<Double, Double>> pointsList = new ArrayList<>();
        for (double x = 0; x <= 14000; x += 10) {
            double y;
            if(x < difMin/s){
                y  = difMin;
            }else {
                y = x*s;
            }
            pointsList.add(new NHMIPoint<>(x, y));
        }
        return pointsList;
    }
}
