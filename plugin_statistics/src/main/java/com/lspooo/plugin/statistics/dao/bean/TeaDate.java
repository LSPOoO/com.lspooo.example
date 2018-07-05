package com.lspooo.plugin.statistics.dao.bean;

/**
 * Created by lspooo on 2018/3/26.
 */

public class TeaDate {

    private String time;
    private Double weight = 0.0;
    private int totalCount = 0;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
