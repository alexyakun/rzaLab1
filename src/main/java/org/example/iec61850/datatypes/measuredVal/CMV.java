package org.example.iec61850.datatypes.measuredVal;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.common.Quality;
import org.example.iec61850.datatypes.common.TimeStamp;
import org.example.iec61850.datatypes.common.Vector;
import org.example.iec61850.datatypes.common.MyData;

@Getter
@Setter
public class CMV extends MyData {
    //Complex measured value
    private Vector instCVal = new Vector();
    private Vector cVal = new Vector();
    private Attribute<Range> range = new Attribute<>();

    private enum Range {
        NORMAL,
        HIGH,
        LOW,
        HIGH_HIGH,
        LOW_LOW
    }

    private Attribute<RangeAng> rangeAng = new Attribute<>();

    private enum RangeAng {
        NORMAL,
        HIGH,
        LOW,
        HIGH_HIGH,
        LOW_LOW
    }

    private Quality q = new Quality();
    private TimeStamp t = new TimeStamp();

}
