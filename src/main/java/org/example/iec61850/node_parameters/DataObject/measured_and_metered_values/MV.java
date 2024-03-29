package org.example.iec61850.node_parameters.DataObject.measured_and_metered_values;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.common.datatypes.MyData;
import org.example.iec61850.common.modelData.AnalogueValue;
import org.example.iec61850.common.modelData.Attribute;
import org.example.iec61850.common.modelData.Quality;
import org.example.iec61850.common.modelData.TimeStamp;

@Getter
@Setter
public class MV extends MyData {
    /**
     * Measured value
     * */
    private AnalogueValue instMag = new AnalogueValue();
    private AnalogueValue mag = new AnalogueValue();
    private Quality q = new Quality();
    private TimeStamp t = new TimeStamp();
}
