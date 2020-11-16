package converter;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.sound.midi.SysexMessage;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@MessageDriven(activationConfig={
        @ActivationConfigProperty(propertyName = "destination",propertyValue="queue/MailContent"),
        @ActivationConfigProperty(propertyName = "destinationType",propertyValue = "javax.jms.Queue")
})
public class MailerMDBBean implements MessageListener {
    @EJB
    IConverter converter;

    public MailerMDBBean() {}

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage mesg = (TextMessage) message;
            String content = null;
            try {
                content = mesg.getText();
            } catch (JMSException e) {
                e.printStackTrace();
            }

            // Recuperer le montant a convertir :
            String amountString = content.substring(0,content.indexOf("#"));
            double amount = Double.parseDouble(amountString);

            // Demander au bean session de faire toutes les conversions ...
            Map<Monnaie,Double> mapMonnaiesConverties = converter.euroToOtherCurrencies(amount);

            Properties p = new Properties();
            p.put("mail.smtp.host", "smtp.gmail.com");
            p.put("mail.smtp.auth", "true");
            p.put("mail.smtp.starttls.enable","true");
            javax.mail.Session session = javax.mail.Session.getInstance(p);
            javax.mail.Message msg = new MimeMessage(session);

            try {
                // Preparation du mail
                msg.setFrom(new InternetAddress(System.getenv("email")));
                String dest = content.substring(content.indexOf("#")+1);
                msg.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(dest));

                String sujet = "Conversions de monnaie";
                msg.setSubject(sujet);


                // Mettre en forme les resultats retournes par le bean session (Map)
                // dans une chaine de caracteres contenant les balises HTML
                // necessaires pour construire le tableau HTML (variable content)
                // Voir la capture d'ecran de la Figure 1
                content = "<table style = \"border:1px solid #333;\">\n" +
                        "    <thead>\n" +
                        "        <tr>\n" +
                        "            <th style = \"border:1px solid #333;\">Currency</th>\n" +
                        "            <th style = \"border:1px solid #333;\">Actual Rate</th>\n" +
                        "            <th style = \"border:1px solid #333;\">Converted Amount</th>\n" +
                        "        </tr>\n" +
                        "    </thead>\n" +
                        "    <tbody>\n";
                for(Monnaie m : mapMonnaiesConverties.keySet()){
                    content += "<tr> <td style = \"border:1px solid #333;\">" + m.getCodeMonnaie()
                            + "("+ m.getNomCompletMonnaie() + ") </td> <td style = \"border:1px solid #333;\">"
                            + m.getTauxChange() + "</td> <td style = \"border:1px solid #333;\">"
                            + amount * m.getTauxChange() +"</td> </tr>";
                }
                 content += "</tbody> </table>";


                msg.setContent(content,"text/html;charset=utf8");
                msg.setSentDate(Calendar.getInstance().getTime());

                // Preparation de l'envoi du mail
                Transport transport = session.getTransport("smtp");
                transport.connect("smtp.gmail.com",587,System.getenv("email"), System.getenv("mdp"));

                // Envoi du mail
                transport.sendMessage(msg,msg.getAllRecipients());
                transport.close();
                System.out.println("Email envoyé à "+dest);
            }
            catch(MessagingException e){e.printStackTrace();}
        }
    }
}
