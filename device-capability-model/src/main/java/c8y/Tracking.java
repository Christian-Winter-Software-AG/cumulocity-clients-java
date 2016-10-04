package c8y;

import org.svenson.AbstractDynamicProperties;

public class Tracking extends AbstractDynamicProperties {

    // Reporting interval in seconds
    protected int interval = 0;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
