package com.codejayant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Email Sender
 */

public class EmailSender {

    public static final Logger logger = LoggerFactory.getLogger(EmailSender.class);

    static Properties mailServerProperties;
    static Session getMailSession;
    static Message generateMailMessage;


    /**
     * To send email
     *
     * @param subject : subject of the mail
     * @param emailBody : message to send in email
     * @param emailids : email ids where message is to send
     * @return true on success
     */
    public static boolean sendEmail(String subject, String emailBody, String... emailids) {
        boolean flag = true;
        Transport transport = null;
        try {
            // Step1
            logger.info("\n 1st ===> setup Mail Server Properties..");
            mailServerProperties = System.getProperties();
            mailServerProperties.put("mail.smtp.port", "587");
            mailServerProperties.put("mail.smtp.auth", "true");
            // may have to comment this below line
            mailServerProperties.put("mail.smtp.starttls.enable", "true");
            logger.info("Mail Server Properties have been setup successfully..");

            // Step2
            logger.info("\n\n 2nd ===> get Mail Session..");
            getMailSession = Session.getDefaultInstance(mailServerProperties, null);
            generateMailMessage = new MimeMessage(getMailSession);

            generateMailMessage.setSubject(subject);
            generateMailMessage.setContent(emailBody, "text/html");
            generateMailMessage.setFrom(new InternetAddress("testitsave@gmail.com", "testitsave"));
            logger.info("Mail Session has been created successfully..");

            // Step3
            logger.info("\n\n 3rd ===> Get Session and Send mail");
            transport = getMailSession.getTransport("smtp");

            transport.connect("smtp.gmail.com", "testitsave@gmail.com", "saveittest");
            for (String email : emailids) {
                generateMailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
                transport.sendMessage(generateMailMessage, new Address[]{new InternetAddress(email)});
            }
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            flag = false;
        } finally {
            if (transport != null && transport.isConnected())    {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                }
            }
        }
        return flag;
    }

    /**
     * Send email in threaded process
     *
     * @param subject : subject of the mail
     * @param emailBody : message to send in email
     * @param emailids : email ids where message is to send
     */
    public static void sendEmailThreaded(String subject, String emailBody, String... emailids) {
        /*new thread from here | executor service*/
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.execute(() -> {
            try {
                sendEmail(subject, emailBody, emailids);
                logger.info("Mail Sent to All");
            } catch (Exception e)  {
                logger.error("error in sending email : ", e.getMessage());
            }
        });
        executorService.shutdown();
    }


}