package org.example.iec61850.lodicalNodes.switchgear;

import lombok.Getter;
import lombok.Setter;
import org.example.iec61850.lodicalNodes.LN;
import org.example.iec61850.node_parameters.DataObject.controls.DPC;
import org.example.iec61850.node_parameters.DataObject.controls.INC;
import org.example.iec61850.node_parameters.DataObject.controls.SPC;
import org.example.iec61850.node_parameters.DataObject.status_information.SPS;

@Getter
@Setter
public class XCBR extends LN {
    /**
     * LN: Circuit breaker Name: XCBR (LN: Название автоматического выключателя: XCBR)
     *
     * Логические узлы этой группы предоставляют данные, необходимые для представления соответствующего
     * оборудования распределительного устройства в системе автоматизации. Существует
     * только два логических узла (XCBR, XSWI), поскольку все отключающие устройства
     * без тока моделируются XSWI. Каждый логический узел имеет сопутствующие
     * логические узлы в группах (например, SCBR, SSWI), предоставляющие
     * подробную информацию о контроле, если это необходимо.
     * */
    /**
     * Descriptions
     */

    //Local control behaviour
    private SPS Loc = new SPS();
    //Operation counter
    private INC OpCnt = new INC();
    //Switch position
    private DPC Pos = new DPC(); //Положение выключателя
    /**Блокировки*/
    //Block opening
    private SPC BlkOpn = new SPC(); //Открытие блока (блокировка на отключение)
    //Block closing
    private SPC BlkCls = new SPC(); //Закрытие блока (блокировка на включение)

    @Override
    public void process() {
        if (!Pos.getStVal().getValue().equals(DPC.StVal.OFF) && !BlkOpn.getStVal().getValue()) {
            Pos.getStVal().setValue(DPC.StVal.OFF);
            BlkCls.getStVal().setValue(true);
        } else {
            Pos.getStVal().setValue(DPC.StVal.ON);
            BlkCls.getStVal().setValue(false);
        }
    }
}
