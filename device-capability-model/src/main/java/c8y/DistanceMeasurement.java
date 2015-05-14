package c8y;

import org.svenson.AbstractDynamicProperties;

import com.cumulocity.model.measurement.MeasurementValue;

public class DistanceMeasurement extends AbstractDynamicProperties {

    private MeasurementValue distance;

    public DistanceMeasurement() {
    }
    
    public DistanceMeasurement(MeasurementValue distance) {
        this.distance = distance;
    }
    
    public MeasurementValue getDistance() {
        return distance;
    }

    public void setDistance(MeasurementValue distance) {
        this.distance = distance;
    }
}
