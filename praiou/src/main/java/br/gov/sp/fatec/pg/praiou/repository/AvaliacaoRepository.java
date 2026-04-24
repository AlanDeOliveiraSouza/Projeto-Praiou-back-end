package br.gov.sp.fatec.pg.praiou.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

import br.gov.sp.fatec.pg.praiou.database.MySQLConnection;
import br.gov.sp.fatec.pg.praiou.model.Avaliacao;
import br.gov.sp.fatec.pg.praiou.model.Usuario;
import br.gov.sp.fatec.pg.praiou.model.Atleta; // So por preulcação(vai ser necessario?)
public class AvaliacaoRepository {

    
    public static void registrarAvaliacao(int idAvaliador, int idAvaliado, int idEvento, boolean curtida) throws Exception {
        String sql = "INSERT INTO avaliacao (id_usuario_avaliador, id_usuario_avaliado, id_evento, ic_like_dislike_avaliacao, dt_avaliacao) VALUES (?, ?, ?, ?, ?);";

        try (Connection conexao = MySQLConnection.conectar(); 
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            
            pstmt.setInt(1, idAvaliador);
            pstmt.setInt(2, idAvaliado);
            pstmt.setInt(3, idEvento);
            pstmt.setBoolean(4, curtida);
            pstmt.setDate(5, Date.valueOf(java.time.LocalDate.now()));

            pstmt.executeUpdate();
            System.out.println("Avaliação salva com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao salvar no banco: " + e.getMessage());
        }
    }

    
    public static List<Avaliacao> buscarAvaliacoesRecebidas(int idUsuarioAvaliado) throws Exception {
        String sql = "SELECT id_avaliacao, id_usuario_avaliador, ic_like_dislike_avaliacao, dt_avaliacao FROM avaliacao WHERE id_usuario_avaliado = ?;";
        List<Avaliacao> lista = new ArrayList<>();

        try (Connection conexao = MySQLConnection.conectar(); 
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuarioAvaliado);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Avaliacao a = new Avaliacao();
                a.setId(rs.getInt("id_avaliacao"));
                a.setCurtida(rs.getBoolean("ic_like_dislike_avaliacao"));
                a.setDataAvaliacao(rs.getDate("dt_avaliacao").toLocalDate());

                
                Usuario avaliador = new Atleta(); 
                avaliador.setId(rs.getInt("id_usuario_avaliador"));
                a.setAvaliador(avaliador);

                lista.add(a);
            }
        }
        return lista;
    }

    
    public static int contarTotalLikes(int idUsuarioAvaliado) throws Exception {
        String sql = "SELECT COUNT(*) AS total FROM avaliacao WHERE id_usuario_avaliado = ? AND ic_like_dislike_avaliacao = 1;";
        
        try (Connection conexao = MySQLConnection.conectar(); 
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuarioAvaliado);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) return rs.getInt("total");
        }
        return 0;
    }
}
