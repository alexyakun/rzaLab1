//package org.example.iec61850.lodicalNodes.protocol;
//
//import org.example.iec61850.lodicalNodes.common.LN;
//import org.example.iec61850.lodicalNodes.sv.EthernetListener;
//import org.example.iec61850.lodicalNodes.sv.SvDecoder;
//import org.example.iec61850.lodicalNodes.sv.SvPacket;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public class LSVS3 extends LN {
//    public LSVS3(){
//        EthernetListener ethernetListener = new EthernetListener();
//        ethernetListener.setNickName("Remote NDIS based Internet Sharing Device");
//        SvDecoder svDecoder = new SvDecoder();
//        int numPacket = 0;
//        double svTime = 0;
//        int checkCount = 0;
//        List<SvPacket> smallPacket = new ArrayList<>();
//
//    }
//        ethernetListener.addListener(packet ->{
//        Optional<SvPacket> svPacket = svDecoder.decode(packet);
//        double instIa = svPacket.get().getDataset().getInstIa();
//        System.out.println(instIa);
//    }
//
//    @Override
//    public void process() {
//
//    });
//        ethernetListener.start();
//}
