package com.miragesql.miragesql.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

public class MapPropertyDescImpl implements PropertyDesc {

	private Object value;
	private String propertyName;

	public MapPropertyDescImpl(String propertyName, Object value){
		this.propertyName = propertyName;
		this.value = value;
	}

//	@Override
	public <T extends Annotation> T getAnnotation(Class<T> type) {
		return null;
	}

//	@Override
	public Field getField() {
		return null;
	}

//	@Override
	public String getPropertyName() {
		return propertyName;
	}

//	@Override
	public Class<?> getPropertyType() {
		return (null == value) ? null : value.getClass();
	}

//	@Override
//	@SuppressWarnings("unchecked")
	public Object getValue(Object entity) {
		return Map.class.cast(entity).get(propertyName);
	}

//	@Override
	public boolean isReadable() {
		return true;
	}

//	@Override
	public boolean isWritable() {
		return true;
	}

//	@Override
	@SuppressWarnings("unchecked")
	public void setValue(Object entity, Object value) {
		((Map<Object, Object>) entity).put(propertyName, value);
	}

//	@Override
	public boolean isTransient() {
		return false;
	}

	@Override
	public String toString() {
		return "MapPropertyDescImpl [value=" + value + ", propertyName=" + propertyName + "]";
	}
}
