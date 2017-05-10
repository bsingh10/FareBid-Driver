package apps.farebid.com.farebid;

/**
 * Created by b.singh10 on 12/25/16.
 */

public class BidObject {
    private  String bidstatus;
    private  String bidvalue;
    private  String driverid;
    private  String requestid;

    public BidObject(String bidstatus, String bidvalue, String driverid, String requestid) {
        this.bidstatus = bidstatus;
        this.bidvalue = bidvalue;
        this.driverid = driverid;
        this.requestid = requestid;
    }

    public BidObject() {
    }

    public String getBidstatus() {
        return bidstatus;
    }

    public void setBidstatus(String bidstatus) {
        this.bidstatus = bidstatus;
    }

    public String getBidvalue() {
        return bidvalue;
    }

    public void setBidvalue(String bidvalue) {
        this.bidvalue = bidvalue;
    }

    public String getDriverid() {
        return driverid;
    }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }
}
