package br.gov.sp.fatec.pg.praiou.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class MySQLConnection {

    // URL de caminho e acesso ao banco de dados
    private static final String URL = "jdbc:mysql://localhost:3306/db_praiou";

    public static void criarBancoDeDados() {
        // Cria/verifica o banco de dados (tabelas e dados)

        // USO DO BANCO DE DADOS:
        // Script de criação do banco de dados DB_PRAIOU
        String criarBanco = "CREATE DATABASE IF NOT EXISTS db_praiou;";

        // Comando que indica que o banco DB_PRAIOU será usado
        String usarBanco = "USE db_praiou;";

        // Criação de tabelas
        // Script de criação da tabela USUARIO
        String tabelaUsuario = 
        "CREATE TABLE IF NOT EXISTS usuario (" + 
            "id_usuario INT(11) NOT NULL AUTO_INCREMENT, " + 
            "nm_usuario VARCHAR(50) NOT NULL, " + 
            "nm_sobrenome_usuario VARCHAR(50) NOT NULL, " + 
            "nm_email_usuario VARCHAR(150) NOT NULL UNIQUE, " + 
            "cd_senha VARCHAR(50) NOT NULL, " + 
            "cd_token_usuario TEXT, "+
            "im_foto_perfil MEDIUMBLOB, " + 
            "cd_telefone VARCHAR(11) UNIQUE, " + 
            "ds_bio VARCHAR(255), " + 
            "vl_pontuacao NUMERIC(2,0), " + 
            "ic_tipo_usuario ENUM('ATLETA', 'ADMINISTRADOR') NOT NULL DEFAULT 'ATLETA', " +
            "PRIMARY KEY (id_usuario)" + 
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
        // String insercaoUSUARIO = "";

        try(Connection conexao = conectar(); Statement stmt = conexao.createStatement();) {
            // Executa os scripts no banco de dados

            stmt.execute(criarBanco);
            stmt.execute(usarBanco);

            stmt.execute(tabelaUsuario);
            // stmt.execute(tabelaAmizade);
            // stmt.execute(tabelaEvento);
            // stmt.execute(tabelaPostagem);
            // stmt.execute(tabelaMensagem);
            // stmt.execute(tabelaAvaliacao);
            // stmt.execute(tabelaUsuarioEvento);
            // stmt.execute(tabelaUsuarioPostagem);

            // stmt.execute(insercaoUsuario);

            System.out.println("Tabela db_praiou criada/verificada com sucesso!");
        } catch(Exception e) {
            System.out.println("Erro ao criar/verificar banco de dados: " + e.getMessage());
        }

    }

    public static Connection conectar() throws Exception {
        // Inicia conexão ao banco de dados

        //                              local, usuario, senha
        return DriverManager.getConnection(URL, "root", null);
    }
    
    
}