package org.example.iec61850.lodicalNodes.protection;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.lodicalNodes.LN;
import org.example.iec61850.node_parameters.DataObject.controls.INC;
import org.example.iec61850.node_parameters.DataObject.measured_and_metered_values.WYE;
import org.example.iec61850.node_parameters.DataObject.settings.*;
import org.example.iec61850.node_parameters.DataObject.status_information.ACD;
import org.example.iec61850.node_parameters.DataObject.status_information.ACT;

@Getter
@Setter
public class PTOC extends LN {
    /**
     * LN: Time overcurrent Name: PTOC (LN: Временная перегрузка по току Название: PTOC)
     *
     * Функция, которая срабатывает, когда входной переменный ток превышает заданное значение, и в которой
     * входной ток и время работы находятся в обратной зависимости на протяжении значительной
     * части диапазона рабочих характеристик.
     * */
    /**
     * Status information
     */
    private ACD Str = new ACD(); //Output: на срабатывание защиты
    private ACT Op = new ACT(); //Output: на отключение оборудования
    /**
     * Controls
     */
    private INC OpCntRs = new INC(); //Счетчик операций
    /**
     * Settings
     */

            //Start value
    private ASG StrVal = new ASG(); //Уставка по току
    //Time dial multiplier
    private ASG TmMult = new ASG(); //Множитель набора времени

    //Operate delay time
    private ING OpDlOpTmms = new ING(); //Уставка по времени


    /**
     * Input
     */
    private WYE A = new WYE();

    /**Уставка по току*/
//    private double currentSetpoint = StrVal.getSetMag().getFloatVal().getValue();

    public PTOC() {
        /**Устанавливаем false: означает, что оборудование еще НЕ отключено*/
        Op.getGeneral().setValue(false); //Управляющее действие
        Op.getPhsA().setValue(false);
        Op.getPhsB().setValue(false);
        Op.getPhsC().setValue(false);

        /**Установка начального набора времени (счетчика операций)*/
        OpCntRs.getStVal().setValue(0);
    }

    @Override
    public void process() {
        /**
         * Измерение превышение тока заданной уставки в фазах
         * Если уставка превышена -> True
         * */
        Str.getPhsA().setValue(
                A.getPhsA().getInstCVal().getMag().getF().getValue() >
                        StrVal.getSetMag().getF().getValue()
        );
        Str.getPhsB().setValue(
                A.getPhsB().getInstCVal().getMag().getF().getValue() >
                        StrVal.getSetMag().getF().getValue()
        );
        Str.getPhsC().setValue(
                A.getPhsC().getInstCVal().getMag().getF().getValue() >
                        StrVal.getSetMag().getF().getValue()
        );

        /**
         * Если хотя бы в одной фазе зафиксировано превышение уставки, то
         * необходимо подать сигнал о том, что управляющее действие прервано
         * */
        Op.getGeneral().setValue(Str.getPhsA().getValue() || Str.getPhsB().getValue() || Str.getPhsC().getValue());

        /**Запуск выдержки времени, после прерывания управляющего воздействия*/
        if (Op.getGeneral().getValue()) {
            OpCntRs.getStVal().setValue(OpCntRs.getStVal().getValue() + 1); //Набор времени
        } else {
            OpCntRs.getStVal().setValue(0); //Сброс счетчика времени
        }

        /**
         * Отправка сигнала на отключение оборудования:
         * - уставка по току превышена;
         * - выдержка времени превышена;
         * */
        if (Op.getGeneral().getValue() && (OpCntRs.getStVal().getValue()
                * TmMult.getSetMag().getF().getValue() > OpDlOpTmms.getSetVal().getValue())) {
            Op.getPhsA().setValue(true);
            Op.getPhsB().setValue(true);
            Op.getPhsC().setValue(true);
        }
    }
}
