package org.example.iec61850.lodicalNodes.measurement;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.Filter.AdaptiveFourier;
import org.example.iec61850.Filter.Fourier;
import org.example.iec61850.datatypes.measuredVal.HWYE;
import org.example.iec61850.datatypes.measuredVal.MV;
import org.example.iec61850.lodicalNodes.common.LN;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class MHAI extends LN {
    private final static int BUFFSIZE = (int) (45/0.001);
    private final static int AMOUNT_HARMONIC = 10;
    private MV Hz = new MV();
    private HWYE HA = new HWYE(AMOUNT_HARMONIC);
    public List<AdaptiveFourier> fourierIa = new ArrayList<>();
    public List<AdaptiveFourier> fourierIb = new ArrayList<>();
    public List<AdaptiveFourier> fourierIc = new ArrayList<>();


    public MHAI(){
        this.Hz.getInstMag().getF().setValue(50.0);
        for (int i = 1; i <= AMOUNT_HARMONIC; i++) {
            fourierIa.add(new AdaptiveFourier(BUFFSIZE, Hz.getInstMag().getF().getValue() * i));
            fourierIb.add(new AdaptiveFourier(BUFFSIZE, Hz.getInstMag().getF().getValue() * i));
            fourierIc.add(new AdaptiveFourier(BUFFSIZE, Hz.getInstMag().getF().getValue() * i));
        }
    }
    //input

    public MV iaMV = new MV();
    public MV ibMV = new MV();
    public MV icMV = new MV();
    public MV uaMV = new MV();
    public MV ubMV = new MV();
    public MV ucMV = new MV();

    @Override
    public void process() {
        for (int i = 0; i < AMOUNT_HARMONIC; i++) {
            fourierIa.get(i).process(iaMV, HA.getPhsAHar().get(i));
            fourierIb.get(i).process(ibMV, HA.getPhsBHar().get(i));
            fourierIc.get(i).process(icMV, HA.getPhsCHar().get(i));
        }
    }
}
