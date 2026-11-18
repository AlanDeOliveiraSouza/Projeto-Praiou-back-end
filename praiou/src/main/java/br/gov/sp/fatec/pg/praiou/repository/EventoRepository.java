package br.gov.sp.fatec.pg.praiou.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import br.gov.sp.fatec.pg.praiou.database.MySQLConnection;
import br.gov.sp.fatec.pg.praiou.model.Evento;
import br.gov.sp.fatec.pg.praiou.enums.TipoEvento;

public class EventoRepository {

    //Cria evento
    public static void criarEvento(Evento evento) throws Exception{
            String sql =    "INSERT INTO evento (id_usuario, nm_evento, nm_tipo_evento, ds_evento, dt_evento, hr_evento, nm_endereco_evento, qt_limite_participantes)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection conexao = MySQLConnection.conectar(); 
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            
            pstmt.setInt(1, evento.getIdUsuario());
            pstmt.setString(2, evento.getNome());
            pstmt.setString(3, evento.getTipoEvento().toString());

            if      (evento.getDescricao() != null)
                    pstmt.setString(4, evento.getDescricao());
            else    pstmt.setNull(4, 0);

            if      (evento.getData() != null)
                    pstmt.setString(5, evento.getData().toString());
            else    pstmt.setNull(5, 0);

            if      (evento.getHorario() != null)
                    pstmt.setString(6, evento.getHorario().toString());
            else    pstmt.setNull(6, 0);

            if      (evento.getEndereco() != null)
                    pstmt.setString(7, evento.getEndereco());
            else    pstmt.setNull(7, 0);

            if      (evento.getLimiteParticipantes() != null)
                    pstmt.setString(8, evento.getLimiteParticipantes().toString());
            else    pstmt.setNull(8, 0);

            pstmt.executeUpdate();
            System.out.println("Evento criado!");
        } catch (SQLException e) {
            System.out.println("Erro ao criar evento: " + e.getMessage());
        }
    }

    //Deleta um evento a partir de um id
    public static void deletarEvento(int id) throws Exception {
        String sql = "DELETE FROM evento WHERE id_evento = ?";
        try (Connection conexao = MySQLConnection.conectar(); 
            PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.execute();
            System.out.println("Evento deletado");
        } catch (SQLException e){
            System.out.println("Erro ao deletar evento: " + e.getMessage());
        }
    }

    public static List<Evento> pegarTodosEventos() throws Exception {
        // Coleta todos os eventos do banco de dados
        String sql = "SELECT * FROM evento";

        List<Evento> eventos = new ArrayList<>();

        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                // É declarada uma nova instância de evento
                Evento ev = new Evento();
                ev.setId(rs.getInt("id_evento"));
                ev.setIdUsuario(rs.getInt("id_usuario"));
                ev.setNome(rs.getString("nm_evento"));
                ev.setDescricao(rs.getString("ds_evento"));
                ev.setEndereco(rs.getString("nm_endereco_evento"));
                ev.setData(LocalDate.parse(rs.getString("dt_evento")));
                ev.setDataPublicacao(LocalDate.parse(rs.getString("dt_publicacao_evento")));
                ev.setHorario(LocalTime.parse(rs.getString("hr_evento")));
                ev.setLimiteParticipantes(rs.getInt("qt_limite_participantes"));
                ev.setTipoEvento(TipoEvento.valueOf(rs.getString("nm_tipo_evento")));

                eventos.add(ev);
            }
        }
        return eventos;
    }

    public static List<Evento> pegarEventosPorCategoria(TipoEvento categoria) throws Exception {
        // Pega um evento do banco de dados a partir de uma categoria
        
        String sql = "SELECT * FROM evento WHERE nm_tipo_evento = ?";

        List<Evento> eventos = new ArrayList<>();
        
        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            pstmt.setString(1, categoria.toString());

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {   
                // É declarada uma nova instância de evento      
                Evento ev = new Evento();       
                ev.setId(rs.getInt("id_evento"));
                ev.setIdUsuario(rs.getInt("id_usuario"));
                ev.setNome(rs.getString("nm_evento"));
                ev.setDescricao(rs.getString("ds_evento"));
                ev.setEndereco(rs.getString("nm_endereco_evento"));
                ev.setData(LocalDate.parse(rs.getString("dt_evento")));
                ev.setDataPublicacao(LocalDate.parse(rs.getString("dt_publicacao_evento")));
                ev.setHorario(LocalTime.parse(rs.getString("hr_evento")));
                ev.setLimiteParticipantes(rs.getInt("qt_limite_participantes"));
                ev.setTipoEvento(categoria);

                eventos.add(ev);
            }
        } catch(SQLException e) {
            System.out.println("Erro ao buscar eventos: " + e.getMessage());
        }
        
        return eventos;
    }

    public static List<Evento> pegarEventosPorNome(String nome) throws Exception {
        // Pega um evento do banco de dados a partir de um nome
        
        String sql = "SELECT * FROM evento WHERE nm_evento LIKE '%" + nome + "%'";

        List<Evento> eventos = new ArrayList<>();
        
        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {   
                // É declarada uma nova instância de evento      
                Evento ev = new Evento();       
                ev.setId(rs.getInt("id_evento"));
                ev.setIdUsuario(rs.getInt("id_usuario"));
                ev.setNome(rs.getString("nm_evento"));
                ev.setDescricao(rs.getString("ds_evento"));
                ev.setEndereco(rs.getString("nm_endereco_evento"));
                ev.setData(LocalDate.parse(rs.getString("dt_evento")));
                ev.setDataPublicacao(LocalDate.parse(rs.getString("dt_publicacao_evento")));
                ev.setHorario(LocalTime.parse(rs.getString("hr_evento")));
                ev.setLimiteParticipantes(rs.getInt("qt_limite_participantes"));
                ev.setTipoEvento(TipoEvento.valueOf(rs.getString("nm_tipo_evento")));

                eventos.add(ev);
            }
        } catch(SQLException e) {
            System.out.println("Erro ao buscar eventos: " + e.getMessage());
        }
        
        return eventos;
    }


    public static Evento pegarEventosPorId(Integer id) throws Exception {
        // Pega um evento do banco de dados a partir do ID dele
        
        String sql = "SELECT * FROM evento WHERE id_evento = ?";

        Evento evento = new Evento();  

        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {

                evento.setId(rs.getInt("id_evento"));
                evento.setIdUsuario(rs.getInt("id_usuario"));
                evento.setNome(rs.getString("nm_evento"));
                evento.setDescricao(rs.getString("ds_evento"));
                evento.setEndereco(rs.getString("nm_endereco_evento"));
                evento.setData(LocalDate.parse(rs.getString("dt_evento")));
                evento.setDataPublicacao(LocalDate.parse(rs.getString("dt_publicacao_evento")));
                evento.setHorario(LocalTime.parse(rs.getString("hr_evento")));
                evento.setLimiteParticipantes(rs.getInt("qt_limite_participantes"));
                evento.setTipoEvento(TipoEvento.valueOf(rs.getString("nm_tipo_evento")));

            }
        } catch(SQLException e) {
            System.out.println("Erro ao buscar eventos: " + e.getMessage());
        }
        
        return evento;
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
    public static List<Evento> pegarEventosDoCriador(String token) throws Exception {
        // Pega todos os evento do banco de dados que um usuario criou
        
        String sql = "SELECT * FROM evento e NATURAL JOIN usuario u WHERE u.cd_token = ?";

        List<Evento> eventos = new ArrayList<>();
        
        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            pstmt.setString(1, token);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {   
                // É declarada uma nova instância de evento      
                Evento ev = new Evento();       
                ev.setId(rs.getInt("id_evento"));
                ev.setIdUsuario(rs.getInt("id_usuario"));
                ev.setNome(rs.getString("nm_evento"));
                ev.setDescricao(rs.getString("ds_evento"));
                ev.setEndereco(rs.getString("nm_endereco_evento"));
                ev.setData(LocalDate.parse(rs.getString("dt_evento")));
                ev.setDataPublicacao(LocalDate.parse(rs.getString("dt_publicacao_evento")));
                ev.setHorario(LocalTime.parse(rs.getString("hr_evento")));
                ev.setLimiteParticipantes(rs.getInt("qt_limite_participantes"));
                ev.setTipoEvento(TipoEvento.valueOf(rs.getString("nm_tipo_evento")));

                eventos.add(ev);
            }
        } catch(SQLException e) {
            System.out.println("Erro ao buscar eventos: " + e.getMessage());
        }
        
        return eventos;
    }

    public static List<Evento> pegarEventosParticipando(String token) throws Exception {
        // Pega todos eventos do banco de dados em que o usuario está participando
        String sql = "SELECT * FROM evento e JOIN usuario_evento ue ON (ue.id_evento = e.id_evento) " + 
                                            "JOIN usuario u ON (ue.id_usuario = u.id_usuario) " + 
                                            "WHERE u.cd_token = ?";

        List<Evento> eventos = new ArrayList<>();
        
        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            pstmt.setString(1, token);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {   
                // É declarada uma nova instância de evento      
                Evento ev = new Evento();       
                ev.setId(rs.getInt("id_evento"));             //System.out.println(ev.getIdEvento());
                ev.setIdUsuario(rs.getInt("id_usuario"));
                ev.setNome(rs.getString("nm_evento"));
                ev.setDescricao(rs.getString("ds_evento"));
                ev.setEndereco(rs.getString("nm_endereco_evento"));
                ev.setData(LocalDate.parse(rs.getString("dt_evento")));
                ev.setDataPublicacao(LocalDate.parse(rs.getString("dt_publicacao_evento")));
                ev.setHorario(LocalTime.parse(rs.getString("hr_evento")));
                ev.setLimiteParticipantes(rs.getInt("qt_limite_participantes"));
                ev.setTipoEvento(TipoEvento.valueOf(rs.getString("nm_tipo_evento")));

                eventos.add(ev);
            }
        } catch(SQLException e) {
            System.out.println("Erro ao buscar eventos: " + e.getMessage());
        }
        return eventos;
    }

    public static void inscreverUsuarioEvento(int id, String token) throws Exception{
        String sql = "INSERT INTO usuario_evento(id_evento, id_usuario) VALUES(?, (SELECT id_usuario FROM usuario WHERE cd_token = ?))";
        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            pstmt.setInt(1, id);
            pstmt.setString(2, token);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Erro ao cadastrar relação entre usuário e evento: " + e.getMessage());
        }
        System.out.println("Relação usuário-evento cadastrado com sucesso!");
    }

}
