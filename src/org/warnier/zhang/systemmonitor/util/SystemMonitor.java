package org.warnier.zhang.systemmonitor.util;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The SystemMonitor class provides access to system info.
 */
public class SystemMonitor {
    private Sigar sigar;

    public SystemMonitor() {
        sigar = new Sigar();
    }

    public double getMemory() {
        double memory = 0;
        try {
            Mem mem = sigar.getMem();
            memory = mem.getUsedPercent();
        } catch (SigarException e) {
            e.printStackTrace();
        }
        return memory;
    }

    //No need.
    private String format(double text) {
        return NumberFormat.getPercentInstance().format(text);
    }
}
