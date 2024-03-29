package org.example.iec61850.node_parameters.DataObject.controls;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.common.datatypes.MyData;
import org.example.iec61850.common.modelData.Attribute;
import org.example.iec61850.common.modelData.Quality;
import org.example.iec61850.common.modelData.TimeStamp;

@Getter
@Setter
public class DPC extends MyData {
    /**
     * Controllable double point
     */
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
