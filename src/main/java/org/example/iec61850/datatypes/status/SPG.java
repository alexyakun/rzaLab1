package org.example.iec61850.datatypes.status;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.common.MyData;
@Getter
@Setter
public class SPG extends MyData {
    private Attribute<Boolean> stVal = new Attribute<>();
}
