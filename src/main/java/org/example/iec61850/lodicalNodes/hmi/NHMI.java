package org.example.iec61850.lodicalNodes.hmi;

import org.example.iec61850.datatypes.common.Attribute;
import org.example.iec61850.lodicalNodes.common.LN;
import org.example.iec61850.lodicalNodes.hmi.other.NHMISignal;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.data.Range;
import org.jfree.data.general.Series;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class NHMI extends LN {
    private final HashMap<XYSeries, Attribute<?>> datasets = new HashMap<>();
    private final CombinedDomainXYPlot plot = new CombinedDomainXYPlot(new NumberAxis("Время"));
    ;
    private final JFrame frame = new JFrame();

    private int notifyCount = 0, updatePoint = 10; // счетчик и период обновления графиков
    private double currentTime = 0.0;


    public NHMI() {
        JFreeChart chart = new JFreeChart("", plot);
        chart.setBorderPaint(Color.black);
        chart.setBorderVisible(true);
        chart.setBackgroundPaint(Color.white);
        chart.setAntiAlias(true);
        ChartPanel chartPanel = new ChartPanel(chart);

        frame.setTitle("МЭИ РЗиАЭ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(chartPanel);
        frame.setSize(1280, 720);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
    }


    public void process() {
        if (!frame.isVisible()) frame.setVisible(true);

        currentTime += 1;
        datasets.forEach((series, rawData) -> {
            if (rawData.getValue() instanceof Number) series.add(currentTime, (Number) rawData.getValue(), false);
            else if (rawData.getValue() instanceof Boolean)
                series.add(currentTime, (Boolean) rawData.getValue() ? 1 : 0, false);
        });

        /* Период обновления */
        if (notifyCount++ > updatePoint) {
            notifyCount = 0;
            datasets.keySet().forEach(Series::fireSeriesChanged);
        }
    }


    /**
     * Добавить группу сигналов
     *
     * @param name    - название группы сгналов
     * @param signals - группа сигналов
     */
    public void addSignals(String name, NHMISignal... signals) {
        addSignals(name, Arrays.asList(signals));
    }

    /**
     * Добавить группу сигналов
     *
     * @param name    - название  группы сгналов
     * @param signals - группа сигналов
     */
    public void addSignals(String name, List<NHMISignal> signals) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        NumberAxis rangeAxis = new NumberAxis(name);
        rangeAxis.setAutoRangeIncludesZero(false);
        rangeAxis.setLabelAngle(45);

        if (name.contains("Mag"))
            rangeAxis.setRange(new Range(0.702, 0.711));

//		rangeAxis.setLabelFont(new Font("Impact", Font.ITALIC, 10));
        XYPlot subplot = new XYPlot(dataset, null, rangeAxis, new StandardXYItemRenderer());
        subplot.setBackgroundPaint(Color.BLACK);
        subplot.setDomainGridlinesVisible(false);
        subplot.setDomainCrosshairVisible(true);
        subplot.setRangeCrosshairVisible(true);
        plot.add(subplot, 5);

        /* Добавить дифференциал, если есть дискрет */
        signals.stream().filter(s -> s.getDataY().getValue() instanceof Boolean || s.getDataY().getValue() instanceof Byte).findFirst().ifPresent(s -> {
            subplot.setRenderer(0, new XYDifferenceRenderer());
            subplot.setWeight(1);
        });

        for (NHMISignal s : signals) {
            XYSeries series = new XYSeries(s.getName());
            dataset.addSeries(series);
            datasets.put(series, s.getDataY());
        }

        process();
    }


    /**
     * Добавить группу сигналов
     *
     * @param signals - группа сигналов
     */
    public void addSignals(NHMISignal... signals) {
        String name = Arrays.stream(signals)
                .map(NHMISignal::getName)
                .collect(Collectors.joining(", "));
        addSignals(name, signals);
    }

    public void dispose() {
        frame.dispose();
    }
}
