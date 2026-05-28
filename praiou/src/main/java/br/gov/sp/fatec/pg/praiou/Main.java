package br.gov.sp.fatec.pg.praiou;

import br.gov.sp.fatec.pg.praiou.database.MySQLConnection;
import br.gov.sp.fatec.pg.praiou.enums.TipoEvento;
import br.gov.sp.fatec.pg.praiou.model.Atleta;
import br.gov.sp.fatec.pg.praiou.model.Evento;
import br.gov.sp.fatec.pg.praiou.model.Mensagem;
import br.gov.sp.fatec.pg.praiou.model.Usuario;
import br.gov.sp.fatec.pg.praiou.repository.AtletaRepository;
import br.gov.sp.fatec.pg.praiou.repository.EventoRepository;
import br.gov.sp.fatec.pg.praiou.repository.UsuarioRepository;
import br.gov.sp.fatec.pg.praiou.repository.MensagemRepository;
import br.gov.sp.fatec.pg.praiou.service.EmailService;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.lang.NumberFormatException;

public class Main {
    public static void main(String[] args) throws Exception, NumberFormatException {

        // Criando banco de dados (caso ele não exista)
        MySQLConnection.criarBancoDeDados();

        // Serviço de envio de e-mails
        EmailService emailService = new EmailService();

        // Chama o diretório de armazenameno de fotos
        String diretorioFotos = System.getProperty("user.dir") + "-upload-fotos";

        // Garante que a pasta exista
        File pasta = new File(diretorioFotos);
        if (!pasta.exists()) {
            pasta.mkdirs();
        }

        // Iniciando aplicação
        Javalin app = Javalin.create(config -> {
            // Configura o Javalin para servir os arquivos dessa pasta na URL "/fotos"
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/fotos";          // Caminho na URL
                staticFiles.directory = diretorioFotos;     // Pasta no seu HD
                staticFiles.location = Location.EXTERNAL;   // Fora do "resources" do projeto
            });
        }).start(7070);

        // --- ENDPOINTS:

        //  Endpoint da rota (raiz) "/"
        app.get("/", ctx -> {
            ctx.json(Map.of("message","Praiou em execução!"));
        });

        // ---ENDPOINTS DE AUTENTICAÇÃO ---

        // Endpoint para verificar presenca do token no armazenamento local
        app.get("/token", ctx -> {
            String token = ctx.header("Authorization");
            try {
                if(UsuarioRepository.buscarToken(token)) {
                    ctx.status(200).json(Map.of("success", true));
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
                ctx.status(201).json(Map.of("success", true));
            } catch(Exception e) {
                ctx.status(500).json(Map.of("success", false));
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
                Usuario usuarioBanco = UsuarioRepository.pegarUsuarioPorToken(token);
                ctx.status(200).json(Map.of("usuario", usuarioBanco, "success", true));
            } catch(Exception e) {
                ctx.status(500).json(Map.of("success", false));
            }
        });

        // Endpoint para recuperar senha do usuário
        app.post("/senha/recuperar", ctx -> {
            // instância do Random para gerar o código
            Random random = new Random();

	    Usuario usuario = ctx.bodyAsClass(Usuario.class);
            String email = usuario.getEmail();
            String codigo = Integer.toString(random.nextInt(9000) + 1000); // Gera número aleatório entre 0 e 9999
            // Primeiro, o código é registrado no banco de dados, para ser confirmado mais tarde pelo usuário
            try {
                UsuarioRepository.alterarCodigoRecuperacao(codigo, email);
            } catch(Exception e) {
                ctx.status(500).json(Map.of("success", false, "error", "Erro ao alterar codigo de recuperação: " + e.getMessage()));
            }

            // Depois o corpo do email é montado
            String conteudo = emailService.getConteudoEmail(1, codigo);

	    if (conteudo != null) {
            	// É criada uma nova thread para executar o envio do e-mail sem diminuir o desempenho da API (Esperar o envio do email para continuar a execução)
            	new Thread(() -> emailService.enviarEmail(email, "Recuperação de Senha", conteudo)).start();
                ctx.status(201).json(Map.of("success", true));
            } else {
                ctx.status(500).json(Map.of("success", false));
            }
        });

        // Endpoint que recebe código de recuperação 
        app.post("/senha/recuperar/{codigo}", ctx -> {
            String codigo = ctx.pathParam("codigo");
            try {
                boolean resultado = UsuarioRepository.compararCodigoRecuperacao(codigo);
                if(resultado) {
                    ctx.status(204).json(Map.of("success", true));
                    System.out.println("Código verificado!");
                } else {
                    ctx.status(401).json(Map.of("success", false));
                }
            } catch(Exception e) {
                ctx.status(500).json(Map.of("success", false, "error", "Erro ao verificar codigo: " + e.getMessage()));
            }
        });

        // Endpoint que atualiza a senha do usuário
        app.post("/senha/atualizar", ctx -> {
	    String email = ctx.bodyAsClass(Usuario.class).getEmail();
            String novaSenha = ctx.formParam("novaSenha");
            try {
                UsuarioRepository.alterarSenha(email, novaSenha);
                ctx.status(201).json(Map.of("success", true));
                System.out.println("Senha alterada com sucesso");
            } catch(Exception e) {
                ctx.status(500).json(Map.of("success", false));
                System.out.println("Erro ao alterar senha no banco de dados: " + e.getMessage());
            }
        });

        // Endpoint para logout do usuario 
        app.put("/logout", ctx -> {
            try {
                String token = ctx.header("Authorization");
                if(token != null) {
                    UsuarioRepository.removerToken(token);
                }
                ctx.status(204).json(Map.of("success", true));
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
                ctx.status(500).json(Map.of("success", false));
                System.out.println("Erro ao buscar token no banco de dados: " + e.getMessage());
            }
        });
        

        // --- ENDPOINTS DOS EVENTOS ---
        // Endpoint para listar todos os eventos
        app.get("/api/eventos", ctx -> {
            // Busca todos os eventos no banco de dados
            try {
                List<Evento> eventos = EventoRepository.pegarTodosEventos();
                ctx.status(200).json(Map.of("eventos", eventos, "success", true));
            } catch(Exception e) {
                ctx.status(500).json(Map.of("success", false));
                System.out.println("Erro ao buscar eventos no banco de dados: " + e.getMessage());
            }
        });

        // Endpoint para criação de eventos
        app.post("/api/eventos", ctx ->{
            Evento evento = ctx.bodyAsClass(Evento.class);
            EventoRepository.CriarEvento(evento);
            try {
                ctx.status(201).json(Map.of("evento", evento, "success", true));
            } catch (Exception e){
                ctx.status(500).json(Map.of("success", false));
            }
        });

        // Endpoint para deletar evento
        app.delete("/api/eventos/{id}", ctx ->{
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            EventoRepository.DeletarEvento(id);
            try {
                ctx.status(204).json(Map.of("id", id, "success", true));
            } catch (Exception e){
                ctx.status(500).json(Map.of("success", false));
            }
        });
        
        // Endpoint para listar todos os eventos de uma determinada categoria
        app.get("/api/eventos/type={categoria}", ctx -> {
            // Busca os eventos desta categoria no banco de dados
            try {
                // Converte a categoria para enum e realiza a busca
                TipoEvento categoria = TipoEvento.valueOf(ctx.pathParam("categoria").toUpperCase());
                List<Evento> eventos = EventoRepository.pegarEventosPorCategoria(categoria);
                ctx.status(200).json(Map.of("eventos", eventos, "success", true));
                //ctx.json(Map.of("success", true));
            } catch(Exception e) {
                ctx.status(500).json(Map.of("success", false));
                System.out.println("Erro ao buscar eventos no banco de dados: " + e.getMessage());
            }
        });
        
        // Endpoint para listar todos os eventos de acordo com a busca de nome parcial
        app.get("/api/eventos/search={busca}", ctx -> {
            // Busca os eventos com nome parcial no banco de dados
            try {
                String busca = ctx.pathParam("busca");
                List<Evento> eventos = EventoRepository.pegarEventosPorNome(busca);
                // for (Evento e : eventos) {
                //     System.out.println(e.getNome());
                // }
                ctx.status(200).json(Map.of("eventos", eventos, "success", true));
            } catch(Exception e) {
                ctx.status(500).json(Map.of("success", false));
                System.out.println("Erro ao buscar eventos no banco de dados: " + e.getMessage());
            }
        });
        
        // Endpoint para buscar evento pelo id
        app.get("/api/eventos/{id}", ctx -> {
            // Busca um evento por seu id
            try {
                Integer id = Integer.parseInt(ctx.pathParam("id"));
                Evento evento = EventoRepository.pegarEventosPorId(id);
                //System.out.println(evento.getNome());
                ctx.status(200).json(Map.of("evento", evento, "success", true));
            } catch(NumberFormatException e) {
                ctx.status(400).json(Map.of("success", false));
                System.out.println("Valor para 'id' inválido: " + e.getMessage());
            } catch(Exception e) {
                ctx.status(500).json(Map.of("success", false));
                System.out.println("Erro ao buscar eventos no banco de dados: " + e.getMessage());
            }
        });

        // Endpoint para buscar eventos criados pelo usuario
        app.get("/api/meus-eventos", ctx -> {
            // Busca todos os eventos que foram criados pelo usuario atual a partir de seu token
            try {
                String token = ctx.header("Authorization");
                List<Evento> eventos = EventoRepository.pegarEventosDoCriador(token);
                ctx.status(200).json(Map.of("eventos", eventos, "success", true));
            } catch(Exception e) {
                ctx.status(500).json(Map.of("success", false));
                System.out.println("Erro ao buscar eventos no banco de dados: " + e.getMessage());
            }
        });

        // Endpoint para buscar eventos em que o usuario está inscrito
        app.get("/api/inscricoes", ctx -> {
            // Busca todos os eventos em que o usuario atual está participando a partir de seu token
            try {
                String token = ctx.header("Authorization");
                List<Evento> eventos = EventoRepository.pegarEventosParticipando(token);
                ctx.status(200).json(Map.of("eventos", eventos, "success", true));
            } catch(Exception e) {
                ctx.status(500).json(Map.of("success", false));
                System.out.println("Erro ao buscar eventos no banco de dados: " + e.getMessage());
            }
        });

        // Endpoint para inscrição de um usuario em um evento
        app.post("/api/eventos/{id}/entrar", ctx -> {
            // Cadastra um novo registro na tabela usuario_evento, 
            // que é responsavel por registrar quais usuarios estão inscritos em quais eventos 
            try {
                Integer id = Integer.parseInt(ctx.pathParam("id")); //id do evento inscrito
                String token = ctx.header("Authorization");         //para ver quem é o usuario
                EventoRepository.inscreverUsuarioEvento(id, token);
                //System.out.println(evento.getNome());
                ctx.status(200).json(Map.of("success", true));
            } catch(NumberFormatException e) {
                ctx.status(400).json(Map.of("success", false));
                System.out.println("Valor para 'id' inválido: " + e.getMessage());
            } catch(Exception e) {
                ctx.status(500).json(Map.of("success", false));
                System.out.println("Erro ao buscar eventos no banco de dados: " + e.getMessage());
            }
        });


        // --- ENDPOINTS DAS MENSAGENS ---
        // Endpoint para busca de todas as mensagens de um evento
        app.get("/api/eventos/{id}/msgs", ctx ->{
            try{
                Integer id = Integer.parseInt(ctx.pathParam("id"));
                List<Mensagem> mensagens = MensagemRepository.buscarMensagensPorEvento(id);
                ctx.status(200).json(Map.of("mensagens", mensagens));
            } catch(NumberFormatException e) {
                ctx.status(400).json(Map.of("success",false));
                System.out.println("Valor para 'id' inválido: " + e.getMessage());
            } catch(Exception e){
                ctx.status(500).json(Map.of("success",false));
                System.out.println("Erro ao buscar mensagens no banco de dados: " + e.getMessage());
            }
        });

        //Endpoint para criar mensagens novas
        app.post("api/eventos/{id}/msgs", ctx -> {
            try{
                Integer idEvento = Integer.parseInt(ctx.pathParam("id"));
                Mensagem msg = ctx.bodyAsClass(Mensagem.class);
                MensagemRepository.enviarMensagem(msg.getIdRemetente(), idEvento, msg.getConteudo());
                ctx.status(201).json(Map.of("idEvento", idEvento, 
                                            "idRemetente", msg.getIdRemetente(), 
                                            "conteudo", msg.getConteudo()));
            } catch(NumberFormatException e) {
                ctx.status(400).json(Map.of("success",false));
                System.out.println("Valor para 'id' inválido: " + e.getMessage());
            } catch(Exception e){
                ctx.status(500).json(Map.of("success",false));
                System.out.println("Erro ao enviar mensagens no banco de dados: " + e.getMessage());
            }        
        });

        //Endpoint para atualizar mensagem
        //api/eventos/1/msgs?id=1
        app.put("api/eventos/{id}/msgs", ctx -> {
            try{
                Integer idMensagem = Integer.parseInt(ctx.queryParam("id"));
                Mensagem msg = ctx.bodyAsClass(Mensagem.class);
                MensagemRepository.editarMensagem(idMensagem, msg.getConteudo());
                ctx.status(201).json(Map.of("success",true,
                                            "mensagem", msg.getConteudo()));
            } catch(NumberFormatException e) {
                ctx.status(400).json(Map.of("success",false));
                System.out.println("Valor para 'id' inválido: " + e.getMessage());
            } catch(Exception e){
                ctx.status(500).json(Map.of("success",false));
                System.out.println("Erro ao enviar mensagens no banco de dados: " + e.getMessage());
            }          
        });

        //Endpoint para deletar mensagem
        //api/eventos/1/msgs?id=1
        app.delete("api/eventos/{id}/msgs", ctx -> {
            try{
                Integer idMensagem = Integer.parseInt(ctx.queryParam("id"));
                MensagemRepository.deletarMensagem(idMensagem);
                ctx.status(204).json(Map.of("success", true));
            } catch(NumberFormatException e) {
                ctx.status(400).json(Map.of("success", false));
                System.out.println("Valor para 'id' inválido: " + e.getMessage());
            } catch(Exception e){
                ctx.status(500).json(Map.of("success", false));
                System.out.println("Erro ao buscar mensagens no banco de dados: " + e.getMessage());
            }
        });

		//ENDPOINTS DE AMIZADE 
        // Endpoint para enviar uma solicitação de amizade
        // POST /api/amizades/solicitar?idRecebedor=2
        app.post("/api/amizades/solicitar", ctx -> {
            try {
                String token = ctx.header("Authorization");
                // Busca o usuário 
                Usuario solicitante = UsuarioRepository.pegarUsuarioPorToken(token);
                Integer idRecebedor = Integer.parseInt(ctx.queryParam("idRecebedor"));

                // Validação simples para o usuário não se auto-adicionar
                if (solicitante.getId().equals(idRecebedor)) {
                    ctx.status(400).json(Map.of("success", false, "error", "Você não pode enviar uma solicitação para si mesmo."));
                    return;
                }

                AmizadeRepository.solicitarAmizade(solicitante.getId(), idRecebedor);
                ctx.status(201).json(Map.of("success", true, "message", "Solicitação de amizade enviada!"));
            } catch (NumberFormatException e) {
                ctx.status(400).json(Map.of("success", false, "error", "ID do recebedor inválido."));
            } catch (Exception e) {
                ctx.status(500).json(Map.of("success", false, "error", e.getMessage()));
            }
        });

        // Endpoint para aceitar uma solicitação de amizade
        // PUT /api/amizades/aceitar?idSolicitante=1
        app.put("/api/amizades/aceitar", ctx -> {
            try {
                String token = ctx.header("Authorization");
                // Quem está aceitando é o usuário dono do token (o recebedor da solicitação)
                Usuario recebedor = UsuarioRepository.pegarUsuarioPorToken(token);
                Integer idSolicitante = Integer.parseInt(ctx.queryParam("idSolicitante"));

                AmizadeRepository.aceitarAmizade(idSolicitante, recebedor.getId());
                ctx.status(200).json(Map.of("success", true, "message", "Solicitação de amizade aceita!"));
            } catch (NumberFormatException e) {
                ctx.status(400).json(Map.of("success", false, "error", "ID do solicitante inválido."));
            } catch (Exception e) {
                ctx.status(500).json(Map.of("success", false, "error", e.getMessage()));
            }
        });

        // Endpoint para recusar solicitação ou desfazer uma amizade existente
        // DELETE /api/amizades/remover?idUsuario2=2
        app.delete("/api/amizades/remover", ctx -> {
            try {
                String token = ctx.header("Authorization");
                Usuario usuarioLogado = UsuarioRepository.pegarUsuarioPorToken(token);
                Integer idUsuario2 = Integer.parseInt(ctx.queryParam("idUsuario2"));

                AmizadeRepository.removerAmizade(usuarioLogado.getId(), idUsuario2);
                ctx.status(204); 
            } catch (NumberFormatException e) {
                ctx.status(400).json(Map.of("success", false, "error", "ID de usuário inválido."));
            } catch (Exception e) {
                ctx.status(500).json(Map.of("success", false, "error", e.getMessage()));
            }
        });

        // Endpoint para listar os amigos aceitos do usuário logado
        // GET /api/amizades/listar
        app.get("/api/amizades/listar", ctx -> {
            try {
                String token = ctx.header("Authorization");
                Usuario usuarioLogado = UsuarioRepository.pegarUsuarioPorToken(token);

                List<Amizade> amigos = AmizadeRepository.listarAmigos(usuarioLogado.getId());
                ctx.status(200).json(Map.of("amigos", amigos, "success", true));
            } catch (Exception e) {
                ctx.status(500).json(Map.of("success", false, "error", e.getMessage()));
            }
        });

        // Endpoint para listar as solicitações pendentes que o usuário logado RECEBEU
        // GET /api/amizades/pendentes
        app.get("/api/amizades/pendentes", ctx -> {
            try {
                String token = ctx.header("Authorization");
                Usuario usuarioLogado = UsuarioRepository.pegarUsuarioPorToken(token);

                List<Amizade> pendentes = AmizadeRepository.listarSolicitacoesPendentes(usuarioLogado.getId());
                ctx.status(200).json(Map.of("pendentes", pendentes, "success", true));
            } catch (Exception e) {
                ctx.status(500).json(Map.of("success", false, "error", e.getMessage()));
            }
        });



        // --- ENDPOINTS PERFIL
        // Endpoint que retorna os dados do perfil do usuário
        app.get("api/perfil", ctx -> {
            String token = ctx.header("Authorization");
            try {
                Atleta atleta = AtletaRepository.pegarPerfilPorToken(token);
                if(atleta.getId() != null) {
                    ctx.status(200).json(Map.of("usuario", atleta, "success", true));
                } else {
                    ctx.status(404).json(Map.of("success", false));
                }
            } catch(Exception e) {
                ctx.status(500).json(Map.of("success", false));
                System.out.println("Erro ao buscar usuário no banco de dados: " + e.getMessage());
            }
        });

        // Endpoint que faz a edição da senha
        app.put("api/perfil/editar-senha", ctx -> {
            String token = ctx.header("Authorization");
            String senhaAntiga = ctx.formParam("senhaAntiga");
            String novaSenha = ctx.formParam("novaSenha");
            try {
                if(UsuarioRepository.editarSenha(token, senhaAntiga, novaSenha)) {
                    ctx.status(204).json(Map.of("success", true));
                } else { 
                    ctx.status(500).json(Map.of("success", false)); 
                }
            } catch(Exception e) {
                ctx.status(500).json(Map.of("success", false));
                System.out.println("Erro ao editar senha do usuário: " + e.getMessage());
            }
        });

        // --- ENDPOINT RANKING
        // Endpoint para buscar Ranking
        app.get("api/ranking", ctx -> {
            try {
                List<Atleta> ranking = AtletaRepository.pegarClassificacao();
                ctx.status(200).json(Map.of("ranking", ranking, "success", true));
            } catch(Exception e){
                ctx.status(500).json(Map.of("success", false));
                System.out.println("Erro ao buscar ranking no banco de dados: " + e.getMessage());
            }
        });

    }

}
