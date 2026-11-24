package br.gov.sp.fatec.pg.praiou.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.sp.fatec.pg.praiou.database.MySQLConnection;
import br.gov.sp.fatec.pg.praiou.model.Praia;

public class PraiaRepository {

    public static List<Praia> pegarTodasPraias() throws Exception {
        // Coleta todas as praias do banco de dados

        String sql = "SELECT id_praia, nm_praia, sg_cidade FROM praia";

        List<Praia> praias = new ArrayList<>();

        try(Connection conexao = MySQLConnection.conectar();
            PreparedStatement pstmt = conexao.prepareStatement(sql);) {

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                // É declarada uma nova instância de praia
                Praia praia = new Praia();

                praia.setId(rs.getInt("id_praia"));
                praia.setNome(rs.getString("nm_praia"));
                praia.setSiglaCidade(rs.getString("sg_cidade"));

                praias.add(praia);
            }
        } catch(SQLException e) {
            System.out.println("Erro ao buscar praias: " + e.getMessage());
        }

        return praias;
    }

    public static List<Praia> pegarPraiasPorCidade(String siglaCidade) throws Exception {
        // Coleta todas as praias de uma cidade específica do banco de dados

        String sql = "SELECT id_praia, nm_praia, sg_cidade FROM praia WHERE sg_cidade = ?";

        List<Praia> praias = new ArrayList<>();

        try(Connection conexao = MySQLConnection.conectar();
            PreparedStatement pstmt = conexao.prepareStatement(sql);) {

            pstmt.setString(1, siglaCidade);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                // É declarada uma nova instância de praia
                Praia praia = new Praia();

                praia.setId(rs.getInt("id_praia"));
                praia.setNome(rs.getString("nm_praia"));
                praia.setSiglaCidade(rs.getString("sg_cidade"));

                praias.add(praia);
            }
        } catch(SQLException e) {
            System.out.println("Erro ao buscar praias por cidade: " + e.getMessage());
        }

        return praias;
    }

}
