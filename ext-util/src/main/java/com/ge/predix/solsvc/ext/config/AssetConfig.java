package com.ge.predix.solsvc.ext.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AssetConfig{

	@Value("${predix.asset.zone.headername:#{null}}")
    private String zoneHeaderName;
    
    @Value("${predix.asset.zone.headervalue:#{null}}")
    private String zoneHeaderValue;

    @Value("${predix.asset.url:#{null}}")
    private String assetURL;

    /**
     * Gets the value of the zoneHeaderName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZoneHeaderName() {
        return zoneHeaderName;
    }

    /**
     * Sets the value of the zoneHeaderName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZoneHeaderName(String value) {
        this.zoneHeaderName = value;
    }

    /**
     * Gets the value of the zoneHeaderValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZoneHeaderValue() {
        return zoneHeaderValue;
    }

    /**
     * Sets the value of the zoneHeaderValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZoneHeaderValue(String value) {
        this.zoneHeaderValue = value;
    }

    /**
     * Gets the value of the assetURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssetURL() {
        return assetURL;
    }

    /**
     * Sets the value of the assetURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssetURL(String value) {
        this.assetURL = value;
    }
}
