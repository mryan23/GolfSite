var CLUBHOUSE = 0;
var HOLES = 1;
var END = 2;
var currentState;

var instructions;
var obstacle;
var map;
var course;
var markers;
function initStateMachine(m,c)
{
	markers = new Array();
	course = c;
	currentState = CLUBHOUSE;
	map = m;
	var marker = new google.maps.Marker({
			position: map.getCenter(),
			draggable: true,
			map: map,
			icon: 'images/flag.png'
      });
	markers.push(marker);
	instructions = document.getElementById("instructions");
	instructions.innerHTML="Place the icon on the clubhouse.";
}
function updateState()
{
	switch(currentState)
	{
	case CLUBHOUSE:
		course.setClubhouse(markers[0].getPosition());
		clearAllMarkers();
		currentState = HOLES;
		updateHoleState();
		//alert(JSON.stringify(course));
		break;
	case HOLES:
		updateHoleState();
		break;
	case END:
		alert("WRITE TO SQL");
		break;
	}
}
var TEE = 0;
var GREEN_CENTER = 1;
var GREEN_EDGES = 2;
var OTHER = 3;
var holeState=-1;
var currentHole = 1;
function updateHoleState()
{
	
	switch(holeState)
	{
	case TEE:
		course.holes[currentHole-1].setTee(markers[0].getPosition());
		
		clearAllMarkers();
		holeState=GREEN_CENTER;
		goToGreenCenter(currentHole);
		break;
	case GREEN_CENTER:
		map.setCenter(markers[1].getPosition());
		map.setZoom(20);
		goToGreenEdges(currentHole);
		holeState=GREEN_EDGES;
		break;
	case GREEN_EDGES:
		course.holes[currentHole-1].addGreen(markers[0].getPosition(),markers[1].getPosition(),markers[2].getPosition);
		clearAllMarkers();
		holeState=OTHER;
		//alert(JSON.stringify(course));
		goToOther();
		break;
	case OTHER:
		
		break;
	default:
		holeState=TEE;
		addTable();
		goToTee(currentHole);
		break;
	}
	
}


function addTable(){
	obstacle = new ObstacleTable(document.getElementById("obstacleTableDiv"),["Name","Feature"," "," "]);
	var rowArray = new Array();
	var dummyDiv = document.createElement("div");
	dummyDiv.innerHTML="Tee";
	rowArray.push(dummyDiv);
	var dummyDiv2 = document.createElement("div");
	dummyDiv2.innerHTML="Tee Box";
	rowArray.push(dummyDiv2);
	var viewButton = document.createElement("button");
	viewButton.innerHTML="View";
	rowArray.push(viewButton);
	var removeButton = document.createElement("button");
	removeButton.innerHTML = "Remove";
	removeButton.disabled="true";
	rowArray.push(removeButton);
	console.log(rowArray);
	obstacle.addRow(rowArray);
	
	
	var addDiv = document.getElementById("addToTableDiv");
	var nameField = document.createElement("input");
	nameField.type="text";
	nameField.size=12;
	addDiv.appendChild(nameField);
	var dropDown = document.createElement("select");
	var options = ["Bunker","Water Hazard", "Tree"];
	var i;
	for(i = 0; i < options.length; i++){
		var option = document.createElement("option");
		option.innerHTML=options[i];
		dropDown.appendChild(option);
	}
	addDiv.appendChild(dropDown);
	var button = document.createElement("button");
	button.innerHTML="Add";
	addDiv.appendChild(button);
}

function goToTee(num)
{
	instructions.innerHTML="Please place the tee marker on tee number "+num+".";
	var marker = new google.maps.Marker({
		position: map.getCenter(),
		draggable: true,
		map: map,
		icon: 'images/teeBox.png'
	});
	markers.push(marker);
}

function goToGreenCenter(num)
{
	instructions.innerHTML="Please place the marker on center of green number "+num+".";
	var marker = new google.maps.Marker({
		position: map.getCenter(),
		draggable: true,
		map: map,
		icon: 'images/greenCenter.png'
	});
	markers[1]=marker;
}
var latLngOffset = .0001;
function goToGreenEdges(num)
{
	instructions.innerHTML="PLEASE place the markers on green number "+num+".<br>Red---Front<br>White---Center<br>Blue---Back";
	var centerPos = markers[1].getPosition();
	var frontMarker = new google.maps.Marker({
		position: new google.maps.LatLng(centerPos.lat()-latLngOffset, centerPos.lng()-latLngOffset),
		draggable: true,
		map: map,
		icon: 'images/greenFront.png'
	});
	markers[0]=frontMarker;
	
	var backMarker = new google.maps.Marker({
		position: new google.maps.LatLng(centerPos.lat()+latLngOffset, centerPos.lng()+latLngOffset),
		draggable: true,
		map: map,
		icon: 'images/greenBack.png'
	});
	markers[2]=backMarker;
}

function goToOther(num)
{
	//TEMPORARY
	if(currentHole < course.numberOfHoles)
		{
		currentHole++;
		holeState=-1;
		updateHoleState();
		}
	else
		{
		currentState = END;
		updateState();
		}
}

function clearAllMarkers()
{
	var i; 
	for(i = 0; i < markers.length; i++)
		{
		markers[i].setMap(null);
		}
	markers = new Array();
}