package apps.farebid.com.farebid;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by b.singh10 on 12/25/16.
 */

public class RideRequest {
    private String  destinationAddress;
    private String  passengerid;
    private String  sourceAddress;
    private String  ridestatus;
    private String  ridedatetime;

    public RideRequest(String destinationAddress, String passengerid, String sourceAddress, String ridestatus, String ridedatetime, String requesttime) {
        this.destinationAddress = destinationAddress;
        this.passengerid = passengerid;
        this.sourceAddress = sourceAddress;
        this.ridestatus = ridestatus;
        this.ridedatetime = ridedatetime;
        this.requesttime = requesttime;
    }

    public String getRequesttime() {
        return requesttime;
    }

    public void setRequesttime(String requesttime) {
        this.requesttime = requesttime;
    }

    private String  requesttime;
    public RideRequest() {

    }

    public RideRequest(String destinationAddress, String passengerid, String sourceAddress, String ridestatus, String ridedatetime) {
        this.destinationAddress = destinationAddress;
        this.passengerid = passengerid;
        this.sourceAddress = sourceAddress;
        this.ridestatus = ridestatus;
        this.ridedatetime = ridedatetime;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getPassengerid() {
        return passengerid;
    }

    public void setPassengerid(String passengerid) {
        this.passengerid = passengerid;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getRidestatus() {
        return ridestatus;
    }

    public void setRidestatus(String ridestatus) {
        this.ridestatus = ridestatus;
    }

    public String getRidedatetime() {
        return ridedatetime;
    }

    public void setRidedatetime(String ridedatetime) {
        this.ridedatetime = ridedatetime;
    }
}
