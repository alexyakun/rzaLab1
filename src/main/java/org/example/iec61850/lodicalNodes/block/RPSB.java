package org.example.iec61850.lodicalNodes.block;

import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.controls.INC;
import org.example.iec61850.datatypes.measuredVal.SEQ;
import org.example.iec61850.datatypes.settings.ASG;
import org.example.iec61850.datatypes.status.ACD;
import org.example.iec61850.datatypes.status.ACT;
import org.example.iec61850.datatypes.status.SPG;
import org.example.iec61850.datatypes.status.SPS;
import org.example.iec61850.lodicalNodes.common.LN;

public class RPSB extends LN {
    //Start(power swing detected)
    private ACD Str = new ACD();
    //Operate (out of step tripping)
    private ACT Op = new ACT();
    // Blocking of PDIS zone
    private SPS BlkZn = new SPS();
    //counter
    private INC OpCntRs = new INC();
    //Power swing delta
    private ASG SwgVal = new ASG();
    private SPG NgEna = new SPG();

    public SEQ seq = new SEQ();
    @Override
    public void process() {
        double negSeqVal = seq.getC2().getInstCVal().getMag().getF().getValue();
        if(NgEna.getStVal().getValue() && negSeqVal < SwgVal.getSetMag().getF().getValue()){
            BlkZn.getStVal().setValue(true);
        } else {
            BlkZn.getStVal().setValue(false);
        }
    }
}
