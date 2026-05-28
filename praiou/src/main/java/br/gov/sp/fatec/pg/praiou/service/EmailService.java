package br.gov.sp.fatec.pg.praiou.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.Message;
import java.util.Properties;

public class EmailService {
    // Executa o envio de emails para recuperação de senha
    
    private final String EMAIL = "praiou.services@gmail.com";
    private final String SENHA = "wiorlonxuoozekxe";

    public void enviarEmail(String destinatario, String assunto, String conteudo) {
        // Definidas as propriedades necessárias para enviar o email
        Properties propriedades = new Properties();

        propriedades.put("mail.smtp.host", "smtp.gmail.com");
        propriedades.put("mail.smtp.port", "587");
        propriedades.put("mail.smtp.auth", "true");
        propriedades.put("mail.smtp.starttls.enable", "true");
        Session sessao = Session.getInstance(propriedades, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, SENHA);
            }
        }); 

        try {
            // Definido o corpo do email
            Message mensagem = new MimeMessage(sessao);
            mensagem.setFrom(new InternetAddress(EMAIL)); // Remetente
            mensagem.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario)); // Destinatário
            mensagem.setSubject(assunto); // Assunto
            mensagem.setText(conteudo); // Conteúdo
            
            Transport.send(mensagem); // Envia o email
            System.out.println("E-mail para '" + assunto + "' enviado com sucesso!");
        } catch(MessagingException e) {
            System.out.println("Erro ao enviar e-mail:" + e.getMessage());
        }
    }

    public String getConteudoEmail(Integer tipo, String dado) {
        String conteudo = null;

        switch(tipo) {
            case 1: 
                conteudo = "Segue o código necesário para recuperação de sua senha: " + dado + " Atenção, este código é válido por 15 minutos";
                break;
            default: 
                System.out.println("Tipo de e-mail inválido");
                break;
        }
        return conteudo;
    }
}