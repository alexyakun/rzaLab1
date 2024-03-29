package org.example.iec61850.Filter;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.measuredVal.CMV;
import org.example.iec61850.datatypes.measuredVal.MV;

@Getter
@Setter
public abstract class Filter {
    public abstract void process(MV inValue, CMV outValue);
}
