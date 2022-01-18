/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.devicetype.model;

import com.cumulocity.lpwan.payload.uplink.model.AlarmMapping;
import com.cumulocity.lpwan.payload.uplink.model.EventMapping;
import com.cumulocity.lpwan.payload.uplink.model.ManagedObjectMapping;
import com.cumulocity.lpwan.payload.uplink.model.MeasurementMapping;

import lombok.Data;

@Data
public class UplinkConfiguration {
    
    private Integer messageTypeId;
    private Integer startBit;
    private Integer noBits;
    private Double multiplier;
    private Double offset;
    private String unit;
    private boolean littleEndian;
    private boolean signed;
    private boolean bcd;
    private AlarmMapping alarmMapping;
    private MeasurementMapping measurementMapping;
    private EventMapping eventMapping;
    private ManagedObjectMapping managedObjectMapping;
    
    public boolean containsAlarmMapping() {
        if (alarmMapping == null) {
            return false;
        }
        return true;
    }
    
    public boolean containsMeasurementMapping() {
        if (measurementMapping == null) {
            return false;
        }
        return true;
    }
    
    public boolean containsEventMapping() {
        if (eventMapping == null) {
            return false;
        }
        return true;
    }
    
    public boolean containsManagedObjectMapping() {
        if (managedObjectMapping == null) {
            return false;
        }
        return true;
    }
    
}
