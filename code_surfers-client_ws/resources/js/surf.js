$(document).ready(function() {
    /*$('#example').DataTable( {
        "processing": true,
        "serverSide": true,
        "ajax": "http://localhost:8090/datatable/"
    } );*/

    if($("#pieChart").length){
		var f=document.getElementById("pieChart"),
		i={ datasets:[
			  {data:[120,50,140,180,100],
			backgroundColor:["#455C73","#9B59B6","#BDC3C7","#26B99A","#3498DB"],
			label:"My dataset"}],
			labels:["Dark Gray","Purple","Gray","Green","Blue"]
	    };

		new Chart(f,{data:i,type:"pie",otpions:{legend:!1}})
	}

  	 

getData();
	
} );


function getData(){
	$.getJSON("http://localhost/ws/dados/", function(result){
       console.log(result);
    });
}









