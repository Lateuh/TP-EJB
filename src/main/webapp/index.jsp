<%@ page import="converter.ConverterEjbBean" %><%--
  Created by IntelliJ IDEA.
  User: AlexX
  Date: 04/11/2020
  Time: 09:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean class="converter.ConverterEjbBean" id="beanConv"/>
<%@ page import="java.util.*" %>
<%@ page import="org.jdom2.JDOMException" %>
<%@ page import="converter.Monnaie" %>
<%@ page import="javax.naming.Context" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.jms.*" %>
<html>
<head>
    <title>Converter</title>
</head>
<body>
    <div>
        <form action="index.jsp" id="currencyChange">
            <label>Euros :
                <input type="number" name="amount">
            </label>
            </br>
            <label>Email :
                <input type="email" name="addrEmailClient">
            </label>
            <input type="submit" value="Change">
        </form>
        <label for="currencies">Choose a currency:</label>
        <select name="currencies" id="currencies" form="currencyChange">
            <%
                ConverterEjbBean ejb = new ConverterEjbBean();
                for(Monnaie m : ejb.getAvailableCurrencies()){
                    out.println("<option value="+ m.getCodeMonnaie()+" >"+ m.getNomCompletMonnaie()+"</option>");
                }
            %>
        </select>
    </div>



    <%
    if(request.getParameter("amount") == null || request.getParameter("amount").equals("")){
        out.println("<h4>Please fill the amount input</h4>");
    }else{
        if(request.getParameter("addrEmailClient") == null || request.getParameter("addrEmailClient").equals("")){}
        else {
            //Récupérer le contexte initial dans le serveur de noms JNDI
            Context jndiContext = new InitialContext();

            //Obtenir une instance de la fabrique de connexions qui a été créée précédemment
            javax.jms.ConnectionFactory connectionFactory = (QueueConnectionFactory) jndiContext.lookup("/ConnectionFactory");

            //Créer une connexion à l’aide de la fabrique de connexions
            Connection connection = connectionFactory.createConnection();

            //Créer une session sans transactions et avec des accusés de réception
            Session sessionQ = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

            //Créer un message de type texte utilisant l’objet session
            TextMessage message = sessionQ.createTextMessage();

            //Mettre le texte nécessaire dans ce message
            message.setText(request.getParameter("amount")+"#"+request.getParameter("addrEmailClient"));

            //Obtenir une référence vers une instance de la file de messages
            javax.jms.Queue queue = (javax.jms.Queue) jndiContext.lookup("queue/MailContent");

            //Créer un objet de type producteur de messages sur la file de messages à l’aide de l’objet session
            MessageProducer messageProducer = sessionQ.createProducer(queue);

            //Envoyer le message à l’aide de cet objet producteur de messages
            messageProducer.send(message);

            out.println("<h3>tentative envoie mail</h3>");
        }
        double resultat = 0;
        resultat = ejb.euroToOtherCurrency(Double.parseDouble(request.getParameter("amount")),request.getParameter("currencies"));
        out.println("<h4>Converted amount is : "+ resultat+"</h4>");
    }


    %>

</body>
</html>
