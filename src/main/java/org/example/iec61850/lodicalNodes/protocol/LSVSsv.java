//package org.example.iec61850.lodicalNodes.protocol;
//
//import lombok.Getter;
//import lombok.Setter;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.example.iec61850.datatypes.measuredVal.MV;
//import org.example.iec61850.lodicalNodes.common.LN;
//import org.pcap4j.core.*;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.concurrent.CopyOnWriteArrayList;
//
//@Slf4j
//@Setter
//@Getter
//public class LSVS extends LN {
//    public MV ia = new MV();
//    public MV ib = new MV();
//    public MV ic = new MV();
//    public MV ua = new MV();
//    public MV ub = new MV();
//    public MV uc = new MV();
//    @Setter
//    private String nicName;
//    private PcapHandle handle;
//    @Setter
//    private int datasetSize;
//    private int selector = 0;
//    private final List<MyPacketListener> packetListeners = new
//            CopyOnWriteArrayList<>();
//    private final PacketListener defaultPacketListener = packet -> {
//        if(selector == 0) {
//            decode(packet);
//            packetListeners.forEach(listener -> listener.listen());
//        }
//        selector ++;
//        if (selector == 2) selector = 0;
//    };
//    @Override
//    @SneakyThrows
//    public void process() {
//
//        if (handle == null){
//            initializeNetworkInterface();
//            if (handle != null) {
//                String filter = "ether proto 0x88ba && ether dst 01:0C:CD:04:00:01";
//                handle.setFilter(filter,
//                        BpfProgram.BpfCompileMode.OPTIMIZE);
//                Thread captureThread = new Thread(() -> {
//                    try {
//                        log.info("Starting packet capture");
//                        handle.loop(0, defaultPacketListener);
//                    } catch (PcapNativeException e) {
//                        throw new RuntimeException(e);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    } catch (NotOpenException e) {
//                        throw new RuntimeException(e);
//                    }
//
//                    log.info("Packet capture finished");
//
//                });
//                captureThread.start();
//            }
//        }
//    }
//    @SneakyThrows
//    private void initializeNetworkInterface() {
//        Optional<PcapNetworkInterface> nic = Pcaps.findAllDevs().stream()
//                .filter(i -> nicName.equals(i.getDescription()))
//                .findFirst();
//        if (nic.isPresent()) {
//            handle = nic.get().openLive(1500,
//                    PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
//            log.info("Network Handler created: {}", nic);
//        } else {
//            log.error("Network interface not found");
//        }
//    }
//    public void addListener(MyPacketListener listener) {
//        packetListeners.add(listener);
//    }
//    public void decode(PcapPacket packet){
//        try {
//            byte[] data = packet.getRawData();
//            int strDataByte = data.length - datasetSize;
//            ia.getInstMag().getF().setValue((double)
//                    (byteArrayToInt(data, strDataByte) / 1000));
//            ib.getInstMag().getF().setValue((double)
//                    (byteArrayToInt(data, strDataByte + 8) / 1000));
//            ic.getInstMag().getF().setValue((double)
//                    (byteArrayToInt(data, strDataByte + 16) / 1000));
//            ua.getInstMag().getF().setValue((double)
//                    (byteArrayToInt(data, strDataByte + 32) / 62));
//            ub.getInstMag().getF().setValue((double)
//                    (byteArrayToInt(data, strDataByte + 40) / 62));
//            uc.getInstMag().getF().setValue((double)
//                    (byteArrayToInt(data, strDataByte + 48) / 62));
//        } catch (Exception e) {log.error("Cannot parse sv packet");}
//    }
//    public static int byteArrayToInt(byte[] b, int offset){
//        return b[offset + 3] & 0xFF | (b[offset + 2] & 0xFF) << 8 |
//                (b[offset + 1] & 0xFF) << 16 | (b[offset] & 0xFF) << 24;
//    }
//}