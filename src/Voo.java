import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Voo
{

    private String cidEntrd, cidSaida,
                   data, hora;

    private int assentos;
    private double preco;
    private List<Object> info;

    public Voo(){
        cidEntrd = new String();
        cidSaida = new String();
        data = new String();
        hora = new String();
        info = new ArrayList<>();
    }

    public Voo(String[] info ){

        cidEntrd = info[0];
        cidSaida = info[1];
        data = info[2];
        hora = info[3];

        String[] n = info[4].split(" ");
        info[4] = n[0];
        assentos = Integer.parseInt(info[4]);

        n = info[5].split(" ");
        info[5] = n[1];
        preco = Double.parseDouble(info[5]);

        setInfo();
    }


    public void setInfo()
    {
        info = new ArrayList<>();

        info.add(this.cidEntrd);
        info.add(this.cidSaida);
        info.add(this.data);
        info.add(this.hora);
        info.add(this.assentos);
        info.add(this.preco);
    }


    public String getCidEntrd(){
        return cidEntrd;
    }
    public String getCidSaida(){
        return cidSaida;
    }

    public int getAssentos(){
        return assentos;
    }

    public void subtrAssentos(){
        if (assentos > 1)
            assentos -= 1;
    }

    public double getPreco(){
        return preco;
    }

    public List<Object> getInfo(){
        return info;
    }

    public boolean vooValido()
    {
        if (assentos > 0) {
            assentos--;
            return true;
        }
        return false;
    }


    public void liberaReserva(){
        assentos++;
    }


    public void displayInfo()
    {

        for (Object dado: info)
            System.out.print("|\t\t" + dado + "\t\t");

    }
}
