
function ObstacleTable(container, columns){
	this.table = document.createElement("table");
	this.columnNames = columns;
	this.table.id="obstacleTable";
	this.table.style.border="solid";
	this.table.width="100%";
	this.table.style.borderCollapse="collapse";
	this.table.cellSpacing=0;
	container.appendChild(this.table);
	var row = document.createElement("tr");
	row.style.paddingBottom=0;
	//row.style.border="solid";
	var i;
	for(i = 0; i < columns.length; i++){
		var col = document.createElement("td");
		//col.style.bordid="teeContainer"er="solid";
		col.innerHTML = columns[i];
		if(i > 1){
			col.width="50px";
		}
		row.appendChild(col);
	}
	this.table.appendChild(row);
}

ObstacleTable.prototype = {
		addRow: function(columns){
			console.log(columns);
			var row = document.createElement("tr");
			row.style.border="solid";
			row.style.borderWidth="1px";
			var i;
			for(i = 0; i < columns.length; i++){
				
				var col = document.createElement("td");
				
				console.log(i,columns[i]);
				col.appendChild(columns[i]);
				row.appendChild(col);
			}
			this.table.appendChild(row);
		},
		removeRow: function(rowNum){
			var rows = this.table.getElementsByTagName("tr");
			console.log(this.table);
			console.log(rows[rowNum]);
			this.table.removeChild(rows[rowNum]);
		}
}