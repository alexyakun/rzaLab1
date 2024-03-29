package org.example.iec61850.lodicalNodes.common;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.controls.SPC;
import org.example.iec61850.datatypes.settings.ING;
import org.example.iec61850.datatypes.status.SPS;

@Getter
@Setter
public abstract class LN {
    //LN: common logical node   Name: Common LN
//    private ENC Mod = new ENC();
    private SPC CmdBlk = new SPC();
    //Calculation period expired
    private SPS ClcExp = new SPS();
    //interval duration
    private ING ClcIntvPer = new ING();
    public abstract void process();
}
