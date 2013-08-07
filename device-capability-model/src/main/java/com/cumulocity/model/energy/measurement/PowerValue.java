package com.cumulocity.model.energy.measurement;

import java.math.BigDecimal;

import com.cumulocity.model.energy.sensor.SinglePhaseElectricitySensor;
import com.cumulocity.model.energy.sensor.ThreePhaseElectricitySensor;
import com.cumulocity.model.measurement.MeasurementValue;
import com.cumulocity.model.measurement.StateType;
import com.cumulocity.model.measurement.ValueType;
import com.cumulocity.model.util.Alias;

/**
 * Represents a PowerValue, as reported by {@link SinglePhaseElectricitySensor} and {@link ThreePhaseElectricitySensor}.
 *
 * At the moment, this representation does not provide any additional properties of its own.
 * @author ricardomarques
 *
 */
@Alias(value = "c8y_PowerValue")
public class PowerValue extends MeasurementValue {

    public PowerValue() {
    }

    public PowerValue(BigDecimal value, String unit, ValueType type, String quantity, StateType state) {
        super(value, unit, type, quantity, state);
    }
}
