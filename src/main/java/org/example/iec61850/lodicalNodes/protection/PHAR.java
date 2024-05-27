package org.example.iec61850.lodicalNodes.protection;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.measuredVal.CMV;
import org.example.iec61850.datatypes.measuredVal.HWYE;
import org.example.iec61850.datatypes.settings.ASG;
import org.example.iec61850.datatypes.settings.ING;
import org.example.iec61850.datatypes.status.ACD;
import org.example.iec61850.lodicalNodes.common.LN;
@Getter
@Setter
public class PHAR extends LN {
    private ACD Str = new ACD(false);
    private ING HaRst = new ING();
    private ASG PhStr = new ASG();
    public PHAR(int numberHarmonic, double partOfHighHarmonic){
        this.HaRst.getSetVal().setValue(numberHarmonic);
        this.PhStr.getSetMag().getF().setValue(partOfHighHarmonic);
    }
    //input DATA
    public HWYE inputI;


    @Override
    public void process() {
        CMV hiHarmA = inputI.getPhsAHar().get(HaRst.getSetVal().getValue() - 1);
        CMV hiHarmB = inputI.getPhsBHar().get(HaRst.getSetVal().getValue() - 1);
        CMV hiHarmC = inputI.getPhsCHar().get(HaRst.getSetVal().getValue() - 1);
        CMV baseHarmA = inputI.getPhsAHar().get(0);
        CMV baseHarmB = inputI.getPhsBHar().get(0);
        CMV baseHarmC = inputI.getPhsCHar().get(0);
        if(partOfHighHarmonic(hiHarmA,baseHarmA) > PhStr.getSetMag().getF().getValue() ){
            this.Str.getPhsA().setValue(true);
        }else {
            this.Str.getPhsA().setValue(false);
        }
        if(partOfHighHarmonic(hiHarmB,baseHarmB) > PhStr.getSetMag().getF().getValue() ){
            this.Str.getPhsB().setValue(true);
        }else {
            this.Str.getPhsB().setValue(false);
        }
        if(partOfHighHarmonic(hiHarmC,baseHarmC) > PhStr.getSetMag().getF().getValue() ){
            this.Str.getPhsC().setValue(true);
        }else {
            this.Str.getPhsC().setValue(false);
        }
    }
    private double partOfHighHarmonic(CMV hiHarm, CMV baseHarm){
        return  hiHarm.getInstCVal().getMag().getF().getValue()/baseHarm.getInstCVal().getMag().getF().getValue();
    }
}
