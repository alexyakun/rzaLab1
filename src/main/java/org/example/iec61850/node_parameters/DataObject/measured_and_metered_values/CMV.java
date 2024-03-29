package org.example.iec61850.node_parameters.DataObject.measured_and_metered_values;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.common.datatypes.MyData;
import org.example.iec61850.common.modelData.*;

@Getter
@Setter
public class CMV extends MyData {
    /**
     * Complex measured value
     * */
    /**
     * Measured attributes
     */
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
