package br.gov.sp.fatec.pg.praiou.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.sp.fatec.pg.praiou.database.MySQLConnection;
import br.gov.sp.fatec.pg.praiou.model.Administrador;
import br.gov.sp.fatec.pg.praiou.model.Atleta;
import br.gov.sp.fatec.pg.praiou.model.Usuario;

public class AdministradorRepository {

    public static List<Administrador> pegarAdministradores() throws Exception {
        String sql = "SELECT id_usuario, nm_usuario, nm_email_usuario FROM usuario WHERE ic_status_administrador = 1 ORDER BY id_usuario";

        List<Administrador> adms = new ArrayList<>();

        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                Administrador adm = new Administrador();

                adm.setId(rs.getInt("id_usuario"));
                adm.setNome(rs.getString("nm_usuario"));
                adm.setEmail(rs.getString("nm_email_usuario"));

                adms.add(adm);
            }
        } catch(SQLException e) {
            System.out.println("Erro ao coletar administradores: " + e.getMessage());
        }

        return adms;
    }

}
