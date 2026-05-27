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

        // Iniciando aplicação
        Javalin app = Javalin.create().start(7070);

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
        app.post("/senha/atualizar/{nova-senha}", ctx -> {
	    String email = ctx.bodyAsClass(Usuario.class).getEmail();
            String novaSenha = ctx.pathParam("nova-senha");
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

        // --- ENDPOINTS PERFIL
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