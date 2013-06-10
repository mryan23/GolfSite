var CLUBHOUSE = 0;
var HOLES = 1;
var OTHER = 2;
var currentState;

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
	
}
function updateState()
{
	switch(currentState)
	{
	case CLUBHOUSE:
		course.setClubhouse(markers[0].getPosition());
		markers[0].setMap(null);
		currentState = HOLES;
		holeState = TEE;
		alert(JSON.stringify(course));
		break;
	case HOLES:
		updateHoleState();
		break;
	case OTHER:
		break;
	}
}
var TEE = 0;
var GREEN_CENTER = 1;
var GREEN_EDGES = 2;
var OTHER = 3;
var holeState;
var currentHole = 1;
function updateHoleState()
{
	
}