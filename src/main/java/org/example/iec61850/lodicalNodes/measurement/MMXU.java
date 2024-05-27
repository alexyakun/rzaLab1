package org.example.iec61850.lodicalNodes.measurement;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.Filter.Filter;
import org.example.iec61850.Filter.Fourier;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.common.Vector;
import org.example.iec61850.frequency.FrequencyMeter;
import org.example.iec61850.frequency.ZeroCrossingMethod;
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

    private Attribute<Double> timeMult = new Attribute<>();
    public static int bufSize = 80;
    //input

    public MV iaMV = new MV();
    public MV ibMV = new MV();
    public MV icMV = new MV();
    public MV uaMV = new MV();
    public MV ubMV = new MV();
    public MV ucMV = new MV();
    //output
    private FrequencyMeter frq ;
    public final Filter ia = new Fourier(bufSize);
    public final Filter ib = new Fourier(bufSize);
    public final Filter ic = new Fourier(bufSize);
    public final Filter ua = new Fourier(bufSize);
    public final Filter ub = new Fourier(bufSize);
    public final Filter uc = new Fourier(bufSize);

    public MV zABxMV = new MV();
    public MV zAByMV = new MV();
    public MV zBCxMV = new MV();
    public MV zBCyMV = new MV();
    public MV zCAxMV = new MV();
    public MV zCAyMV = new MV();
    public MMXU(double timeMult){
        this.timeMult.setValue(timeMult);
        frq = new ZeroCrossingMethod(this.timeMult.getValue());
        zABxMV.getInstMag().getF().setValue(0.0);
        zAByMV.getInstMag().getF().setValue(0.0);
        zBCxMV.getInstMag().getF().setValue(0.0);
        zBCyMV.getInstMag().getF().setValue(0.0);
        zCAxMV.getInstMag().getF().setValue(0.0);
        zCAyMV.getInstMag().getF().setValue(0.0);
    }
    public Attribute<Double> freqq = new Attribute<>();


    @Override
    public void process() {
        this.frq.process(this.iaMV, freqq);
        this.ia.process(this.iaMV, A.getPhsA());
        this.ib.process(this.ibMV, A.getPhsB());
        this.ic.process(this.icMV, A.getPhsC());
        this.ua.process(this.uaMV, PNV.getPhsA());
        this.ub.process(this.ubMV, PNV.getPhsB());
        this.uc.process(this.ucMV, PNV.getPhsC());

        PPV.getPhsAB().setInstCVal(CompCal.dif(PNV.getPhsA().getInstCVal(),PNV.getPhsB().getInstCVal()));
        PPV.getPhsBC().setInstCVal(CompCal.dif(PNV.getPhsB().getInstCVal(),PNV.getPhsC().getInstCVal()));
        PPV.getPhsCA().setInstCVal(CompCal.dif(PNV.getPhsC().getInstCVal(),PNV.getPhsA().getInstCVal()));
        Z.getPhsA().setInstCVal(calcZ(A.getPhsA().getInstCVal(),A.getPhsB().getInstCVal(),PPV.getPhsAB().getInstCVal()));
        Z.getPhsB().setInstCVal(calcZ(A.getPhsB().getInstCVal(),A.getPhsC().getInstCVal(),PPV.getPhsBC().getInstCVal()));
        Z.getPhsC().setInstCVal(calcZ(A.getPhsC().getInstCVal(),A.getPhsA().getInstCVal(),PPV.getPhsCA().getInstCVal()));
        double zaMag = Z.getPhsA().getInstCVal().getMag().getF().getValue();
        double zaAng = Z.getPhsA().getInstCVal().getAng().getF().getValue();
        double zbMag = Z.getPhsA().getInstCVal().getMag().getF().getValue();
        double zbAng = Z.getPhsA().getInstCVal().getAng().getF().getValue();
        double zcMag = Z.getPhsA().getInstCVal().getMag().getF().getValue();
        double zcAng = Z.getPhsA().getInstCVal().getAng().getF().getValue();
        this.zABxMV.getInstMag().getF().setValue(zaMag*Math.cos(zaAng));

        this.zAByMV.getInstMag().getF().setValue(zaMag*Math.sin(zaAng));

        this.zBCxMV.getInstMag().getF().setValue(zbMag*Math.cos(zbAng));
        this.zBCyMV.getInstMag().getF().setValue(zbMag*Math.sin(zbAng));
        this.zCAxMV.getInstMag().getF().setValue(zcMag*Math.cos(zcAng));
        this.zCAyMV.getInstMag().getF().setValue(zcMag*Math.sin(zcAng));

    }
    private Vector calcZ(Vector i1, Vector i2, Vector u){
        Vector difCurrent = CompCal.dif(i1, i2);
        Vector res = CompCal.div(u, difCurrent);
        return res;

    }
}

