import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ScanFiles
{

    List<Cliente> clientes;
    List<Hotel> hoteis;
    List<Voo> voos;

    public ScanFiles(String cli, String hot, String voo) throws FileNotFoundException {

        clientes = new ArrayList<>();
        hoteis = new ArrayList<>();
        voos = new ArrayList<>();
        scanCli(cli);
        scanHot(hot);
        scanVoo(voo);
    }


    public ReservaSystem startReserva(){

        ReservaSystem rs = new ReservaSystem(clientes, hoteis, voos);

        return rs;
    }


    public void scanCli(String cli) throws FileNotFoundException{

        File f = new File(cli);

        Scanner sc = new Scanner(f);

        while (sc.hasNextLine()){

            String[] info = sc.nextLine().split(";");
            clientes.add(new Cliente(info));

        }

    }

    public void scanHot(String hot) throws FileNotFoundException{

        File f = new File(hot);

        Scanner sc = new Scanner(f);

        while (sc.hasNextLine()){

            String[] info = sc.nextLine().split(";");
            hoteis.add(new Hotel(info));

        }

    }

    public void scanVoo(String voo) throws FileNotFoundException{

        File f = new File(voo);

        Scanner sc = new Scanner(f);

        while (sc.hasNextLine()){

            String[] info = sc.nextLine().split(";");
            voos.add(new Voo(info));

        }

    }




}
