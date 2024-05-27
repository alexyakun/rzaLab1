package org.example;

import org.example.iec61850.datatypes.status.ACD;
import org.example.iec61850.lodicalNodes.block.RPSB;
import org.example.iec61850.lodicalNodes.common.LN;
import org.example.iec61850.lodicalNodes.hmi.NHMI;
import org.example.iec61850.lodicalNodes.hmi.NHMIP;
import org.example.iec61850.lodicalNodes.hmi.other.NHMIPoint;
import org.example.iec61850.lodicalNodes.hmi.other.NHMISignal;
import org.example.iec61850.lodicalNodes.measurement.MMXU;
import org.example.iec61850.lodicalNodes.measurement.MSQI;
import org.example.iec61850.lodicalNodes.protection.PDIS;
import org.example.iec61850.lodicalNodes.protocol.LSVS;

import java.util.ArrayList;
import java.util.List;

public class NoSvStart {
    private static final List<LN> logicalNode = new ArrayList<>();
    private static String path = "D:\\2mag\\algorithm rza\\lr2\\Opyty\\Опыты\\";
    private static String name = "KZ3";
    private static final double TIME_MULTIPLIER_FOR_FREQ = 20.0 / 80 / 1000;
    public static void main(String[] args) throws Exception {
        LSVS lsvs = new LSVS();
        lsvs.setPath(path);
        lsvs.setFileName(name);
        logicalNode.add(lsvs);

        MMXU mmxu = new MMXU(TIME_MULTIPLIER_FOR_FREQ );
        mmxu.uaMV = lsvs.getOut().get(0);
        mmxu.ubMV = lsvs.getOut().get(1);
        mmxu.ucMV = lsvs.getOut().get(2);
        mmxu.iaMV = lsvs.getOut().get(3);
        mmxu.ibMV = lsvs.getOut().get(4);
        mmxu.icMV = lsvs.getOut().get(5);
        logicalNode.add(mmxu);

        MSQI msqi = new MSQI();
        msqi.cur = mmxu.getA();
        msqi.volt = mmxu.getPNV();
        logicalNode.add(msqi);

        RPSB rpsb = new RPSB();
        rpsb.seq = msqi.SeqA;
        rpsb.getSwgVal().getSetMag().getF().setValue(100.0);
        rpsb.getNgEna().getStVal().setValue(true);
        logicalNode.add(rpsb);

        PDIS pdis1 = new PDIS();
        pdis1.getPoRch().getSetMag().getF().setValue(70.0);
        pdis1.getLinAng().getSetMag().getF().setValue(Math.PI / 4);
        pdis1.getOpDITmmms().getSetVal().setValue(10);
        pdis1.getTmMult().getSetMag().getF().setValue(20.0 / 80);
        pdis1.DirMod.getSetVal().setValue(ACD.DirGeneral.BOTH);
        pdis1.Z = mmxu.getZ();
        pdis1.BlkZn = rpsb.getBlkZn();
        logicalNode.add(pdis1);

        PDIS pdis2 = new PDIS();
        pdis2.getPoRch().getSetMag().getF().setValue(150.0);
        pdis2.getLinAng().getSetMag().getF().setValue(Math.PI / 4);
        pdis2.getOpDITmmms().getSetVal().setValue(175);
        pdis2.getTmMult().getSetMag().getF().setValue(20.0 / 80);
        pdis2.DirMod.getSetVal().setValue(ACD.DirGeneral.BOTH);
        pdis2.Z = mmxu.getZ();
        pdis2.BlkZn = rpsb.getBlkZn();
        logicalNode.add(pdis2);

        PDIS pdis3 = new PDIS();
        pdis3.getPoRch().getSetMag().getF().setValue(300.0);
        pdis3.getLinAng().getSetMag().getF().setValue(Math.PI / 4);
        pdis3.getOpDITmmms().getSetVal().setValue(350);
        pdis3.getTmMult().getSetMag().getF().setValue(20.0 / 80);
        pdis3.DirMod.getSetVal().setValue(ACD.DirGeneral.BOTH);
        pdis3.Z = mmxu.getZ();
        pdis3.BlkZn = rpsb.getBlkZn();
        logicalNode.add(pdis3);

        PDIS pdisDir1 = new PDIS();
        pdisDir1.getPoRch().getSetMag().getF().setValue(70.0);
        pdisDir1.getLinAng().getSetMag().getF().setValue(Math.PI / 4);
        pdisDir1.getOpDITmmms().getSetVal().setValue(10);
        pdisDir1.getTmMult().getSetMag().getF().setValue(20.0 / 80);
        pdisDir1.DirMod.getSetVal().setValue(ACD.DirGeneral.FORWARD);
        pdisDir1.Z = mmxu.getZ();
        pdisDir1.BlkZn = rpsb.getBlkZn();
        logicalNode.add(pdisDir1);

        PDIS pdisDir2 = new PDIS();
        pdisDir2.getPoRch().getSetMag().getF().setValue(150.0);
        pdisDir2.getLinAng().getSetMag().getF().setValue(Math.PI / 4);
        pdisDir2.getOpDITmmms().getSetVal().setValue(175);
        pdisDir2.getTmMult().getSetMag().getF().setValue(20.0 / 80);
        pdisDir2.DirMod.getSetVal().setValue(ACD.DirGeneral.FORWARD);
        pdisDir2.Z = mmxu.getZ();
        pdisDir2.BlkZn = rpsb.getBlkZn();
        logicalNode.add(pdisDir2);

        PDIS pdisDir3 = new PDIS();
        pdisDir3.getPoRch().getSetMag().getF().setValue(300.0);
        pdisDir3.getLinAng().getSetMag().getF().setValue(Math.PI / 4);
        pdisDir3.getOpDITmmms().getSetVal().setValue(350);
        pdisDir3.getTmMult().getSetMag().getF().setValue(20.0 / 80);
        pdisDir3.DirMod.getSetVal().setValue(ACD.DirGeneral.FORWARD);
        pdisDir3.Z = mmxu.getZ();
        pdisDir3.BlkZn = rpsb.getBlkZn();
        logicalNode.add(pdisDir3);

        NHMI nhmi = new NHMI();
        nhmi.addSignals("IA", new NHMISignal("ia", mmxu.iaMV.getInstMag().getF()));
        nhmi.addSignals("IB", new NHMISignal("ib", mmxu.ibMV.getInstMag().getF()));
        nhmi.addSignals("IC", new NHMISignal("ic", mmxu.icMV.getInstMag().getF()));
        nhmi.addSignals("UA", new NHMISignal("ua", mmxu.uaMV.getInstMag().getF()));
        nhmi.addSignals("UB", new NHMISignal("ub", mmxu.ubMV.getInstMag().getF()));
        nhmi.addSignals("UC", new NHMISignal("uc", mmxu.ucMV.getInstMag().getF()));
        logicalNode.add(nhmi);
        NHMI nhmiFourier = new NHMI();

        nhmiFourier.addSignals("PhsA", new NHMISignal("Phase_A", mmxu.getA().getPhsA().getInstCVal().getMag().getF()));

        nhmiFourier.addSignals("PhsB",
                new NHMISignal("Phase_B", mmxu.getA().getPhsB().getInstCVal().getMag().getF()));
        nhmiFourier.addSignals("PhsC",
                new NHMISignal("Phase_C", mmxu.getA().getPhsC().getInstCVal().getMag().getF()));
        nhmiFourier.addSignals("neqSeq",
                new NHMISignal("neqSeq", msqi.SeqA.getC2().getInstCVal().getMag().getF()));
        logicalNode.add(nhmiFourier);

        NHMI nhmiDigitalSignal = new NHMI();
        nhmiDigitalSignal.addSignals("Discrete I STR", new NHMISignal("pdis1_1str", pdis1.getStr().getGeneral()));
        nhmiDigitalSignal.addSignals("Discrete I OP", new NHMISignal("pdis1_1op", pdis1.getOp().getGeneral()));
        nhmiDigitalSignal.addSignals("Discrete II STR", new NHMISignal("pdis2_2str", pdis2.getStr().getGeneral()));
        nhmiDigitalSignal.addSignals("Discrete II OP", new NHMISignal("pdis2_2op", pdis2.getOp().getGeneral()));
        nhmiDigitalSignal.addSignals("Discrete III STR", new NHMISignal("pdis3_2str", pdis3.getStr().getGeneral()));
        nhmiDigitalSignal.addSignals("Discrete III OP", new NHMISignal("pdis3_2op", pdis3.getOp().getGeneral()));
        nhmiDigitalSignal.addSignals("BLK", new NHMISignal("BLK", rpsb.getBlkZn().getStVal()));
        logicalNode.add(nhmiDigitalSignal);
        NHMI nhmiDigitalSignalDir = new NHMI();
        nhmiDigitalSignalDir.addSignals("Discrete I DIR STR", new NHMISignal("pdis1_1str", pdisDir1.getStr().getGeneral()));
        nhmiDigitalSignalDir.addSignals("Discrete I DIR OP", new NHMISignal("pdis1_1op", pdisDir1.getOp().getGeneral()));
        nhmiDigitalSignalDir.addSignals("Discrete II DIR STR", new NHMISignal("pdis2_2str", pdisDir2.getStr().getGeneral()));
        nhmiDigitalSignalDir.addSignals("Discrete II DIR OP", new NHMISignal("pdis2_2op", pdisDir2.getOp().getGeneral()));
        nhmiDigitalSignalDir.addSignals("Discrete III DIR STR", new NHMISignal("pdis3_2str", pdisDir3.getStr().getGeneral()));
        nhmiDigitalSignalDir.addSignals("Discrete III DIR OP", new NHMISignal("pdis3_2op", pdisDir3.getOp().getGeneral()));
        nhmiDigitalSignalDir.addSignals("BLK", new NHMISignal("BLK", rpsb.getBlkZn().getStVal()));
        logicalNode.add(nhmiDigitalSignalDir);


        NHMIP nhmip = new NHMIP();
        double pdisDir1Rad = pdisDir1.getPoRch().getSetMag().getF().getValue() / 2;
        double pdisDir2Rad = pdisDir2.getPoRch().getSetMag().getF().getValue() / 2;
        double pdisDir3Rad = pdisDir3.getPoRch().getSetMag().getF().getValue() / 2;
        double pdisDir1Ang = pdisDir1.getLinAng().getSetMag().getF().getValue();
        double pdisDir2Ang = pdisDir1.getLinAng().getSetMag().getF().getValue();
        double pdisDir3Ang = pdisDir1.getLinAng().getSetMag().getF().getValue();
        nhmip.drawCharacteristic("1 ступени",
                makeCharacteristic(0, 0, pdis1.getPoRch().getSetMag().getF().getValue() / 2));
        nhmip.drawCharacteristic("2 ступени",
                makeCharacteristic(0, 0, pdis2.getPoRch().getSetMag().getF().getValue() / 2));
        nhmip.drawCharacteristic("3 ступени",
                makeCharacteristic(0, 0, pdis3.getPoRch().getSetMag().getF().getValue() / 2));
        nhmip.drawCharacteristic("1 ступени напр",
                makeCharacteristic(findXo(pdisDir1Rad, pdisDir1Ang), findYo(pdisDir1Rad, pdisDir1Ang), pdisDir1Rad));
        nhmip.drawCharacteristic("2 ступени напр",
                makeCharacteristic(findXo(pdisDir2Rad, pdisDir2Ang), findYo(pdisDir2Rad, pdisDir2Ang), pdisDir2Rad));
        nhmip.drawCharacteristic("3 ступени напр",
                makeCharacteristic(findXo(pdisDir3Rad, pdisDir3Ang), findYo(pdisDir3Rad, pdisDir3Ang), pdisDir3Rad));
        nhmip.addSignals(new NHMISignal("ZAB", mmxu.zABxMV.getInstMag().getF(), mmxu.zAByMV.getInstMag().getF()));
        nhmip.addSignals(new NHMISignal("ZBC", mmxu.zBCxMV.getInstMag().getF(), mmxu.zBCyMV.getInstMag().getF()));
        nhmip.addSignals(new NHMISignal("ZCA", mmxu.zCAxMV.getInstMag().getF(), mmxu.zCAyMV.getInstMag().getF()));
        logicalNode.add(nhmip);

        while (lsvs.hasNext()) {
            logicalNode.forEach(LN::process);
        }
    }

    private static double findXo(double rad, double angle) {
        return rad * Math.cos(angle);
    }

    private static double findYo(double rad, double angle) {
        return rad * Math.sin(angle);
    }

    private static List<NHMIPoint<Double, Double>> makeCharacteristic(double x0, double y0, double r) {
        List<NHMIPoint<Double, Double>> pointsList = new ArrayList<>();
        for (double x = -2 * r; x <= 2 * r; x += 0.1) {
            double y1 = Math.sqrt(Math.pow(r, 2) - Math.pow((x - x0), 2)) + y0;
            double y2 = -Math.sqrt(Math.pow(r, 2) - Math.pow((x - x0), 2)) + y0;
            pointsList.add(new NHMIPoint<>(x, y1));
            pointsList.add(new NHMIPoint<>(x, y2));
        }
        return pointsList;
    }
}
