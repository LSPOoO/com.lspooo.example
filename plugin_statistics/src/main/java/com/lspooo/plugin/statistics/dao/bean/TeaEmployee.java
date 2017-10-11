package com.lspooo.plugin.statistics.dao.bean;

/**
 * Created by LSP on 2017/10/10.
 */

public class TeaEmployee {

    private Long id;
    private String mobile;
    private String name;
    private Integer gender;
    /**
     * 员工入职时间
     */
    private String startTime;
    /**
     * 员工离职时间
     */
    private String endTime;

    public TeaEmployee(Long id, String mobile, String name, Integer gender, String startTime, String endTime) {
        this.id = id;
        this.mobile = mobile;
        this.name = name;
        this.gender = gender;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "TeaEmployee{" +
                "id=" + id +
                ", mobile='" + mobile + '\'' +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
