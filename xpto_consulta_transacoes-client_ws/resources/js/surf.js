$(document).ready(function() {
	carregarDashbaord();
} );

function preecherTransacoes(){
	$.getJSON("http://localhost/ws/transacoes/dados", function(result){
		$('#transacoes_ok').html(result.semErro);
		$('#transacoes_regra1').html(result.regra1);
		$('#transacoes_regra2').html(result.regra2);
		$('#transacoes_regra3').html(result.regra3);
		$('#transacoes_regra4').html(result.regra4);

		for(let i = 1; i <= 4; i++){
			if( $('#transacoes_regra'+i).html() > 0 ){
				 $('#transacoes_regra'+i).addClass('red');
			}
		}

	});
}


function preencherGrafico(){
	$.getJSON("http://localhost/ws/transacoes/mensais", function(result){
       console.log(result.length);
       console.log(result);
       new Chart(document.getElementById("grafico"), {
		    type: 'bar',
		    data: {
		      labels:["Janeiro","Fevereiro","Março","Abril", "Maio","Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"],
		      datasets: [
		        {
		          label: "Transações Mensais",
		          backgroundColor: ["#3e95cd", "#8e5ea2","#1ABB9C", "#2A3F54","#3e95cd", "#8e5ea2","#1ABB9C", "#2A3F54","#3e95cd", "#8e5ea2","#1ABB9C", "#2A3F54"],
		          data:[result[0],result[1], result[2], result[3],result[4], result[5], result[6], result[7], result[8], result[9], result[10], result[11]]
		        }
		      ]
		    },
		    options: {
		      legend: { display: false },
		      title: {
		        display: true,
		        text: 'Transações de aceitas 2017'
		      }
		    }
		});
    });
}



function carregaHtml(idPainel, caminho, arquivo, parametros, callback) {

        var url  =  caminho +  arquivo +"?"+ parametros+ "&rnd=" + Math.random();

		$.ajax({
            url: url,
            contentType: 'Content-type: text/plain; charset=ISO-8859-1',
            beforeSend: function(jqXHR) {
                jqXHR.overrideMimeType('text/html;charset=ISO-8859-1');
            },
            success: function(data){
                $( "#conteudo" ).empty();
                $("#conteudo").append( data );

                if(callback){
                    callback(parametros);
                }
            }
        });

}

function carregarLista(validas){
	carregaHtml("conteudo", "", "lista.html", "", function(){inicializarDataTable(validas)});
}
function carregarDashbaord(){
	carregaHtml("conteudo", "", "dashboard.html", "", function(){inicializarDash()});
}

function inicializarDash(){
	preecherTransacoes();
	preencherGrafico();
}

function inicializarDataTable(validas){
	 $('#tbl_dados').DataTable( {
        "processing": true,
        "serverSide": true,
        "ordering": false ,
        "searching": false,
        "lengthChange": false,
        "pageLength": 25,
        "ajax": "http://localhost/ws/transacoes/datatablesFormat?validas="+validas,
        "columnDefs": [ {
	        "targets": [ 6 ],
	        "visible": !validas
	     } ]
    });

	if( validas ){
		 $('#trans_title').addClass('green');
		 $('#trans_title').html('Válidas');
	}else{
		 $('#trans_title').addClass('red');
		 $('#trans_title').html('Inválidas');
	}
}






