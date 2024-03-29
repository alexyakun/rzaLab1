package org.example.iec61850.datatypes.status;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.MyData;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.common.Quality;
import org.example.iec61850.datatypes.common.TimeStamp;

@Getter
@Setter
public class SPS extends MyData {
    //Single point status
    private Attribute<Boolean> stVal = new Attribute<>();
    private Quality q = new Quality();
    private TimeStamp t = new TimeStamp();

}
