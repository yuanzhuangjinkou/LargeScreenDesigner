package io.dataease.plugins.view.official.handler;

import io.dataease.plugins.common.dto.chart.Series;
import io.dataease.plugins.view.entity.*;
import io.dataease.plugins.view.handler.PluginViewRSHandler;
import io.dataease.plugins.view.handler.impl.DefaultViewRSHandler;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AfaMapViewRSHandler implements PluginViewRSHandler<Map> {

    // 文本卡类型

    @Override
    public Map format(PluginViewParam pluginViewParam, List<String[]> data, boolean isDrill) {

        List<PluginViewField> xAxis = new ArrayList<>();
//        List<PluginViewField> yAxis = new ArrayList<>();

        pluginViewParam.getPluginViewFields().forEach(pluginViewField -> {
            if (StringUtils.equals(pluginViewField.getTypeField(), "xAxis")) {
                xAxis.add(pluginViewField);
            }
//            if (StringUtils.equals(pluginViewField.getTypeField(), "yAxis")) {
//                yAxis.add(pluginViewField);
//            }
        });

        Map<String, Object> map = new HashMap<>();

        List<String> x = new ArrayList<>();
        List<Series> series = new ArrayList<>();
        Series series1 = new Series();
        series1.setName(xAxis.get(0).getName());
        series1.setType(pluginViewParam.getPluginViewLimit().getType());
        series1.setData(new ArrayList<>());
        series.add(series1);
        for (String[] d : data) {
            StringBuilder a = new StringBuilder();
            if (isDrill) {
                a.append(d[xAxis.size() - 1]);
            } else {
                for (int i = 0; i < xAxis.size(); i++) {
                    if (i == xAxis.size() - 1) {
                        a.append(d[i]);
                    } else {
                        a.append(d[i]).append("\n");
                    }
                }
            }
            x.add(a.toString());
            series.get(0).getData().add(a.toString());
        }

        map.put("x", x);
        map.put("series", series);
        return map;
    }
}
