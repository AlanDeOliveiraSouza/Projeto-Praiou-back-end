package br.gov.sp.fatec.pg.praiou;

import br.gov.sp.fatec.pg.praiou.database.MySQLConnection;
import br.gov.sp.fatec.pg.praiou.model.Atleta;
import br.gov.sp.fatec.pg.praiou.model.Usuario;
import br.gov.sp.fatec.pg.praiou.repository.UsuarioRepository;
import io.javalin.Javalin;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws Exception {

        // Criando banco de dados (caso ele não exista)
        MySQLConnection.criarBancoDeDados();

        // Iniciando aplicação
        Javalin app = Javalin.create().start(7070);

        // ENDPOINTS:

        //  Endpoint da rota (raiz) "/"
        app.get("/", ctx -> {
            ctx.result("Praiou em execução!");
        });

        // AUTENTICAÇÃO

        // Endpoint para verificar presenca do token no armazenamento local
        app.get("/token", ctx -> {
            String token = ctx.header("Authorization");
            if(token != null) {
                // Lógica na presença do token
                ctx.json(Map.of("success", "true"));
            } else {
                //ctx.status(401).json(Map.of("message", "Cabeçalho não encontrado."));
                ctx.json(Map.of("success", "false"));
            }
        });

        // Endpoint para cadastro de usuário 
        app.post("/registrar", ctx -> {
            Usuario usuario = ctx.bodyAsClass(Usuario.class);
            try {
                UsuarioRepository.cadastrar(usuario.getNome(), usuario.getEmail(), usuario.getSenha());
                ctx.json(Map.of("success", "true"));
            } catch(Exception e) {
                ctx.json(Map.of("success", "false"));
                System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
            }
        });

        // Endpoint de login do usuario
        app.post("/login", ctx -> {
            Usuario usuario = ctx.bodyAsClass(Usuario.class);
            try {
                if(!UsuarioRepository.autenticar(usuario.getEmail(), usuario.getSenha())) {
                    ctx.status(401).json(Map.of("error", "Email e/ou senha inválidos."));
                    return;
                }
                // Cria token e atualiza no banco e no usuario
                String token = UUID.randomUUID().toString();
                UsuarioRepository.atualizarToken(usuario.getEmail(), token);
                ctx.json(Map.of("token", token));
                ctx.json(Map.of("success", "true"));
            } catch(Exception e) {
                ctx.json(Map.of("success", "false"));
            }
        });

        /*
        app.post("/senha/recuperar", ctx -> {

        });
        */
        
        /*
        app.post("/senha/resetar", ctx -> {
            
        });
        */
        
        // Endpoint para logout do usuario 
        app.put("/logout", ctx -> {
            try {
                String token = ctx.header("Authorization");
                if(token != null) {
                    UsuarioRepository.removerToken(token);
                }
                ctx.json(Map.of("success", "true"));
                System.out.println("Logout efetuado!");
            } catch(Exception e) {
                ctx.status(500).json(Map.of("error", "Erro ao fazer logout: " + e.getMessage()));
            }
        });
        
    }

}