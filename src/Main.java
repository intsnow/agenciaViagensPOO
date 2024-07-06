import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Main {


    public static void main(String[] args) throws FileNotFoundException {

        //  Escaneando/lendo arquivos
        String  n1 = "C:\\Users\\mundo\\Documents\\GitHub\\agenciaViagens\\src\\clientes.csv",
                n2 = "C:\\Users\\mundo\\Documents\\GitHub\\agenciaViagens\\src\\hoteis.csv",
                n3 = "C:\\Users\\mundo\\Documents\\GitHub\\agenciaViagens\\src\\voos.csv";

        ScanFiles scfiles = new ScanFiles(n1, n2, n3);

        //  Ao escanear os arquivos, cria o sistema de reservas
        ReservaSystem rs = scfiles.startReserva();

        // Com o sistema criado, coleta cada cliente lido e produz o seu respetivo orçamento
        //scanInputs(rs);

        //  Cria arquivo_saida sequencial
        //geraSaida_Seq(rs);


        /* */
        long startTime = System.currentTimeMillis();

        // Leitura sequencial:
        processaClientes_Sequencial(rs, "saida-sequencial.csv");

        long endTime = System.currentTimeMillis();
        long totalTimeSeq = endTime - startTime,
                totalTimeParal = 0;



        startTime = System.currentTimeMillis();

        // Leitura sequencial:
        //rs = null;
        //rs = scfiles.startReserva();
        processaClientes_Paralelo(rs,"saida-paralela.csv");

        endTime = System.currentTimeMillis();
        totalTimeParal = endTime - startTime;

        //processaClientesParalelo(rs, rs.getClientes());
        //geraSaida_Paral(rs);


        System.out.println("Tempo total de SEQUENCIAL: " + totalTimeSeq + " milissegundos");
        System.out.println("Tempo total de PARALELA: " + totalTimeParal + " milissegundos");


    }





    public static void processaClientes_Paralelo(ReservaSystem rs, String nomeArq) {



        int totalPedidos = rs.getClientes().size();
//        int pedidosAtendidos = 0;
//        double totalGastoClientes = 0;
//        double totalGastoHoteis = 0;
//        double totalGastoVoos = 0;
        List<String> clientesDistintos = rs.getClientesDistintos();

        AtomicReference<Double> totalGastoClientes = new AtomicReference<>(0.0);
        AtomicReference<Double> totalGastoHoteis = new AtomicReference<>(0.0);
        AtomicReference<Double> totalGastoVoos = new AtomicReference<>(0.0);

        AtomicInteger pedidosAtendidosTotal = new AtomicInteger(0);

        // Criar threads para processar os clientes
        ReservaSystem_Runnable runnable1 = new ReservaSystem_Runnable(rs);
        ReservaSystem_Runnable runnable2 = new ReservaSystem_Runnable(rs);
        ReservaSystem_Runnable runnable3 = new ReservaSystem_Runnable(rs);

        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);
        Thread thread3 = new Thread(runnable3);
        thread1.start();
        thread2.start();
        thread3.start();

        // Aguardar todas as threads terminarem
        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Cliente> cli= new ArrayList<>();


        Set<Integer> pedidosUnicos = new HashSet<>();

        // Restante do código...

        // Coletar resultados das threads
        adicionarPedidosUnicos(pedidosUnicos, runnable1.getPedidosAtendidosLocal());
        adicionarPedidosUnicos(pedidosUnicos, runnable2.getPedidosAtendidosLocal());
        adicionarPedidosUnicos(pedidosUnicos, runnable3.getPedidosAtendidosLocal());

        int numPedidos = pedidosUnicos.size(); // Número total de pedidos únicos

//        pedidosAtendidosTotal.updateAndGet(total -> total + runnable1.getPedidosAtendidosLocal());
//        pedidosAtendidosTotal.updateAndGet(total -> total + runnable2.getPedidosAtendidosLocal());
//        pedidosAtendidosTotal.updateAndGet(total -> total + runnable3.getPedidosAtendidosLocal());
//        int numPedidos = pedidosAtendidosTotal.get(); // Número total de pedidos únicos




        somarValoresDistintos(totalGastoClientes, runnable1.getTotalGastoClientesLocal());
        somarValoresDistintos(totalGastoClientes, runnable2.getTotalGastoClientesLocal());
        somarValoresDistintos(totalGastoClientes, runnable3.getTotalGastoClientesLocal());

        somarValoresDistintos(totalGastoHoteis, runnable1.getTotalGastoHoteisLocal());
        somarValoresDistintos(totalGastoHoteis, runnable2.getTotalGastoHoteisLocal());
        somarValoresDistintos(totalGastoHoteis, runnable3.getTotalGastoHoteisLocal());

        somarValoresDistintos(totalGastoVoos, runnable1.getTotalGastoVoosLocal());
        somarValoresDistintos(totalGastoVoos, runnable2.getTotalGastoVoosLocal());
        somarValoresDistintos(totalGastoVoos, runnable3.getTotalGastoVoosLocal());


//        totalGastoClientes.updateAndGet(value -> value + runnable1.getTotalGastoClientesLocal());
//        totalGastoClientes.updateAndGet(value -> value + runnable2.getTotalGastoClientesLocal());
//        totalGastoClientes.updateAndGet(value -> value + runnable3.getTotalGastoClientesLocal());
//
//        totalGastoHoteis.updateAndGet(value -> value + runnable1.getTotalGastoHoteisLocal());
//        totalGastoHoteis.updateAndGet(value -> value + runnable2.getTotalGastoHoteisLocal());
//        totalGastoHoteis.updateAndGet(value -> value + runnable3.getTotalGastoHoteisLocal());
//
//        totalGastoVoos.updateAndGet(value -> value + runnable1.getTotalGastoVoosLocal());
//        totalGastoVoos.updateAndGet(value -> value + runnable2.getTotalGastoVoosLocal());
//        totalGastoVoos.updateAndGet(value -> value + runnable3.getTotalGastoVoosLocal());





//        // Coletar resultados das threads
//        int pedidosAtendidos1 = runnable1.getPedidosAtendidosLocal();
//        int pedidosAtendidos2 = runnable2.getPedidosAtendidosLocal();
//        int pedidosAtendidos3 = runnable3.getPedidosAtendidosLocal();
//
//        int numPedidos = Math.min(Math.min(pedidosAtendidos1,pedidosAtendidos2), pedidosAtendidos3);
//
//        totalGastoClientes += runnable1.getTotalGastoClientesLocal();
//        totalGastoClientes += runnable2.getTotalGastoClientesLocal();
//        totalGastoClientes += runnable3.getTotalGastoClientesLocal();
//        totalGastoHoteis += runnable1.getTotalGastoHoteisLocal();
//        totalGastoHoteis += runnable2.getTotalGastoHoteisLocal();
//        totalGastoHoteis += runnable3.getTotalGastoHoteisLocal();
//        totalGastoVoos += runnable1.getTotalGastoVoosLocal();
//        totalGastoVoos += runnable2.getTotalGastoVoosLocal();
//        totalGastoVoos += runnable3.getTotalGastoVoosLocal();



        // Escrever no arquivo de saída
        try (FileWriter writer = new FileWriter(nomeArq)) {
            writer.append(String.format("%d;%d;%d;%.2f;%.2f;%.2f\n",
                    totalPedidos,
                    clientesDistintos.size(),
                    numPedidos,//pedidosAtendidosTotal.get(),
                    totalGastoClientes.get(),
                    totalGastoHoteis.get(),
                    totalGastoVoos.get()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //
    public static void processaClientes_Sequencial(ReservaSystem rs, String nomeArq){

        //  Lista auxiliar
        List<String> auxCli = new ArrayList<>();

        //  Input de clientes
        for ( Cliente cli: rs.getClientes())
        {

            //  Criando orcamento de cada cliente
            rs.criaOrcamento(cli);
            Orcamento orcm = rs.getOrcamento(cli);

            //orcm.displayOrcamento();

            //  Avaliação de cada cliente, aleatoriamente cada um aceitando ou não
            Random rand = new Random();
            //Boolean aceita = rand.nextBoolean();
            Boolean aceita = true;
            boolean orcmValido = rs.avaliaOrcamento(aceita, orcm);

            double novoSaldo = cli.getSaldo() - orcm.getPrecoTotalOrcamento();
            if (orcmValido)
                cli.setSaldo(novoSaldo);


            /*      Com cliente já registrado, adiciona o mesmo
             a lista auxiliar, referente a cada nome        */
            if (auxCli.isEmpty() || !auxCli.contains(cli.getNome()))
                auxCli.add(cli.getNome());

        }

        //  Por fim, copia a lista auxiliar à lista de Clientes Distintos
        //rs.getClientesDistintos().addAll(auxCli);


        // Escrever no arquivo de saída
        try (FileWriter writer = new FileWriter(nomeArq)) {

            int  numTotal_CliDistintos = rs.getClientesDistintos().size(),
                    numTotal_Orcm = rs.getOrcamentos().size(),
                    numTotal_OrcmValidos = rs.getOrcamentosValidos().size();


            double  precoTotal_Clientes = rs.getPrecoTotalClientes(),
                    precoTotal_Voos = rs.getPrecoTotalVoos(),
                    precoTotal_Hoteis = rs.getPrecoTotalHoteis();

            writer.append(String.format("%d;%d;%d;%.2f;%.2f;%.2f\n",
                    numTotal_Orcm,
                    numTotal_CliDistintos,
                    numTotal_OrcmValidos,
                    precoTotal_Clientes,
                    precoTotal_Hoteis,
                    precoTotal_Hoteis));

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private static void adicionarPedidosUnicos(Set<Integer> pedidosUnicos, int pedidosThread) {
        for (int i = 0; i < pedidosThread; i++) {
            pedidosUnicos.add(i);
        }
    }

    private static void somarValoresDistintos(AtomicReference<Double> total, double valor ) {
        total.updateAndGet(current -> current + valor);
    }


}



