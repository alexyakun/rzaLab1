package org.example.iec61850.Filter;

import org.example.iec61850.common.modelData.Attribute;
import org.example.iec61850.node_parameters.DataObject.measured_and_metered_values.CMV;
import org.example.iec61850.node_parameters.DataObject.measured_and_metered_values.MV;

public class Fourier extends Filter {
    private Attribute<Integer> bufferSize = new Attribute<>();
    private final MV[] buffer;

    public Attribute<Integer> bufferCount = new Attribute<>();
    public Attribute<Double> summValRe = new Attribute<>();
    public Attribute<Double> summValIm = new Attribute<>();
    public Attribute<Double> frequency = new Attribute<>();
    public Attribute<Double> samplStep = new Attribute<>();

    public Fourier(int bufsize) {
        this.bufferSize.setValue(bufsize); //Размер буфера
        this.buffer = new MV[bufsize]; //Создание буфера нужного размера

        this.bufferCount.setValue(0); //Счетчик выборки
        this.summValRe.setValue(0.0); //Действительное значение
        this.summValIm.setValue(0.0); //Мнимое значение
        this.frequency.setValue(50.0); //Частота
        this.samplStep.setValue(0.02 / bufsize); //Шаг дискретизации

        /**Заполнение буфера нулями*/
        for (int i = 0; i < bufsize; i++) {
            MV val = new MV();
            val.getInstMag().getF().setValue(0.0);
            buffer[i] = val;
        }
    }

    @Override
    public void process(MV measuredValue, CMV complexMeasurementValue) {
        /**Новое измеренное значение*/
        double newVal = measuredValue.getInstMag().getF().getValue();

        /**Старое измеренное значение, хранящееся в буфере*/
        double oldVal = buffer[bufferCount.getValue()].getInstMag().getF().getValue();

        /**Расчет действительного и мнимого значения*/
        summValRe.setValue(summValRe.getValue() + (newVal - oldVal) *
                Math.sin(2 * Math.PI * frequency.getValue() * bufferCount.getValue() * samplStep.getValue())
                * (2 / bufferSize.getValue()));
        summValIm.setValue(summValIm.getValue() + (newVal - oldVal) *
                Math.cos(2 * Math.PI * frequency.getValue() * bufferCount.getValue() * samplStep.getValue())
                * (2 / bufferSize.getValue()));

        /**Расчет величины и угла измеряемого вектора*/
        complexMeasurementValue.getInstCVal().getMag().getF().setValue(
                Math.sqrt(Math.pow(summValRe.getValue(), 2) + Math.pow(summValIm.getValue(), 2)));
        complexMeasurementValue.getInstCVal().getAng().getF().setValue(
                Math.atan(summValIm.getValue() / summValRe.getValue()) * (180 / Math.PI)
        );

        /**Обновление значения буфера*/
        buffer[bufferCount.getValue()].getInstMag().getF().setValue(newVal);
        bufferCount.setValue(bufferCount.getValue() + 1); //Обновление счетчика

        /**Проверка полного заполнения буфера*/
        if (bufferCount.getValue().equals(bufferSize.getValue())) {
            bufferCount.setValue(0); //Начинаем заново заполнять буфер
        }
    }
}
