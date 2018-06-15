/*package com.iot.dadajo;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.iot.dadajo.model.Window;
import com.iot.dadajo.mongo.MongoDB;
import com.iot.dadajo.mqtt.SimpleMqttCallBack;

*//**
 * Handles requests for the application home page.
 *//*
@Controller
public class HomeController {
	
	//private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Autowired
	MongoDB mongoDB;
	@Autowired
	Window window;	//현재 창문 상태를 저장할 객체
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) throws MqttException {
		Mqtt Client 준비
		System.out.println("== START SUBSCRIBER ==");
		
		MqttClient client = new MqttClient("tcp://localhost:1883", 
									MqttClient.generateClientId());
		client.setCallback(new SimpleMqttCallBack());
		client.connect();
		
		client.subscribe("home/in/#"); //지정한 토픽으로 구독자 등록
		client.subscribe("home/out/#");
		
				
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
}
*/