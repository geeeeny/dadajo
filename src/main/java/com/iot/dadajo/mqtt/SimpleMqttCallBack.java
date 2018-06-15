package com.iot.dadajo.mqtt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;

import com.iot.dadajo.DadajoTest;
import com.iot.dadajo.model.Sensor;
import com.iot.dadajo.model.SensorIn;
import com.iot.dadajo.model.SensorOut;
import com.iot.dadajo.model.Window;
import com.iot.dadajo.mongo.MongoDB;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class SimpleMqttCallBack implements MqttCallback {

	MongoDB mongoDB = new MongoDB();
	SensorIn sensorIn = new SensorIn(); 	// 현재 실내 상태를 저장할 객체
	SensorOut sensorOut = new SensorOut();  // 현재 실외 상태를 저장할 객체
	@Autowired
	static Window window; //window = new Window();
	
	MqttClient client;
	
	public SimpleMqttCallBack(MqttClient client) {
		super();
		this.client = client;
	}

	// 연결 끊겼을 때 실행
	public void connectionLost(Throwable throwable) {
		System.out.println("Connection to MQTT broker lost!");
	}

	// 토픽이 수신되었을 때
	public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
		final String location = topic.substring(topic.indexOf("/") + 1, topic.lastIndexOf("/")); // 센서의 위치(in 또는 out)
		final String sensorName = topic.substring(topic.lastIndexOf("/") + 1); // 센서명(temp/humid/dust/rain)
		final String value = new String(mqttMessage.getPayload()); // 센서값
		System.out.println("lsv:\t" + location + ")" + sensorName + ": " + value);

		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

		// 현재 상태 업데이트
		updateSensor(location, sensorName, value);

		// 현재 상태 비교하여 창문 상태 설정하고, 창문 개폐를 위한 토픽 발행
		setWindow();

		// mongoDB에 insert
		mongoDB.insert(location, sensorName, value, timeStamp);

	}

	// QoS를 위한 메서드
	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

	}

	public void updateSensor(String location, String sensorName, String value) {
		Sensor sensor = null;
		// 자바 객체에 현재 상태 저장
		if (location.equals("in")) {
			sensor = sensorIn;
		} else if (location.equals("out")) {
			sensor = sensorOut;
		}
		switch (sensorName) {
		case "temp":
			sensor.setTemp(Float.parseFloat(value));
			break;
		case "humid":
			sensor.setHumid(Float.parseFloat(value));
			break;
		case "dust":
			sensor.setDust(Float.parseFloat(value));
			break;
		case "rain":
			sensor.setRain(Float.parseFloat(value));
		default:
			break;
		}

		System.out.println("sensorIn: " + sensorIn);
		System.out.println("sensorOut: " + sensorOut);
	}
	
	public void setWindow(){
		float dustIn = sensorIn.getDust();
		float dustOut = sensorOut.getDust();
		
        boolean state; // 변화될 창문 상태
        
        if(dustIn > dustOut){
            state = true;
        }else{
            state = false;
        }

        // 현재 창문 상태와 다를 경우에만 토픽 발행
        if(state != window.isState()){
            try {
                MqttMessage message = new MqttMessage();
                message.setPayload((state+"").getBytes());
                client.publish("home/control/window", message);
                
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                window.setState(state);
                window.setDate(timeStamp);

                // mongoDB에 insert
                mongoDB.insert("window", window.isState(), timeStamp);
                
                System.out.println(window);
            } catch (MqttException e) {
                e.printStackTrace();
            }
            
        }

    }
}
