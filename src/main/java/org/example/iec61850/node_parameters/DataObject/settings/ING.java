package org.example.iec61850.node_parameters.DataObject.settings;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.common.datatypes.MyData;
import org.example.iec61850.common.modelData.Attribute;

@Getter
@Setter
public class ING extends MyData {
    /**
     * Integer status setting
     * */
    private Attribute<Integer> setVal = new Attribute<>();
}
