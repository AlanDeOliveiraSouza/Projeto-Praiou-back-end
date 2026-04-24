package br.gov.sp.fatec.pg.praiou.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.sp.fatec.pg.praiou.database.MySQLConnection;
import br.gov.sp.fatec.pg.praiou.model.Amizade;
import br.gov.sp.fatec.pg.praiou.model.Usuario;

public class AmizadeRepository {

    public static void solicitarAmizade(int idSolicitante, int idRecebedor) throws Exception {
       
        String sql = "INSERT INTO amizade (id_usuario, id_usuario_recebedor_amizade, ic_status_pendente_aceito) VALUES (?, ?, 'PENDENTE');";

        try (Connection conexao = MySQLConnection.conectar(); 
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            
            pstmt.setInt(1, idSolicitante);
            pstmt.setInt(2, idRecebedor);
            pstmt.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
          
            pstmt.executeUpdate();
            System.out.println("Solicitação de amizade enviada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao solicitar amizade: " + e.getMessage());
        }
    }

    public static void aceitarAmizade(int idSolicitante, int idRecebedor) throws Exception {

        String sql = "UPDATE amizade SET ic_status_pendente_aceito = 'ACEITO' WHERE id_usuario = ? AND id_usuario_recebedor_amizade = ?;";

        try (Connection conexao = MySQLConnection.conectar(); 
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            
            pstmt.setInt(1, idSolicitante);
            pstmt.setInt(2, idRecebedor);

            pstmt.executeUpdate();
            System.out.println("Amizade aceita com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao aceitar amizade: " + e.getMessage());
        }
    }

    public static void removerAmizade(int idUsuario1, int idUsuario2) throws Exception {

        String sql = "DELETE FROM amizade WHERE (id_usuario = ? AND id_usuario_recebedor_amizade = ?) OR (id_usuario = ? AND id_usuario_recebedor_amizade = ?);";

        try (Connection conexao = MySQLConnection.conectar(); 
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario1);
            pstmt.setInt(2, idUsuario2);
            pstmt.setInt(3, idUsuario2);
            pstmt.setInt(4, idUsuario1);

            pstmt.executeUpdate();
            System.out.println("Amizade removida com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao remover amizade: " + e.getMessage());
        }
    }

    public static List<Amizade> listarAmigos(int idUsuario) throws Exception {
       
        String sql = "SELECT id_usuario, id_usuario_recebedor_amizade, ic_status_pendente_aceito " +
                     "FROM amizade WHERE (id_usuario = ? OR id_usuario_recebedor_amizade = ?) " +
                     "AND ic_status_pendente_aceito = 'ACEITO';";

        List<Amizade> listaAmizades = new ArrayList<>();

        try (Connection conexao = MySQLConnection.conectar(); 
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, idUsuario);
            
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Amizade amizade = new Amizade();
            
                Usuario solicitante = new Usuario() {}; 
                solicitante.setId(rs.getInt("id_usuario"));
                
                Usuario recebedor = new Usuario() {};
                recebedor.setId(rs.getInt("id_usuario_recebedor_amizade"));
                
                amizade.setUsuarioSolicitante(solicitante);
                amizade.setUsuarioAmigo(recebedor);
                amizade.setStatus(rs.getString("ic_status_pendente_aceito"));
           
                listaAmizades.add(amizade);
            }
        }
        return listaAmizades;
    }
    
    public static List<Amizade> listarSolicitacoesPendentes(int idUsuarioRecebedor) throws Exception {
        
        String sql = "SELECT id_usuario, id_usuario_recebedor_amizade, ic_status_pendente_aceito " +
                     "FROM amizade WHERE id_usuario_recebedor_amizade = ? AND ic_status_pendente_aceito = 'PENDENTE';";

        List<Amizade> listaPendentes = new ArrayList<>();

        try (Connection conexao = MySQLConnection.conectar(); 
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuarioRecebedor);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Amizade amizade = new Amizade();
                
                Usuario solicitante = new Usuario() {};
                solicitante.setId(rs.getInt("id_usuario"));
                
                Usuario recebedor = new Usuario() {};
                recebedor.setId(rs.getInt("id_usuario_recebedor_amizade"));
                
                amizade.setUsuarioSolicitante(solicitante);
                amizade.setUsuarioAmigo(recebedor);
                amizade.setStatus(rs.getString("ic_status_pendente_aceito"));
                
                listaPendentes.add(amizade);
            }
        }
        return listaPendentes;
    }
}
