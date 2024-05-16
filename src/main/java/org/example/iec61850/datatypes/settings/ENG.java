package org.example.iec61850.datatypes.settings;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.common.MyData;
import org.example.iec61850.datatypes.status.ACD;

@Getter
@Setter
public class ENG extends MyData {

    private Attribute<ACD.DirGeneral> setVal = new Attribute<>();

}
