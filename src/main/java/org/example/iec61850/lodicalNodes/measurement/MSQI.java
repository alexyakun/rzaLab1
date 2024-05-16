package org.example.iec61850.lodicalNodes.measurement;

import org.example.iec61850.datatypes.common.Vector;
import org.example.iec61850.datatypes.measuredVal.SEQ;
import org.example.iec61850.datatypes.measuredVal.WYE;
import org.example.iec61850.lodicalNodes.common.LN;
import org.example.iec61850.utils.CompCal;
import java.util.List;

public class MSQI  extends LN {
    //Positive, negative and zero sequence current
    public SEQ SeqA= new SEQ();
    //Positive, negative and zero sequence voltage
    public SEQ SeqV = new SEQ();

    // inputs
    public WYE cur = new WYE();
    public WYE volt = new WYE();

    private Vector a;
    private Vector a2;
    private Vector coef;

    public MSQI(){
        this.a = new Vector();
        this.a.getAng().getF().setValue(Math.PI*2/3);
        this.a.getMag().getF().setValue(1.0);
        this.a2 = new Vector();
        this.a2.getAng().getF().setValue(Math.PI*4/3);
        this.a2.getMag().getF().setValue(1.0);
        this.coef = new Vector();
        this.coef.getAng().getF().setValue(0.0);
        this.coef.getMag().getF().setValue(1.0/3);

    }
    @Override
    public void process() {
        Vector curPos = CompCal.sumAll(
                List.of(cur.getPhsA().getInstCVal(),
                        CompCal.mult(a,cur.getPhsB().getInstCVal()),
                        CompCal.mult(a2,cur.getPhsC().getInstCVal())));
        Vector curNeg = CompCal.sumAll(
                List.of(cur.getPhsA().getInstCVal(),
                        CompCal.mult(a2,cur.getPhsB().getInstCVal()),
                        CompCal.mult(a,cur.getPhsC().getInstCVal())));
        Vector curZero = CompCal.sumAll(
                List.of(cur.getPhsA().getInstCVal(),
                        cur.getPhsB().getInstCVal(),
                        cur.getPhsC().getInstCVal()));
        Vector voltPos = CompCal.sumAll(
                List.of(volt.getPhsA().getInstCVal(),
                        CompCal.mult(a,volt.getPhsB().getInstCVal()),
                        CompCal.mult(a2,volt.getPhsC().getInstCVal())));
        Vector voltNeg = CompCal.sumAll(
                List.of(volt.getPhsA().getInstCVal(),
                        CompCal.mult(a2,volt.getPhsB().getInstCVal()),
                        CompCal.mult(a,volt.getPhsC().getInstCVal())));
        Vector voltZero = CompCal.sumAll(
                List.of(volt.getPhsA().getInstCVal(),
                        CompCal.mult(a2,volt.getPhsB().getInstCVal()),
                        CompCal.mult(a,volt.getPhsC().getInstCVal())));

        this.SeqA.getC1().getInstCVal().getMag().getF().setValue(curPos.getMag().getF().getValue()/3);
        this.SeqA.getC1().getInstCVal().getAng().getF().setValue(curPos.getAng().getF().getValue());
        this.SeqA.getC2().getInstCVal().getMag().getF().setValue(curNeg.getMag().getF().getValue()/3);
        this.SeqA.getC2().getInstCVal().getAng().getF().setValue(curNeg.getAng().getF().getValue());
        this.SeqA.getC3().getInstCVal().getMag().getF().setValue(curZero.getMag().getF().getValue()/3);
        this.SeqA.getC3().getInstCVal().getAng().getF().setValue(curZero.getMag().getF().getValue());

        this.SeqV.getC1().getInstCVal().getMag().getF().setValue(voltPos.getMag().getF().getValue()/3);
        this.SeqV.getC1().getInstCVal().getAng().getF().setValue(voltPos.getAng().getF().getValue());
        this.SeqV.getC2().getInstCVal().getMag().getF().setValue(voltNeg.getMag().getF().getValue()/3);
        this.SeqV.getC2().getInstCVal().getAng().getF().setValue(voltNeg.getAng().getF().getValue());
        this.SeqV.getC3().getInstCVal().getMag().getF().setValue(voltZero.getMag().getF().getValue()/3);
        this.SeqV.getC3().getInstCVal().getAng().getF().setValue(voltZero.getMag().getF().getValue());


    }

}
