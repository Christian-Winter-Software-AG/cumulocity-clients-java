package c8y;

import java.io.Serializable;

import org.svenson.AbstractDynamicProperties;

public class RemoteAccessConnect extends AbstractDynamicProperties implements Serializable {

    private static final long serialVersionUID = -6443811928706492241L;
    
    public static final String PROTOCOL_VNC = RemoteAccess.PROTOCOL_VNC;

    private String connectionKey;

    private String hostname;

    private Integer port;

    private String protocol;

    public RemoteAccessConnect() {
        this(null, null, null);
    }
	
    public RemoteAccessConnect(String connectionKey, String host, Integer port) {
        this.connectionKey = connectionKey;
        this.hostname = host;
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getConnectionKey() {
        return connectionKey;
    }

    public void setConnectionKey(String connectionKey) {
        this.connectionKey = connectionKey;
    }

    @Override
    public String toString() {
        return "RemoteAccessConnect [hostname=" + hostname + ", port=" + port + ", protocol=" + protocol + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((connectionKey == null) ? 0 : connectionKey.hashCode());
        result = prime * result + ((hostname == null) ? 0 : hostname.hashCode());
        result = prime * result + ((port == null) ? 0 : port.hashCode());
        result = prime * result + ((protocol == null) ? 0 : protocol.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RemoteAccessConnect other = (RemoteAccessConnect) obj;
        if (connectionKey == null) {
            if (other.connectionKey != null)
                return false;
        } else if (!connectionKey.equals(other.connectionKey))
            return false;
        if (hostname == null) {
            if (other.hostname != null)
                return false;
        } else if (!hostname.equals(other.hostname))
            return false;
        if (port == null) {
            if (other.port != null)
                return false;
        } else if (!port.equals(other.port))
            return false;
        if (protocol == null) {
            if (other.protocol != null)
                return false;
        } else if (!protocol.equals(other.protocol))
            return false;
        return true;
    }
}
