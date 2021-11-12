package com.cool.biz.monotor.controller;
import com.cool.core.base.api.R;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *<p>
 * api监控
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/20
 */
@RestController
@AllArgsConstructor
@RequestMapping("/monitor/apiStat")
public class ApiStatController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping()
    public R getInfo() throws Exception {
        Map<String, Object> statMap = new HashMap<>();
        String sql = "select oper_url \"name\", sum(1) \"value\" from sys_oper_log group by oper_url order by value";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        statMap.put("pieSeries", list);
        List<String> nameList = new ArrayList<>();
        List<Integer> valueList = new ArrayList<>();
        list.stream().forEach(map -> {
            nameList.add(map.get("name").toString());
            valueList.add(Integer.parseInt(map.get("value").toString()));
        });
        statMap.put("pieLegend", nameList);
        statMap.put("barSeries", valueList);
        statMap.put("barYAxis", nameList);
        return R.ok(statMap);
    }
}
