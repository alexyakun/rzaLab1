package org.example.iec61850.lodicalNodes.measurement;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.Filter.Filter;
import org.example.iec61850.Filter.Fourier;
import org.example.iec61850.lodicalNodes.LN;
import org.example.iec61850.node_parameters.DataObject.measured_and_metered_values.DEL;
import org.example.iec61850.node_parameters.DataObject.measured_and_metered_values.MV;
import org.example.iec61850.node_parameters.DataObject.measured_and_metered_values.WYE;

@Getter
@Setter
public class MMXU extends LN {
    /**
     * LN: Measurement Name: MMXU (LN: Название измерения: MMXU)
     */
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
    /**
     * Input
     * */
    public MV IaInst = new MV();
    public MV IbInst = new MV();
    public MV IcInst = new MV();
    /**
     * Output
     * */

    /**
     * Filter (буферы на каждую фазу)
     * */
    public final Filter ia = new Fourier(bufSize);
    public final Filter ib = new Fourier(bufSize);
    public final Filter ic = new Fourier(bufSize);
    @Override
    public void process() {
        this.ia.process(this.IaInst, A.getPhsA());
        this.ia.process(this.IbInst, A.getPhsB());
        this.ia.process(this.IcInst, A.getPhsC());
    }
}
