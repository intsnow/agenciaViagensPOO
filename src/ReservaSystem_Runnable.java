import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ReservaSystem_Runnable implements Runnable{


        private List<Cliente> clientes;
        private List<Voo> voos;
        private List<Hotel> hoteis;
        private List<String> nomesClientes_Dist;
        private List<Orcamento> orcamentos, orcamentosValidos;

        private ReservaSystem rs;

        private int pedidosAtendidosLocal = 0;
        private double totalGastoClientesLocal = 0;
        private double totalGastoHoteisLocal = 0;
        private double totalGastoVoosLocal = 0;

        public ReservaSystem_Runnable(ReservaSystem rs)
        {
            this.clientes = rs.getClientes();
            this.voos = rs.getVoos();
            this.hoteis = rs.getHoteis();
            this.nomesClientes_Dist = new ArrayList<>();
            orcamentos = new ArrayList<>();
            orcamentosValidos = new ArrayList<>();
            this.rs = rs;
        }




        @Override
        public void run() {
            for (Cliente cliente : clientes)
            {
                //Orcamento orcamento = gerarOrcamento(cliente);
                rs.criaOrcamento(cliente);
                Orcamento orcamento = rs.getOrcamento(cliente);

                boolean aceita = true;
                boolean orcmValido = rs.avaliaOrcamento(aceita, orcamento);

                if (orcamento != null)
                {
                    orcamento.displayOrcamento();

                    if (nomesClientes_Dist.isEmpty() || !nomesClientes_Dist.contains(cliente.getNome()))
                        nomesClientes_Dist.add(cliente.getNome());


                    if (orcmValido)
                    {

                        orcamentosValidos.add(orcamento);

                        Hotel hotel = orcamento.getHotel();

                        Voo voo1 = new Voo(), voo2 = new Voo();
                        List<Boolean> vooValido = new ArrayList<>();
                        boolean hotelValido = false;


                        if (orcamento.getVoo1() != null)
                        {
                            voo1 = orcamento.getVoo1();
                            vooValido.add(voo1.vooValido());

                            if (orcamento.getVoo2() != null) {
                                voo2 = orcamento.getVoo2();
                                vooValido.add(voo2.vooValido());
                            }


                        }

                        //boolean confirmaVoo = voo.();
                        if (hotel != null)
                            hotelValido = hotel.hotelValido();

                        if (vooValido.contains(true) )
                        {

                            //pedidosAtendidosLocal++;
                            totalGastoClientesLocal += orcamento.getPrecoTotalOrcamento();
                            totalGastoHoteisLocal += orcamento.getPrecoTotalHotel();
                            totalGastoVoosLocal += orcamento.getPrecoTotalVoo();
                            cliente.setSaldo(cliente.getSaldo() - orcamento.getPrecoTotalOrcamento());
                        }
//                        else
//                        {
//                            if (voo1 != null){
//                                voo1.liberaReserva();
//
//                                if(voo2 != null)
//                                    voo2.liberaReserva();
//
//                            }
//
//                            if (hotelValido)
//                                hotel.liberaReserva();
//
//                        }
                    }

                }
            }
        }

//        private Orcamento gerarOrcamento(Cliente cliente) {
//            Voo voo1 = null, voo2 = null;
//            Hotel hotel = null;
//
//            for (Voo voo : voos) {
//                if (voo.getCidEntrd().equals(cliente.getCidEntrd()) && voo.getCidSaida().equals(cliente.getCidSaida()) && voo.getAssentos() > 0) {
//                    if (vooEscolhido == null || voo.getPreco() < vooEscolhido.getPreco()) {
//                        vooEscolhido = voo;
//                    }
//                }
//            }
//
//            for (Hotel hotel : hoteis) {
//                if (hotel.getCidade().equals(cliente.getCidSaida()) && hotel.getStars() >= cliente.getReqStars() && hotel.getVagas() > 0) {
//                    if (hotelEscolhido == null || hotel.getPreco() < hotelEscolhido.getPreco()) {
//                        hotelEscolhido = hotel;
//                    }
//                }
//            }
//
//            if (vooEscolhido != null && hotelEscolhido != null) {
//                return new Orcamento(rs, cliente);
//            } else {
//                return null;
//            }
//        }

        public int getPedidosAtendidosLocal() {
            //return contarOrcamentosValidos();
            return orcamentosValidos.size();
        }

        public double getTotalGastoClientesLocal() {
            return totalGastoClientesLocal;
        }

        public double getTotalGastoHoteisLocal() {
            return totalGastoHoteisLocal;
        }

        public double getTotalGastoVoosLocal() {
            return totalGastoVoosLocal;
        }

        public int contarOrcamentosValidos() {

            int contador = 0;

            for (Cliente cliente : clientes) {
                rs.criaOrcamento(cliente);
                Orcamento orcamento = rs.getOrcamento(cliente);
                boolean aceita = true;
                boolean orcmValido = rs.avaliaOrcamento(aceita, orcamento);

                if (orcamento != null && orcmValido) {
                    contador++;
                    // Atualize os totais e saldos conforme necessário
                }
            }

            return contador;
        }


}
