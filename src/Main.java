import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Main {



    public static void main(String[] args) throws FileNotFoundException {

        //  Escaneando/lendo arquivos
        String  n1 = "C:\\Users\\mundo\\Documents\\GitHub\\agenciaViagens\\src\\clientes_10000.csv",
                n2 = "C:\\Users\\mundo\\Documents\\GitHub\\agenciaViagens\\src\\hoteis.csv",
                n3 = "C:\\Users\\mundo\\Documents\\GitHub\\agenciaViagens\\src\\voos.csv";

        //  Coleta nome para fins de print
        String nomeArq = n1.substring(51);


        //  Ao escanear os arquivos, cria o sistema de reservas
        ScanFiles scfiles = new ScanFiles(n1, n2, n3);
        ReservaSystem rs = scfiles.startReserva();


        // Leitura sequencial:

        long startTime = System.currentTimeMillis();

        processaClientes_Sequencial(rs, "saida-sequencial.csv");

        long endTime = System.currentTimeMillis();
        long totalTimeSeq = endTime - startTime;


        // Leitura Paralela:

        startTime = System.currentTimeMillis();

        processaClientes_Paralelo(rs, "saida-paralela.csv");

        endTime = System.currentTimeMillis();
        long totalTimeParal = endTime - startTime;

        System.out.println("\n\nNome Arquivo_Lido: " + nomeArq );
        System.out.println("\n\nTempo total de SEQUENCIAL: " + totalTimeSeq + " milissegundos");
        System.out.println("Tempo total de PARALELA: " + totalTimeParal + " milissegundos\n\n");


    }


    public static void processaClientes_Sequencial(ReservaSystem rs, String nomeArq){

        //  Lista auxiliar
        List<String> auxCli = new ArrayList<>();

        //  Input de clientes
        for ( Cliente cli: rs.getClientes())
        {

            //  Criando orcamento de cada cliente
            rs.criaOrcamento(cli);
            Orcamento orcm = rs.getOrcamento(cli);


            rs.avaliaOrcamento(true, orcm);

            //orcm.displayOrcamento();


            /*      Com cliente já registrado, adiciona o mesmo
             a lista auxiliar, referente a cada nome        */
            if (auxCli.isEmpty() || !auxCli.contains(cli.getNome()))
                auxCli.add(cli.getNome());

        }

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
                    precoTotal_Voos));

        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    public static void processaClientes_Paralelo(ReservaSystem rs, String nomeArq) {
        ConcurrentHashMap<String, Integer> totalOrcamentosFeitos = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Integer> numeroClientesDistintos = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Integer> totalOrcamentosValidos = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Double> totalGastoClientes = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Double> totalGastoHoteis = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Double> totalGastoVoos = new ConcurrentHashMap<>();

        List<Thread> threads = new ArrayList<>();
        List<Cliente> clientes = rs.getClientes();
        int qtdTotal_clientes = clientes.size();
        int blocos = (int) Math.ceil((double) qtdTotal_clientes / 10);

        for (int i = 0; i < blocos; i++) {
            int start = i * 10;
            int end = Math.min(start + 10, qtdTotal_clientes);
            List<Cliente> blocoClientes = clientes.subList(start, end);

            Thread novaT = new Thread(() -> {
                for (Cliente cli : blocoClientes) {

                    rs.criaOrcamento(cli);
                    Orcamento orcm = rs.getOrcamento(cli);

                    if (orcm.getValido()) {
                        synchronized (totalOrcamentosValidos) {
                            totalOrcamentosValidos.put("totalOrcamentosValidos", totalOrcamentosValidos.getOrDefault("totalOrcamentosValidos", 0) + 1);
                            totalGastoClientes.put("totalGastoClientes", totalGastoClientes.getOrDefault("totalGastoClientes", 0.0) + orcm.getPrecoTotalOrcamento());
                            totalGastoHoteis.put("totalGastoHoteis", totalGastoHoteis.getOrDefault("totalGastoHoteis", 0.0) + orcm.getPrecoTotalHotel());
                            totalGastoVoos.put("totalGastoVoos", totalGastoVoos.getOrDefault("totalGastoVoos", 0.0) + orcm.getPrecoTotalVoo());
                        }
                    }
                    synchronized (totalOrcamentosFeitos) {
                        totalOrcamentosFeitos.put("totalOrcamentosFeitos", totalOrcamentosFeitos.getOrDefault("totalOrcamentosFeitos", 0) + 1);
                    }
                    synchronized (numeroClientesDistintos) {
                        numeroClientesDistintos.put(cli.getNome(), 1);
                    }
                }
            });

            threads.add(novaT);
            novaT.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try (FileWriter fw = new FileWriter(nomeArq)) {
            fw.append(String.format("%d;%d;%d;%.2f;%.2f;%.2f\n",
                    totalOrcamentosFeitos.get("totalOrcamentosFeitos"),
                    numeroClientesDistintos.size(),
                    totalOrcamentosValidos.get("totalOrcamentosValidos"),
                    totalGastoClientes.get("totalGastoClientes"),
                    totalGastoHoteis.get("totalGastoHoteis"),
                    totalGastoVoos.get("totalGastoVoos")
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//
//    public static void processaClientes_Paralelo(ReservaSystem rs, String nomeArq)
//    {
//        ConcurrentHashMap<String, Integer> totalOrcamentosFeitos = new ConcurrentHashMap<>();
//        ConcurrentHashMap<String, Integer> numeroClientesDistintos = new ConcurrentHashMap<>();
//        ConcurrentHashMap<String, Integer> totalOrcamentosValidos = new ConcurrentHashMap<>();
//        ConcurrentHashMap<String, Double> totalGastoClientes = new ConcurrentHashMap<>();
//        ConcurrentHashMap<String, Double> totalGastoHoteis = new ConcurrentHashMap<>();
//        ConcurrentHashMap<String, Double> totalGastoVoos = new ConcurrentHashMap<>();
//
//        List<Thread> threads = new ArrayList<>();
//        List<Cliente> clientes = rs.getClientes();
//        int qtdTotal_clientes = clientes.size();
//        int blocos = (int) Math.ceil((double) qtdTotal_clientes / 10);
//
//        for (int i = 0; i < blocos; i++)
//        {
//            int start = i * 10;
//            int end = Math.min(start + 10, qtdTotal_clientes);
//            List<Cliente> blocoClientes = clientes.subList(start, end);
//
//            Thread novaT = new Thread(() ->
//            {
//                for (Cliente cli : blocoClientes)
//                {
//                    rs.criaOrcamento(cli);
//                    Orcamento orcm = rs.getOrcamento(cli);
//                    //orcm.displayOrcamento();
//                    boolean orcmValido = rs.avaliaOrcamento(true, orcm);
//
//                    synchronized (totalOrcamentosFeitos) {
//                        totalOrcamentosFeitos.put("totalOrcamentosFeitos", totalOrcamentosFeitos.getOrDefault("totalOrcamentosFeitos", 0) + 1);
//                    }
//
//                    synchronized (numeroClientesDistintos) {
//                        numeroClientesDistintos.put(cli.getNome(), 1);
//                    }
//
//                    synchronized (totalOrcamentosValidos){
//                        if (orcmValido)
//                        {
//                            double novoSaldo = cli.getSaldo() - orcm.getPrecoTotalOrcamento();
//                            cli.setSaldo(novoSaldo);
//
//                            totalOrcamentosValidos.put("totalOrcamentosValidos", totalOrcamentosValidos.getOrDefault("totalOrcamentosValidos", 0) + 1);
//                            totalGastoClientes.put("totalGastoClientes", totalGastoClientes.getOrDefault("totalGastoClientes", 0.0) + orcm.getPrecoTotalOrcamento());
//                            totalGastoHoteis.put("totalGastoHoteis", totalGastoHoteis.getOrDefault("totalGastoHoteis", 0.0) + orcm.getPrecoTotalHotel());
//                            totalGastoVoos.put("totalGastoVoos", totalGastoVoos.getOrDefault("totalGastoVoos", 0.0) + orcm.getPrecoTotalVoo());
//                        }
//                    }
//
//                }
//            });
//
//            threads.add(novaT);
//            novaT.start();
//        }
//
//        for (Thread t : threads) {
//            try {
//                t.join();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        try (FileWriter fw = new FileWriter(nomeArq))
//        {
//
//            fw.append(String.format("%d;%d;%d;%.2f;%.2f;%.2f\n",
//                    totalOrcamentosFeitos.get("totalOrcamentosFeitos"),
//                    numeroClientesDistintos.size(),
//                    totalOrcamentosValidos.get("totalOrcamentosValidos"),
//                    totalGastoClientes.get("totalGastoClientes"),
//                    totalGastoHoteis.get("totalGastoHoteis"),
//                    totalGastoVoos.get("totalGastoVoos")
//            ));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

}



