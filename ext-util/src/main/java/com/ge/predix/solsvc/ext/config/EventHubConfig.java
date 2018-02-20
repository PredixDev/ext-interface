package com.ge.predix.solsvc.ext.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EventHubConfig {

	@Value("${predix.eventhub.zone.wss.endpoint:#{null}}")
    private String websocketEndPoint;
    
    @Value("${predix.eventhub.zone.headername:#{null}}")
    private String zoneHeaderName;
    
    @Value("${predix.eventhub.zone.headervalue:#{null}}")
    private String zoneHeaderValue;
    
    @Value("${predix.eventhub.zone.grpc.endpoint:#{null}}")
    private String grpcEndPoint;

    /**
     * Gets the value of the websocketEndPoint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWebsocketEndPoint() {
        return websocketEndPoint;
    }

    /**
     * Sets the value of the websocketEndPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWebsocketEndPoint(String value) {
        this.websocketEndPoint = value;
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

    /**
     * Gets the value of the grpcEndPoint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGRPCEndPoint() {
        return grpcEndPoint;
    }
}
