package org.example.iec61850.datatypes.measuredVal;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.MyData;

@Getter
@Setter
public class WYE extends MyData {
    //Phase to ground/neutral related measured values of a three-phase system
    private CMV phsA = new CMV();
    private CMV phsB = new CMV();
    private CMV phsC = new CMV();
    private CMV neut = new CMV();
    private CMV net = new CMV();
    private CMV res = new CMV();
}
