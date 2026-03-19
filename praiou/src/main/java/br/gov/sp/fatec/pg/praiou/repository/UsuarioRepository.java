package br.gov.sp.fatec.pg.praiou.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.gov.sp.fatec.pg.praiou.database.MySQLConnection;

public class UsuarioRepository {

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
