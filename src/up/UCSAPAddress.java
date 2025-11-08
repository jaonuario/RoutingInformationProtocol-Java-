package up;
public class UCSAPAddress {
    private short ucsapId;
    private String hostName;
    private int portNumber;

    public UCSAPAddress(short ucsapId, String hostName, int portNumber) {
        this.ucsapId = ucsapId;
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    public short getUcsapId() {
        return ucsapId;
    }

    public String getHostName() {
        return hostName;
    }

    public int getPortNumber() {
        return portNumber;
    }
}