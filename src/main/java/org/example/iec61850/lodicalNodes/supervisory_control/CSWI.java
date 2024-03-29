package org.example.iec61850.lodicalNodes.supervisory_control;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.lodicalNodes.LN;
import org.example.iec61850.node_parameters.DataObject.controls.DPC;
import org.example.iec61850.node_parameters.DataObject.status_information.ACT;
import org.example.iec61850.node_parameters.DataObject.status_information.SPS;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CSWI extends LN {
    /**
     * LN: Switch controller Name: CSWI (LN: Название контроллера коммутатора: CSWI)
     *
     * Коммутатор управления LN обрабатывает все операции CSWI распределительного устройства
     * от операторов и связанной автоматики. Он проверяет авторизацию команд. Он контролирует выполнение команд
     * и подает сигнал тревоги в случае неправильного завершения команды. Он запрашивает освобождение от
     * блокировки, синхронизации, автоматического закрытия и т.д. если применимо.
     * */
    /**
     * Status information
     */

//    //Selection “Open switch”
    private SPS SelOpn = new SPS(); //Выбор “Разомкнутый выключатель”
//    //Operation “Close switch”

    //Selection “Close switch”
    private SPS SelCLs = new SPS(); //Выбор "Замкнутый выключатель"
    //Switch, general
    private DPC Pos = new DPC();
    //Switch L1
    private DPC PosA = new DPC();
    //Switch L2
    private DPC PosB = new DPC();
    //Switch L3
    private DPC PosC = new DPC();

    /**Лист хранящий значения срабатывания защит (сигналов на отключения), для каждой из ступеней*/
    private List<ACT> OpOpnList = new ArrayList<>();

    /**Конструктор для задания начальных значений*/
    public CSWI() {
        SelOpn.getStVal().setValue(false);
        SelCLs.getStVal().setValue(true);
    }

    @Override
    public void process() {
        /**Проверяем каждую из ступеней ТО на наличие отключающего сигнала*/
        for (int i = 0; i < OpOpnList.size(); i++) {
            if (OpOpnList.get(i).getGeneral().getValue()) {
                SelOpn.getStVal().setValue(true);
                SelCLs.getStVal().setValue(false);
                break;
            }
        }

        /**При получении сигнала на отключение, отключает все выключатели фаз*/
        if (SelOpn.getStVal().getValue()) {
            Pos.getStVal().setValue(DPC.StVal.OFF);
            PosA.getStVal().setValue(DPC.StVal.OFF);
            PosB.getStVal().setValue(DPC.StVal.OFF);
            PosC.getStVal().setValue(DPC.StVal.OFF);
        } else {
            Pos.getStVal().setValue(DPC.StVal.ON);
            PosA.getStVal().setValue(DPC.StVal.ON);
            PosB.getStVal().setValue(DPC.StVal.ON);
            PosC.getStVal().setValue(DPC.StVal.ON);
        }
    }
}
