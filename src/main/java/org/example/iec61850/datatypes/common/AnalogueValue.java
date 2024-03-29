package org.example.iec61850.datatypes.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalogueValue extends MyData {
    //integer value
    private Attribute<Integer> i = new Attribute<>();
    //float value
    private Attribute<Double> f = new Attribute<>();
}
