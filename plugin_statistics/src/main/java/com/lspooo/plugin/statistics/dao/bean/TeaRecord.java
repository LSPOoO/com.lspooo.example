package com.lspooo.plugin.statistics.dao.bean;

/**
 * Created by LSP on 2017/10/12.
 */

public class TeaRecord {

    private Long id;
    private Long employeeId;
    private Float weight;
    private String time;

    public TeaRecord(Long id, Long employeeId, Float weight, String time) {
        this.id = id;
        this.employeeId = employeeId;
        this.weight = weight;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "TeaRecord{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", weight=" + weight +
                ", time='" + time + '\'' +
                '}';
    }
}
