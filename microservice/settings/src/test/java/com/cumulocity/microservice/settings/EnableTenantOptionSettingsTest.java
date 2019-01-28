package com.cumulocity.microservice.settings;


import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.annotation.EnableContextSupportConfiguration;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.settings.service.MicroserviceSettingsService;
import com.cumulocity.rest.representation.tenant.OptionsRepresentation;
import com.cumulocity.sdk.client.RestOperations;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.core.MediaType;

import java.util.Map;
import java.util.concurrent.Callable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        EnableContextSupportConfiguration.class,
        EnableTenantOptionSettingsTestConfiguration.class
})
@TestPropertySource(properties = {"C8Y.encryptor.password=efuhMsAEAdP2wgHw", "C8Y.encryptor.salt=9D5B38224EF610C7"})
public class EnableTenantOptionSettingsTest {

    @Autowired
    private Environment environment;
    @Autowired
    private RestOperations restOperations;
    @Autowired
    private MicroserviceSettingsService microserviceSettingsService;
    @Autowired
    private ContextService<MicroserviceCredentials> contextService;

    @Test
    public void mustTenantOptionsBeAvailableByService() {
        // given
        OptionsRepresentation options = OptionsRepresentation.builder()
                .property("option1", "value1")
                .property("option2", "value2")
                .build();
        doReturn(options).when(restOperations).get(anyString(), any(MediaType.class), eq(OptionsRepresentation.class));
        // when
        Map<String, String> settings = contextService.callWithinContext(context("t500+"), new Callable<Map<String, String>>() {
            public Map<String, String> call() {
                return microserviceSettingsService.getAll();
            }
        });
        // then
        assertThat(settings).containsOnlyKeys("option1", "option2").containsValues("value1", "value2");
    }

    @Test
    public void mustTenantOptionsBeAvailableByEnvironment() {
        // given
        OptionsRepresentation options = OptionsRepresentation.builder()
                .property("option3", "value31")
                .property("option4", "value40")
                .build();
        doReturn(options).when(restOperations).get(anyString(), any(MediaType.class), eq(OptionsRepresentation.class));
        // when
        String result = contextService.callWithinContext(context("t100"), new Callable<String>() {
            public String call() {
                return environment.getProperty("option3");
            }
        });
        // then
        assertThat(result).isEqualTo("value31");
    }

    @Test
    public void mustDecryptConfidentialTenantOptionWhenDecryptAndGet() {
        // given
        OptionsRepresentation options = OptionsRepresentation.builder()
                .property("credentials.password1", "3e501290913206c2d23147b37141f9db8181b6a635cf189b64740dac99b6f0fd")
                .build();
        doReturn(options).when(restOperations).get(anyString(), any(MediaType.class), eq(OptionsRepresentation.class));
        // when
        String password1 = contextService.callWithinContext(context("t1000"), new Callable<String>() {
            public String call() {
                return microserviceSettingsService.decryptAndGet("password1");
            }
        });
        // then
        assertThat(password1).isEqualTo("pa$$w0rd");
    }

    private MicroserviceCredentials context(String tenant) {
        return MicroserviceCredentials.builder().tenant(tenant).username("service_app").build();
    }

}
