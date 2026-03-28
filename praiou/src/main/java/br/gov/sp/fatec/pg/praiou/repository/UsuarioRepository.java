package br.gov.sp.fatec.pg.praiou.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.sp.fatec.pg.praiou.database.MySQLConnection;
import br.gov.sp.fatec.pg.praiou.model.Usuario;

public class UsuarioRepository {

    public static void criarUsuario(String nome, String email, String senha) throws Exception {
        // Cria um novo usuário no banco de dados
        String sql = "INSERT INTO usuario (nm_usuario, nm_email_usuario, cd_senha) VALUES (?, ?, ?);";

        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            pstmt.setString(1, nome);
            pstmt.setString(2, email);
            pstmt.setString(3, senha);

            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
        }

        System.out.println("Usuário cadastrado com sucesso!");
    }

    public static List<Usuario> buscarUsuarios() throws Exception {
        List<Usuario> usuarios = new ArrayList<>();
        return usuarios;
    }

    public static boolean autenticar(String email, String senha) throws Exception {
        // Verifica se os dados de acesso do usuário batem com os do banco de dados

        String sql = "SELECT nm_email_usuario, cd_senha FROM usuario WHERE nm_email_usuario = ? AND cd_senha = ?";

        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            pstmt.setString(1, email);
            pstmt.setString(2, senha);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                if(rs.getString("nm_email_usuario").equals(email) && rs.getString("cd_senha").equals(senha)) {
                    System.out.println("Usuário autenticado!");
                    return true;
                }
            }
        } catch(SQLException e) {
            System.out.println("Erro ao realizar autenticação: " + e.getMessage());
        }
        System.out.println("E-mail ou senha inválidos!"); 
        return false;
    }

}
