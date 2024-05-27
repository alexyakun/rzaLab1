package org.example.iec61850.frequency;

import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.measuredVal.MV;

public interface FrequencyMeter {
    void process(MV inValue, Attribute<Double> frequency);
}
