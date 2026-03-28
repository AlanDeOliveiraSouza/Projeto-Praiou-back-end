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
            System.out.println("Banco de Dados " + DBNAME + " criado/verificado com sucesso!");
        } catch(SQLException e) {
            System.out.println("Erro ao criar/verificar banco de dados: " + e.getMessage());
        } 

    }

    public static void criarTabelas() {
        // Cria/verifica tabelas e dados do banco de dados

        // USO DO BANCO DE DADOS:

        // Criação de tabelas
        // Script de criação da tabela USUARIO
        String tabelaUsuario = 
        "CREATE TABLE IF NOT EXISTS usuario (" + 
            "id_usuario INT(11) NOT NULL AUTO_INCREMENT, " + 
            "nm_usuario VARCHAR(100) NOT NULL, " +  
            "nm_email_usuario VARCHAR(50) NOT NULL UNIQUE, " + 
            "cd_senha VARCHAR(50) NOT NULL, " + 
            "im_perfil LONGBLOB, " + 
            "ds_bio VARCHAR(250), " + 
            "vl_pontuacao INT, " + 
            "ic_status_administrador TINYINT(1) NOT NULL DEFAULT 0, " +
            "cd_token CHAR(10), " +
            " PRIMARY KEY (id_usuario)" +
        ");";

        // Script de criação da tabela AMIZADE
        // String tabelaAmizade = "";

        // Script de criação da tabela EVENTO
        // String tabelaEvento = "";

        // Script de criação da tabela POSTAGEM
        // String tabelaPostagem = "";

        // Script de criação da tabela MENSAGEM
        // String tabelaMensagem = "";

        // Script de criação da tabela AVALIACAO
        // String tabelaAvaliacao = "";

        // Script de criação da tabela USUARIO_EVENTO
        // String tabelaUsuarioEvento = "";

        // Script de criação da tabela USUARIO_POSTAGEM
        // String tabelaUsuarioPostagem = "";

        // Inserindo dados na tabela USUARIO para testes
        String insercaoUsuario = 
        "INSERT IGNORE usuario (nm_usuario, nm_email_usuario, cd_senha) VALUES " +
        "('Alan de Oliveira', 'alansouza@gmail.com', '123456'), " + 
        "('Emanuel de Brito', 'emanuelbrito@gmail.com', '123456'), " + 
        "('Cauê Simões', 'cauesimoes@gmail.com', '123456'), " + 
        "('Fellipe Cerqueira', 'fellipecerqueira@gmail.com', '123456'); ";

        try(Connection conexao = conectar(); Statement stmt = conexao.createStatement();) {
            // Executa os scripts no banco de dados

            stmt.execute(tabelaUsuario);
            // stmt.execute(tabelaAmizade);
            // stmt.execute(tabelaEvento);
            // stmt.execute(tabelaPostagem);
            // stmt.execute(tabelaMensagem);
            // stmt.execute(tabelaAvaliacao);
            // stmt.execute(tabelaUsuarioEvento);
            // stmt.execute(tabelaUsuarioPostagem);

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