package br.gov.sp.fatec.pg.praiou.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.sp.fatec.pg.praiou.database.MySQLConnection;
import br.gov.sp.fatec.pg.praiou.model.Cidade;

public class CidadeRepository {

    public static List<Cidade> pegarTodasCidades() throws Exception {
        // Coleta todas as cidades do banco de dados

        String sql = "SELECT sg_cidade, nm_cidade, sg_estado FROM cidade";

        List<Cidade> cidades = new ArrayList<>();

        try(Connection conexao = MySQLConnection.conectar();
            PreparedStatement pstmt = conexao.prepareStatement(sql);) {

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                // É declarada uma nova instância de cidade
                Cidade cidade = new Cidade();

                cidade.setSigla(rs.getString("sg_cidade"));
                cidade.setNome(rs.getString("nm_cidade"));
                cidade.setSiglaEstado(rs.getString("sg_estado"));

                cidades.add(cidade);
            }
        } catch(SQLException e) {
            System.out.println("Erro ao buscar cidades: " + e.getMessage());
        }

        return cidades;
    }

}