package org.example.iec61850.datatypes.measuredVal;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.MyData;
@Getter
@Setter
public class SEQ extends MyData {
    private CMV c1 = new CMV();
    private CMV c2 = new CMV();
    private CMV c3 = new CMV();
    private enum seqT{
        POS_NEG_ZERO,
        DIR_QUAD_ZERO
    }

}
