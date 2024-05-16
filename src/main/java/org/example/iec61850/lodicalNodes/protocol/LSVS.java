package org.example.iec61850.lodicalNodes.protocol;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.lodicalNodes.common.LN;
import org.example.iec61850.datatypes.measuredVal.MV;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class LSVS extends LN {
    @XmlAttribute
    private String path;
    @XmlAttribute
    private String fileName;
    private final List<MV> out = new ArrayList<>();
    private List<String> cfgFileList = new ArrayList<>();
    private List<String> csvFileList = new ArrayList<>();
    private int analogSignal = 0;
    private int digitalSignal = 0;
    private Iterator<String> scvIterator;

    public LSVS() {
        for (int i = 0; i < 20; i++)
            out.add(new MV());
    }

    @Override
    public void process() {
        if (this.scvIterator.hasNext()) {
            String[] str = this.scvIterator.next().split(",");

            for (int i = 0; i < this.analogSignal; i++) {
                double values = Double.parseDouble(str[i + 1]);
                this.out.get(i).getInstMag().getF().setValue(values * 1000);
            }
        }
    }

    public boolean hasNext() {
        return this.scvIterator.hasNext();
    }

    public void setFileName(String fileName) throws Exception {
        this.fileName = fileName;
        String csvPath = path + fileName + ".csv";
        String cfgPath = path + fileName + ".cfg";

        File csvFile = new File(csvPath);
        File cfgFile = new File(cfgPath);

        if (!csvFile.exists()) throw new Exception("Путь указан неверно!");
        if (!cfgFile.exists()) throw new Exception("Путь указан неверно!");

        this.csvFileList = Files.readAllLines(csvFile.toPath());
        this.cfgFileList = Files.readAllLines(cfgFile.toPath());

        String strNumber = this.cfgFileList.get(1)
                .replace("A", "")
                .replace("D", "");

        this.analogSignal = Integer.parseInt(strNumber.split(",")[1]);
        this.digitalSignal = Integer.parseInt(strNumber.split(",")[2]);
        this.scvIterator = this.csvFileList.iterator();
        if (this.scvIterator.hasNext()) {
            this.scvIterator.next();
        }
    }



}
