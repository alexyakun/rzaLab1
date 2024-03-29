package org.example.iec61850.datatypes.controls;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.MyData;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.common.Quality;
import org.example.iec61850.datatypes.common.TimeStamp;

@Getter
@Setter
public class SPC extends MyData {
    //Controllable single point
    private Attribute<Boolean> stVal = new Attribute<>();
    private Quality q = new Quality();
    private TimeStamp t = new TimeStamp();
}
