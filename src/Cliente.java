import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Cliente
{

    private String nome;
    private String cidEntrd, cidSaida;
    private int dias, reqStars;

    private double saldo;

    private List<Object> info;

    public Cliente(){
        nome = new String();
        cidEntrd = new String();
        cidSaida = new String();
        info = new ArrayList<>();
    }
    public Cliente(String[] info ){

        nome = info[0];
        cidEntrd = info[1];
        cidSaida = info[2];

        String[] n = info[3].split(" ");
        info[3] = n[0];
        dias = Integer.parseInt(info[3]);

        n = info[4].split(" ");
        info[4] = n[0];
        reqStars = Integer.parseInt(info[4]);

        n = info[5].split(" ");
        info[5] = n[1];
        saldo = Double.parseDouble(info[5]);

        setInfo();
    }


    public void setInfo(){
        info = new ArrayList<>();

        info.add(this.nome);
        info.add(this.cidEntrd);
        info.add(this.cidSaida);
        info.add(this.dias);
        info.add(this.reqStars);
        info.add(this.saldo);

    }

    public String getNome(){
        return nome;
    }

    public String getCidEntrd(){
        return cidEntrd;
    }

    public String getCidSaida(){
        return cidSaida;
    }


    public int getDias(){
        return dias;
    }

    public double getSaldo(){
        return saldo;
    }

    public void setSaldo(double novoSaldo){
        saldo = novoSaldo;
    }

    public int getReqStars(){
        return reqStars;
    }

    public List<Object> getInfo(){
        return info;
    }

    public void displayInfo()
    {

        for (Object dado: info)
            System.out.print("|\t\t" + dado + "\t\t");

    }

}
