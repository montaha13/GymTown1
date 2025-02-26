package controllers;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailNotification {
    private final String username = "projetmobile945@gmail.com"; // Remplacez par votre adresse email
    private final String password = "projetmobile2024"; // Remplacez par le mot de passe d'application

    public void sendEmail(String toAddress, String subject, String messageBody) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toAddress));
            message.setSubject(subject);
            message.setText(messageBody);

            Transport.send(message);
            System.out.println("Email envoyé avec succès");
        } catch (MessagingException e) {
            e.printStackTrace(); // Aide au débogage en affichant les erreurs
        }
    }
}