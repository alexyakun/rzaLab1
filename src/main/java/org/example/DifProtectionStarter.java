package org.example;

import org.example.iec61850.lodicalNodes.common.LN;
import org.example.iec61850.lodicalNodes.measurement.MHAI;
import org.example.iec61850.lodicalNodes.measurement.MMXU;
import org.example.iec61850.lodicalNodes.protection.PHAR;
import org.example.iec61850.lodicalNodes.protocol.LSVS;

import java.util.ArrayList;
import java.util.List;

public class DifProtectionStarter {
    private static final List<LN> logicalNode = new ArrayList<>();
    private static String path = "D:\\2mag\\algorithm rza\\lr2\\Opyty\\Опыты\\";
    private static String name = "KZ3";
    private static final double TIME_MULTIPLIER_FOR_FREQ = 20.0 / 80 / 1000;
    private static final int NUMBER_OF_CONNECTION = 5;
    private static final int NUMBER_OF_BLOCKED_HARMONIC = 5;
    private static final double PART_OF_HIGH_HARM_IN_BASE_HARM = 0.01;
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
        List<MHAI> mhaiList = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_CONNECTION; i++) {
            MHAI mhai = new MHAI();
            mhai.iaMV = lsvs.getOut().get(0 + i*3);
            mhai.ibMV = lsvs.getOut().get(1 + i*3);
            mhai.icMV = lsvs.getOut().get(2 + i*3);
            mhaiList.add(mhai);
        }
        List<PHAR> pharList = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_CONNECTION; i++) {
            PHAR phar = new PHAR(NUMBER_OF_BLOCKED_HARMONIC, PART_OF_HIGH_HARM_IN_BASE_HARM);
            phar.inputI  = mhaiList.get(i).getHA();
            pharList.add(phar);
        }
        logicalNode.addAll(pharList);
    }
}
