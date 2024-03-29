package org.example.iec61850.datatypes.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vector extends MyData {
    private AnalogueValue mag = new AnalogueValue();
    private AnalogueValue ang = new AnalogueValue();
}
