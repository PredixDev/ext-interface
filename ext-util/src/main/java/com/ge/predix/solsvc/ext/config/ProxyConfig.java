package com.ge.predix.solsvc.ext.config;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProxyConfig {

	@Value("${predix.rest.proxyHost:#{null}}")
    private String proxyHost;
	@Value("${predix.rest.proxyPort:#{null}}")
    private String proxyPort;

    /**
     * Gets the value of the proxyHost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProxyHost() {
        return proxyHost;
    }

    /**
     * Sets the value of the proxyHost property.
     * 
     * @param proxyHost - Proxy Host
     */
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    /**
     * Gets the value of the proxyPort property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public String getProxyPort() {
        return proxyPort;
    }

    /**
     * Sets the value of the proxyPort property.
     * 
     * @param proxyPort - Proxy Port
     *     
     */
    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }
}
