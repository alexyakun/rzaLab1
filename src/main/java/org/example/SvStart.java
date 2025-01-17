package org.example;

import org.example.iec61850.datatypes.status.ACD;
import org.example.iec61850.lodicalNodes.common.LN;
import org.example.iec61850.lodicalNodes.hmi.NHMI;
import org.example.iec61850.lodicalNodes.hmi.other.NHMISignal;
import org.example.iec61850.lodicalNodes.measurement.MMXU;
import org.example.iec61850.lodicalNodes.measurement.MSQI;
import org.example.iec61850.lodicalNodes.measurement.RDIR;
import org.example.iec61850.lodicalNodes.protection.PTOC;
import org.example.iec61850.lodicalNodes.sv.EthernetListener;
import org.example.iec61850.lodicalNodes.sv.SvDecoder;
import org.example.iec61850.lodicalNodes.sv.SvPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class SvStart {
    private static final List<LN> logicalNode = new ArrayList<>();
    private static final double TIME_MULTIPLIER = 20.0 / 80;
    public static void main(String[] args) {
        MMXU mmxu = new MMXU(TIME_MULTIPLIER);
        logicalNode.add(mmxu);


        logicalNode.add(mmxu);
        boolean operAccel = false;

        MSQI msqi = new MSQI();
        logicalNode.add(msqi);
        msqi.cur = mmxu.getA();
        msqi.volt = mmxu.getPNV();

        RDIR rdir = new RDIR(80, -80, 35, 5000);
        logicalNode.add(rdir);
        rdir.cur = mmxu.getA();
        rdir.lvolt = mmxu.getPPV();

        PTOC ptoc1 = new PTOC();
        logicalNode.add(ptoc1);
        ptoc1.getStrVal().getSetMag().getF().setValue(400.0);
        ptoc1.OpDiTmms.getSetVal().setValue(200);
        ptoc1.getTmMult().getSetMag().getF().setValue(20.0/160);
        ptoc1.dir = rdir.getDir();
        ptoc1.seq = msqi.SeqA;
        ptoc1.DirMod.getSetVal().setValue(ACD.DirGeneral.BOTH);
        ptoc1.getAccelEnable().setValue(false);
        ptoc1.accel.setValue(operAccel);

        PTOC ptoc2 = new PTOC();
        logicalNode.add(ptoc2);
        ptoc2.getStrVal().getSetMag().getF().setValue(200.0);
        ptoc2.OpDiTmms.getSetVal().setValue(400);
        ptoc2.getTmMult().getSetMag().getF().setValue(20.0/160);
        ptoc2.dir = rdir.getDir();
        ptoc2.seq = msqi.SeqA;
        ptoc2.DirMod.getSetVal().setValue(ACD.DirGeneral.BOTH);
        ptoc2.getAccelEnable().setValue(false);
        ptoc2.accel.setValue(operAccel);

        PTOC ptoc1Dir = new PTOC();
        logicalNode.add(ptoc1Dir);
        ptoc1Dir.getStrVal().getSetMag().getF().setValue(400.0);
        ptoc1Dir.OpDiTmms.getSetVal().setValue(200);
        ptoc1Dir.getTmMult().getSetMag().getF().setValue(20.0 / 160);
        ptoc1Dir.dir = rdir.getDir();
        ptoc1Dir.seq = msqi.SeqA;
        ptoc1Dir.DirMod.getSetVal().setValue(ACD.DirGeneral.FORWARD);
        ptoc1Dir.getAccelEnable().setValue(false);
        ptoc1Dir.accel.setValue(operAccel);

        PTOC ptoc2Dir = new PTOC();
        logicalNode.add(ptoc2Dir);
        ptoc2Dir.getStrVal().getSetMag().getF().setValue(200.0);
        ptoc2Dir.OpDiTmms.getSetVal().setValue(400);
        ptoc2Dir.getTmMult().getSetMag().getF().setValue(20.0 / 160);
        ptoc2Dir.dir = rdir.getDir();
        ptoc2Dir.seq = msqi.SeqA;
        ptoc2Dir.DirMod.getSetVal().setValue(ACD.DirGeneral.FORWARD);
        ptoc2Dir.getAccelEnable().setValue(false);
        ptoc2Dir.accel.setValue(operAccel);
        NHMI nhmi = new NHMI();
        nhmi.addSignals("IA", new NHMISignal("ia", mmxu.iaMV.getInstMag().getF()));
        nhmi.addSignals("IB", new NHMISignal("ib", mmxu.ibMV.getInstMag().getF()));
        nhmi.addSignals("IC", new NHMISignal("ic", mmxu.icMV.getInstMag().getF()));
        nhmi.addSignals("UA", new NHMISignal("ua", mmxu.uaMV.getInstMag().getF()));
        nhmi.addSignals("UB", new NHMISignal("ub", mmxu.ubMV.getInstMag().getF()));
        nhmi.addSignals("UC", new NHMISignal("uc", mmxu.ucMV.getInstMag().getF()));

        logicalNode.add(nhmi);
        NHMI nhmiPTOC = new NHMI();

        nhmiPTOC.addSignals("PhsA",
                new NHMISignal("Phase_A", mmxu.getA().getPhsA().getInstCVal().getMag().getF()));

        nhmiPTOC.addSignals("PhsB",
                new NHMISignal("Phase_B", mmxu.getA().getPhsB().getInstCVal().getMag().getF()));
        nhmiPTOC.addSignals("PhsC",
                new NHMISignal("Phase_C", mmxu.getA().getPhsC().getInstCVal().getMag().getF()));
        logicalNode.add(nhmiPTOC);

        NHMI nhmiSequence = new NHMI();
        logicalNode.add(nhmiSequence);
        nhmiSequence.addSignals(
                new NHMISignal("i_s3_mag",
                        msqi.SeqA.getC3().getInstCVal().getMag().getF()),
                new NHMISignal("str1", ptoc1.getStrVal().getSetMag().getF()),
                new NHMISignal("str2", ptoc2.getStrVal().getSetMag().getF()));
        NHMI nhmiDirection = new NHMI();
        logicalNode.add(nhmiDirection);
        nhmiDirection.addSignals(new NHMISignal("dir_A",
                rdir.getDir().getPhsA()));
        nhmiDirection.addSignals(new NHMISignal("dir_B",
                rdir.getDir().getPhsB()));
        nhmiDirection.addSignals(new NHMISignal("dir_C",
                rdir.getDir().getPhsC()));
        nhmiDirection.addSignals(new NHMISignal("dir_Gen",
                rdir.getDir().getGeneral()));
        NHMI nhmiPTOC1 = new NHMI();
        logicalNode.add(nhmiPTOC1);
        nhmiPTOC1.addSignals(new NHMISignal("ptoc1Str", ptoc1.getStr().getGeneral()));
        nhmiPTOC1.addSignals(new NHMISignal("ptoc1Op", ptoc1.getOp().getGeneral()));
        nhmiPTOC1.addSignals(new NHMISignal("ptoc2Str", ptoc2.getStr().getGeneral()));
        nhmiPTOC1.addSignals(new NHMISignal("ptoc2Op", ptoc2.getOp().getGeneral()));
        NHMI nhmiPTOC2 = new NHMI();
        logicalNode.add(nhmiPTOC2);
        nhmiPTOC2.addSignals(new NHMISignal("ptocDir1Str", ptoc1Dir.getStr().getGeneral()));
        nhmiPTOC2.addSignals(new NHMISignal("ptocDir1Op", ptoc1Dir.getOp().getGeneral()));
        nhmiPTOC2.addSignals(new NHMISignal("ptocDir2Str", ptoc2Dir.getStr().getGeneral()));
        nhmiPTOC2.addSignals(new NHMISignal("ptocDir2Op", ptoc2Dir.getOp().getGeneral()));


        EthernetListener ethernetListener = new EthernetListener();
        ethernetListener.setNickName("Remote NDIS based Internet Sharing Device");
        SvDecoder svDecoder = new SvDecoder();
        AtomicInteger counter= new AtomicInteger();
        ethernetListener.addListener(packet ->{

            Optional<SvPacket> svPacket = svDecoder.decode(packet);
            if(svPacket.isPresent()&& counter.get() !=svPacket.get().getSmpCount()){
                mmxu.uaMV.getInstMag().getF().setValue(svPacket.get().getDataset().getInstUa());
                mmxu.ubMV.getInstMag().getF().setValue(svPacket.get().getDataset().getInstUb());
                mmxu.ucMV.getInstMag().getF().setValue(svPacket.get().getDataset().getInstUc());
                mmxu.iaMV.getInstMag().getF().setValue(svPacket.get().getDataset().getInstIa());
                mmxu.ibMV.getInstMag().getF().setValue(svPacket.get().getDataset().getInstIb());
                mmxu.icMV.getInstMag().getF().setValue(svPacket.get().getDataset().getInstIc());
                for (LN ln : logicalNode) {
                    ln.process();
                }
                counter.set(svPacket.get().getSmpCount());
            }


        });
        ethernetListener.start();

    }
}

