"# Projeto-Praiou - Back-End" 
O Praiou é uma plataforma de rede social e organização de eventos voltada exclusivamente para entusiastas de esportes de praia. 
Localizado geograficamente na Baixada Santista, o projeto visa resolver a fragmentação de grupos de atletas locais, facilitando
o encontro de pessoas com interesses em comum para a prática de esportiva e o lazer regional.

--

## Funcionalidades Planejadas 

● Gestão de Eventos e Comunidade: O sistema permite que qualquer usuário atue como um organizador, criando eventos esportivos (como
partidas de Beach Tennis ou Futevôlei), enquanto outros usuários daregião podem se inscrever como participantes através de um modelo
de agregação.

● Rede Social Ativa: A plataforma funciona como uma rede social completa, onde usuários podem estabelecer laços de amizade (com
controle de status pendente/aceito), realizar postagens de suas atividades e interagir através de curtidas e comentários.

● Sistema de Gamificação e Feedback: Para garantir uma comunidade saudável e competitiva, o Praiou conta com um sistema de avaliações.
Os atletas podem avaliar uns aos outros após as partidas, gerando uma pontuação de reputação que ajuda na filtragem de grupos e na
segurança da comunidade local.

● Foco Regional: Diferente de grandes redes genéricas, o Praiou utiliza a proximidade da Baixada Santista como motor de engajamento,
transformando conexões digitais em encontros físicos nas praias da região.

● Usuário: Um usuário pode controlar seus dados de usuário; gerenciar Eventos; realizar Avaliações de outros usuários; criar Postagens na comunidade de atletas;
enviar Comentários nas postagens citadas; e possuir uma rede de Amizades. Um usuário não pode alterar dados e controlar eventos e postagens de outros usuários.

## Como executar o Back-end (API)

Esta API foi desenvolvida em **Java** utilizando o framework **Javalin** para as rotas HTTP e **JDBC** para comunicação direta com o banco de dados MySQL.

-- Pré-requisitos

Antes de começar, você vai precisar ter instalado em sua máquina:
* **Java JDK 17** (ou superior)
* **MySQL Server** (rodando localmente ou em container)
* Uma IDE de sua preferência (VS Code, IntelliJ IDEA ou Eclipse) com suporte a projetos Maven.

--- Psso a Passo para Execução 

Se você estiver utilizando uma IDE como VS Code:

Abra a pasta raiz do projeto back-end.
Aguarde a IDE sincronizar e baixar as dependências do projeto (Javalin, Jackson, MySQL Driver, etc.).
Localize a classe principal que contém o método main:
br.gov.sp.fatec.pg.praiou.Main (ou o caminho exato da sua classe com o Javalin).
Clique com o botão direito sobre ela e selecione Run ou Executar.

## Integrantes
Alan de Oliveira Souza <br>
Fellipe de Deus Bairos Cerqueira <br>
Emanuel de Brito <br>
Cauê Mayo Simões

*Projeto desenvolvido para a disciplina de Laboratório de Engenharia de Software*
