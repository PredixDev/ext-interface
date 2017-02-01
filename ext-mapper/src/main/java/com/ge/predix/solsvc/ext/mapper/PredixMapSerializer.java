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
import java.util.HashSet;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.impl.TypeWrappedSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

/**
 * Jackson does not currently support JsonTypeInfo annotated objects in a Map.  This fixes that.
 * @author predix -
 */
public class PredixMapSerializer extends MapSerializer {

	/**
	 * @param ignoredEntries
	 *            -
	 * @param keyType
	 *            -
	 * @param valueType
	 *            -
	 * @param valueTypeIsStatic
	 *            -
	 * @param vts
	 *            -
	 * @param keySerializer
	 *            -
	 * @param valueSerializer
	 *            -
	 */
	protected PredixMapSerializer(HashSet<String> ignoredEntries, JavaType keyType, JavaType valueType,
			boolean valueTypeIsStatic, TypeSerializer vts, JsonSerializer<?> keySerializer,
			JsonSerializer<?> valueSerializer) {
		super(ignoredEntries, keyType, valueType, valueTypeIsStatic, vts, keySerializer, valueSerializer);
	}

	/**
	 * @param src
	 *            -
	 * @param property
	 *            -
	 * @param keySerializer
	 *            -
	 * @param valueSerializer
	 *            -
	 * @param ignored
	 *            -
	 */
	public PredixMapSerializer(MapSerializer src, BeanProperty property, JsonSerializer<?> keySerializer,
			JsonSerializer<?> valueSerializer, HashSet<String> ignored) {
		super(src, property, keySerializer, valueSerializer, ignored);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.fasterxml.jackson.databind.ser.std.MapSerializer#serializeFields(java
	 * .util.Map, com.fasterxml.jackson.core.JsonGenerator,
	 * com.fasterxml.jackson.databind.SerializerProvider)
	 */
	@Override
	public void serializeFields(Map<?, ?> value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonGenerationException {
		// If value type needs polymorphic type handling, some more work needed:
		if (this._valueTypeSerializer != null) {
			serializeTypedFields(value, jgen, provider);
			return;
		}
		final JsonSerializer<Object> keySerializer = this._keySerializer;

		final HashSet<String> ignored = this._ignoredEntries;
		final boolean skipNulls = !provider.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES);

		PropertySerializerMap serializers = this._dynamicValueSerializers;

		for (Map.Entry<?, ?> entry : value.entrySet()) {
			Object valueElem = entry.getValue();
			// First, serialize key
			Object keyElem = entry.getKey();
			if (keyElem == null) {
				provider.findNullKeySerializer(this._keyType, this._property).serialize(null, jgen, provider);
			} else {
				// [JACKSON-314] skip entries with null values?
				if (skipNulls && valueElem == null)
					continue;
				// One twist: is entry ignorable? If so, skip
				if (ignored != null && ignored.contains(keyElem))
					continue;
				keySerializer.serialize(keyElem, jgen, provider);
			}

			// And then value
			if (valueElem == null) {
				provider.defaultSerializeNull(jgen);
			} else {
				Class<?> cc = valueElem.getClass();
				JsonSerializer<Object> serializer = serializers.serializerFor(cc);
				if (serializer == null) {
					if (this._valueType.hasGenericTypes()) {
						serializer = _findAndAddDynamic(serializers,
								provider.constructSpecializedType(this._valueType, cc), provider);
					} else {
						serializer = _findAndAddDynamic(serializers, cc, provider);
					}
					serializers = this._dynamicValueSerializers;
				}
				// tturner - added this part
				BeanSerializerFactory instance = BeanSerializerFactory.instance;
				TypeSerializer typeSer = instance.createTypeSerializer(provider.getConfig(),
						provider.getConfig().constructType(valueElem.getClass()));
				if (typeSer != null) {
					// typeSer = typeSer.forProperty(property);
					serializer = new TypeWrappedSerializer(typeSer, serializer);
				}
				// tturner - end added this part

				try {
					serializer.serialize(valueElem, jgen, provider);
				} catch (Exception e) {
					// [JACKSON-55] Need to add reference information
					String keyDesc = "" + keyElem; //$NON-NLS-1$
					wrapAndThrow(provider, e, value, keyDesc);
				}
			}
		}
	}

}
