package com.cumulocity.sdk.client;

import lombok.*;
import lombok.Builder.Default;

@Data
@Builder(builderMethodName = "connectionPool", toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ConnectionPoolConfig {
    @Default
    private boolean enabled =true;
    @Default
    private int perHost = 50;
    @Default
    private int max = 100;
    @Default
    private int awaitTimeout = 10000;
}
