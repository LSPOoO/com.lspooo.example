package com.lspooo.plugin.statistics.dao.bean;

/**
 * Created by LSP on 2017/10/10.
 */

public class TeaEmployee {

    private Long id;
    private String name;
    private Double totalWeight = 0.0;
    private long count = 0;

    public TeaEmployee() {
    }

    public TeaEmployee(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "TeaEmployee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
