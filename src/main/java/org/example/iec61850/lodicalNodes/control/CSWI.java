package org.example.iec61850.lodicalNodes.control;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.lodicalNodes.common.LN;
import org.example.iec61850.datatypes.controls.DPC;
import org.example.iec61850.datatypes.status.ACT;
import org.example.iec61850.datatypes.status.SPS;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CSWI extends LN {
//LN: Switch controller Name: CSWI
//    //Selection “Open switch”
    private SPS SelOpn = new SPS();
    //Selection “Close switch”
    private SPS SelCLs = new SPS();
    //Switch, general
    private DPC Pos = new DPC();
    //Switch L1
    private DPC PosA = new DPC();
    //Switch L2
    private DPC PosB = new DPC();
    //Switch L3
    private DPC PosC = new DPC();

    // Лист срабатывания защит
    private List<ACT> OpOpnList = new ArrayList<>();

    public CSWI() {
        SelOpn.getStVal().setValue(false);
        SelCLs.getStVal().setValue(true);
    }

    @Override
    public void process() {
        for (int i = 0; i < OpOpnList.size(); i++) {
            if (OpOpnList.get(i).getGeneral().getValue()) {
                SelOpn.getStVal().setValue(true);
                SelCLs.getStVal().setValue(false);
                break;
            }
        }
        //Отключение всех фаз
        if (SelOpn.getStVal().getValue()) {
            Pos.getStVal().setValue(DPC.StVal.OFF);
            PosA.getStVal().setValue(DPC.StVal.OFF);
            PosB.getStVal().setValue(DPC.StVal.OFF);
            PosC.getStVal().setValue(DPC.StVal.OFF);
        } else {
            Pos.getStVal().setValue(DPC.StVal.ON);
            PosA.getStVal().setValue(DPC.StVal.ON);
            PosB.getStVal().setValue(DPC.StVal.ON);
            PosC.getStVal().setValue(DPC.StVal.ON);
        }
    }
}
