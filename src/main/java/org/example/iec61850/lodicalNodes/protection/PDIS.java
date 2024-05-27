package org.example.iec61850.lodicalNodes.protection;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Vector;
import org.example.iec61850.datatypes.controls.INC;
import org.example.iec61850.datatypes.measuredVal.WYE;
import org.example.iec61850.datatypes.settings.ASG;
import org.example.iec61850.datatypes.settings.ENG;
import org.example.iec61850.datatypes.settings.ING;
import org.example.iec61850.datatypes.status.ACD;
import org.example.iec61850.datatypes.status.ACT;
import org.example.iec61850.datatypes.status.SPS;
import org.example.iec61850.lodicalNodes.common.LN;
import org.example.iec61850.utils.CompCal;
@Getter
@Setter
public class PDIS extends LN {
    private ACD Str = new ACD();
    private ACT Op = new ACT();
    //Polar reach is the diameter of the Mho diagram
    private ASG PoRch = new ASG();
    //Line angle
    private ASG LinAng = new ASG();
    public ENG DirMod = new ENG();
    private ING OpDITmmms = new ING();
    private INC OpCntRs = new INC();
    private ASG TmMult = new ASG();
    //input
    public WYE Z = new WYE();
    public SPS BlkZn = new SPS();
    public PDIS() {
        OpCntRs.getStVal().setValue(0);
        Op.getGeneral().setValue(false);
        Op.getPhsA().setValue(false);
        Op.getPhsB().setValue(false);
        Op.getPhsC().setValue(false);
    }


    @Override
    public void process() {
        if(!BlkZn.getStVal().getValue()){
            switch (this.DirMod.getSetVal().getValue()) {
                case FORWARD -> nonDirect();
                case BOTH -> direct();
            }
        }
    }
    private void nonDirect(){
        Vector zA = Z.getPhsA().getInstCVal();
        Vector zB = Z.getPhsB().getInstCVal();
        Vector zC = Z.getPhsC().getInstCVal();
        boolean startA= zA.getMag().getF().getValue() < this.PoRch.getSetMag().getF().getValue()/2;
        boolean startB= zB.getMag().getF().getValue() < this.PoRch.getSetMag().getF().getValue()/2;
        boolean startC= zC.getMag().getF().getValue() < this.PoRch.getSetMag().getF().getValue()/2;
        boolean start = startA||startB||startC;
        this.Str.getPhsA().setValue(startA);
        this.Str.getPhsB().setValue(startB);
        this.Str.getPhsC().setValue(startC);
        this.Str.getGeneral().setValue(start);
        if(startA || startB || startC){
            operate();
        }

    }
    private void direct(){
        Vector zA = Z.getPhsA().getInstCVal();
        Vector zB = Z.getPhsB().getInstCVal();
        Vector zC = Z.getPhsC().getInstCVal();
        Vector center = new Vector();
        center.getMag().getF().setValue(PoRch.getSetMag().getF().getValue()/2);
        center.getAng().getF().setValue(LinAng.getSetMag().getF().getValue());
        Vector difA = CompCal.dif(zA, center);
        Vector difB = CompCal.dif(zB, center);
        Vector difC = CompCal.dif(zC, center);
//        System.out.println("dif a= "+difA.getMag().getF().getValue());
//        System.out.println("dif b= "+difB.getMag().getF().getValue());
//        System.out.println("dif c= "+difC.getMag().getF().getValue());
        boolean startA= difA.getMag().getF().getValue() < this.PoRch.getSetMag().getF().getValue()/2;
        boolean startB= difB.getMag().getF().getValue() < this.PoRch.getSetMag().getF().getValue()/2;
        boolean startC= difC.getMag().getF().getValue() < this.PoRch.getSetMag().getF().getValue()/2;
        boolean start = startA||startB||startC;
        this.Str.getPhsA().setValue(startA);
        this.Str.getPhsB().setValue(startB);
        this.Str.getPhsC().setValue(startC);
        this.Str.getGeneral().setValue(start);
        if(startA || startB || startC){
            operate();
        }

    }
    private void operate(){
        if(Str.getGeneral().getValue()){
            OpCntRs.getStVal().setValue(OpCntRs.getStVal().getValue() + 1);
        }else {
            OpCntRs.getStVal().setValue(0);
        }
//        System.out.println(OpCntRs.getStVal().getValue()
//                * TmMult.getSetMag().getF().getValue());
        if (Str.getGeneral().getValue() && (OpCntRs.getStVal().getValue()
                * TmMult.getSetMag().getF().getValue() > OpDITmmms.getSetVal().getValue())) {
            Op.getPhsA().setValue(true);
            Op.getPhsB().setValue(true);
            Op.getPhsC().setValue(true);
            Op.getGeneral().setValue(true);
        }
    }


}
