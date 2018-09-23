
$(document).ready(function() {
	carregaHtml("conteudo", "", "dashboard.html", "", function(){inicializarDash()});
} );

function inicializarDash(){
	preecherTransacoes();
	preencherGrafico();
}
