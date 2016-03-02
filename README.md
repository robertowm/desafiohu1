[![Build Status](https://travis-ci.org/robertowm/desafiohu1.svg?branch=master)](https://travis-ci.org/robertowm/desafiohu1) [![Coverage Status](https://coveralls.io/repos/github/robertowm/desafiohu1/badge.svg?branch=master)](https://coveralls.io/github/robertowm/desafiohu1?branch=master)

# Desafio de auto-complete e busca disponibilidade

Neste problema você deve implementar o widget de busca de hoteis. Este desenvolvimento engloba o auto-complete de hoteis e a busca por disponibilidades quando o usuário informa um periodo de estadia. 

A interface em anexo precisa ser implementada assim como o backend para consumir a lista de hoteis e as disponibilidades. Tudo será avaliado. Faça o seu melhor na linguagem onde vc possui o maior domínio.

***Restrições***
* Eu preciso conseguir rodar seu código no mac os x OU no ubuntu;
* Eu vou executar seu código com os seguintes comandos:

>1. *git clone seu-fork*
2. *cd seu-fork*
3. *./sbt assembly*
4. *cd target/scala-2.11*
5. *java -jar desafiohu1.jar*

Também é possível executar através do comando *./sbt run*, sendo que executará toda a aplicação pelo SBT.

Esses comandos tem que ser o suficiente para configurar meu mac os x OU ubuntu e rodar seu programa. Pode considerar que eu tenho instalado no meu sistema Python, Java, PHP, Ruby e/ou Node. Qualquer outra dependência que eu precisar vc tem que prover.

***Performance***
* Preciso que os seus serviços suportem um volume de 1000 requisições por segundo

***Artefatos***
* Imagens e database de hoteis e disponibilidades estão na pasta arquivos

## Detalhes da implementação

Foi desenvolvido em Scala com Finatra, com foco em desempenho.  Para realizar as buscas, os dados são carregados dos arquivos .txt diretamente índices na memória RAM.  Assim, o foco é em desempenho e com grande capacidade para aprimorar as buscas.

Para o desenvolvimento da UI, foi utilizado jQuery e Bootstrap.  O jQuery é utilizado para acessar o serviço REST de autocomplete e busca.  O Bootstrap para fazer o layout de forma rápida e responsiva.

Para os testes, foi utilizada a própria infraestrutura fornecida pelo Finatra, que é baseada no Scalatest.

Para automação do build, foi utilizado o SBT.  A fim de agilizar o uso do projeto, foi adicionado o SBT no próprio repositório, que é acessável pelo script *sbt*.  Assim, o único requisito é ter o Java 8 instalado no computador.

Os detalhes para compilação e execução do projeto estão detalhados anteriormente.

Também está integrado com o Travis-CI para acompanhar a cobertura do projeto.
