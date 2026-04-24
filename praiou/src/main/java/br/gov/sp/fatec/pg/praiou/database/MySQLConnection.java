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

        // Script de criação do banco de dados DB_PRAIOU
        String criarBanco = "CREATE DATABASE IF NOT EXISTS " + DBNAME + ";";

        try {
            Connection c = DriverManager.getConnection(URL, "root", null); 
            Statement stmt = c.createStatement();

            stmt.execute(criarBanco);
            System.out.println("Banco de Dados '" + DBNAME + "' criado/verificado com sucesso!");
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao criar/verificar banco de dados: " + e.getMessage());
        } 

        criarTabelas();

    }

    public static void criarTabelas() {
        // Cria/verifica tabelas e dados do banco de dados

        // CRIAÇÃO DE TABELAS

        // Script de criação da tabela USUARIO
        String tabelaUsuario = 
        "CREATE TABLE IF NOT EXISTS usuario (" + 
            "id_usuario INT NOT NULL AUTO_INCREMENT, " + 
            "nm_usuario VARCHAR(100) NOT NULL, " +  
            "nm_email_usuario VARCHAR(50) NOT NULL UNIQUE, " + 
            "cd_senha VARCHAR(255) NOT NULL, " + 
            "im_perfil MEDIUMBLOB, " + 
            "ds_bio VARCHAR(255), " + 
            "vl_pontuacao INT, " + 
            "ic_status_administrador TINYINT(1) NOT NULL DEFAULT 0, " +
            "cd_token CHAR(36), " +
            " PRIMARY KEY (id_usuario)" +
        ");";

        // Script de criação da tabela AMIZADE
        String tabelaAmizade = 
    "CREATE TABLE IF NOT EXISTS amizade (" +
            "id_amizade INT NOT NULL AUTO_INCREMENT," +
            "id_usuario INT NOT NULL, " +
            "id_usuario_recebedor_amizade INT NOT NULL," +
            "dt_amizade DATE NOT NULL," +            
            "ic_status_pendente_aceito ENUM('PENDENTE', 'ACEITO') NOT NULL," +
            "PRIMARY KEY (id_amizade)," +             
            "CONSTRAINT fk_amizade_usuario1 FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)," +
            "CONSTRAINT fk_amizade_usuario2 FOREIGN KEY (id_usuario_recebedor_amizade) REFERENCES usuario (id_usuario)" + "
        );";

        // Script de criação da tabela AVALIACAO
        String tabelaAvaliacao = 
    "CREATE TABLE IF NOT EXISTS avaliacao ("
            "id_avaliacao INT AUTO_INCREMENT PRIMARY KEY," + 
            "id_usuario_avaliador INT NOT NULL," +    
            "id_usuario_avaliado INT NOT NULL," +     
            "id_evento INT NOT NULL," +               
            "ic_like_dislike_avaliacao TINYINT(1) NOT NULL," +
            "dt_avaliacao DATE NOT NULL," +
            "CONSTRAINT fk_aval_avaliador FOREIGN KEY (id_usuario_avaliador) REFERENCES usuario (id_usuario)," +
            "CONSTRAINT fk_aval_avaliado FOREIGN KEY (id_usuario_avaliado) REFERENCES usuario (id_usuario)," +
            "CONSTRAINT fk_aval_evento FOREIGN KEY (id_evento) REFERENCES evento (id_evento)" +
        ");";

        // Script de criação da tabela EVENTO
        String tabelaEvento = 
        "CREATE TABLE IF NOT EXISTS evento (" + 
            "id_evento INT AUTO_INCREMENT PRIMARY KEY, " + 
            "id_usuario INT NOT NULL, " + 
            "nm_evento VARCHAR(100) NOT NULL, " + 
            "nm_tipo_evento ENUM('FUTEBOL', 'VOLEI', 'SURF', 'TENIS', 'CORRIDA', 'OUTRO') NOT NULL," + 
            "ds_evento VARCHAR(250), " + 
            "dt_evento DATE, " + 
            "hr_evento TIME, " + 
            "nm_endereco_evento VARCHAR(150), " + 
            "qt_limite_participantes INTEGER, " + 
            "dt_publicacao_evento DATE DEFAULT NOW(), " + 
            "CONSTRAINT fk_evento_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)" + 
        ");";

        // Script de criação da tabela POSTAGEM
        String tabelaPostagem = 
        "CREATE TABLE IF NOT EXISTS postagem (" + 
            "id_postagem INT AUTO_INCREMENT PRIMARY KEY, " + 
            "id_usuario INT NOT NULL, " + 
            "im_postagem MEDIUMBLOB, " + 
            "ds_postagem VARCHAR(250), " + 
            "dt_postagem DATETIME DEFAULT NOW(), " + 
            "CONSTRAINT fk_postagem_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario) " + 
        ");";

        // Script de criação da tabela MENSAGEM
        String tabelaMensagem = 
        "CREATE TABLE IF NOT EXISTS mensagem (" + 
            "id_mensagem INT AUTO_INCREMENT PRIMARY KEY, " + 
            "id_usuario INT NOT NULL, " + 
            "id_evento INT NOT NULL, " + 
            "ds_mensagem VARCHAR(250) NOT NULL, " + 
            "dt_mensagem TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " + 
            "dt_edicao_mensagem TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " + 
            "CONSTRAINT fk_mensagem_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario), " + 
            "CONSTRAINT fk_mensagem_evento FOREIGN KEY (id_evento) REFERENCES evento (id_evento)" + 
        ");";

        // Script de criação da tabela USUARIO_EVENTO
        String tabelaUsuarioEvento = 
        "CREATE TABLE IF NOT EXISTS usuario_evento (" + 
            "id_usuario INT NOT NULL, " + 
            "id_evento INT NOT NULL, " + 
            "PRIMARY KEY (id_usuario, id_evento), " + 
            "CONSTRAINT fk_usuario_evento_1 FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario), " + 
            "CONSTRAINT fk_usuario_evento_2 FOREIGN KEY (id_evento) REFERENCES evento (id_evento)" + 
        ");";

        // Script de criação da tabela USUARIO_POSTAGEM
        String tabelaAvaliacaoPostagem = 
        "CREATE TABLE IF NOT EXISTS avaliacao_postagem (" + 
            "id_usuario INT NOT NULL, " + 
            "id_postagem INT NOT NULL, " + 
            "ic_like_dislike_postagem TINYINT(1), " + 
            "PRIMARY KEY (id_usuario, id_postagem), " +
            "CONSTRAINT fk_avaliacao_postagem_1 FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario), " + 
            "CONSTRAINT fk_avaliacao_postagem_2 FOREIGN KEY (id_postagem) REFERENCES postagem (id_postagem)" + 
        ");";

        // INSERÇÃO DE DADOS

        // Inserindo dados na tabela USUARIO
        String insercaoUsuario = 
        "INSERT IGNORE usuario (nm_usuario, nm_email_usuario, cd_senha, ds_bio, vl_pontuacao) VALUES " +
        "('Alan Souza', 'alansouza@gmail.com', '$2a$10$4iQXP7MZ/83yxSBUKPELu.F6NL9W3MQ47oCx/2Ub9ZyFEAvbluFNW', 'Lorem ipsum dolor sit amet', 3), " + 
        "('Emanuel de Brito', 'emanuelbrito@gmail.com', '$2a$10$d20jGP4jqqb1a.3uTvODROGqMfWumXSDJkGHYj68tWSN2QX5hOABy', 'Lorem ipsum dolor sit amet', 6), " + 
        "('Cauê Simões', 'cauesimoes@gmail.com', '$2a$10$KB4LsbJ6HOBR.dAWW4VM5O/L38r6S4qtzIQMIAJCxNpngJBHYCbbC', 'Lorem ipsum dolor sit amet', 4), " + 
        "('Fellipe Cerqueira', 'fellipecerqueira@gmail.com', '$2a$10$DUs0UX5Kp5wGQOmwYtHI7.6YBkXlnp.vRElyN/kHnDM8hRxuTaNqK', 'Lorem ipsum dolor sit amet', 5); ";
        // Todas as senhas são '123456'

        try(Connection conexao = conectar(); Statement stmt = conexao.createStatement();) {
            // Executa os scripts no banco de dados

            stmt.execute(tabelaUsuario);
            stmt.execute(tabelaAmizade);
            stmt.execute(tabelaAvaliacao);
            stmt.execute(tabelaEvento);
            stmt.execute(tabelaPostagem);
            stmt.execute(tabelaMensagem);
            stmt.execute(tabelaUsuarioEvento);
            stmt.execute(tabelaAvaliacaoPostagem);

            stmt.execute(insercaoUsuario);

            System.out.println("Tabelas criadas/verificadas com sucesso!");
        } catch(Exception e) {
            System.out.println("Erro ao criar/verificar banco de dados: " + e.getMessage());
        }

    }

    public static Connection conectar() throws Exception {
        // Inicia conexão ao banco de dados

        //                              local, usuario, senha
        return DriverManager.getConnection(URL + DBNAME, "root", null);
    }
    
    
}
