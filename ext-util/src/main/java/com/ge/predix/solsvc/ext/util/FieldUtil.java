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

import org.mimosa.osacbmv3_3.DataEvent;

import com.ge.dsp.pm.ext.entity.engunit.EngUnit;
import com.ge.dsp.pm.ext.entity.field.Field;
import com.ge.dsp.pm.ext.entity.field.fieldidentifier.FieldIdentifier;
import com.ge.dsp.pm.ext.entity.fielddata.FieldData;
import com.ge.dsp.pm.ext.entity.fielddata.OsaData;
import com.ge.dsp.pm.ext.entity.fieldidentifiervalue.FieldIdentifierValue;

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
        fieldData.setField(new Field());
        fieldData.getField().setFieldIdentifier(new FieldIdentifier());
        fieldData.getField().getFieldIdentifier().setId(fieldId);
        fieldData.getField().getFieldIdentifier().setName(fieldName);
        return fieldData;
    }

    /**
     * @param fieldIdentifier -
     * @return -
     */
    public static FieldData createFieldData(FieldIdentifier fieldIdentifier)
    {
        FieldData fieldData = new FieldData();
        fieldData.setField(new Field());
        fieldData.getField().setFieldIdentifier(fieldIdentifier);
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

}
