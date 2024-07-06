import java.util.ArrayList;
import java.util.List;

public class ReservaSystem
{
    private List<Cliente> clientes;
    private List<String> clientesDistintos;
    private List<Hotel> hoteis;
    private List<Voo> voos;

    private List<Orcamento> orcamentos, orcamentosValidos;

    private ScanFiles sc;




    private double precoTotalClientes, precoTotalVoos, getPrecoTotalHoteis;

    public ReservaSystem(List<Cliente> clientes, List<Hotel> hoteis, List<Voo> voos){
        this.clientes = clientes;
        clientesDistintos = new ArrayList<>();
        this.hoteis = hoteis;
        this.voos = voos;
        orcamentos = new ArrayList<>();
        orcamentosValidos = new ArrayList<>();
        precoTotalClientes = precoTotalVoos = getPrecoTotalHoteis = 0;
        sc = new ScanFiles();
    }

    public void criaOrcamento(Cliente cli){

        Orcamento orcm = new Orcamento(this, cli);
        orcamentos.add(orcm);

        if ( clientesDistintos.isEmpty() || !clientesDistintos.contains(cli.getNome()))
            clientesDistintos.add(cli.getNome());

    }

    public Orcamento getOrcamento(Cliente cli){

        List<Orcamento> aux = new ArrayList<>(orcamentos);

        for ( Orcamento orcm: aux){

            if (cli.equals(orcm.getCliente()))
                return orcm;


        }

        return null;
    }

    public boolean avaliaOrcamento(Boolean bool, Orcamento orcamento){


        // Se orcamento é valido de registro
        if ( orcamento.getValido() == true
             && orcamento.getPrecoTotalOrcamento() <= orcamento.getCliente().getSaldo())
        {
            //  Se clienteb ACEITA orcamento
            if (bool.equals(Boolean.TRUE))
            {
                System.out.println("\n\t\tOrcamento ACEITO!!");

                //  Adiciona orcamento aos validos
                orcamentosValidos.add(orcamento);

                //  Subtrai numero de vagas do(s) voo(s) escolhido(s)

                orcamento.getVoo1().subtrAssentos();

                if (orcamento.getVoo2() != null)
                    orcamento.getVoo2().subtrAssentos();

                //  Subtrai numero de vagas do Hotel escolhido
                if (orcamento.getHotel() != null)
                    orcamento.getHotel().subtrVagas();

                //  Calcula precoVoo gasto do orcamento
                precoTotalVoos += orcamento.getPrecoTotalVoo();

                //  Calcula precoHotel gasto do orcamento
                getPrecoTotalHoteis += orcamento.getPrecoTotalHotel();

                //  Calcula valorTotal gasto pelo cliente ( voo(s) + hotel )
                precoTotalClientes += precoTotalVoos + getPrecoTotalHoteis;

                return true;

            }

            else {
                System.out.println("\n\t\tOrcamento NAO aceito...");

            }
        }

        return false;
    }


    public List<Orcamento> getOrcamentos(){
        return orcamentos;
    }

    public List<Orcamento> getOrcamentosValidos(){
        return orcamentosValidos;
    }


    public Cliente getCliente(String nome){
        for (Cliente cli: clientes){
            if (nome.equals(cli.getNome()))
                return cli;
        }

        return null;
    }



    public List<Cliente> getClientes(){
        return clientes;
    }

    public List<String> getClientesDistintos(){
        return  clientesDistintos;
    }

    public List<Hotel> getHoteis(){
        return hoteis;
    }


    public List<Voo> getVoos(){
        return voos;
    }


    public double getPrecoTotalClientes(){
        return precoTotalClientes;
    }


    public double getPrecoTotalVoos(){
        return precoTotalVoos;
    }


    public double getPrecoTotalHoteis(){
        return getPrecoTotalHoteis;
    }


    public ScanFiles getSc(){
        return sc;
    }

    public void setSc(ScanFiles sc){
        this.sc = sc;
    }




    public void displayInfoGeral(){

        System.out.println("\n\tClientes Infos:\n");
        System.out.println("|\t\tNomeCliente\t\t" + "|\tCidEntrada\t" + "|\tCidSaida\t" + "|\t\tDias\t" + "|\t\tStars\t" + "|\t\tSaldo\t");
        System.out.println(".............................................................................................................");
        for (Cliente cli: clientes){
            cli.displayInfo();
            System.out.println();
        }

        System.out.println("\n\tHoteis Infos:\n");
        System.out.println("|\t\tCidade\t\t" + "|\t\tNomeHotel\t\t" + "|\tVagas\t\t" + "|\t\tPreço\t\t" + "|\tClassific/Stars");
        System.out.println(".............................................................................................................");
        for (Hotel hot: hoteis){
            hot.displayInfo();
            System.out.println();
        }

        System.out.println("\n\tVoos Infos:\n");
        System.out.println("|\tCidEntrada\t" + "|\tCidSaida\t" + "|\t\t\tData\t\t" + "|\t\tHorario\t\t" + "|\tAssentos\t" + "|\t\tValor");
        System.out.println(".............................................................................................................");
        for (Voo voo: voos){
            voo.displayInfo();
            System.out.println();
        }

    }

}
