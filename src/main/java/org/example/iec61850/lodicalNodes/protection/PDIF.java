package org.example.iec61850.lodicalNodes.protection;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.measuredVal.SAV;
import org.example.iec61850.datatypes.settings.ASG;
import org.example.iec61850.datatypes.status.ACD;
import org.example.iec61850.datatypes.status.ACT;
import org.example.iec61850.lodicalNodes.common.LN;
import java.util.List;
@Getter
@Setter
public class PDIF extends LN {
    private final double s = 0.53;
    private ACT Operate = new ACT(false);
    private ASG LoSet = new ASG();
    public PDIF(double loSet){
        this.LoSet.getSetMag().getF().setValue(loSet);
    }
    //input
    public SAV difA;
    public SAV difB;
    public SAV difC;
    public SAV rstA;
    public SAV rstB;
    public SAV rstC;
    public ACD block = new ACD(false);
    public List<PHAR>pharList;
    @Override
    public void process() {
        block.getPhsA().setValue(false);
        block.getPhsB().setValue(false);
        block.getPhsC().setValue(false);
        for (PHAR phar : pharList) {
            if(phar.getStr().getPhsA().getValue()){
                block.getPhsA().setValue(true);
            }
            if(phar.getStr().getPhsB().getValue()){
                block.getPhsB().setValue(true);
            }
            if(phar.getStr().getPhsC().getValue()){
                block.getPhsC().setValue(true);
            }
        }
        if(!block.getPhsA().getValue()){
            if(difA.getInstMag().getF().getValue() > this.LoSet.getSetMag().getF().getValue() &&
                            difA.getInstMag().getF().getValue() > rstA.getInstMag().getF().getValue()*this.s){
                this.Operate.getPhsA().setValue(true);
            }else{
                this.Operate.getPhsA().setValue(false);
            }
        }
        if(!block.getPhsB().getValue()){
            if(difB.getInstMag().getF().getValue() > this.LoSet.getSetMag().getF().getValue() ||
                    (rstB.getInstMag().getF().getValue() > this.LoSet.getSetMag().getF().getValue()/this.s &&
                            difB.getInstMag().getF().getValue() > rstB.getInstMag().getF().getValue()*this.s)){
                this.Operate.getPhsB().setValue(true);
            }else{
                this.Operate.getPhsB().setValue(false);
            }
        }
        if(!block.getPhsC().getValue()){
            if(difC.getInstMag().getF().getValue() > this.LoSet.getSetMag().getF().getValue() ||
                    (rstC.getInstMag().getF().getValue() > this.LoSet.getSetMag().getF().getValue()/this.s &&
                            difC.getInstMag().getF().getValue() > rstC.getInstMag().getF().getValue()*this.s)){
                this.Operate.getPhsC().setValue(true);
            }else{
                this.Operate.getPhsC().setValue(false);
            }
        }

    }
}
