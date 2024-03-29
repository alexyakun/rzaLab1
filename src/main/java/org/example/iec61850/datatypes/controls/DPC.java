package org.example.iec61850.datatypes.controls;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.MyData;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.common.Quality;
import org.example.iec61850.datatypes.common.TimeStamp;

@Getter
@Setter
public class DPC extends MyData {
    //Controllable double point
    private Attribute<StVal> stVal = new Attribute<>();

    public enum StVal {
        INTERMEDIATE_STATE,
        OFF,
        ON,
        BAD_STATE
    }

    private Quality q = new Quality();
    private TimeStamp t = new TimeStamp();
}
