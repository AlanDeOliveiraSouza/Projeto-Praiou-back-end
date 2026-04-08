package br.gov.sp.fatec.pg.praiou.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

import br.gov.sp.fatec.pg.praiou.database.MySQLConnection;
import br.gov.sp.fatec.pg.praiou.model.Evento;
import br.gov.sp.fatec.pg.praiou.enums.TipoEvento;

//deletar dps
/* import br.gov.sp.fatec.pg.praiou.model.Administrador;
import br.gov.sp.fatec.pg.praiou.model.Atleta;
import br.gov.sp.fatec.pg.praiou.model.Usuario; */

public class EventoRepository {

    public static List<Evento> getTodosEventos() throws Exception {
        // Coleta todos os eventos do banco de dados
        String sql = "SELECT * FROM eventos";

        List<Evento> eventos = new ArrayList<>();

        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                // É declarada uma nova instância de evento
                Evento ev = new Evento();
                ev.setIdEvento(rs.getInt("id_evento"));
                ev.setIdUsuario(rs.getInt("id_usuario"));
                ev.setNome(rs.getString("nm_evento"));
                ev.setDescricao(rs.getString("ds_evento"));
                ev.setEndereco(rs.getString("nm_endereco_evento"));
                /*ev.setData(rs.getDate("dt_evento")));
                ev.setDataPublicacao(rs.getString("dt_publicacao_evento"));
                ev.setHorario(rs.getString("hr_evento"));*/
                ev.setLimiteParticipantes(rs.getInt("qt_limite_participantes"));
                ev.setTipoEvento(TipoEvento.valueOf(rs.getString("hr_evento")));

                eventos.add(ev);
            }
        }
        return eventos;
    }

    public static List<Evento> pegarEventosPorCategoria(TipoEvento categoria) throws Exception {
        // Pega um usuário do banco de dados a partir do token
        
        String sql = "SELECT * FROM usuario WHERE cd_token = ?";

        List<Evento> eventos = new ArrayList<>();
        
        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            pstmt.setString(1, categoria.toString());

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {   
                // É declarada uma nova instância de evento      
                Evento ev = new Evento();       
                ev.setIdEvento(rs.getInt("id_evento"));
                ev.setIdUsuario(rs.getInt("id_usuario"));
                ev.setNome(rs.getString("nm_evento"));
                ev.setDescricao(rs.getString("ds_evento"));
                ev.setEndereco(rs.getString("nm_endereco_evento"));
                /*ev.setData(rs.getDate("dt_evento"));
                ev.setDataPublicacao(rs.getString("dt_publicacao_evento"));
                ev.setHorario(rs.getTime("hr_evento"));*/
                ev.setLimiteParticipantes(rs.getInt("qt_limite_participantes"));
                ev.setTipoEvento(categoria);

                eventos.add(ev);
            }
        } catch(SQLException e) {
            System.out.println("Erro ao buscar eventos: " + e.getMessage());
        }
        
        return eventos;
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
