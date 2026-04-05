package br.gov.sp.fatec.pg.praiou.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.gov.sp.fatec.pg.praiou.database.MySQLConnection;
import br.gov.sp.fatec.pg.praiou.model.Atleta;
import br.gov.sp.fatec.pg.praiou.model.Usuario;

public class AtletaRepository {

    public static Atleta pegarAtletaPorId(Integer id) throws Exception {
        // Pega os dados de um atleta do banco de dados pelo id

        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";

        Atleta atleta = new Atleta();

        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                atleta.setId(rs.getInt("id_usuario"));
                atleta.setNome(rs.getString("nm_usuario"));
                atleta.setEmail(rs.getString("nm_email_usuario"));
                atleta.setDataNascimento(LocalDate.parse(rs.getString("dt_nascimento"))); // Converte data do banco para LocalDate
                atleta.setFotoPerfil(rs.getString("im_perfil"));
                atleta.setBio(rs.getString("ds_bio"));
                atleta.setPontuacao(rs.getInt("vl_pontuacao"));
                atleta.setStatusAdministrador(false);
                atleta.setToken(rs.getString("cd_token"));
            }
        } catch(SQLException e) {
            System.out.println("Erro ao coletar usuário do banco: " + e.getMessage());
        }

        return atleta;
    }

    public static Atleta pegarAtletaPorEmail(String email) throws Exception {
        // Pega os dados de um atleta do banco de dados pelo id

        String sql = "SELECT * FROM usuario WHERE nm_email_usuario = ?";

        Atleta atleta = new Atleta();

        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                atleta.setId(rs.getInt("id_usuario"));
                atleta.setNome(rs.getString("nm_usuario"));
                atleta.setEmail(rs.getString("nm_email_usuario"));
                atleta.setDataNascimento(LocalDate.parse(rs.getString("dt_nascimento"))); // Converte data do banco para LocalDate
                atleta.setFotoPerfil(rs.getString("im_perfil"));
                atleta.setBio(rs.getString("ds_bio"));
                atleta.setPontuacao(rs.getInt("vl_pontuacao"));
                atleta.setStatusAdministrador(false);
                atleta.setToken(rs.getString("cd_token"));
            }
        } catch(SQLException e) {
            System.out.println("Erro ao coletar usuário do banco: " + e.getMessage());
        }

        return atleta;
    }

    public static Atleta pegarAtletaPorToken(String token) throws Exception {
        // Pegs um usuário do banco de dados a partir do token
        
        String sql = "SELECT * FROM usuario WHERE cd_token = ?";
        
        Atleta atleta = new Atleta();
        
        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            pstmt.setString(1, token);
            
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                atleta.setId(rs.getInt("id_usuario"));
                atleta.setNome(rs.getString("nm_usuario"));
                atleta.setEmail(rs.getString("nm_email_usuario"));
                atleta.setDataNascimento(LocalDate.parse(rs.getString("dt_nascimento"))); // Converte data do banco para LocalDate
                atleta.setFotoPerfil(rs.getString("im_perfil"));
                atleta.setBio(rs.getString("ds_bio"));
                atleta.setPontuacao(rs.getInt("vl_pontuacao"));
                atleta.setStatusAdministrador(false);
                atleta.setToken(token);
            }
        } catch(SQLException e) {
            System.out.println("Erro ao buscar usuário: " + e.getMessage());
        } catch(ParseException e) {
            System.out.println("Erro na coleta de data do banco: " + e.getMessage());
        }

        return atleta;
    }

    public static List<Atleta> pegarClassificacao() throws Exception {
        String sql = "SELECT id_usuario, nm_usuario, vl_pontuacao FROM usuario ORDER BY vl_pontuacao";

        List<Atleta> atletas = new ArrayList<>();

        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                Atleta a = new Atleta();

                a.setId(rs.getInt("id_usuario"));
                a.setNome(rs.getString("nm_usuario"));
                a.setPontuacao(rs.getInt("vl_pontuacao"));

                atletas.add(a);
            }
        } catch(SQLException e) {
            System.out.println("Erro ao coletar classificação: " + e.getMessage());
        }

        return atletas;
    }

    public static void alterarBio(Integer id, String bio) throws Exception {
        String sql = "UPDATE usuario SET ds_bio = ? WHERE id_usuario = ?";

        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            pstmt.setString(1, bio);
            pstmt.setInt(2, id);

            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Erro ao alterar bio: " + e.getMessage());
        }
    }

    public static void alterarFotoPerfil(Integer id, String fotoPerfil) throws Exception {
        String sql = "UPDATE usuario SET im_perfil = ? WHERE id_usuario = ?";

        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            pstmt.setString(1, fotoPerfil);
            pstmt.setInt(2, id);

            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Erro ao alterar imagem: " + e.getMessage());
        }
    }

}
