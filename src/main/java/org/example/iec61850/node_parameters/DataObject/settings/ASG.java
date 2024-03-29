package org.example.iec61850.node_parameters.DataObject.settings;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.common.datatypes.MyData;
import org.example.iec61850.common.modelData.AnalogueValue;

@Getter
@Setter
public class ASG extends MyData {
    /**
     * Analogue setting
     * */
    private AnalogueValue setMag = new AnalogueValue();
}
