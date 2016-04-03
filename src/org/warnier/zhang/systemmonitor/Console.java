package org.warnier.zhang.systemmonitor;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.warnier.zhang.systemmonitor.util.SystemMonitor;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * Startpoint for whole project.
 */
public class Console extends ApplicationFrame {
    private static final long delayMillis = 1000;
    private List<Map<String, Double>> dataSet = new ArrayList<>(8);

    public Console(String title) {
        super(title);
        ChartFactory.setChartTheme(getTheme());

        //Gather system info.
        gatherSystemInfo();
    }

    //Enable zh_CN.
    private StandardChartTheme getTheme() {
        StandardChartTheme theme = new StandardChartTheme("CN");
        Font font = new Font("宋体", Font.PLAIN, 12);
        theme.setExtraLargeFont(new Font("宋体", Font.BOLD, 18));
        theme.setRegularFont(font);
        theme.setLargeFont(font);
        return theme;
    }

    private void gatherSystemInfo() {
        final SystemMonitor monitor = new SystemMonitor();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Map<String, Double> dataItem = new HashMap<>(1);
                dataItem.put(SimpleDateFormat.getTimeInstance().format(new Date()), monitor.getMemory());
                dataSet.add(dataItem);
                //Retain 8 latest items.
                if (dataSet.size() > 8) {
                    dataSet.remove(0);
                }
                invalidate(getChart());
            }
        }, 0, delayMillis);
    }

    private void invalidate(JFreeChart chart) {
        ChartPanel chartPanel = new ChartPanel(chart);
        // ? Can't use zh_CN.
        //chartPanel.setFont(new Font("MS Song", Font.PLAIN, 12));
        chartPanel.repaint();

        setContentPane(chartPanel);
    }

    private JFreeChart getChart() {
        JFreeChart chart = ChartFactory.createLineChart("内存监视器",
                null,
                null,
                wrapDataSet(dataSet));
        return chart;
    }

    private DefaultCategoryDataset wrapDataSet(List<Map<String, Double>> rawData) {
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        for (int i = 0; i < rawData.size(); i++) {
            for (Map.Entry<String, Double> entry : rawData.get(i).entrySet()) {
                dataSet.addValue(entry.getValue(), "内存占用率", entry.getKey());
            }
        }
        return dataSet;
    }

    public static void main(String[] args) {
        Console console = new Console("System Monitor");
        console.setSize(680, 420);
        console.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        console.setVisible(true);
    }
}
