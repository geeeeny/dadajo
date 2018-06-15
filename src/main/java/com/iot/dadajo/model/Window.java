package com.iot.dadajo.model;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Window {
	private boolean state;	//창문 상태(1:열림, 0:닫힘)
	private String date;	//업데이트된 시간
}
