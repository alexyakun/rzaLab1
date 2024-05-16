package org.example.iec61850.lodicalNodes.protocol;
import org.example.iec61850.lodicalNodes.common.LN;


import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.datatypes.measuredVal.MV;

import java.io.File;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
@Getter
@Setter
public class LSVS extends LN {

    private String path;
    private String fileName;
    private final List<MV> out = new ArrayList<>();
    private List<String> cfgFileList = new ArrayList<>();
    private List<String> datFileList = new ArrayList<>();
    private int analogSignal = 0;
    private int digitalSignal = 0;
    private Iterator<String> datIterator;

    private List<Double> kAList = new ArrayList<>();
    private List<Double> kBList = new ArrayList<>();

    public LSVS() {
        for (int i = 0; i < 20; i++) out.add(new MV());
    }


    @Override
    public void process() {
        if (this.datIterator.hasNext()) {
            String[] str = this.datIterator.next().split(",");

            for (int i = 2, j = 0; i < this.analogSignal + 2; i++, j++) {
                double values = Double.parseDouble(str[i]) * this.kAList.get(j) + this.kBList.get(j);
                this.out.get(j).getInstMag().getF().setValue(values * 1000);
                //System.out.println(values);
            }
        }
    }

    public boolean hasNext() {
        return this.datIterator.hasNext();
    }

    public void setFileName(String fileName) throws Exception {
        this.fileName = fileName;
        String datPath = path + fileName + ".dat";
        String cfgPath = path + fileName + ".cfg";

        File datFile = new File(datPath);
        File cfgFile = new File(cfgPath);

        if (!datFile.exists()) throw new Exception("Путь указан неверно!");
        if (!cfgFile.exists()) throw new Exception("Путь указан неверно!");

        this.datFileList = Files.readAllLines(datFile.toPath());
        this.cfgFileList = Files.readAllLines(cfgFile.toPath());

        String strNumber = this.cfgFileList.get(1)
                .replace("A", "")
                .replace("D", "");

        this.analogSignal = Integer.parseInt(strNumber.split(",")[1]);
        this.digitalSignal = Integer.parseInt(strNumber.split(",")[2]);

        for (int i = 2; i < this.analogSignal + 2; i++) {
            double kA = Double.parseDouble(this.cfgFileList.get(i).split(",")[5]);
            double kB = Double.parseDouble(this.cfgFileList.get(i).split(",")[6]);

            this.kAList.add(kA);
            this.kBList.add(kB);
        }
        this.datIterator = this.datFileList.iterator();
    }

}
