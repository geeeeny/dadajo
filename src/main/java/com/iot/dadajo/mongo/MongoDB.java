package com.iot.dadajo.mongo;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.iot.dadajo.model.Window;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class MongoDB {
	// String MongoDB_IP = "192.168.219.102";
	String MongoDB_IP = "70.12.112.61";
	int MongoDB_PORT = 27017;
	String DB_NAME = "dadajo";
	MongoClient mongoClient;
	DB db;
	DBCollection collection;

	public MongoDB() {
		// Connect to MongoDB
		mongoClient = new MongoClient(new ServerAddress(MongoDB_IP, MongoDB_PORT));
		db = mongoClient.getDB(DB_NAME);
		collection = db.getCollection("window");
	}

	// 초기 창문 상태 받아오기
	public Window getWindowState() {
		collection = db.getCollection("window");
		DBCursor dbCursor = collection.find().sort(new BasicDBObject("date", -1)).limit(1);
		DBObject dbObject = dbCursor.next();

		String stateStr = dbObject.get("state").toString();
		boolean state = (stateStr.equals("true"))?true:false;
		String date = dbObject.get("date").toString();
		
		Window window = new Window(state, date);


		return window;
	}
	
	// 데이터 삽입 메소드
	public void insert(String location, String sensorName, String value, String timeStamp) {
		collection = db.getCollection(location);

		BasicDBObject document = new BasicDBObject();

		document.put("date", timeStamp.toString());
		document.put(sensorName, value);

		collection.insert(document);
	}
	
	public void insert(String location, boolean state, String timeStamp) {
		collection = db.getCollection(location);

		BasicDBObject document = new BasicDBObject();
		
		document.put("state", state);
		document.put("date", timeStamp.toString());
		
		collection.insert(document);
	}
}