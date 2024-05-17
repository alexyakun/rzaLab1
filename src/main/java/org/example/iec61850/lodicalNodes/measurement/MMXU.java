package org.example.iec61850.lodicalNodes.measurement;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.Filter.Filter;
import org.example.iec61850.Filter.Fourier;
import org.example.iec61850.Filter.RMS;
import org.example.iec61850.datatypes.common.Vector;
import org.example.iec61850.lodicalNodes.common.LN;
import org.example.iec61850.datatypes.measuredVal.DEL;
import org.example.iec61850.datatypes.measuredVal.MV;
import org.example.iec61850.datatypes.measuredVal.WYE;
import org.example.iec61850.utils.CompCal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class MMXU extends LN {
    //LN: Measurement Name: MMXU
    //Total active power (total P)
    private MV TotW = new MV();
    //Total reactive power (total Q)
    private MV TotWVAr = new MV();
    //Total apparent power (total S)
    private MV TotVA = new MV();
    //Average power factor (total PF)
    private MV TotPF = new MV();
    //Frequency
    private MV Hz = new MV();
    //Phase to phase voltages (VL1,VL2, …)
    private DEL PPV = new DEL();
    //Phase to neutral voltage
    private WYE PNV = new WYE();
    //Phase to ground voltages (VL1ER, …)
    private WYE PhV = new WYE();
    //Phase currents (IL1, IL2, IL3)
    private WYE A = new WYE();
    //Phase active power (P)
    private WYE W = new WYE();
    //Phase reactive power (Q)
    private WYE VAr = new WYE();
    //Phase apparent power (S)
    private WYE VA = new WYE();
    //Phase power factor
    private WYE PF = new WYE();
    //Phase impedance
    private WYE Z = new WYE();
    public static int bufSize = 80;
    //input
    public MV iaMV = new MV();
    public MV ibMV = new MV();
    public MV icMV = new MV();
    public MV uaMV = new MV();
    public MV ubMV = new MV();
    public MV ucMV = new MV();
    //output
    public final Filter ia = new Fourier(bufSize);
    public final Filter ib = new Fourier(bufSize);
    public final Filter ic = new Fourier(bufSize);
    public final Filter ua = new Fourier(bufSize);
    public final Filter ub = new Fourier(bufSize);
    public final Filter uc = new Fourier(bufSize);

    @Override
    public void process() {
        this.ia.process(this.iaMV, A.getPhsA());
        this.ib.process(this.ibMV, A.getPhsB());
        this.ic.process(this.icMV, A.getPhsC());
        this.ua.process(this.uaMV, PNV.getPhsA());
        this.ub.process(this.ubMV, PNV.getPhsB());
        this.uc.process(this.ucMV, PNV.getPhsC());

        PPV.getPhsAB().setInstCVal(CompCal.dif(A.getPhsA().getInstCVal(),A.getPhsB().getInstCVal()));
        PPV.getPhsBC().setInstCVal(CompCal.dif(A.getPhsB().getInstCVal(),A.getPhsC().getInstCVal()));
        PPV.getPhsCA().setInstCVal(CompCal.dif(A.getPhsC().getInstCVal(),A.getPhsA().getInstCVal()));
        Z.getPhsA().setInstCVal(calcZ(A.getPhsA().getInstCVal(),A.getPhsB().getInstCVal(),PPV.getPhsAB().getInstCVal()));
        Z.getPhsB().setInstCVal(calcZ(A.getPhsB().getInstCVal(),A.getPhsC().getInstCVal(),PPV.getPhsBC().getInstCVal()));
        Z.getPhsC().setInstCVal(calcZ(A.getPhsC().getInstCVal(),A.getPhsA().getInstCVal(),PPV.getPhsCA().getInstCVal()));
    }
    private Vector calcZ(Vector i1, Vector i2, Vector u){
        Vector difCurrent = CompCal.dif(i1, i2);
        return CompCal.div(u, difCurrent);

    }
}

