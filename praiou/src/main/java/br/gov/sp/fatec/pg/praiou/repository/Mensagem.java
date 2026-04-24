package br.gov.sp.fatec.pg.praiou.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.sp.fatec.pg.praiou.database.MySQLConnection;
import br.gov.sp.fatec.pg.praiou.model.Atleta;
import br.gov.sp.fatec.pg.praiou.model.Evento;
import br.gov.sp.fatec.pg.praiou.model.Mensagem;
import br.gov.sp.fatec.pg.praiou.model.Usuario;

public class MensagemRepository {

    
    public static void enviarMensagem(int idUsuario, int idEvento, String conteudo) throws Exception {
        String sql = "INSERT INTO mensagem (id_usuario, id_evento, ds_mensagem) VALUES (?, ?, ?);";

        try (Connection conexao = MySQLConnection.conectar(); 
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, idEvento);
            pstmt.setString(3, conteudo);

            pstmt.executeUpdate();
            System.out.println("Mensagem enviada!");
        } catch (SQLException e) {
            System.out.println("Erro ao enviar mensagem: " + e.getMessage());
        }
    }

    
    public static List<Mensagem> buscarMensagensPorEvento(int idEvento) throws Exception {
        String sql = "SELECT id_mensagem, id_usuario, ds_mensagem, dt_mensagem, dt_edicao_mensagem FROM mensagem WHERE id_evento = ? ORDER BY dt_mensagem ASC;";
        List<Mensagem> chat = new ArrayList<>();

        try (Connection conexao = MySQLConnection.conectar(); 
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            
            pstmt.setInt(1, idEvento);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Mensagem m = new Mensagem();
                m.setId(rs.getInt("id_mensagem"));
                m.setConteudo(rs.getString("ds_mensagem"));
                
                
                m.setDataEnvio(rs.getTimestamp("dt_mensagem").toLocalDateTime().toLocalDate());
                m.setDataEdicao(rs.getTimestamp("dt_edicao_mensagem").toLocalDateTime().toLocalDate());

                
                Usuario remetente = new Atleta(); 
                remetente.setId(rs.getInt("id_usuario"));
                m.setRemetente(remetente);

                chat.add(m);
            }
        }
        return chat;
    }

    
    public static void editarMensagem(int idMensagem, String novoConteudo) throws Exception {
        String sql = "UPDATE mensagem SET ds_mensagem = ? WHERE id_mensagem = ?;";

        try (Connection conexao = MySQLConnection.conectar(); 
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            
            pstmt.setString(1, novoConteudo);
            pstmt.setInt(2, idMensagem);

            pstmt.executeUpdate();
            System.out.println("Mensagem atualizada!");
        } catch (SQLException e) {
            System.out.println("Erro ao editar mensagem: " + e.getMessage());
        }
    }


    public static void deletarMensagem(int idMensagem) throws Exception {
        String sql = "DELETE FROM mensagem WHERE id_mensagem = ?;";

        try (Connection conexao = MySQLConnection.conectar(); 
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            
            pstmt.setInt(1, idMensagem);
            pstmt.executeUpdate();
            System.out.println("Mensagem removida!");
        } catch (SQLException e) {
            System.out.println("Erro ao deletar mensagem: " + e.getMessage());
        }
    }
}
