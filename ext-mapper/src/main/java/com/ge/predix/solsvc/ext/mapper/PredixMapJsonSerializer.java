package com.ge.predix.solsvc.ext.mapper;
/*
 * Copyright (c) 2016 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */



import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.type.TypeBindings;

/**
 * Jackson does not currently support JsonTypeInfo annotated objects in a Map.  This fixes that.
 * @author predix -
 */
public class PredixMapJsonSerializer extends JsonSerializer<Map<?, ?>> implements ContextualSerializer {

	private PredixMapSerializer mapSerializer =null;
	
	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)
	 */
	@Override
	public void serialize(Map<?,?> value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		if ( this.mapSerializer == null)
			this.mapSerializer = new PredixMapSerializer(null, TypeBindings.UNBOUND, TypeBindings.UNBOUND, false, null, null, null);

		this.mapSerializer.serialize(value, jgen, provider);
		
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ser.ContextualSerializer#createContextual(com.fasterxml.jackson.databind.SerializerProvider, com.fasterxml.jackson.databind.BeanProperty)
	 */
	@Override
	public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
			throws JsonMappingException {
		if ( this.mapSerializer == null)
			this.mapSerializer = new PredixMapSerializer(null, TypeBindings.UNBOUND, TypeBindings.UNBOUND, false, null, null, null);
		MapSerializer ms = (MapSerializer) this.mapSerializer.createContextual(prov, property);
		
		this.mapSerializer = new PredixMapSerializer(ms, property, ms.getKeySerializer(), ms.getContentSerializer(), null);

		return this.mapSerializer;
	}

}
