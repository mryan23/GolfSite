/**
 * @author Ryan
 */

function TeeTable(c){
	this.container = c;
	this.table = document.createElement("table");
	this.table.className="table table-bordered";
	var header = this.table.createTHead();
	var headRow = header.insertRow(0);
	this.columnNames=["Tee","M/F","Slope","Rating",""];
	var i;
	for( i = this.columnNames.length - 1; i >=0; i--){
		var cell = headRow.insertCell(0);
		cell.innerHTML=this.columnNames[i];
	}
	this.tbody = document.createElement("tbody");
	this.table.appendChild(this.tbody);
	this.container.appendChild(this.table);
	this.addRow();
	console.log(this.table.rows[1]);
}

TeeTable.prototype = {
		addRow: function(){
			var rowNum = this.tbody.rows.length;
			var row = this.tbody.insertRow(rowNum);
			var teeCell = row.insertCell(0);
			var teeInput = document.createElement("input");
			teeInput.id="tee"+rowNum+"input";
			teeInput.className="input-small";
			teeCell.appendChild(teeInput);
			
			var genderCell = row.insertCell(1);
			var genderInput = document.createElement("select");
			genderInput.id="gender"+rowNum+"input";
			genderInput.className="input-small";
			var selects = ["","M","F"];
			var i;
			for( i = 0;i < selects.length; i++){
				var option = document.createElement("option");
				option.value=selects[i];
				option.innerHTML=selects[i];
				genderInput.appendChild(option);
			}
			genderCell.appendChild(genderInput);
			
			var slopeCell = row.insertCell(2);
			var slopeInput = document.createElement("input");
			slopeInput.id="slope"+rowNum+"input";
			slopeInput.className="input-small";
			slopeCell.appendChild(slopeInput);
			
			var ratingCell = row.insertCell(3);
			var ratingInput = document.createElement("input");
			ratingInput.id="rating"+rowNum+"input";
			ratingInput.className="input-small";
			ratingCell.appendChild(ratingInput);
			
			var removeCell = row.insertCell(4);
			var removeButton = document.createElement("button");
			removeButton.id="remove"+rowNum+"button";
			removeButton.className="btn";
			removeButton.innerHTML="Remove";
			removeButton.type="button";
			//removeButton.setAttribute('onclick',"this.tbody.removeRow(rowNum)");
			removeButton.setAttribute("onclick","teeTable.removeRow(this)");
			removeCell.appendChild(removeButton);
		},
		removeRow: function(button){
			this.tbody.deleteRow(parseInt(button.id[6]));
			if(this.tbody.rows.length == 0)
				this.addRow();
		},
		getTees: function(){
			var result = new Array;
			var i;
			console.log(this.tbody.rows.length);
			for(i = 0; i < this.tbody.rows.length; i++){
				var row = this.tbody.rows[i];
				var cells = row.cells;
				var j;
				//ignore buttons
				/*for( j = 0; j < cells.length-1; j++){
					var cell = cells[j];
					var useful = cell.childNodes[0];
					console.log(useful.value);
				}*/
				console.log(cells[0].childNodes[0].value);
				var tee={
					name:cells[0].childNodes[0].value,
					gender:cells[1].childNodes[0].value,
					slope: cells[2].childNodes[0].value,
					rating: cells[3].childNodes[0].value
				};
				result.push(tee);
				
			}
			console.log(result);
			return result;
		}
}
