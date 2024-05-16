package org.example.iec61850.lodicalNodes.control;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.lodicalNodes.common.LN;
import org.example.iec61850.datatypes.controls.DPC;
import org.example.iec61850.datatypes.controls.INC;
import org.example.iec61850.datatypes.controls.SPC;
import org.example.iec61850.datatypes.status.SPS;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class XCBR extends LN {
    //LN: Circuit breaker Name: XCBR
    //Local control behaviour
    private SPS Loc = new SPS();
    //Operation counter
    private INC OpCnt = new INC();
    //Switch position
    private DPC Pos = new DPC();
    //Block opening
    private SPC BlkOpn = new SPC();
    //Block closing
    private SPC BlkCls = new SPC();

    @Override
    public void process() {

    }
}
