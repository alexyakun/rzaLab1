package org.example.iec61850.Filter;

import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.datatypes.measuredVal.CMV;
import org.example.iec61850.datatypes.measuredVal.MV;

public class Fourier extends Filter{
    private Attribute<Integer> buffSize = new Attribute<>();
    private final MV[] buffer;
    public Attribute<Integer> buffCnt = new Attribute<>();
    public Attribute<Double> sumRe = new Attribute<>();
    public Attribute<Double> sumIm = new Attribute<>();
    public Attribute<Double> freq = new Attribute<>();
    public Attribute<Double> step = new Attribute<>();
    public Attribute<Double> k = new Attribute<>();
    public Fourier(int buffsize, double freq) {
        this.buffSize.setValue(buffsize);
        this.buffer = new MV[buffsize];
        this.buffCnt.setValue(0);
        this.sumRe.setValue(0.0);
        this.sumIm.setValue(0.0);
        this.freq.setValue(freq);
        this.step.setValue(0.02/buffsize);
        this.k.setValue(1.0 / buffsize);
        for (int i = 0; i < buffsize; i++) {
            MV val = new MV();
            val.getInstMag().getF().setValue(0.0);
            buffer[i] = val;
        }
    }
    @Override
    public void process(MV inValue, CMV outValue) {
        double newVal = inValue.getInstMag().getF().getValue();
        double oldVal = buffer[buffCnt.getValue()].getInstMag().getF().getValue();
        sumRe.setValue(sumRe.getValue() + (newVal - oldVal) *
                Math.sin(2 * Math.PI * freq.getValue() * buffCnt.getValue() * step.getValue()) * (2.0 / this.buffSize.getValue()));
        sumIm.setValue(sumIm.getValue() + (newVal - oldVal) *
                Math.cos(2 * Math.PI * freq.getValue() * buffCnt.getValue() * step.getValue()) * (2.0 / this.buffSize.getValue()));
        outValue.getInstCVal().getMag().getF().setValue(Math.sqrt((sumRe.getValue()*sumRe.getValue()+sumIm.getValue()*sumIm.getValue())/2.0));
        double angle = 0.0;
        if(sumRe.getValue() > 0){
            angle = Math.atan(sumIm.getValue() / sumRe.getValue());
        } else if(sumRe.getValue()< 0 && sumIm.getValue() >= 0){
            angle = Math.PI + Math.atan(sumIm.getValue() / sumRe.getValue());
        } else if(sumRe.getValue() < 0 && sumIm.getValue() < 0){
            angle = -Math.PI + Math.atan(sumIm.getValue() / sumRe.getValue());
        } else if(sumRe.getValue() == 0 && sumIm.getValue() > 0){
            angle = Math.PI/2;
        }else if(sumRe.getValue() == 0 && sumIm.getValue() < 0){
            angle = -Math.PI/2;
        }
        outValue.getInstCVal().getAng().getF().setValue(angle);
        buffer[buffCnt.getValue()].getInstMag().getF().setValue(newVal);
        buffCnt.setValue(buffCnt.getValue() + 1);
        if (buffCnt.getValue().equals(buffSize.getValue())) {
            buffCnt.setValue(0);
        }

    }

}
//public class Fourier extends Filter {
//    private Attribute<Integer> bufferSize = new Attribute<>();
//    private final MV[] buffer;
//
//    public Attribute<Integer> bufferCount = new Attribute<>();
//    public Attribute<Double> summValRe = new Attribute<>();
//    public Attribute<Double> summValIm = new Attribute<>();
//    public Attribute<Double> frequency = new Attribute<>();
//    public Attribute<Double> samplStep = new Attribute<>();
//
//    public Fourier(int bufsize) {
//        this.bufferSize.setValue(bufsize); //Размер буфера
//        this.buffer = new MV[bufsize]; //Создание буфера нужного размера
//
//        this.bufferCount.setValue(0); //Счетчик выборки
//        this.summValRe.setValue(0.0); //Действительное значение
//        this.summValIm.setValue(0.0); //Мнимое значение
//        this.frequency.setValue(50.0); //Частота
//        this.samplStep.setValue(0.02 / bufsize); //Шаг дискретизации
//
///**Заполнение буфера нулями*/
//        for (int i = 0; i < bufsize; i++) {
//            MV val = new MV();
//            val.getInstMag().getF().setValue(0.0);
//            this.buffer[i] = val;
//        }
//    }
//
//    @Override
//    public void process(MV measuredValue, CMV complexMeasurementValue) {
///**Новое измеренное значение*/
//        double newVal = measuredValue.getInstMag().getF().getValue();
//
///**Старое измеренное значение, хранящееся в буфере*/
//        double oldVal = buffer[bufferCount.getValue()].getInstMag().getF().getValue();
//
///**Расчет действительного и мнимого значения*/
//        summValRe.setValue(summValRe.getValue() + (newVal - oldVal) *
//                Math.sin(2 * Math.PI * frequency.getValue() * bufferCount.getValue() * samplStep.getValue()) * (2.0 / this.bufferSize.getValue()));
//        summValIm.setValue(summValIm.getValue() + (newVal - oldVal) *
//                Math.cos(2 * Math.PI * frequency.getValue() * bufferCount.getValue() * samplStep.getValue()) * (2.0 / this.bufferSize.getValue()));
//
///**Расчет величины и угла измеряемого вектора*/
//        complexMeasurementValue.getInstCVal().getMag().getF().setValue(
//                Math.sqrt((Math.pow(summValRe.getValue(), 2) + Math.pow(summValIm.getValue(), 2)) / 2.0));
//        complexMeasurementValue.getInstCVal().getAng().getF().setValue(
//                Math.atan(summValIm.getValue() / summValRe.getValue()) * (180 / Math.PI) //atan2
//        );
//
///**Обновление значения буфера*/
//        buffer[bufferCount.getValue()].getInstMag().getF().setValue(newVal);
//        bufferCount.setValue(bufferCount.getValue() + 1); //Обновление счетчика
//
///**Проверка полного заполнения буфера*/
//        if (bufferCount.getValue() == bufferSize.getValue()) {
//            bufferCount.setValue(0); //Начинаем заново заполнять буфер
//        }
//    }
//}