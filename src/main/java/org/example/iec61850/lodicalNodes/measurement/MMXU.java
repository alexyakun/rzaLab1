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
     *
     * Получать значения от CTs и VTs и вычислять измеряемые величины, такие как среднеквадратичные значения
     * тока и напряжения или потоки мощности из полученных выборок напряжения и тока. Эти значения обычно
     * используются для оперативных целей, таких как контроль и управление потоком мощности, отображение
     * на экране, оценка состояния и т.д. Должна быть обеспечена требуемая точность для этих функций.
     * (61850-5 - IEC: 2013)
     *
     * Функциональный класс LN: LN MMXU
     * Процедуры измерения в устройствах защиты являются частью специального алгоритма защиты,
     * представленного логическими узлами Xyz. Алгоритмы защиты, как и любая функция, выходят за рамки
     * стандарта связи. Поэтому LN Mxyz не должен использоваться в качестве входных данных для Pxyz.
     * Данные, связанные с неисправностью, такие как пиковое значение неисправности и т.д.,
     * всегда предоставляются строками типа Xyz, а не заимствованиями типа Xyz.
     */
    /**
     * Measured and metered values
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
//    private MV AvAPhs = new MV();
//    private MV AvPPVPhs = new MV();
//    private MV AvPhVPhs = new MV();
//    private MV AvWPhs = new MV();
//    private MV AvVAPhs = new MV();
//    private MV AvVArPhs = new MV();
//    private MV AvPFPhs = new MV();
//    private MV AvZPhs = new MV();
//    private MV MaxAPhs = new MV();
//    private MV MaxPPVPhs = new MV();
//    private MV MaxPhVPhs = new MV();
//    private MV MaxWPhs = new MV();
//    private MV MaxVAPhs = new MV();
//    private MV MaxVArPhs = new MV();
//    private MV MaxPFPhs = new MV();
//    private MV MaxZPPhs = new MV();
//    private MV MinAPhs = new MV();
//    private MV MinPPVPhs = new MV();
//    private MV MinPhVPhs = new MV();
//    private MV MinWPhs = new MV();
//    private MV MinVAPhs = new MV();
//    private MV MinVArPhs = new MV();
//    private MV MinPFPhs = new MV();
//    private MV MinZPPhs = new MV();
    /**
     * Setting
     */
//    private ENG ClcTotVA = new ENG();
//    private ENG PFSign = new ENG();

    /**
     * Size buffer
     * */
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
