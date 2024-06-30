import java.util.ArrayList;
import java.util.List;

public class Hotel
{
    public String cidade, nome;
    private int vagas, stars;
    private double preco;
    private List<Object> info;

    public Hotel(){
        cidade = new String();
        nome = new String();
        info = new ArrayList<>();
    }

    public Hotel(String[] info){
        cidade = info[0];
        nome = info[1];

        String[] n = info[2].split(" ");
        info[2] = n[0];
        vagas = Integer.parseInt(info[2]);

        n = info[3].split(" ");
        info[3] = n[1];
        preco = Double.parseDouble(info[3]);

        n = info[4].split(" ");
        info[4] = n[0];
        stars = Integer.parseInt(info[4]);

        setInfo();
    }

    public String getNome(){
        return nome;
    }

    public String getCidade(){
        return cidade;
    }

    public int getVagas(){
        return vagas;
    }

    public void subtrVagas(){

        if (vagas > 0)
            vagas -= 1;
    }

    public int getStars(){
        return stars;
    }

    public double getPreco(){
        return preco;
    }


    public void setInfo(){
        info = new ArrayList<>();

        info.add(this.cidade);
        info.add(this.nome);
        info.add(this.vagas);
        info.add(this.preco);
        info.add(this.stars);

    }

    public List<Object> getInfo(){
        return info;
    }

    public void displayInfo()
    {

        for (Object dado: info)
            System.out.print("|\t\t" + dado + " \t\t");

    }

}
