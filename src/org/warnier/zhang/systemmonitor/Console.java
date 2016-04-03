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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Startpoint for whole project.
 */
public class Console extends ApplicationFrame {
    private static final long delayMillis = 1000;

    public Console(String title) {
        super(title);
        ChartFactory.setChartTheme(getTheme());
        JFreeChart chart = ChartFactory.createLineChart("内存监视器",
                null,
                null,
                getDataSet());
        ChartPanel chartPanel = new ChartPanel(chart);
        // ? Can't use zh_CN.
        //chartPanel.setFont(new Font("MS Song", Font.PLAIN, 12));
        setContentPane(chartPanel);
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

    private DefaultCategoryDataset getDataSet() {
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        Set<Map.Entry<String, Double>> entrySet = gatherSystemInfo();
        for (Map.Entry<String, Double> entry : entrySet) {
            dataSet.addValue(entry.getValue(), "内存占用率", entry.getKey());
        }
        return dataSet;
    }

    private Set<Map.Entry<String, Double>> gatherSystemInfo() {
        SystemMonitor monitor = new SystemMonitor();
        Map<String, Double> dataSet = new HashMap<>(10);
        for (int i = 0; i < 8; i++) {
            dataSet.put(SimpleDateFormat.getTimeInstance().format(new Date()), monitor.getMemory());
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return dataSet.entrySet();
    }

    public static void main(String[] args) {
        Console console = new Console("System Monitor");
        console.pack();
        console.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        console.setVisible(true);
    }
}
