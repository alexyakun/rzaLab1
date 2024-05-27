package org.example.iec61850.datatypes.measuredVal;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.MyData;
import org.example.iec61850.datatypes.common.AnalogueValue;
import org.example.iec61850.datatypes.common.Quality;
import org.example.iec61850.datatypes.common.TimeStamp;

@Getter
@Setter
public class MV extends MyData {
    //Measured value
    private AnalogueValue instMag = new AnalogueValue(0.0);
    private AnalogueValue mag = new AnalogueValue(0.0);
    private Quality q = new Quality();
    private TimeStamp t = new TimeStamp();

}
