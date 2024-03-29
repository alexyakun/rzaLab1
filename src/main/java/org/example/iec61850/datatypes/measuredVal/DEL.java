package org.example.iec61850.datatypes.measuredVal;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.MyData;

@Getter
@Setter
public class DEL extends MyData {
    //Phase to phase related measured values of a three-phase system
    private CMV phsAB = new CMV();
    private CMV phsBC = new CMV();
    private CMV phsCA = new CMV();

}
