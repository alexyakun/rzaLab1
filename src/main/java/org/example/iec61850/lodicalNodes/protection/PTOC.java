package org.example.iec61850.lodicalNodes.protection;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.measuredVal.SEQ;
import org.example.iec61850.datatypes.settings.ASG;
import org.example.iec61850.datatypes.settings.ENG;
import org.example.iec61850.datatypes.settings.ING;
import org.example.iec61850.lodicalNodes.common.LN;
import org.example.iec61850.datatypes.controls.INC;
import org.example.iec61850.datatypes.status.ACD;
import org.example.iec61850.datatypes.status.ACT;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class PTOC extends LN {

    //LN: Time overcurrent Name: PTOC
    //на срабатывание защиты Start
    private ACD Str = new ACD();
    //на отключение оборудования Operate
    private ACT Op = new ACT();
    //Счетчик операций Resettable operation counter
    private INC OpCntRs = new INC();
    //Start value
    @XmlElement
    private ASG StrVal = new ASG();
    //Time dial multiplier
    @XmlElement
    private ASG TmMult = new ASG();
    //Operate delay time
    @XmlElement
    private ING OpDlOpTmms = new ING();
    public ENG DirMod = new ENG();
    public ING OpDiTmms = new ING();
    public Attribute<Boolean> accelEnable = new Attribute<>();
    public Attribute<Boolean> accel = new Attribute<>();
    //INPUT
    public ACD dir = new ACD();
    public SEQ seq = new SEQ();
    public PTOC() {
        OpCntRs.getStVal().setValue(0);
        Op.getGeneral().setValue(false);
        Op.getPhsA().setValue(false);
        Op.getPhsB().setValue(false);
        Op.getPhsC().setValue(false);
    }

    @Override
    public void process() {
        if (accelEnable.getValue()&& accel.getValue()){OpDiTmms.getSetVal().setValue(0);}
        boolean start = seq.getC3().getInstCVal().getMag().getF().getValue() >= StrVal.getSetMag().getF().getValue();
       // System.out.println(start);
        Str.getGeneral().setValue(start);
       // System.out.println(Str.getPhsA().getValue());
        Str.getPhsA().setValue(start);
        Str.getPhsB().setValue(start);
        Str.getPhsC().setValue(start);
        switch (this.DirMod.getSetVal().getValue()) {
            case FORWARD -> {if (dir.getGeneral().getValue()) {operate();}}
            case BOTH -> operate();
        }
    }
    private void operate(){
        if (Str.getGeneral().getValue()) {
            OpCntRs.getStVal().setValue(OpCntRs.getStVal().getValue() + 1);
        } else {
            OpCntRs.getStVal().setValue(0);
        }
                System.out.println(OpCntRs.getStVal().getValue()
                * TmMult.getSetMag().getF().getValue());
        if (Str.getGeneral().getValue() && (OpCntRs.getStVal().getValue()
                * TmMult.getSetMag().getF().getValue() > OpDiTmms.getSetVal().getValue())) {
            Op.getPhsA().setValue(true);
            Op.getPhsB().setValue(true);
            Op.getPhsC().setValue(true);
            Op.getGeneral().setValue(true);
        }
    }
}
