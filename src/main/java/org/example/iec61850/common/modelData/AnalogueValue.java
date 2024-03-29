package org.example.iec61850.common.modelData;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.common.datatypes.MyData;

@Getter
@Setter
public class AnalogueValue extends MyData {
    /**
     * INTEGER
     * FLOATING
     */
    //integer value
    private Attribute<Integer> i = new Attribute<>();
    //float value
    private Attribute<Double> f = new Attribute<>();
}
