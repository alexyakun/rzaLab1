package org.example.iec61850.node_parameters.DataObject.status_information;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.common.datatypes.MyData;
import org.example.iec61850.common.modelData.Attribute;
import org.example.iec61850.common.modelData.Quality;
import org.example.iec61850.common.modelData.TimeStamp;

@Getter
@Setter
public class ACD extends MyData {
    /**
     * Directional protection activation information
     * (Информация об активации направленной защиты)
     * */
    /**
     * Status
     */
    private Attribute<Boolean> general = new Attribute<>();
    private Attribute<DirGeneral> dirGeneral = new Attribute<>();

    private enum DirGeneral {
        UNKNOWN,
        FORWARD,
        BACKWARD,
        BOTH
    }

    private Attribute<Boolean> phsA = new Attribute<>();
    private Attribute<DirPhsA> dirPhsA = new Attribute<>();

    private enum DirPhsA {
        UNKNOWN,
        FORWARD,
        BACKWARD
    }

    private Attribute<Boolean> phsB = new Attribute<>();
    private Attribute<DirPhsB> dirPhsB = new Attribute<>();

    private enum DirPhsB {
        UNKNOWN,
        FORWARD,
        BACKWARD
    }

    private Attribute<Boolean> phsC = new Attribute<>();
    private Attribute<DirPhsC> dirPhsC = new Attribute<>();

    private enum DirPhsC {
        UNKNOWN,
        FORWARD,
        BACKWARD
    }

    private Attribute<Boolean> neut = new Attribute<>();
    private Attribute<DirNeut> dirNeut = new Attribute<>();

    private enum DirNeut {
        UNKNOWN,
        FORWARD,
        BACKWARD
    }

    private Quality q = new Quality();
    private TimeStamp t = new TimeStamp();
//    /**
//     * Substitution and blocked
//     * ...*/
//
//    /**
//     * Configuration, description and extension
//     */
//    private Attribute<String> d = new Attribute<>();
//    private Attribute<Character.UnicodeBlock> dU = new Attribute<>();
//    private Attribute<String> cdcNs = new Attribute<>();
//    private Attribute<String> cdcName = new Attribute<>();
//    private Attribute<String> dataNs = new Attribute<>();
}
