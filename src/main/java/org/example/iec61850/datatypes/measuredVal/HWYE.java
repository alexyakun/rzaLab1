package org.example.iec61850.datatypes.measuredVal;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.common.MyData;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class HWYE extends MyData {
    private List<CMV> phsAHar = new ArrayList<>();
    private List<CMV> phsBHar = new ArrayList<>();
    private List<CMV> phsCHar = new ArrayList<>();
    private Attribute<Integer> numHar = new Attribute<>(10);
    public HWYE(int numHar){
        this.numHar.setValue(numHar);
        for (int i = 0; i < numHar; i++) {
            phsAHar.add(new CMV());
            phsBHar.add(new CMV());
            phsCHar.add(new CMV());
        }
    }
}
