package org.example.iec61850.lodicalNodes.protection;

import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.common.Vector;
import org.example.iec61850.datatypes.measuredVal.WYE;
import org.example.iec61850.datatypes.settings.ASG;
import org.example.iec61850.datatypes.settings.ENG;
import org.example.iec61850.datatypes.status.ACD;
import org.example.iec61850.datatypes.status.ACT;
import org.example.iec61850.lodicalNodes.common.LN;

public class PDIS extends LN {
    private ACD Str = new ACD();
    private ACT Op = new ACT();
    //Polar reach is the diameter of the Mho diagram
    private ASG PoRch = new ASG();
    //Line angle
    private ASG LinAng = new ASG();
    public ENG DirMod = new ENG();
    //input
    public WYE Z = new WYE();


    @Override
    public void process() {
        switch (this.DirMod.getSetVal().getValue()) {
            case FORWARD -> nonDirect();
            case BOTH -> direct();
        }

    }
    private void nonDirect(){
        Vector zA = Z.getPhsA().getInstCVal();
        Vector zB = Z.getPhsB().getInstCVal();
        Vector zC = Z.getPhsC().getInstCVal();
        boolean startA= zA.getMag().getF().getValue() > this.PoRch.getSetMag().getF().getValue();
        boolean startB= zB.getMag().getF().getValue() > this.PoRch.getSetMag().getF().getValue();
        boolean startC= zC.getMag().getF().getValue() > this.PoRch.getSetMag().getF().getValue();
        boolean start = startA||startB||startC;
        this.Str.getPhsA().setValue(startA);
        this.Str.getPhsB().setValue(startB);
        this.Str.getPhsC().setValue(startC);
        this.Str.getGeneral().setValue(start);
        if(startA || startB || startC){
            startProtect();
        }

    }
    private void direct(){
        Vector zA = Z.getPhsA().getInstCVal();
        Vector zB = Z.getPhsB().getInstCVal();
        Vector zC = Z.getPhsC().getInstCVal();

    }
    private void startProtect(){
        if(Str.getGeneral().getValue()){

        }
    }


}
