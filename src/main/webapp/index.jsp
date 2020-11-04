<%@ page import="converter.ConverterEjbBean" %><%--
  Created by IntelliJ IDEA.
  User: AlexX
  Date: 04/11/2020
  Time: 09:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <div>
        <form action="index.jsp" id="currencyChange">
            <label>Euros :
                <input type="number" name="amount">
            </label>
            <input type="submit" value="Change">
        </form>
        <label for="currencies">Choose a currency:</label>
        <select name="currencies" id="currencies" form="currencyChange">
            <option value="USD">Dollars (American)</option>
            <option value="JPY">JPY</option>
            <option value="BGN">BGN</option>
            <option value="CZK">CZK</option>
            <option value="DKK">DKK</option>
            <option value="GBP">GBP</option>
            <option value="HUF">HUF</option>
            <option value="PLN">PLN</option>
            <option value="RON">RON</option>
            <option value="SEK">SEK</option>
            <option value="CHF">CHF</option>
            <option value="ISK">ISK</option>
            <option value="NOK">NOK</option>
            <option value="HRK">HRK</option>
            <option value="RUB">RUB</option>
            <option value="TRY">TRY</option>
            <option value="AUD">AUD</option>
            <option value="BRL">BRL</option>
            <option value="CAD">CAD</option>
            <option value="CNY">CNY</option>
            <option value="HKD">HKD</option>
            <option value="IDR">IDR</option>
            <option value="ILS">ILS</option>
            <option value="INR">INR</option>
            <option value="KRW">KRW</option>
            <option value="MXN">MXN</option>
            <option value="NZD">NZD</option>
            <option value="PHP">PHP</option>
            <option value="SGD">SGD</option>
            <option value="THB">THB</option>
            <option value="ZAR">ZAR</option>
        </select>
    </div>


    <jsp:useBean class="converter.ConverterEjbBean" id="beanConv"/>
    <%@page import="java.util.*" %>
    <%@ page import="org.jdom2.JDOMException" %>
    <%
    if(request.getParameter("amount") == null || request.getParameter("amount").equals("")){
        out.println("<h4>Fill the amount input</h4>");
    }else{
        ConverterEjbBean ejb = new ConverterEjbBean();
        double resultat = 0;
        try {
            resultat = ejb.euroToOtherCurrency(Double.parseDouble(request.getParameter("amount")),request.getParameter("currencies"));
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        out.println("<h4>Converted amount is : </h4>"+ resultat);
    }
%>

</body>
</html>
