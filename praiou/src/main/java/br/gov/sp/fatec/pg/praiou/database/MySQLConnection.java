package br.gov.sp.fatec.pg.praiou.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnection {

    // URL de caminho e acesso ao banco de dados
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DBNAME = "praiou";

    public static void criarBancoDeDados() {
        // Cria/verifica banco de dados

        // Script de criação do banco de dados PRAIOU
        String criarBanco = "CREATE DATABASE IF NOT EXISTS " + DBNAME + ";";

        try {
            Connection c = DriverManager.getConnection(URL, "root", null); 
            Statement stmt = c.createStatement();

            //stmt.execute("DROP DATABASE " + DBNAME);

            stmt.execute(criarBanco);
            System.out.println("Banco de Dados '" + DBNAME + "' criado/verificado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao criar/verificar banco de dados: " + e.getMessage());
        } 

        criarTabelas();

    }

    public static void criarTabelas() {
        // Cria/verifica tabelas e dados do banco de dados

        // CRIAÇÃO DE TABELAS

        // Script de criação da tabela USUARIO
        String tabelaUsuario =
        "CREATE TABLE IF NOT EXISTS USUARIO (" +
            "id_usuario INTEGER AUTO_INCREMENT PRIMARY KEY, " +
            "nm_usuario VARCHAR(100) NOT NULL, " +
            "nm_email_usuario VARCHAR(100) NOT NULL UNIQUE, " +
            "cd_senha VARCHAR(255) NOT NULL, " +
            "dt_nascimento DATE, " +
            "im_perfil TEXT DEFAULT 'user.png', " +
            "ds_bio VARCHAR(255), " +
            "vl_pontuacao INT, " +
            "ic_status_administrador TINYINT(1) NOT NULL DEFAULT 0, " +
            "cd_token VARCHAR(36), " +
            "cd_recuperacao CHAR(4), " +
            "dt_criacao_codigo_recuperacao TIMESTAMP, " +
            "nm_nivel_habilidade ENUM('INICIANTE', 'INTERMEDIARIO', 'AVANCADO') DEFAULT 'INICIANTE'" +
        ");";

        // Script de criação da tabela CIDADE
        String tabelaCidade =
        "CREATE TABLE IF NOT EXISTS CIDADE (" +
            "sg_cidade CHAR(3) PRIMARY KEY, " +
            "nm_cidade VARCHAR(50) NOT NULL, " +
            "sg_estado CHAR(2) NOT NULL" +
            "im_cidade TEXT NOT NULL" +
        ");";

        // Script de criação da tabela PRAIA
        String tabelaPraia =
        "CREATE TABLE IF NOT EXISTS PRAIA (" +
            "id_praia INTEGER AUTO_INCREMENT PRIMARY KEY, " +
            "sg_cidade CHAR(3) NOT NULL, " +
            "nm_praia VARCHAR(50) NOT NULL, " +
            "im_praia TEXT NOT NULL" +
            "CONSTRAINT fk_praia_cidade FOREIGN KEY (sg_cidade) REFERENCES CIDADE(sg_cidade)" +
        ");";

        // Script de criação da tabela EVENTO
        String tabelaEvento =
        "CREATE TABLE IF NOT EXISTS EVENTO (" +
            "id_evento INTEGER AUTO_INCREMENT PRIMARY KEY, " +
            "id_usuario INTEGER NOT NULL, " +
            "nm_evento VARCHAR(100) NOT NULL, " +
            "ds_evento VARCHAR(255), " +
            "dt_evento DATE, " +
            "hr_evento TIME, " +
            "nm_tipo_evento ENUM('FUTEBOL','VOLEI','SURF','TENIS','CORRIDA','OUTRO') NOT NULL, " +
            "id_praia INTEGER NOT NULL, " +
            "nm_endereco VARCHAR(255), " +
            "qt_limite_participantes INTEGER, " +
            "dt_publicacao_evento DATE DEFAULT NOW(), " +
            "nm_nivel_habilidade ENUM('NENHUM', 'INICIANTE', 'INTERMEDIARIO', 'AVANCADO') DEFAULT 'NENHUM', " +
            "CONSTRAINT fk_evento_usuario FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario), " +
            "CONSTRAINT fk_evento_praia FOREIGN KEY (id_praia) REFERENCES PRAIA(id_praia)" +
        ");";

        // Script de criação da tabela AMIZADE
        String tabelaAmizade =
        "CREATE TABLE IF NOT EXISTS AMIZADE (" +
            "id_amizade INTEGER AUTO_INCREMENT PRIMARY KEY, " +
            "id_usuario INTEGER NOT NULL, " +
            "id_usuario_recebedor_amizade INTEGER NOT NULL, " +
            "ic_status_pendente_aceito ENUM('PENDENTE','ACEITO') NOT NULL, " +
            "dt_amizade DATE DEFAULT NOW(), " +
            "CONSTRAINT fk_amizade_solicitante FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario), " +
            "CONSTRAINT fk_amizade_amigo FOREIGN KEY (id_usuario_recebedor_amizade) REFERENCES USUARIO(id_usuario)" +
        ");";

        // Script de criação da tabela USUARIO_EVENTO
        String tabelaUsuarioEvento =
        "CREATE TABLE IF NOT EXISTS USUARIO_EVENTO (" +
            "id_usuario INTEGER NOT NULL, " +
            "id_evento INTEGER NOT NULL, " +
            "PRIMARY KEY (id_usuario, id_evento), " +
            "CONSTRAINT fk_ue_usuario FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario), " +
            "CONSTRAINT fk_ue_evento FOREIGN KEY (id_evento) REFERENCES EVENTO(id_evento)" +
        ");";

        // Script de criação da tabela AVALIACAO
        String tabelaAvaliacao =
        "CREATE TABLE IF NOT EXISTS AVALIACAO (" +
            "id_usuario INTEGER NOT NULL, " +
            "id_usuario_avaliado INTEGER NOT NULL, " +
            "id_evento INTEGER NOT NULL, " +
            "ic_like_dislike_avaliacao TINYINT(1) NOT NULL, " +
            "PRIMARY KEY (id_usuario, id_usuario_avaliado, id_evento), " +
            "CONSTRAINT fk_avaliacao_usuario1 FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario), " +
            "CONSTRAINT fk_avaliacao_usuario2 FOREIGN KEY (id_usuario_avaliado) REFERENCES USUARIO(id_usuario), " +
            "CONSTRAINT fk_avaliacao_evento FOREIGN KEY (id_evento) REFERENCES EVENTO(id_evento)" +
        ");";

        // Script de criação da tabela MENSAGEM
        String tabelaMensagem =
        "CREATE TABLE IF NOT EXISTS MENSAGEM (" +
            "id_mensagem INTEGER PRIMARY KEY AUTO_INCREMENT, " +
            "id_usuario INTEGER NOT NULL, " +
            "id_evento INTEGER NOT NULL, " +
            "ds_mensagem VARCHAR(250) NOT NULL, " +
            "dt_mensagem TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "dt_edicao_mensagem TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
            "CONSTRAINT fk_mensagem_usuario FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario), " +
            "CONSTRAINT fk_mensagem_evento foreign key (id_evento) REFERENCES EVENTO(id_evento)" +
        ");";

        // Script de criação da tabela POSTAGEM
        String tabelaPostagem =
        "CREATE TABLE IF NOT EXISTS POSTAGEM (" +
            "id_postagem INTEGER PRIMARY KEY AUTO_INCREMENT, " +
            "id_usuario INTEGER NOT NULL, " +
            "ds_postagem VARCHAR(255) NOT NULL, " +
            "im_postagem TEXT, " +
            "dt_postagem DATETIME NOT NULL DEFAULT NOW(), " +
            "CONSTRAINT fk_postagem_usuario FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario)" +
        ");";

        // Script de criação da tabela AVALIACAO_POSTAGEM
        String tabelaAvaliacaoPostagem =
        "CREATE TABLE IF NOT EXISTS AVALIACAO_POSTAGEM (" +
            "id_usuario INT NOT NULL, " +
            "id_postagem INT NOT NULL, " +
            "ic_like_dislike_postagem TINYINT(1), " +
            "PRIMARY KEY (id_usuario, id_postagem), " +
            "CONSTRAINT fk_avaliacao_postagem_1 FOREIGN KEY (id_usuario) REFERENCES USUARIO (id_usuario), " +
            "CONSTRAINT fk_avaliacao_postagem_2 FOREIGN KEY (id_postagem) REFERENCES POSTAGEM (id_postagem)" +
        ");";

        // INSERÇÃO DE DADOS

        // Inserção de dados na tabela USUARIO
        String insercaoUsuario =
        "INSERT IGNORE INTO USUARIO (id_usuario, nm_usuario, nm_email_usuario, cd_senha, ds_bio, vl_pontuacao, dt_nascimento, cd_token) VALUES " +
        "(1, 'Alan Souza', 'alandeoliveirasou@gmail.com', '$2a$10$4iQXP7MZ/83yxSBUKPELu.F6NL9W3MQ47oCx/2Ub9ZyFEAvbluFNW', 'Lorem ipsum dolor sit amet', 3, '2001-01-01', NULL), " +
        "(2, 'Emanuel de Brito', 'emanuelbrit16@gmail.com', '$2a$10$d20jGP4jqqb1a.3uTvODROGqMfWumXSDJkGHYj68tWSN2QX5hOABy', 'Lorem ipsum dolor sit amet', 6, '2001-01-01', NULL), " +
        "(3, 'Kaique Melo', 'kaiquemelo@gmail.com', '$2a$10$KB4LsbJ6HOBR.dAWW4VM5O/L38r6S4qtzIQMIAJCxNpngJBHYCbbC', 'Lorem ipsum dolor sit amet', 4, '2001-01-01', NULL), " +
        "(4, 'Fellipe Cerqueira', 'fefebairos@gmail.com', '$2a$10$DUs0UX5Kp5wGQOmwYtHI7.6YBkXlnp.vRElyN/kHnDM8hRxuTaNqK', 'Lorem ipsum dolor sit amet', 5, '2001-01-01', NULL), " +
        "(5, 'Mariana Costa', 'marianacosta@gmail.com', '$2a$10$4iQXP7MZ/83yxSBUKPELu.F6NL9W3MQ47oCx/2Ub9ZyFEAvbluFNW', 'Amante de vôlei de praia e corrida matinal', 7, '1999-08-14', 'NULL);";
        // Todas as senhas são '123456'

        // Inserção de dados na tabela CIDADE
        String insercaoCidade =
        "INSERT IGNORE INTO CIDADE (sg_cidade, nm_cidade, sg_estado, im_cidade) VALUES " +
        "('PRG', 'Praia Grande', 'SP', '/praiou-midias/cidades/sp_praia_grande.png'), " +
        "('SAN', 'Santos', 'SP', '/praiou-midias/cidades/sp_santos.png'), " +
        "('SVI', 'São Vicente', 'SP', '/praiou-midias/cidades/sp_sao_vicente.png'), " +
        "('GUA', 'Guarujá', 'SP', '/praiou-midias/cidades/sp_guaruja.png'), " +
        "('ITA', 'Itanhaém', 'SP', '/praiou-midias/cidades/sp_itanhaem.png');";

        // Inserção de dados na tabela PRAIA
        String insercaoPraia =
        "INSERT IGNORE INTO PRAIA (id_praia, sg_cidade, nm_praia, im_praia) VALUES " +
        "(1, 'PRG', 'Praia do Boqueirão', '/praiou-midias/praias/prg_boqueirao.png'), " +
        "(2, 'PRG', 'Praia da Guilhermina', '/praiou-midias/praias/prg_guilhermina.png'), " +
        "(3, 'SAN', 'Praia do Gonzaga', '/praiou-midias/praias/san_gonzaga.png'), " +
        "(4, 'SVI', 'Praia do Gonzaguinha', '/praiou-midias/praias/svi_gonzaguinha.png'), " +
        "(5, 'ITA', 'Praia do Suarão', '/praiou-midias/praias/ita_suarao.png');";

        // Inserção de dados na tabela EVENTO
        String insercaoEvento =
        "INSERT IGNORE INTO EVENTO (id_evento, id_usuario, nm_evento, ds_evento, dt_evento, hr_evento, nm_tipo_evento, id_praia, nm_endereco, qt_limite_participantes, nm_nivel_habilidade) VALUES " +
        "(1, 2, 'Futebol na praia', 'Venham e se divirtam conosco!', '2026-06-24', '15:30:00', 'FUTEBOL', 1, 'Av Pres. Castelo Branco, 1490, Boqueirão - Praia Grande', 10, 'NENHUM'), " +
        "(2, 4, 'Futebol com novos colegas', 'Vamos aproveitar o fim de tarde', '2026-06-21', '16:00:00', 'FUTEBOL', 2, 'Av Pres. Castelo Branco, 2762, Guilhermina - Praia Grande', 10, 'NENHUM'), " +
        "(3, 3, 'Corrida na praia', 'Venha participar de uma corrida nas praias de Praia Grande conosco!', '2026-06-28', '12:00:00', 'CORRIDA', 1, 'Av Pres. Castelo Branco, 1490, Boqueirão - Praia Grande', 6, 'NENHUM'), " +
        "(4, 2, 'Correndo por aí', 'Venham e se divirtam conosco!', '2026-06-20', '09:00:00', 'CORRIDA', 3, 'Av. Presidente Wilson, 120, Gonzaga - Santos', 4, 'NENHUM'), " +
        "(5, 1, 'Volei na praia com amigos do Praiou', 'Venham e se divirtam conosco!', '2026-06-17', '15:00:00', 'VOLEI', 1, 'Av Pres. Castelo Branco, 1490, Boqueirão - Praia Grande', 6, 'NENHUM');";

        // Inserção de dados na tabela AMIZADE
        String insercaoAmizade =
        "INSERT IGNORE INTO AMIZADE (id_amizade, id_usuario, id_usuario_recebedor_amizade, ic_status_pendente_aceito) VALUES " +
        "(1, 1, 2, 'ACEITO'), " +
        "(2, 1, 3, 'ACEITO'), " +
        "(3, 2, 4, 'ACEITO'), " +
        "(4, 3, 5, 'PENDENTE'), " +
        "(5, 4, 1, 'PENDENTE');";

        // Inserção de dados na tabela USUARIO_EVENTO
        String insercaoUsuarioEvento =
        "INSERT IGNORE INTO USUARIO_EVENTO (id_usuario, id_evento) VALUES " +
        "(1, 1), " +
        "(2, 1), " +
        "(3, 2), " +
        "(4, 2), " +
        "(5, 3);";

        // Inserção de dados na tabela AVALIACAO 
        String insercaoAvaliacao =
        "INSERT IGNORE INTO AVALIACAO (id_usuario, id_usuario_avaliado, id_evento, ic_like_dislike_avaliacao) VALUES " +
        "(1, 2, 1, 1), " +
        "(2, 1, 1, 1), " +
        "(3, 4, 2, 1), " +
        "(4, 3, 2, 0), " +
        "(5, 3, 3, 1);";

        // Inserção de dados na tabela MENSAGEM
        String insercaoMensagem =
        "INSERT IGNORE INTO MENSAGEM (id_mensagem, id_usuario, id_evento, ds_mensagem) VALUES " +
        "(1, 2, 1, 'Fala galera! Tudo certo para o futebol na areia hoje às 15h30?'), " +
        "(2, 1, 1, 'Tudo certo por aqui! Vou levar uma bola extra e coletes.'), " +
        "(3, 3, 1, 'Fechado! Chego uns 10 minutos antes para a gente já aquecer.'), " +
        "(4, 2, 1, 'Show, nos encontramos na altura do posto no Boqueirão. Não esqueçam água e protetor!'), " +
        "(5, 1, 1, 'Combinado, partiu praia!');";

        // Inserção de dados na tabela POSTAGEM
        String insercaoPostagem =
        "INSERT IGNORE INTO POSTAGEM (id_postagem, id_usuario, ds_postagem, im_postagem, dt_postagem) VALUES " +
        "(1, 1, 'Achei incrível a experiência no aplicativo Praiou', NULL, '2026-05-15 15:30:12'), " +
        "(2, 3, 'Este aplicativo foi uma boa oportunidade para conhecer pessoas e criar relações!', NULL, '2026-05-19 11:45:28'), " +
        "(3, 2, 'Treino de futebol na areia muito animado hoje no Boqueirão!', NULL, '2026-05-22 17:10:00'), " +
        "(4, 4, 'Alguém anima uma corrida leve no fim da tarde?', NULL, '2026-05-25 08:20:15'), " +
        "(5, 5, 'Ótima estrutura para organizar partidas e encontrar parceiros de esporte.', NULL, '2026-05-28 19:05:40');";

        // Inserção de dados na tabela AVALIACAO_POSTAGEM
        String insercaoAvaliacaoPostagem =
        "INSERT IGNORE INTO AVALIACAO_POSTAGEM (id_usuario, id_postagem, ic_like_dislike_postagem) VALUES " +
        "(1, 2, 1), " +
        "(2, 1, 1), " +
        "(3, 1, 1), " +
        "(4, 3, 1), " +
        "(5, 4, 0);";

        try (Connection conexao = conectar(); Statement stmt = conexao.createStatement();) {
            // Criação das tabelas 
            stmt.execute(tabelaUsuario);
            stmt.execute(tabelaCidade);
            stmt.execute(tabelaPraia);
            stmt.execute(tabelaEvento);
            stmt.execute(tabelaAmizade);
            stmt.execute(tabelaUsuarioEvento);
            stmt.execute(tabelaAvaliacao);
            stmt.execute(tabelaMensagem);
            stmt.execute(tabelaPostagem);
            stmt.execute(tabelaAvaliacaoPostagem);
            System.out.println("Tabelas criadas/verificadas com sucesso!");

            // Inserção dos dados
            stmt.execute(insercaoUsuario);
            stmt.execute(insercaoCidade);
            stmt.execute(insercaoPraia);
            stmt.execute(insercaoEvento);
            stmt.execute(insercaoAmizade);
            stmt.execute(insercaoUsuarioEvento);
            stmt.execute(insercaoAvaliacao);
            stmt.execute(insercaoMensagem);
            stmt.execute(insercaoPostagem);
            stmt.execute(insercaoAvaliacaoPostagem);
            System.out.println("Dados inseridos nas tabelas com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao criar/verificar banco de dados: " + e.getMessage());
        }
    }

    public static Connection conectar() throws Exception {
        // Inicia conexão ao banco de dados

        //                                       local, usuario, senha
        return DriverManager.getConnection(URL + DBNAME, "root", null);
    }
}
