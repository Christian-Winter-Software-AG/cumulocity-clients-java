/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.cumulocity.sdk.agent.action;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.sdk.agent.driver.DeviceDriver;
import com.cumulocity.sdk.agent.model.Device;
import com.cumulocity.sdk.agent.model.DevicesManagingAgent;

public class ObtainDeviceMeasurementsActionTest {

    @Mock
    private DevicesManagingAgent<Device> agent;

    private Queue<MeasurementRepresentation> queue = new LinkedList<MeasurementRepresentation>();

    @Mock
    private Device device1;

    @Mock
    private Device device2;

    @Mock
    private DeviceDriver<Device> deviceDriver;

    private ObtainDeviceMeasurementsAction<Device> action;

    private MeasurementRepresentation mr11 = measurementRepresentation("11");

    private MeasurementRepresentation mr12 = measurementRepresentation("12");

    private MeasurementRepresentation mr2 = measurementRepresentation("2");

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(deviceDriver.loadMeasuremntsFromDevice(device1)).thenReturn(asList(mr11, mr12));
        when(deviceDriver.loadMeasuremntsFromDevice(device2)).thenReturn(asList(mr2));

        action = new ObtainDeviceMeasurementsAction<Device>(agent);
        action.setDeviceDriver(deviceDriver);
    }

    @Test
    public void testPerformStartupAction() {
        //given
        when(agent.getDevices()).thenReturn(asList(device1, device2));
        when(agent.getMeasurementsQueue()).thenReturn(queue);

        //when
        action.run();

        //then
        Queue<MeasurementRepresentation> expected = new LinkedList<MeasurementRepresentation>(asList(mr11, mr12, mr2));
        assertThat(queue, is(expected));
    }

    private static MeasurementRepresentation measurementRepresentation(String id) {
        MeasurementRepresentation mr = new MeasurementRepresentation();
        mr.setId(new GId(id));
        return mr;
    }
}
