

var light1;

function convertObjToArr(obj){
	var arr = [];
	for(var prop in obj){
		arr.push(obj[prop]);
	}
	return arr;
}




$.fn.deviceGroup = function(deviceGroup){
	var self = this;
	self.find('.group-name').text(deviceGroup.groupName);
	
	convertObjToArr(deviceGroup.group) // turn object into array.
	.sort((a, b) => a.name > b.name ? 1 : // array the lights.
		a.name < b.name ? -1 : 0)
	.forEach(function(device){
		var obj = $('<div class = "col-3"></div>');
		switch(device.type){
		case '조도' : 
			obj.lightSensor(device); // 조도일 때 lightSensor 조명 일 때 light 를 쓰겠다는
										// 것.
			break;
		case 'LIGHT' : 
			obj.light(device);
			break;
		}
		self.append(obj);
	})
	return self;
}

var templLightSensor = {
		rgb : function(value){
			value = parseInt(Math.ceil(value/20)*20);
			var v = parseInt((225*value/100)).toString(16); // only 5 colors,
															// and if it's
															// over80 set it as
															// 100.
			return '#' + v + v + v ;
		},
		templ : function(dev){
			light1 = this.rgb(dev.value);
			var bright = this.rgb(dev.value);
			console.log(dev.value, bright)
			return $(`
			<div class = "center m-1" style = "background:lightgray">
			<div class = "text-center pt-3">
				<h3 class = "mb-2">
					<i class = "fas fa-sun" style = "color : ${bright}"></i>
					<sup><small>${dev.value}</small></sup>
				</h3>
			</div>
			<div class = "text-center">${dev.name}</div>
			</div>`
		);			
	}		
};



$.fn.lightSensor = function(dev){
	var self = this;
	self.append(templLightSensor.templ(dev));
	
	return self;
}


var templLight = {
	

		
		templ : function(dev) {
			
			var bright = dev.value;
			switch(bright){
			//if 문 써서 on off를 결정하고, 출력.
		/*	case dev.value : 
				bright = "yellow"; // 조도일 때 lightSensor 조명 일 때 light 를 쓰겠다는
											// 것.
				break;
				
			case light1 => dev.value : 
				bright = "yellow";
				break;
		*/	}
			
			var status = parseInt(dev.value) ?
					`<i style = "color:${bright}" class = "fas fa-lightbulb"></i>` :
					`<i class = "fas fa-lightbulb"></i>` 
						
						
		return $(`		
				<div class= "center m-1" style = "background:lightgray">
					<div class = "text-center pt-3"> <h3 class = "mb-2">${status}</h3>
					</div>
					<div class= "text-center"> ${dev.name}</div>
				</div>`	);	
					
				
	}
}



$.fn.light = function(dev){
	var self = this;
	self.append(templLight.templ(dev));
	return self;
}




$.fn.location = function(location){
	var self = this;
	
	self.find('.location').text(location.location);
	
	for(var groupName in location.deviceGroup){
		var group = location.deviceGroup[groupName];
		// device plugin 적용
		var groupObj = $(templDeviceGroup).deviceGroup(group);
		console.log(groupObj)
		self.find('.device-groups').append(groupObj);
	}

		
	return this;
}
	

var templDeviceGroup = 
	`<div class = "row m-1">
		<div class = "col-2 group-name text-right"></div>
		<div class = "col-10">
			<div class = "row devices"></div>
		</div>
	</div>`;




var templLocation = 
	`<div class= "card col-6 mb-2">
			<div class = "card-header">
				<h4 class = "card-title">
				<a class = "location"></a>
				</h4>
			</div>
			<div class = "card-body p-1 device-groups">
			</div>
	</div>`;

$.fn.monitoring = function(){
	var self = this;
	
	
	var socket = new SockJS("dashboard");
	socket.onopen = ()=>console.log('접속 성공');
	socket.onclose= ()=>console.log('접속 해제');
	socket.onerror= err=>console.log('에러', err);
	
	socket.onmessage = function(msg){
		console.log('monitoring', msg.data)
		var locations = JSON.parse(msg.data);
		self.empty();
		for(var loc in locations){
			var location = $(templLocation).location(locations[loc]); 
			console.log(self, location)
			self.append(location);
		}
	}
	return this;
}


	


