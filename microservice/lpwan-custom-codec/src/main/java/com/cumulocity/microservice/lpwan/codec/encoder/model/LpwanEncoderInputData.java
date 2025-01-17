/*
 * Copyright (c) 2024 Cumulocity GmbH, Düsseldorf, Germany and/or its affiliates and/or their licensors. *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Cumulocity GmbH.
 */

package com.cumulocity.microservice.lpwan.codec.encoder.model;

import com.cumulocity.microservice.customencoders.api.model.EncoderInputData;
import com.cumulocity.microservice.lpwan.codec.model.DeviceInfo;
import com.cumulocity.model.idtype.GId;
import com.google.common.base.Strings;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import java.util.*;

/**
 * The <b>LpwanEncoderInputData</b> class represents the format and content of the request coming in for encoding a device command.
 */
public class LpwanEncoderInputData extends EncoderInputData {
    public static final String SOURCE_DEVICE_EUI_KEY = "sourceDeviceEui";

    /**
     * Instantiates a new LpwanEncoderInputData.
     *
     * @param sourceDeviceId the managed object id of the device
     * @param sourceDeviceEui represents the eui of the device
     * @param sourceDeviceInfo contains the information about the device such as Manufacturer or model and/or the supported commands of the device
     * @param commandName the name of the command to be encoded
     * @param commandData the text of the command to be encoded
     */
    public LpwanEncoderInputData(@NotBlank String sourceDeviceId,
                                 @NotBlank String sourceDeviceEui,
                                 @NotNull DeviceInfo sourceDeviceInfo,
                                 @NotBlank String commandName,
                                 @Null String commandData) {

        initializeAndValidate(sourceDeviceId, sourceDeviceEui, sourceDeviceInfo, commandName, commandData, null);
    }

    /**
     * Instantiates a new LpwanEncoderInputData.
     *
     * @param sourceDeviceId the managed object id of the device
     * @param commandName the name of the command to be encoded
     * @param commandData the text of the command to be encoded
     * @param args represents the properties
     */
    public LpwanEncoderInputData(@NotBlank GId sourceDeviceId,
                                 @NotBlank String commandName,
                                 @Null String commandData,
                                 @NotNull Map<String, String> args) {
        String sourceDeviceIdString = null;
        if (Objects.nonNull(sourceDeviceId)) {
            sourceDeviceIdString = sourceDeviceId.getValue();
        }

        String sourceDeviceEui = null;
        DeviceInfo sourceDeviceInfo = null;

        if (Objects.nonNull(args)) {
            sourceDeviceEui = args.get(SOURCE_DEVICE_EUI_KEY);
            sourceDeviceInfo = new DeviceInfo(args);
        }

        initializeAndValidate(sourceDeviceIdString, sourceDeviceEui, sourceDeviceInfo, commandName, commandData, args);
    }

    private void initializeAndValidate(@NotBlank String sourceDeviceId,
                                       @NotBlank String sourceDeviceEui,
                                       @NotNull DeviceInfo sourceDeviceInfo,
                                       @NotBlank String commandName,
                                       @Null String commandData,
                                       @Null Map<String, String> inputArguments) {

        setSourceDeviceId(sourceDeviceId);
        setSourceDeviceEui(sourceDeviceEui);
        setSourceDeviceInfo(sourceDeviceInfo);
        setCommandName(commandName);
        setCommandData(commandData);

        if (Objects.nonNull(inputArguments)) {
            getInputArguments().putAll(inputArguments);
        }

        validate();
    }

    /**
     * Gets the source device eui
     *
     * @return the source device eui
     */
    public @NotBlank String getSourceDeviceEui() {
        return getInputArguments().get(SOURCE_DEVICE_EUI_KEY);
    }

    private void setSourceDeviceEui(@NotBlank String sourceDeviceEui) {
        getInputArguments().put(SOURCE_DEVICE_EUI_KEY, sourceDeviceEui);
    }

    /**
     * Gets the source device info that contains the information about the device such as Manufacturer or model of the device
     *
     * @return the source device info
     */
    public @NotNull DeviceInfo getSourceDeviceInfo() {
        return new DeviceInfo(getInputArguments());
    }

    private void setSourceDeviceInfo(@NotNull DeviceInfo sourceDeviceInfo) {
        if (Objects.nonNull(sourceDeviceInfo)) {
            getInputArguments().put(DeviceInfo.DEVICE_MANUFACTURER, sourceDeviceInfo.getDeviceManufacturer());
            getInputArguments().put(DeviceInfo.DEVICE_MODEL, sourceDeviceInfo.getDeviceModel());
        }
    }

    private @NotNull Map<String, String> getInputArguments() {
        Map<String, String> args = super.getArgs();
        if (Objects.isNull(args)) {
            args = new HashMap<>();
            setArgs(args);
        }

        return args;
    }

    /**
     * This method validates the object fields.
     *
     * @throws IllegalArgumentException if the field marked with <b>@NotNull</b> or <b>@NotBlank</b> are either null or blank.
     * @see <a href="https://docs.oracle.com/javase/7/docs/api/java/lang/IllegalArgumentException.html">IllegalArgumentException</a>
     */
    private void validate() {
        List<String> missingParameters = new ArrayList<>();

        if (Strings.isNullOrEmpty(getSourceDeviceId())) {
            missingParameters.add("'sourceDeviceId'");
        }

        if (Strings.isNullOrEmpty(getSourceDeviceEui())) {
            missingParameters.add("'sourceDeviceEui'");
        }

        if (Objects.isNull(getSourceDeviceInfo())) {
            missingParameters.add("'sourceDeviceInfo'");
        }
        else {
            try {
                getSourceDeviceInfo().validate();
            } catch (IllegalArgumentException e) {
                missingParameters.add("'manufacturer, model and/or supportedCommands'");
            }
        }

        if(Strings.isNullOrEmpty(getCommandName())) {
            missingParameters.add("'commandName'");
        }

        if (!missingParameters.isEmpty()) {
            throw new IllegalArgumentException("EncoderInputData is missing mandatory fields: " + String.join(", ", missingParameters));
        }
    }
}