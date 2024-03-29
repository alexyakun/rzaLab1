package org.example.iec61850.lodicalNodes.measurement;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.Filter.Filter;
import org.example.iec61850.Filter.RMS;
import org.example.iec61850.lodicalNodes.common.LN;
import org.example.iec61850.datatypes.measuredVal.DEL;
import org.example.iec61850.datatypes.measuredVal.MV;
import org.example.iec61850.datatypes.measuredVal.WYE;

@Getter
@Setter
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
    public static int bufSize = 20;
    //input
    public MV iaMV = new MV();
    public MV ibMV = new MV();
    public MV icMV = new MV();
    //output
    public final Filter ia = new RMS(bufSize);
    public final Filter ib = new RMS(bufSize);
    public final Filter ic = new RMS(bufSize);
    @Override
    public void process() {
        this.ia.process(this.iaMV, A.getPhsA());
        this.ib.process(this.ibMV, A.getPhsB());
        this.ic.process(this.icMV, A.getPhsC());
    }
}
