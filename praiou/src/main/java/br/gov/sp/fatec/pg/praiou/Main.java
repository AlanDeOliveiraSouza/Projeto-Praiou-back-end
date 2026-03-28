package br.gov.sp.fatec.pg.praiou;

import br.gov.sp.fatec.pg.praiou.database.MySQLConnection;
import br.gov.sp.fatec.pg.praiou.model.Usuario;
import br.gov.sp.fatec.pg.praiou.repository.UsuarioRepository;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) throws Exception {

        // Criando banco de dados (caso ele não exista)
        MySQLConnection.criarBancoDeDados();
        MySQLConnection.criarTabelas();

        // Instância de usuário para teste
        Usuario usuario = new Usuario();

        // Inserindo dados para teste
        usuario.setEmail("alansouza@gmail.com");
        usuario.setSenha("123456");

        // Autenticação do usuário
        UsuarioRepository.autenticar(usuario.getEmail(), usuario.getSenha());
        
        // Iniciando aplicação
        Javalin app = Javalin.create().start(7070);

        //  Rota (raiz) "/"
        app.get("/", ctx -> {
            ctx.result("Hello World, Praiou!");
        });
        
    }

}