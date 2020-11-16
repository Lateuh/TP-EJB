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
            <option value="TOUT">See all</option>
            <%
                ConverterEjbBean ejb = new ConverterEjbBean();
                for(String s : ejb.getAvailableCurrencies()){
                    out.println("<option value="+ s +" >"+ s +"</option>");
                }
            %>
        </select>
    </div>


    <jsp:useBean class="converter.ConverterEjbBean" id="beanConv"/>
    <%@page import="java.util.*" %>
    <%@ page import="org.jdom2.JDOMException" %>
    <%
    if(request.getParameter("amount") == null || request.getParameter("amount").equals("")){
        out.println("<h4>Fill the amount input</h4>");
    }else if(request.getParameter("currencies").equals("TOUT")){
        List<String> resultat = ejb.euroToOtherCurrencies(Double.parseDouble(request.getParameter("amount")));
        for (String s : resultat){
            out.println("<h4>"+s+"</h4>");
        }
        }else {
        double resultat = ejb.euroToOtherCurrency(Double.parseDouble(request.getParameter("amount")), request.getParameter("currencies"));
        out.println("<h4>"+request.getParameter("amount")+" EUR is : " + resultat+" "+request.getParameter("currencies")+"</h4>");
    }
    %>

</body>
</html>
