package com.lspooo.plugin.statistics.dao.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LSP on 2017/10/12.
 */

public class TeaRecord implements Parcelable{

    private Long id;
    private Long employeeId;
    private Float weight;
    private String time;

    private String name;

    public TeaRecord() {
    }

    public TeaRecord(Long id, Long employeeId, Float weight, String time) {
        this.id = id;
        this.employeeId = employeeId;
        this.weight = weight;
        this.time = time;
    }

    protected TeaRecord(Parcel in) {
        id = in.readLong();
        employeeId = in.readLong();
        weight = in.readFloat();
        time = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(employeeId);
        dest.writeFloat(weight);
        dest.writeString(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TeaRecord> CREATOR = new Creator<TeaRecord>() {
        @Override
        public TeaRecord createFromParcel(Parcel in) {
            return new TeaRecord(in);
        }

        @Override
        public TeaRecord[] newArray(int size) {
            return new TeaRecord[size];
        }
    };

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (obj instanceof TeaRecord && ((TeaRecord) obj).getId() == this.getId()){
            return true;
        }
        return false;
    }
}
