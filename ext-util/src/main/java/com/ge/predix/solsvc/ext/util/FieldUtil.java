/*
 * Copyright (c) 2015 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.ge.predix.solsvc.ext.util;

import java.util.ArrayList;
import java.util.List;

import org.mimosa.osacbmv3_3.DataEvent;

import com.ge.predix.entity.engunit.EngUnit;
import com.ge.predix.entity.field.Field;
import com.ge.predix.entity.field.fieldidentifier.FieldIdentifier;
import com.ge.predix.entity.fielddata.FieldData;
import com.ge.predix.entity.fielddata.OsaData;
import com.ge.predix.entity.fieldidentifiervalue.FieldIdentifierValue;
import com.ge.predix.entity.util.map.DataMap;
import com.ge.predix.entity.util.map.Map;

/**
 * 
 * @author predix -
 */
public class FieldUtil
{

    /**
     * @param fieldId -
     * @param fieldName -
     * @return -
     */
    public static FieldData createFieldData(Object fieldId, String fieldName)
    {
        FieldData fieldData = new FieldData();
        Field field = new Field();
        field.setFieldIdentifier(new FieldIdentifier());
		fieldData.getField().add(field );
		field.getFieldIdentifier().setId(fieldId);
		field.getFieldIdentifier().setName(fieldName);
        return fieldData;
    }

    /**
     * @param fieldIdentifier -
     * @return -
     */
    public static FieldData createFieldData(FieldIdentifier fieldIdentifier)
    {
        FieldData fieldData = new FieldData();
        Field field = new Field();
		fieldData.getField().add(field);
		field.setFieldIdentifier(fieldIdentifier);
        return fieldData;
    }

    /**
     * @param fieldIdentifier -
     * @param resultId -
     * @param engUnit -
     * @param dataEvent -
     * @return -
     */
    public static FieldData createOsaData(FieldIdentifier fieldIdentifier, String resultId, EngUnit engUnit,
            DataEvent dataEvent)
    {

        OsaData osaData = new OsaData();
        osaData.setDataEvent(dataEvent);
        FieldData fieldData = FieldUtil.createFieldData(fieldIdentifier); // TODO handle multiple
        fieldData.setResultId(resultId);
        fieldData.setData(osaData);
        fieldData.setEngUnit(engUnit);
        return fieldData;
    }

    /**
     * @param fieldId - 
     * @param fieldName -
     * @param fieldSource - 
     * @param value  -
     * @return  -
     */
    public static FieldIdentifierValue createFieldIdentifierValue(String fieldId, String fieldName, String fieldSource,
            Object value)
    {
        FieldIdentifierValue fieldIdentifierValue = new FieldIdentifierValue();
        FieldIdentifier fieldIdentifier = new FieldIdentifier();
        fieldIdentifier.setId(fieldId);
        fieldIdentifier.setName(fieldName);
        fieldIdentifier.setSource(fieldSource);
        fieldIdentifierValue.setValue(value);
        fieldIdentifierValue.setFieldIdentifier(fieldIdentifier);
        return fieldIdentifierValue;
    }

	/**
	 * @param json -
	 * @param jsonMapper -
	 * @return -
	 */
	@SuppressWarnings("unchecked")
	public static DataMap toDataMap(String json, JsonMapper jsonMapper) {
		java.util.Map<?,?> linkedMap = (java.util.Map<?,?>) jsonMapper.fromJson(json, Object.class);
		DataMap data = new DataMap();
		List<com.ge.predix.entity.util.map.Map> list = new ArrayList<com.ge.predix.entity.util.map.Map>();
		Map map = new Map();
		map.putAll(linkedMap);
		list.add(map);
		data.setMap(list);
		return data;
	}

}
