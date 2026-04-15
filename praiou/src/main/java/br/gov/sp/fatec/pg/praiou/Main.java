package br.gov.sp.fatec.pg.praiou;

import br.gov.sp.fatec.pg.praiou.database.MySQLConnection;
import br.gov.sp.fatec.pg.praiou.enums.TipoEvento;
import br.gov.sp.fatec.pg.praiou.model.Atleta;
import br.gov.sp.fatec.pg.praiou.model.Evento;
import br.gov.sp.fatec.pg.praiou.model.Usuario;
import br.gov.sp.fatec.pg.praiou.repository.EventoRepository;
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
            ctx.json(Map.of("message","Praiou em execução!"));
        });

        // AUTENTICAÇÃO

        // Endpoint para verificar presenca do token no armazenamento local
        app.get("/token", ctx -> {
            String token = ctx.header("Authorization");
            try {
                if(UsuarioRepository.buscarToken(token)) {
                    ctx.json(Map.of("success", true));
                    System.out.println("Token validado!");
                } else {
                    ctx.json(Map.of("success", false));
                    System.out.println("Token inválido, faça login novamente!");
                }
            } catch(Exception e) {
                ctx.json(Map.of("success", false));
            }
        });

        // Endpoint para cadastro de usuário 
        app.post("/registrar", ctx -> {
            Usuario usuario = ctx.bodyAsClass(Usuario.class);
            try {
                UsuarioRepository.cadastrar(usuario.getNome(), usuario.getEmail(), usuario.getSenha());
                ctx.json(Map.of("success", true));
            } catch(Exception e) {
                ctx.json(Map.of("success", false));
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
                ctx.json(Map.of("token", token, "success", true));
            } catch(Exception e) {
                ctx.json(Map.of("success", false));
            }
        });

        /*
        // Endpoint para recuperar senha do usuário
        app.post("/senha/recuperar", ctx -> {
	    Usuario usuario = ctx.bodyAsClass(Usuario.class);
            String email = usuario.getEmail();
        });
        */
        
        /*
        // Endpoint que recebe código de recuperação 
        app.post("/senha/codigo", ctx -> {
            
        });
        */

        /*
        // Endpoint que atualiza a senha do usuário
        app.post("/senha/atualizar", ctx -> {
            
        });
        */
        
        // Endpoint para logout do usuario 
        app.put("/logout", ctx -> {
            try {
                String token = ctx.header("Authorization");
                if(token != null) {
                    UsuarioRepository.removerToken(token);
                }
                ctx.json(Map.of("success", true));
                System.out.println("Logout efetuado!");
            } catch(Exception e) {
                ctx.status(500).json(Map.of("error", "Erro ao fazer logout: " + e.getMessage()));
            }
        });

        
        // Endpoint verificar token em todos as rotas privadas
        app.before("/api/*", ctx -> {
            // Pega o cabeçalho e verifica se existe um usuário logado (token válido)
            String token = ctx.header("Authorization");
            try {
                if(!UsuarioRepository.buscarToken(token)) {
                    ctx.status(401).json(Map.of("success", false, "message", "Cabeçalho não encontrado."));
                    ctx.skipRemainingHandlers();
                    return;
                }
            } catch(Exception e) {
                ctx.json(Map.of("success", false));
                System.out.println("Erro ao buscar token no banco de dados: " + e.getMessage());
            }
        });
        
        // EVENTOS
        
        // Endpoint para listar todos os eventos
        app.get("/api/eventos", ctx -> {
            // Busca todos os eventos no banco de dados
            try {
                List<Evento> eventos = EventoRepository.pegarTodosEventos();
                ctx.json(eventos);
                ctx.json(Map.of("success", true));
            } catch(Exception e) {
                ctx.json(Map.of("success", false));
                System.out.println("Erro ao buscar eventos no banco de dados: " + e.getMessage());
            }
        });
        
        
        // Endpoint para listar todos os eventos de uma determinada categoria
        app.get("/api/eventos/{categoria}", ctx -> {
            // Busca os eventos desta categoria no banco de dados
            try {
                // Converte a categoria para enum e realiza a busca
                TipoEvento categoria = TipoEvento.valueOf(ctx.pathParam("categoria").toUpperCase());
                List<Evento> eventos = EventoRepository.pegarEventosPorCategoria(categoria);
                ctx.json(eventos);
                ctx.json(Map.of("success", true));
            } catch(Exception e) {
                ctx.json(Map.of("success", false));
                System.out.println("Erro ao buscar eventos no banco de dados: " + e.getMessage());
            }
        });
        
        /*
        // Endpoint para listar todos os eventos de acordo com a busca de nome parcial
        app.get("/api/eventos/{busca}", ctx -> {
            // Busca os eventos com nome parcial no banco de dados
            try {
                String busca = ctx.pathParam("busca");
                List<Evento> eventos = EventoRepository.pegarEventosPorNome(busca);
                ctx.json(eventos);
                ctx.json(Map.of("success", true));
            } catch(Exception e) {
                ctx.json(Map.of("success", false));
                System.out.println("Erro ao buscar eventos no banco de dados: " + e.getMessage());
            }
        });
        */
        
        /*
        // Endpoint para buscar evento pelo id
        app.get("/api/eventos/{id}", ctx -> {
            // Busca um evento por seu id
            try {
                Integer id = Integer.parseInt(ctx.pathParam("id"));
                Evento evento = EventoRepository.pegarEventosPorId(id);
                ctx.json(evento);
                ctx.json(Map.of("success", true));
            } catch(Exception e) {
                ctx.json(Map.of("success", false));
                System.out.println("Erro ao buscar eventos no banco de dados: " + e.getMessage());
            } catch(NumberFormatException e) {
                ctx.json(Map.of("success", false));
                System.out.println("Valor para 'id' inválido: " + e.getMessage());
            }
        });
        */
        
    }

}