package org.example.iec61850.lodicalNodes.protection;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.measuredVal.SAV;
import org.example.iec61850.datatypes.measuredVal.SEQ;
import org.example.iec61850.datatypes.settings.ASG;
import org.example.iec61850.datatypes.settings.ENG;
import org.example.iec61850.datatypes.settings.ING;
import org.example.iec61850.lodicalNodes.common.LN;
import org.example.iec61850.datatypes.controls.INC;
import org.example.iec61850.datatypes.status.ACD;
import org.example.iec61850.datatypes.status.ACT;
import org.example.iec61850.lodicalNodes.measurement.RMXU;

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
    public SAV difA;
    public SAV difB;
    public SAV difC;
    public PTOC() {
        OpCntRs.getStVal().setValue(0);
        Op.getGeneral().setValue(false);
        Op.getPhsA().setValue(false);
        Op.getPhsB().setValue(false);
        Op.getPhsC().setValue(false);
    }

    @Override
    public void process() {
        if(difA.getInstMag().getF().getValue() > StrVal.getSetMag().getF().getValue()){
            this.Op.getPhsA().setValue(true);
        } else{
            this.Op.getPhsA().setValue(false);
        }
        if(difB.getInstMag().getF().getValue() > StrVal.getSetMag().getF().getValue()){
            this.Op.getPhsB().setValue(true);
        } else{
            this.Op.getPhsB().setValue(false);
        }
        if(difC.getInstMag().getF().getValue() > StrVal.getSetMag().getF().getValue()){
            this.Op.getPhsC().setValue(true);
        } else{
            this.Op.getPhsC().setValue(false);
        }
    }

}
