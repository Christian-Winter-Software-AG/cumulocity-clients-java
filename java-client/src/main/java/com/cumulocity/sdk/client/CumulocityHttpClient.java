package com.cumulocity.sdk.client;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.ApacheHttpClientHandler;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CumulocityHttpClient extends ApacheHttpClient {
    
    private final Pattern hostPattern = Pattern.compile("((http|https):\\/\\/.+?)(\\/|\\?|$)");
    
    private PlatformParameters platformParameters;

    CumulocityHttpClient(ApacheHttpClientHandler createDefaultClientHander, IoCComponentProviderFactory provider) {
        super(createDefaultClientHander, provider);
    }

    @Override
    public WebResource resource(String path) {
        return super.resource(resolvePath(path));
    }
    
    protected String resolvePath(String path) {
        if (platformParameters != null && platformParameters.isForceInitialHost()) {
            if (platformParameters.getHost().equals(path)) {
                return path;
            }
            Matcher matcher = hostPattern.matcher(path);
            if (matcher.find()) {
                String capturedHost = matcher.group(1);
                return path.replace(capturedHost, platformParameters.getHost());
            }
        }
        return path;
    }
    
    public void setPlatformParameters(PlatformParameters platformParameters) {
        this.platformParameters = platformParameters;
    }
    
    

}
