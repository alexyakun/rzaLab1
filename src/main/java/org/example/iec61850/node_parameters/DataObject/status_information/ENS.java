//package org.example.iec61850.node_parameters.DataObject.status_information;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.example.iec61850.common.datatypes.MyData;
//import org.example.iec61850.common.modelData.Attribute;
//import org.example.iec61850.common.modelData.Quality;
//import org.example.iec61850.common.modelData.TimeStamp;
//
//@Getter
//@Setter
//public class ENS extends MyData {
//    /**
//     * Enumerated status (Перечисленный статус)
//     * */
//    /**
//     * Status
//     */
//    private Attribute<stVal> stValAttribute = new Attribute<>();
//
//    private enum stVal {
//
//    }
//
//    private Quality q = new Quality();
//    private TimeStamp t = new TimeStamp();
//    /**
//     * Substitution and blocked
//     */
//    private Attribute<Boolean> subEna = new Attribute<>();
//    private Attribute<subVal> subValAttribute = new Attribute<>();
//
//    private enum subVal {
//
//    }
//
//    private Quality subQ = new Quality();
//    private Attribute<String> subID = new Attribute<>();
//    private Attribute<Boolean> blkEna = new Attribute<>();
//    /**
//     * Configuration, description and extension
//     */
//    private Attribute<String> d = new Attribute<>();
//    private Attribute<Character.UnicodeBlock> dU = new Attribute<>();
//    private Attribute<String> cdcNs = new Attribute<>();
//    private Attribute<String> cdcName = new Attribute<>();
//    private Attribute<String> dataNs = new Attribute<>();
//}
