package com.ddd.what2eat.utils;

import org.modelmapper.ModelMapper;

public class ModelMapperUtils {

	private static ModelMapper modelMapper = new ModelMapper();

	public static ModelMapper getModelMapper() {
		return modelMapper;
	}
}
