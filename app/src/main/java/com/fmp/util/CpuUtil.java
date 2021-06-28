package com.fmp.util;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class CpuUtil {
    private static final List<String> CPU_TEMP_FILE_PATHS = Arrays.asList(
            "/sys/devices/system/cpu/cpu0/cpufreq/cpu_temp",
            "/sys/devices/system/cpu/cpu0/cpufreq/FakeShmoo_cpu_temp",
            "/sys/class/thermal/thermal_zone0/temp",
            "/sys/class/i2c-adapter/i2c-4/4-004c/temperature",
            "/sys/devices/platform/tegra-i2c.3/i2c-4/4-004c/temperature",
            "/sys/devices/platform/omap/omap_temp_sensor.0/temperature",
            "/sys/devices/platform/tegra_tmon/temp1_input",
            "/sys/kernel/debug/tegra_thermal/temp_tj",
            "/sys/devices/platform/s5p-tmu/temperature",
            "/sys/class/thermal/thermal_zone1/temp",
            "/sys/class/hwmon/hwmon0/device/temp1_input",
            "/sys/devices/virtual/thermal/thermal_zone1/temp",
            "/sys/devices/virtual/thermal/thermal_zone0/temp",
            "/sys/class/thermal/thermal_zone3/temp",
            "/sys/class/thermal/thermal_zone4/temp",
            "/sys/class/hwmon/hwmonX/temp1_input",
            "/sys/devices/platform/s5p-tmu/curr_temp");

    public static String getCpuTemperatureString() {
        return String.format("%.1f℃", getCpuTemperature());
    }


    public static float getCpuTemperature() {
        for (String path : CPU_TEMP_FILE_PATHS) {
            float temperature = getTemperature(path);
            if (isTemperatureValid(temperature)) {
                return temperature / 1000.0f;
            }
        }
        return 0.0F;
    }


    public static float getTemperature(String path) {
        try {
            Process process = Runtime.getRuntime().exec(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = br.readLine();
            if (!TextUtils.isEmpty(line)) {
                return Float.parseFloat(line) / 1000.0f;
            }
            br.close();
            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -100.0F;
    }


    private static boolean isTemperatureValid(float temp) {
        return temp >= -30.0F && temp <= 250.0F;
    }

}

