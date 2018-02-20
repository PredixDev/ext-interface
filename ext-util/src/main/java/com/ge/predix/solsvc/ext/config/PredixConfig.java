package com.ge.predix.solsvc.ext.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PredixConfig {

	@Autowired
    private AssetConfig assetConfig;
	@Autowired
    private TimeseriesConfig timeseriesConfig;
	@Autowired
    private ProxyConfig proxyConfig;
	@Autowired
    private UAAConfig uaaConfig;
	@Autowired
    private EventHubConfig eventHubConfig;
	@Autowired
    private AnalyticsConfig analyticsConfig;

    /**
     * Gets the value of the assetConfig property.
     * 
     * @return
     *     possible object is
     *     {@link AssetConfig }
     *     
     */
    public AssetConfig getAssetConfig() {
        return assetConfig;
    }

    /**
     * Sets the value of the assetConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link AssetConfig }
     *     
     */
    public void setAssetConfig(AssetConfig value) {
        this.assetConfig = value;
    }

    /**
     * Gets the value of the timeseriesConfig property.
     * 
     * @return
     *     possible object is
     *     {@link TimeseriesConfig }
     *     
     */
    public TimeseriesConfig getTimeseriesConfig() {
        return timeseriesConfig;
    }

    /**
     * Sets the value of the timeseriesConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimeseriesConfig }
     *     
     */
    public void setTimeseriesConfig(TimeseriesConfig value) {
        this.timeseriesConfig = value;
    }

    /**
     * Gets the value of the proxyConfig property.
     * 
     * @return
     *     possible object is
     *     {@link ProxyConfig }
     *     
     */
    public ProxyConfig getProxyConfig() {
        return proxyConfig;
    }

    /**
     * Sets the value of the proxyConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProxyConfig }
     *     
     */
    public void setProxyConfig(ProxyConfig value) {
        this.proxyConfig = value;
    }

    /**
     * Gets the value of the uaaConfig property.
     * 
     * @return
     *     possible object is
     *     {@link UAAConfig }
     *     
     */
    public UAAConfig getUAAConfig() {
        return uaaConfig;
    }

    /**
     * Sets the value of the uaaConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link UAAConfig }
     *     
     */
    public void setUAAConfig(UAAConfig value) {
        this.uaaConfig = value;
    }

    /**
     * Gets the value of the eventHubConfig property.
     * 
     * @return
     *     possible object is
     *     {@link EventHubConfig }
     *     
     */
    public EventHubConfig getEventHubConfig() {
        return eventHubConfig;
    }

    /**
     * Sets the value of the eventHubConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link EventHubConfig }
     *     
     */
    public void setEventHubConfig(EventHubConfig value) {
        this.eventHubConfig = value;
    }

    /**
     * Gets the value of the analyticsConfig property.
     * 
     * @return
     *     possible object is
     *     {@link AnalyticsConfig }
     *     
     */
    public AnalyticsConfig getAnalyticsConfig() {
        return analyticsConfig;
    }

    /**
     * Sets the value of the analyticsConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link AnalyticsConfig }
     *     
     */
    public void setAnalyticsConfig(AnalyticsConfig value) {
        this.analyticsConfig = value;
    }
}
