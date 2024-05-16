package org.example.iec61850.lodicalNodes.measurement;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.measuredVal.CMV;
import org.example.iec61850.datatypes.measuredVal.DEL;
import org.example.iec61850.datatypes.measuredVal.WYE;
import org.example.iec61850.datatypes.settings.ASG;
import org.example.iec61850.datatypes.status.ACD;
import org.example.iec61850.lodicalNodes.common.LN;

@Getter
@Setter
public class RDIR extends LN {
    // Direction
    private ACD Dir = new ACD();
    // Characteristic angle
   // private ASG ChrAng;
    // Minimum phase angle in forward direction
    private ASG MinFwdAng = new ASG();
    // Minimum phase angle in reverse direction
    //private ASG MinRvAng;
    // Maximum phase angle in forward direction
    private ASG MaxFwdAng = new ASG();
    // Maximum phase angle in reverse direction
    //private ASG MaxRvAng;
    // Minimum operating current
    private ASG BlkValA = new ASG();
    // Minimum operating voltage
    //private ASG BlkValV;
    // Min phase-phase voltage
    private ASG MinPPV = new ASG();

    //input
    public DEL lvolt;
    public WYE cur;
    public RDIR(double maxFwAng, double minFwAng, double minI, double minPPV){
        this.MaxFwdAng.getSetMag().getF().setValue(maxFwAng);
        this.MinFwdAng.getSetMag().getF().setValue(minFwAng);
        this.BlkValA.getSetMag().getF().setValue(minI);
        this.MinPPV.getSetMag().getF().setValue(minPPV);
    }

    @Override
    public void process() {
        Dir.getPhsA().setValue(checkDirection(lvolt.getPhsBC(), cur.getPhsA()));
        Dir.getPhsB().setValue(checkDirection(lvolt.getPhsCA(), cur.getPhsB()));
        Dir.getPhsC().setValue(checkDirection(lvolt.getPhsAB(), cur.getPhsC()));
        Dir.getGeneral().setValue(Dir.getPhsA().getValue() && Dir.getPhsB().getValue() && Dir.getPhsC().getValue());
    }
    private boolean checkDirection(CMV volt, CMV cur){
        if (cur.getInstCVal().getMag().getF().getValue() < BlkValA.getSetMag().getF().getValue()){return false;}
        if (volt.getInstCVal().getMag().getF().getValue() < MinPPV.getSetMag().getF().getValue()) {return false;}
        double angle = volt.getInstCVal().getAng().getF().getValue() - cur.getInstCVal().getAng().getF().getValue();
        return (angle >= MinFwdAng.getSetMag().getF().getValue()) && (angle <= MaxFwdAng.getSetMag().getF().getValue());
    }

}
