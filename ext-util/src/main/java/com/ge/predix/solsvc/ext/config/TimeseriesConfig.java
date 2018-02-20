package com.ge.predix.solsvc.ext.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TimeseriesConfig {

	@Value("${predix.timeseries.ingest.endpoint:#{null}}")
    private String ingestEndPoint;
	
	@Value("${predix.timeseries.query.endpoint:#{null}}")
    private String queryEndPoint;
	
	@Value("${predix.timeseries.zone.headername:Predix-Zone-Id}")
    private String zoneHeaderName;
    
    @Value("${predix.timeseries.zone.headervalue:#{null}}")
    private String zoneHeaderValue;

    /**
     * Gets the value of the ingestEndPoint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIngestEndPoint() {
        return ingestEndPoint;
    }

    /**
     * Sets the value of the ingestEndPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIngestEndPoint(String value) {
        this.ingestEndPoint = value;
    }

    /**
     * Gets the value of the queryEndPoint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQueryEndPoint() {
        return queryEndPoint;
    }

    /**
     * Sets the value of the queryEndPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQueryEndPoint(String value) {
        this.queryEndPoint = value;
    }

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
}
