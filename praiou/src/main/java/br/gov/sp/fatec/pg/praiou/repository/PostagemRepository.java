package br.gov.sp.fatec.pg.praiou.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import br.gov.sp.fatec.pg.praiou.database.MySQLConnection;
import br.gov.sp.fatec.pg.praiou.model.Postagem;

public class PostagemRepository {

    public static List<Postagem> getTodasPostagens() throws Exception {
        // Coleta todos os usuários do banco de dados
        String sql = "SELECT * FROM postagem";

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<Postagem> postagens = new ArrayList<>();

        try(Connection conexao = MySQLConnection.conectar(); PreparedStatement pstmt = conexao.prepareStatement(sql);) {
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                Postagem post = new Postagem();
                post.setIdPostagem(rs.getInt("id_postagem"));
                post.setIdUsuario(rs.getInt("id_usuario"));
                post.setTexto(rs.getString("ds_postagem"));
                post.setData(Timestamp.valueOf(LocalDateTime.parse(rs.getString("dt_postagem"), formatador)));
                if(rs.getString("im_postagem") != null) {
                    post.setImagem(rs.getString("im_postagem"));
                }

                postagens.add(post);
            }
        }
        return postagens;
    }


}