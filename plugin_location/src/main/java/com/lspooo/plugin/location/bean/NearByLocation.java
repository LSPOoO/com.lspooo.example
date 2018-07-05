package com.lspooo.plugin.location.bean;

import com.tencent.lbssearch.object.Location;

/**
 * @author 容联•云通讯
 * @version 5.2.0
 * @since 2016-05-19
 */
public class NearByLocation {

    public String id;
    public String title;
    public String address;
    public Location location;
    public boolean choice = false;

    public NearByLocation() {
    }

    public NearByLocation(String address, Location location, boolean choice) {
        this.address = address;
        this.location = location;
        this.choice = choice;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("NearByLocation{");
        sb.append("id='").append(id).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", location=").append(location);
        sb.append(", choice=").append(choice);
        sb.append('}');
        return sb.toString();
    }
}
