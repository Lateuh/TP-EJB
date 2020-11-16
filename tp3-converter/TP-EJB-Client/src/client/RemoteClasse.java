package client;


import java.util.Scanner;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import converter.IConverter;

public class RemoteClasse {

	public static void main(String[] args) {
		
		
		IConverter converter = null;
		try {
			converter = (IConverter) InitialContext.doLookup("ejb:/Converter-1.0-SNAPSHOT/ConverterEjbEJB!converter.IConverter");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("In which currency ?");
		String currency = sc.nextLine();
		
		
		System.out.println("How much do you want to change ?");
		double amount = sc.nextDouble();
		
		
		
		double res = converter.euroToOtherCurrency(amount, currency);
		
		System.out.println("RESULT = "+res);
	}

}
