//import java.io.FileNotFoundException;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.time.Duration;
//import java.time.Instant;
//import java.util.*;
//
//public class MainBAckUP {
//
//
//    public static void main(String[] args) throws FileNotFoundException {
//
//        //  Escaneando/lendo arquivos
//        String  n1 = "C:\\Users\\mundo\\Documents\\GitHub\\agenciaViagens\\src\\clientes.csv",
//                n2 = "C:\\Users\\mundo\\Documents\\GitHub\\agenciaViagens\\src\\hoteis.csv",
//                n3 = "C:\\Users\\mundo\\Documents\\GitHub\\agenciaViagens\\src\\voos.csv";
//
//        ScanFiles scfiles = new ScanFiles(n1, n2, n3);
//
//        //  Ao escanear os arquivos, cria o sistema de reservas
//        ReservaSystem rs = scfiles.startReserva();
//
//        // Com o sistema criado, coleta cada cliente lido e produz o seu respetivo orçamento
//        //scanInputs(rs);
//
//        //  Cria arquivo_saida sequencial
//        //geraSaida_Seq(rs);
//
//
//        /* */
//        long startTime = System.currentTimeMillis();
//
//        // Leitura sequencial:
//        //processaClientes_Sequencial(rs, "saida-sequencial.csv");
//
//        long endTime = System.currentTimeMillis();
//        long totalTimeSeq = endTime - startTime,
//                totalTimeParal = 0;
//
//
//
//
//        //  Reinicia ReservaSystem
//        //rs = scfiles.startReserva();
//
//        startTime = System.currentTimeMillis();
//
//        // Leitura sequencial:
//        processaClientes_Paralelo(rs,"saida-paralela.csv");
//
//        endTime = System.currentTimeMillis();
//        totalTimeParal = endTime - startTime;
//
//        //processaClientesParalelo(rs, rs.getClientes());
//        //geraSaida_Paral(rs);
//
//
//        System.out.println("Tempo total de SEQUENCIAL: " + totalTimeSeq + " milissegundos");
//        System.out.println("Tempo total de PARALELA: " + totalTimeParal + " milissegundos");
//
//
//    }
//
////    //  Para gerar o arquivo sequencial de saída:
////    public static void geraSaida_Seq(ReservaSystem rs){
////
////        try
////        {
////            FileWriter writer = new FileWriter("saida-sequencial.csv");
////
////            String  numTotal_CliDistintos = Integer.toString(rs.getClientesDistintos().size()),
////                    numTotal_Orcm = Integer.toString(rs.getOrcamentos().size()),
////                    numTotal_OrcmValidos = Integer.toString(rs.getOrcamentosValidos().size());
////
////
////            String  precoTotal_Clientes = Double.toString(rs.getPrecoTotalClientes()),
////                    precoTotal_Voos = Double.toString(rs.getPrecoTotalVoos()),
////                    precoTotal_Hoteis = Double.toString(rs.getPrecoTotalHoteis());
////
////            writer.write(numTotal_Orcm);
////            writer.write(" ; ");
////            writer.write(numTotal_CliDistintos);
////            writer.write(" ; ");
////            writer.write(numTotal_OrcmValidos);
////            writer.write(" ; ");
////            writer.write(precoTotal_Clientes);
////            writer.write(" ; ");
////            writer.write(precoTotal_Hoteis);
////            writer.write(" ; ");
////            writer.write(precoTotal_Voos);
////
////
////            writer.close();
////
////        }
////
////        catch (IOException e) {
////            System.out.println("\n\t\tFalha ao criar arquivo...");
////        }
////
////    }
////
////
////    public static void geraSaida_Paral(ReservaSystem rs, String nomeArq){
////
////        try
////        {
////            FileWriter writer = new FileWriter("saida-paralela.csv");
////
////            String  numTotal_CliDistintos = Integer.toString(rs.getClientesDistintos().size()),
////                    numTotal_Orcm = Integer.toString(rs.getOrcamentos().size()),
////                    numTotal_OrcmValidos = Integer.toString(rs.getOrcamentosValidos().size());
////
////
////            String  precoTotal_Clientes = Double.toString(rs.getPrecoTotalClientes()),
////                    precoTotal_Voos = Double.toString(rs.getPrecoTotalVoos()),
////                    precoTotal_Hoteis = Double.toString(rs.getPrecoTotalHoteis());
////
////            writer.write(numTotal_Orcm);
////            writer.write(" ; ");
////            writer.write(numTotal_CliDistintos);
////            writer.write(" ; ");
////            writer.write(numTotal_OrcmValidos);
////            writer.write(" ; ");
////            writer.write(precoTotal_Clientes);
////            writer.write(" ; ");
////            writer.write(precoTotal_Hoteis);
////            writer.write(" ; ");
////            writer.write(precoTotal_Voos);
////
////
////            writer.close();
////
////        }
////
////        catch (IOException e) {
////            System.out.println("\n\t\tFalha ao criar arquivo...");
////        }
////
////    }
////
//
////    // Classe Runnable para processar clientes em paralelo
////    static class ProcessaClientes implements Runnable
////    {
////        private ReservaSystem rs;
////        private List<Cliente> clientes;
////
////        public ProcessaClientes(ReservaSystem rs, List<Cliente> clientes) {
////            this.rs = rs;
////            this.clientes = clientes;
////        }
//
////        @Override
////        public void run() {
////            //  Lista auxiliar
////            List<Cliente> auxCli = new ArrayList<>();
////
////            for (Cliente cli : clientes) {
////                // Criando orcamento de cada cliente
////                rs.criaOrcamento(rs, cli);
////                Orcamento orcm = rs.getOrcamento(cli);
////                orcm.displayOrcamento();
////
////                // Avaliação de cada cliente, aleatoriamente cada um aceitando ou não
////                //boolean aceita = new Random().nextBoolean();
////                boolean aceita = true;
////                rs.avaliaOrcamento(aceita, orcm);
////
////
////                /*      Com cliente já registrado, adiciona o mesmo
////                 a lista auxiliar, referente a cada nome        */
////                if (!auxCli.stream().anyMatch(c -> c.getNome().equals(cli.getNome())))
////                    auxCli.add(cli);
////
////            }
////
////            //  Por fim, copia a lista auxiliar à lista de Clientes Distintos
////            rs.getClientesDistintos().addAll(auxCli);
////
////        }
////    }
////
//
//
//
//
//    public static void processaClientes_Paralelo(ReservaSystem rs, String nomeArq) {
//        int totalPedidos = rs.getClientes().size();
//        int pedidosAtendidos = 0;
//        double totalGastoClientes = 0;
//        double totalGastoHoteis = 0;
//        double totalGastoVoos = 0;
//
//        // Criar threads para processar os clientes
//        ReservaSystem_Runnable runnable1 = new ReservaSystem_Runnable(rs);
//        ReservaSystem_Runnable runnable2 = new ReservaSystem_Runnable(rs);
//        ReservaSystem_Runnable runnable3 = new ReservaSystem_Runnable(rs);
//
//        Thread thread1 = new Thread(runnable1);
//        Thread thread2 = new Thread(runnable2);
//        Thread thread3 = new Thread(runnable3);
//        thread1.start();
//        thread2.start();
//        thread3.start();
//
//        // Aguardar todas as threads terminarem
//        try {
//            thread1.join();
//            thread2.join();
//            thread3.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        // Coletar resultados das threads
//        pedidosAtendidos += runnable1.getPedidosAtendidosLocal();
//        pedidosAtendidos += runnable2.getPedidosAtendidosLocal();
//        pedidosAtendidos += runnable3.getPedidosAtendidosLocal();
//        totalGastoClientes += runnable1.getTotalGastoClientesLocal();
//        totalGastoClientes += runnable2.getTotalGastoClientesLocal();
//        totalGastoClientes += runnable3.getTotalGastoClientesLocal();
//        totalGastoHoteis += runnable1.getTotalGastoHoteisLocal();
//        totalGastoHoteis += runnable2.getTotalGastoHoteisLocal();
//        totalGastoHoteis += runnable3.getTotalGastoHoteisLocal();
//        totalGastoVoos += runnable1.getTotalGastoVoosLocal();
//        totalGastoVoos += runnable2.getTotalGastoVoosLocal();
//        totalGastoVoos += runnable3.getTotalGastoVoosLocal();
//
//        List<String> clientesDistintos = rs.getClientesDistintos();
//
//        // Escrever no arquivo de saída
//        try (FileWriter writer = new FileWriter(nomeArq)) {
//            writer.append(String.format("%d;%d;%d;%.2f;%.2f;%.2f\n",
//                    totalPedidos,
//                    clientesDistintos.size(),
//                    pedidosAtendidos,
//                    totalGastoClientes,
//                    totalGastoHoteis,
//                    totalGastoVoos));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//    //
//    public static void processaClientes_Sequencial(ReservaSystem rs, String nomeArq){
//
//        //  Lista auxiliar
//        List<String> auxCli = new ArrayList<>();
//
//        //  Input de clientes
//        for ( Cliente cli: rs.getClientes())
//        {
//
//            //  Criando orcamento de cada cliente
//            rs.criaOrcamento(cli);
//            Orcamento orcm = rs.getOrcamento(cli);
//
//            orcm.displayOrcamento();
//
//            //  Avaliação de cada cliente, aleatoriamente cada um aceitando ou não
//            Random rand = new Random();
//            //Boolean aceita = rand.nextBoolean();
//            Boolean aceita = true;
//            boolean orcmValido = rs.avaliaOrcamento(aceita, orcm);
//
//            double novoSaldo = cli.getSaldo() - orcm.getPrecoTotalOrcamento();
//            if (orcmValido)
//                cli.setSaldo(novoSaldo);
//
//
//            /*      Com cliente já registrado, adiciona o mesmo
//             a lista auxiliar, referente a cada nome        */
//            if (auxCli.isEmpty() || !auxCli.contains(cli.getNome()))
//                auxCli.add(cli.getNome());
//
//        }
//
//        //  Por fim, copia a lista auxiliar à lista de Clientes Distintos
//        rs.getClientesDistintos().addAll(auxCli);
//
//
//        // Escrever no arquivo de saída
//        try (FileWriter writer = new FileWriter(nomeArq)) {
//
//            int  numTotal_CliDistintos = rs.getClientesDistintos().size(),
//                    numTotal_Orcm = rs.getOrcamentos().size(),
//                    numTotal_OrcmValidos = rs.getOrcamentosValidos().size();
//
//
//            double  precoTotal_Clientes = rs.getPrecoTotalClientes(),
//                    precoTotal_Voos = rs.getPrecoTotalVoos(),
//                    precoTotal_Hoteis = rs.getPrecoTotalHoteis();
//
//            writer.append(String.format("%d;%d;%d;%.2f;%.2f;%.2f\n",
//                    numTotal_Orcm,
//                    numTotal_CliDistintos,
//                    numTotal_OrcmValidos,
//                    precoTotal_Clientes,
//                    precoTotal_Hoteis,
//                    precoTotal_Hoteis));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//
//    }
//
//
//
//
//}
//
//
//
