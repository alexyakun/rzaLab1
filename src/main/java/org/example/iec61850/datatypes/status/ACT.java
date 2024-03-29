package org.example.iec61850.datatypes.status;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.MyData;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.common.Quality;
import org.example.iec61850.datatypes.common.TimeStamp;

@Getter
@Setter
public class ACT extends MyData {
//Protection activation information
    private Attribute<Boolean> general = new Attribute<>();
    private Attribute<Boolean> phsA = new Attribute<>();
    private Attribute<Boolean> phsB = new Attribute<>();
    private Attribute<Boolean> phsC = new Attribute<>();
    private Attribute<Boolean> neut = new Attribute<>();
    private Quality q = new Quality();
    private TimeStamp t = new TimeStamp();
}
