package br.gov.sp.fatec.pg.praiou.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

import br.gov.sp.fatec.pg.praiou.database.MySQLConnection;
import br.gov.sp.fatec.pg.praiou.model.Administrador;
import br.gov.sp.fatec.pg.praiou.model.Atleta;
import br.gov.sp.fatec.pg.praiou.model.Usuario;

public class UsuarioRepository {

    public static void cadastrar(String nome, String email, String senha) throws Exception {
        // Cria um novo usuário no banco de dados
        String sql = "INSERT INTO usuario (nm_usuario, nm_email_usuario, cd_senha) VALUES (?, ?, ?);";

        // Criptografia da senha para inserir no banco de dados
        String senhaCriptografada = BCrypt.hashpw(senha, BCrypt.gensalt());

        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            pstmt.setString(1, nome);
            pstmt.setString(2, email);
            pstmt.setString(3, senhaCriptografada);

            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
        System.out.println("Usuário cadastrado com sucesso!");
    }

    public static void deletarUsuario(Integer id) throws Exception {
        // Apaga um usuário do banco de dados
        
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        
        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            pstmt.setInt(1, id);
            
            pstmt.executeUpdate();
            System.out.println("Usuário deletado com sucesso!");
        } catch(SQLException e) {
            System.out.println("Erro ao deletar usuário: " + e.getMessage());
        }
    }

    public static List<Usuario> getTodosUsuarios() throws Exception {
        // Coleta todos os usuários do banco de dados
        String sql = "SELECT id_usuario, nm_usuario, nm_email_usuario, ic_status_administrador FROM usuario";

        List<Usuario> usuarios = new ArrayList<>();

        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                // É declarada uma nova instância de usuário de acordo com o tipo de usuário
                Usuario u;
                if(rs.getInt("ic_status_administrador") == 0) {
                    u = new Atleta();
                    u.setStatusAdministrador(false);
                } else {
                    u = new Administrador();
                    u.setStatusAdministrador(true);
                }
                u.setId(rs.getInt("id_usuario"));
                u.setNome(rs.getString("nm_usuario"));
                u.setEmail(rs.getString("nm_email_usuario"));

                usuarios.add(u);
            }
        }
        return usuarios;
    }

    public static Usuario pegarUsuarioPorToken(String token) throws Exception {
        // Pega um usuário do banco de dados a partir do token
        
        String sql = "SELECT * FROM usuario WHERE cd_token = ?";
        Usuario usuario = new Usuario();
        
        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            pstmt.setString(1, token);
            
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                // Verifica se o usuário é atleta ou administrador
                boolean status = rs.getInt("ic_status_administrador") == 1 ? true : false;
                
                usuario.setId(rs.getInt("id_usuario"));
                usuario.setNome(rs.getString("nm_usuario"));
                usuario.setEmail(rs.getString("nm_email_usuario"));
                usuario.setStatusAdministrador(status);
                usuario.setToken(token);
            }
        } catch(SQLException e) {
            System.out.println("Erro ao buscar usuário: " + e.getMessage());
        }
        
        return usuario;
    }

    public static boolean autenticar(String email, String senha) throws Exception {
        // Verifica se os dados de acesso do usuário batem com os do banco de dados

        String sql = "SELECT cd_senha FROM usuario WHERE nm_email_usuario = ?;";

        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                boolean resultado = BCrypt.checkpw(senha, rs.getString("cd_senha"));
                if(resultado == true) {
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
    
    public static boolean buscarToken(String token) throws Exception {
        // Busca o token no banco de dados e retorna se ele existe

        String sql = "SELECT cd_token FROM usuario WHERE cd_token = ?";
        boolean resultado = false;

        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            pstmt.setString(1, token);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                String tokenBanco = rs.getString("cd_token");
                resultado = token.equals(tokenBanco);
            }
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar o token: " + e.getMessage());
        }

        return resultado;
    }

    public static void atualizarToken(String email, String token) throws Exception {
        // Atualiza o token do usuario no banco de dados

        String sql = "UPDATE usuario SET cd_token = ? WHERE nm_email_usuario = ?";

        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            pstmt.setString(1, token);
            pstmt.setString(2, email);

            pstmt.executeUpdate();
            System.out.println("Token atualizado!");
        } catch(SQLException e) {
            System.out.println("Erro ao atualizar o token: " + e.getMessage());
        }
    }

    public static void removerToken(String token) throws Exception {
        // Remove o token do usuário no banco de dados

        String sql = "UPDATE usuario SET cd_token = NULL WHERE cd_token = ?";

        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            pstmt.setString(1, token);

            pstmt.executeUpdate();
            System.out.println("Token removido!");
        } catch(SQLException e) {
            System.out.println("Erro ao remover token do usuário: " + e.getMessage());
        }
    }

    /*
    public static void alterarDadoUsuario(String dado, Integer id) throws Exception {
        // Atualiza os dados de um usuário no banco de dados

        String sql = "UPDATE usuario SET dado = ? WHERE id_usuario = ?";

        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            pstmt.setString(1, dado);
            pstmt.setInt(2, id);

            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Erro ao atualizar dado do usuário: " + e.getMessage());
        }
    }
    */

}
