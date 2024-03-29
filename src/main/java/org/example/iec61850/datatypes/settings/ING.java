package org.example.iec61850.datatypes.settings;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.MyData;
import org.example.iec61850.datatypes.common.Attribute;

@Getter
@Setter
public class ING extends MyData {
    //Integer status setting
    private Attribute<Integer> setVal = new Attribute<>();
}
