package converter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Monnaie implements Serializable {
    private List<String> listePays;
    private String nomCompletMonnaie;
    private String codeMonnaie;
    private double tauxChange;

    public Monnaie(){
        listePays = new ArrayList<>();
    }
    public Monnaie(String currency) {
        listePays = new ArrayList<>();
        codeMonnaie = currency;
    }

    public Monnaie(String currency, double rate) {
        listePays = new ArrayList<>();
        codeMonnaie = currency;
        tauxChange = rate;
        nomCompletMonnaie = "";
    }





    public List<String> getListePays() {
        return listePays;
    }

    public void setListePays(List<String> listePays) {
        this.listePays = listePays;
    }

    public String getNomCompletMonnaie() {
        return nomCompletMonnaie;
    }

    public void setNomCompletMonnaie(String nomCompletMonnaie) {
        this.nomCompletMonnaie = nomCompletMonnaie;
    }

    public String getCodeMonnaie() {
        return codeMonnaie;
    }

    public void setCodeMonnaie(String codeMonnaie) {
        this.codeMonnaie = codeMonnaie;
    }

    public double getTauxChange() {
        return tauxChange;
    }

    public void setTauxChange(double tauxChange) {
        this.tauxChange = tauxChange;
    }


}

