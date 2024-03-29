package org.example.iec61850.datatypes.settings;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.MyData;
import org.example.iec61850.datatypes.common.AnalogueValue;

@Getter
@Setter
public class ASG extends MyData {
    //Analogue setting
    private AnalogueValue setMag = new AnalogueValue();
}
