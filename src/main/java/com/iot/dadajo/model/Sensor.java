package com.iot.dadajo.model;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sensor {
	private float temp;
	private float humid;
	private float dust;
	private float rain;
}
