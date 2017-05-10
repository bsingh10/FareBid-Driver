package apps.farebid.com.farebid;

import com.google.firebase.database.IgnoreExtraProperties;


/**
 * Created by b.singh10 on 12/11/16.
 */
@IgnoreExtraProperties
public class Driver {


    public String dname;
    public String dlat;
    public String dlong;
    public String donwork;
    public String dtimestamp;


    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDlat() {
        return dlat;
    }

    public void setDlat(String dlat) {
        this.dlat = dlat;
    }

    public String getDlong() {
        return dlong;
    }

    public void setDlong(String dlong) {
        this.dlong = dlong;
    }

    public String getDonwork() {
        return donwork;
    }

    public void setDonwork(String donwork) {
        this.donwork = donwork;
    }

    public String getDtimestamp() {
        return dtimestamp;
    }

    public void setDtimestamp(String dtimestamp) {
        this.dtimestamp = dtimestamp;
    }

    public Driver() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Driver(String DName, String DLat,String DLong,String DOnWork,String DTimestamp) {
        this.dname =DName;
        this.dlat = DLat;
        this.dlong = DLong;
        this.donwork =DOnWork;
        this.dtimestamp=DTimestamp;

    }


}