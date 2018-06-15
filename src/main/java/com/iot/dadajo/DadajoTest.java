package com.iot.dadajo;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;

import com.iot.dadajo.model.Window;
import com.iot.dadajo.mongo.MongoDB;
import com.iot.dadajo.mqtt.SimpleMqttCallBack;

public class DadajoTest {
	static MongoDB mongoDB = new MongoDB();
	@Autowired
	static Window window = new Window(); //현재 창문 상태를 저장할 객체
	
	public static void main(String[] args) throws MqttException {
		/*Mqtt Client 준비*/
		System.out.println("== START SUBSCRIBER ==");
		
		/*MqttClient client = new MqttClient("tcp://70.12.112.61:1883", 
									MqttClient.generateClientId());*/
		MqttClient client = new MqttClient("tcp://70.12.112.61:1883", 
				MqttClient.generateClientId());
		client.setCallback(new SimpleMqttCallBack(client));
		client.connect();
		
		client.subscribe("home/in/#"); //지정한 토픽으로 구독자 등록
		client.subscribe("home/out/#");
		
		window = mongoDB.getWindowState();
		System.out.println(window.isState());
	}

}
