package org.example.iec61850.lodicalNodes.protection;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.settings.ASG;
import org.example.iec61850.datatypes.settings.ING;
import org.example.iec61850.lodicalNodes.common.LN;
import org.example.iec61850.datatypes.controls.INC;
import org.example.iec61850.datatypes.measuredVal.WYE;
import org.example.iec61850.datatypes.status.ACD;
import org.example.iec61850.datatypes.status.ACT;

@Getter
@Setter
public class PTOC extends LN {

    //LN: Time overcurrent Name: PTOC
    //на срабатывание защиты
    private ACD Str = new ACD();
    //на отключение оборудования
    private ACT Op = new ACT();
    //Счетчик операций
    private INC OpCntRs = new INC();
    //Start value
    private ASG StrVal = new ASG();
    //Time dial multiplier
    private ASG TmMult = new ASG();
    //Operate delay time
    private ING OpDlOpTmms = new ING();

    //INPUT
    private WYE A = new WYE();


    public PTOC() {
        Op.getGeneral().setValue(false);
        Op.getPhsA().setValue(false);
        Op.getPhsB().setValue(false);
        Op.getPhsC().setValue(false);
        OpCntRs.getStVal().setValue(0);
    }

    @Override
    public void process() {
        //Проверка на срабатывание защиты
        Str.getPhsA().setValue(
                A.getPhsA().getCVal().getMag().getF().getValue() >
                        StrVal.getSetMag().getF().getValue()
        );
        Str.getPhsB().setValue(
                A.getPhsB().getCVal().getMag().getF().getValue() >
                        StrVal.getSetMag().getF().getValue()
        );
        Str.getPhsC().setValue(
                A.getPhsC().getCVal().getMag().getF().getValue() >
                        StrVal.getSetMag().getF().getValue()
        );
        //Сигнал на отключение

        Str.getGeneral().setValue(Str.getPhsA().getValue() || Str.getPhsB().getValue() || Str.getPhsC().getValue());

        //выдержка времени
        if (Str.getGeneral().getValue()) {
            OpCntRs.getStVal().setValue(OpCntRs.getStVal().getValue() + 1);
        } else {
            OpCntRs.getStVal().setValue(0);
        }
        System.out.println(OpCntRs.getStVal().getValue()
                * TmMult.getSetMag().getF().getValue());
        //Отправка сигнала на отключение
        if (Str.getGeneral().getValue() && (OpCntRs.getStVal().getValue()
                * TmMult.getSetMag().getF().getValue() > OpDlOpTmms.getSetVal().getValue())) {
            Op.getPhsA().setValue(true);
            Op.getPhsB().setValue(true);
            Op.getPhsC().setValue(true);
            Op.getGeneral().setValue(true);
        }else{
            Op.getPhsA().setValue(false);
            Op.getPhsB().setValue(false);
            Op.getPhsC().setValue(false);
            Op.getGeneral().setValue(false);
        }
    }
}
