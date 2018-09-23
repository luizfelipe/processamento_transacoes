# Desafio Hackathon 2018 - Equipe Code Surfers
# Projeto xpto-processamento_transacoes


## Introdução

Este projeto foi desenvolvido com a intenção de atender as especificações previstas na página do <a href="https://hackathon.dtplabs.in/snippets/1">Desafio - Hackathon 2018</a>, que, em síntese, dizem respeito ao seguinte:

    1. Download e processamento de um arquivo texto com registros financeiros, de acordo com regras pré-estabelecidas, a fim de validar os registros constantes neste arquivo.

    2. Persistência dos registros de transações processadas em base de dados e gravação de registros inválidados em log.

    3. Disponibilização de API para consulta à transações processadas e válidadas.

Além de obedecer as especificações anteriores, este projeto contemplou o desenvolvimento de um componente com interface gráfica web para consumir e exibir dados das transações processadas disponibilizadas pela API.


## Visão Geral da Arquitetura

![Diagrama Archimate](docs/visao_geral_arquitetura.png)

Como se pode observar no diagrama apresentado acima, a solução ```xpto-processamento_transacoes```, é composta por 4 componentes principais:

- ```xpto_processamento_transacoes-batch```:

- ```xpto_processamento_transacoes-domain```:

- ```xpto_processamento_transacoes-ws```:




## Tecnologias Utilizadas

> Spring Batch

> Spring Boot

> Restful

> HTML5

> Javascript

> Gentelella Theme
 
> Bower

> Apache Launcher

> PostgreSQL 10.5



## Instruções de Desenvolvimento

> Listar os procedimentos necessários para configuração do ambiente de desenvolvimento e ferramentas de programação


## Procedimentos para Deploy e Implantação

> Listar os procedimentos necessários para deploy e implantação do produto em ambiente produtivo 


## Facilidade para Auditoria

Este projeto contém um módulo web para realizar consultas. Construído em cima do framework Gentelella.

Construímos um painel (Dashboard) interativo que apresenta um monitor de situação de acordo com as classificações das transações, separando por suas regras de validação.

Um gráfico dinâmico que apresenta a distribuição de transações ao longo do ano.

![Painel](docs/tela_1.png)
    
Para facilitar a auditoria criamos duas telas que apresentam a listagem de transações que foram realizadas com sucesso e as que foram rejeitas.

![Painel](docs/tela_2.png)

![Painel](docs/tela_3.png)