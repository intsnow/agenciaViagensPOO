import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Main {


    //  Para gerar o arquivo sequencial de saída:
    public static void geraSaida_Seq(ReservaSystem rs){

        try
        {
            FileWriter writer = new FileWriter("saida_sequencial.csv");

            String  numTotal_CliDistintos = Integer.toString(rs.getClientesDistintos().size()),
                    numTotal_Orcm = Integer.toString(rs.getOrcamentos().size()),
                    numTotal_OrcmValidos = Integer.toString(rs.getOrcamentosValidos().size());


            String  precoTotal_Clientes = Double.toString(rs.getPrecoTotalClientes()),
                    precoTotal_Voos = Double.toString(rs.getPrecoTotalVoos()),
                    precoTotal_Hoteis = Double.toString(rs.getPrecoTotalHoteis());

            writer.write(numTotal_Orcm);
            writer.write(" ; ");
            writer.write(numTotal_CliDistintos);
            writer.write(" ; ");
            writer.write(numTotal_OrcmValidos);
            writer.write(" ; ");
            writer.write(precoTotal_Clientes);
            writer.write(" ; ");
            writer.write(precoTotal_Hoteis);
            writer.write(" ; ");
            writer.write(precoTotal_Voos);


            writer.close();

        }

        catch (IOException e) {
            System.out.println("\n\t\tFalha ao criar arquivo...");
        }

    }

    public static void geraSaida_Paral(ReservaSystem rs){

        try
        {
            FileWriter writer = new FileWriter("saida-paralela.csv");

            String  numTotal_CliDistintos = Integer.toString(rs.getClientesDistintos().size()),
                    numTotal_Orcm = Integer.toString(rs.getOrcamentos().size()),
                    numTotal_OrcmValidos = Integer.toString(rs.getOrcamentosValidos().size());


            String  precoTotal_Clientes = Double.toString(rs.getPrecoTotalClientes()),
                    precoTotal_Voos = Double.toString(rs.getPrecoTotalVoos()),
                    precoTotal_Hoteis = Double.toString(rs.getPrecoTotalHoteis());

            writer.write(numTotal_Orcm);
            writer.write(" ; ");
            writer.write(numTotal_CliDistintos);
            writer.write(" ; ");
            writer.write(numTotal_OrcmValidos);
            writer.write(" ; ");
            writer.write(precoTotal_Clientes);
            writer.write(" ; ");
            writer.write(precoTotal_Hoteis);
            writer.write(" ; ");
            writer.write(precoTotal_Voos);


            writer.close();

        }

        catch (IOException e) {
            System.out.println("\n\t\tFalha ao criar arquivo...");
        }

    }


    // Classe Runnable para processar clientes em paralelo
    static class ProcessaClientes implements Runnable
    {
        private ReservaSystem rs;
        private List<Cliente> clientes;

        public ProcessaClientes(ReservaSystem rs, List<Cliente> clientes) {
            this.rs = rs;
            this.clientes = clientes;
        }

        @Override
        public void run()
        {
            //  Lista auxiliar
            List<Cliente> auxCli = new ArrayList<>();

            for (Cliente cli : clientes)
            {
                // Criando orcamento de cada cliente
                rs.criaOrcamento(rs, cli);
                Orcamento orcm = rs.getOrcamento(cli);
                orcm.displayOrcamento();

                // Avaliação de cada cliente, aleatoriamente cada um aceitando ou não
                //boolean aceita = new Random().nextBoolean();
                boolean aceita = true;
                rs.avaliaOrcamento(aceita, orcm);


                /*      Com cliente já registrado, adiciona o mesmo
                 a lista auxiliar, referente a cada nome        */
                    if (!auxCli.stream().anyMatch(c -> c.getNome().equals(cli.getNome())))
                        auxCli.add(cli);

            }

            //  Por fim, copia a lista auxiliar à lista de Clientes Distintos
            rs.getClientesDistintos().addAll(auxCli);
        }
    }

    public static void processaClientesParalelo(ReservaSystem rs, List<Cliente> clientes)
    {
        int blockSize = 10;
        List<List<Cliente>> blocos = new ArrayList<>();


        for (int i = 0; i < rs.getClientes().size(); i += blockSize)
        {
            int endIndex = Math.min(i + blockSize, rs.getClientes().size());
            List<Cliente> bloco = new ArrayList<>(rs.getClientes().subList(i, endIndex));
            blocos.add(bloco);

            Thread thread = new Thread(new ProcessaClientes(rs, bloco));
            thread.start();

            try {
                thread.join(); // Aguarda a conclusão da thread antes de continuar
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }





    //
    public static void scanInputs(ReservaSystem rs){

        //  Lista auxiliar
        List<Cliente> auxCli = new ArrayList<>();

        //  Input de clientes
        for ( Cliente cli: rs.getClientes())
        {

            //  Criando orcamento de cada cliente
            rs.criaOrcamento(rs, cli);
            Orcamento orcm = rs.getOrcamento(cli);

            orcm.displayOrcamento();

            //  Avaliação de cada cliente, aleatoriamente cada um aceitando ou não
            Random rand = new Random();
            //Boolean aceita = rand.nextBoolean();
            Boolean aceita = true;
            rs.avaliaOrcamento(aceita, orcm);

            /*      Com cliente já registrado, adiciona o mesmo
             a lista auxiliar, referente a cada nome        */
            if (!auxCli.stream().anyMatch(c -> c.getNome().equals(cli.getNome())))
                auxCli.add(cli);

        }

        //  Por fim, copia a lista auxiliar à lista de Clientes Distintos
        rs.getClientesDistintos().addAll(auxCli);
    }



    public static void main(String[] args) throws FileNotFoundException {

        //  Escaneando/lendo arquivos
        String  n1 = "C:\\Users\\mundo\\Documents\\GitHub\\agenciaViagens\\src\\clientes_10000.csv",
                n2 = "C:\\Users\\mundo\\Documents\\GitHub\\agenciaViagens\\src\\hoteis.csv",
                n3 = "C:\\Users\\mundo\\Documents\\GitHub\\agenciaViagens\\src\\voos.csv";

        ScanFiles scfiles = new ScanFiles(n1, n2, n3);

        //  Ao escanear os arquivos, cria o sistema de reservas
        ReservaSystem rs = scfiles.startReserva();

        // Com o sistema criado, coleta cada cliente lido e produz o seu respetivo orçamento
        //scanInputs(rs);

        //  Cria arquivo_saida sequencial
        //geraSaida_Seq(rs);



        long startTime = System.currentTimeMillis();

        // Leitura sequencial:
        scanInputs(rs);
        geraSaida_Seq(rs);

        //processaClientesParalelo(rs, rs.getClientes());
        //geraSaida_Paral(rs);

        long endTime = System.currentTimeMillis();

        long totalTime = endTime - startTime;
        System.out.println("Tempo total de execução: " + totalTime + " milissegundos");


    }

    private static void executeTask() {
        // Simula uma tarefa demorada
        try {
            Thread.sleep(1000); // pausa por 1 segundo
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}



