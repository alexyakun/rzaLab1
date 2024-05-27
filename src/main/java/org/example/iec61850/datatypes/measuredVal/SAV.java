package org.example.iec61850.datatypes.measuredVal;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.AnalogueValue;
import org.example.iec61850.datatypes.common.MyData;
@Getter
@Setter
public class SAV extends MyData {
    private AnalogueValue instMag = new AnalogueValue(0.0);
}
