function Course(n, add, ph, num)
{
	this.address = add;
	this.name=n;
	this.phone=ph;
	this.numberOfHoles=num;
	this.holes = new Array();
	var i;
	for(i = 0; i < this.numberOfHoles; i++)
		{
		this.holes[i]=new Hole(i+1);
		}
}

Course.prototype = {
		setClubhouse: function(loc)
		{
			this.clubhouseLoc = loc;
		},
}

function Green(f,m,b)
{
	this.front = f;
	this.middle = m;
	this.back = b;
}

function Hole(num)
{
	this.number = num;
}
Hole.prototype = {
		setTee: function(loc)
		{
			this.tee=loc;
		},
		addGreen: function(f,m,b)
		{
			this.green = new Green(f,m,b);
		},
		
}