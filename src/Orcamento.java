import java.util.*;

public class Orcamento
{
    private Cliente cliente;
    private Hotel hotel;
    private Voo voo1, voo2;
    private List<Voo> voos1, voos1Final, voos2, voosFinais;

    private double precoTotalOrcamento, precoTotalVoo, precoTotalHotel;
    private boolean valido;
    private ReservaSystem rs;


    public Orcamento(ReservaSystem rs, Cliente cliente){
        this.cliente = cliente;
        this.rs = rs;
        precoTotalVoo = precoTotalHotel = 0;
        start();
    }

    public void setValido(){
        if (cliente.getSaldo() >= precoTotalOrcamento)
            valido = true;
        else
            valido = false;
    }

    public boolean getValido(){
        return valido;
    }

    public Cliente getCliente(){
        return cliente;
    }

    public Voo getVoo1(){
        return voo1;
    }

    public Voo getVoo2(){
        return voo2;
    }

    public List<Voo> getVoosFinais(){
        return voosFinais;
    }

    public Hotel getHotel(){
        return hotel;
    }



    public void start(){
        buscaVoo();
        buscaHotel();
        setPrecoTotalOrcamento();
        setValido();
    }

    public void buscaHotel()
    {
        double valor = 0;
        List<Double> precos = new ArrayList<>();

        //  Lista precos e organiza hoteis referentes a cidade
        for (Hotel hot: rs.getHoteis())
        {
            if (hot.getCidade().equals(cliente.getCidSaida()))
                precos.add(hot.getPreco());
        }

        if (!precos.isEmpty())
        {
            precos.sort(Comparator.naturalOrder());
            double menorPreco = precos.get(0);

            for (Double preco : precos)
            {
                for (Hotel hot : rs.getHoteis())
                {
                    if (cliente.getCidSaida().equals(hot.getCidade())
                            && hot.getStars() >= cliente.getReqStars()
                            && hot.getVagas() >= 1 && hot.getPreco() == menorPreco)
                        hotel = hot;

                }
            }

            //  Calcula precoTotal do hotel, referente a qtd. de dias do cliente
            if (hotel != null)
                precoTotalHotel = hotel.getPreco()*cliente.getDias();

        }

    }


    public void buscaVoo()
    {
        /*      Coleta hotel de menor preco, conforme
            as vagas disponiveis e estrelas requisitadas
        */

        //  1° voo (ou vôo unico, dependendo da necessidade)
        voo1 = new Voo();

        //      Inicializa listas de vôos, para cada uma conter
        //  somente as cidades desejadas pelo cliente
        voos1 = new ArrayList<>();
        voos2 = new ArrayList<>();


        // Coleta voos referentes a cidEntrada
        for (Voo voo: rs.getVoos())
        {
            if ( voo.getAssentos() >= 1
                &&  cliente.getCidEntrd().equals(voo.getCidEntrd()) )
            {
                if (!voos1.contains(voo))
                    voos1.add(voo);
            }

        }

        List<Boolean> precisa2voo = new ArrayList<>();

        // Se existe um unico voo (ou o 1° vôo, dependendo do contexto)
        if (!voos1.isEmpty())
        {
            // Verifica necessidade de cliente pegar 2 vôos
            for (Voo v: voos1)
            {
                if ( cliente.getCidEntrd().equals(v.getCidEntrd())
                    && cliente.getCidSaida().equals(v.getCidSaida()))
                    precisa2voo.add(Boolean.FALSE);
            }

            // Se tiver necessidade de um segundo voo:
            if ( !precisa2voo.contains(Boolean.FALSE))
            {
                // Conforme cada 1° vôo, adiciona possiveis 2° vôos
                for (Voo v : voos1)
                {
                    for (Voo voo : rs.getVoos())
                    {
                        if ( voo.getAssentos() >= 1
                                && v.getCidSaida().equals(voo.getCidEntrd())
                                && voo.getCidSaida().equals(cliente.getCidSaida())
                                && !voos2.contains(voo)
                            )
                                voos2.add(voo);
                    }
                }

                // Compara cidSaida de cada voo em "voos1" com a cidEntrada, dos de "voos2"
                voos1Final = new ArrayList<>();

                for (Voo v1: voos1)
                {
                    for (Voo v2: voos2)
                    {
                        if (v1.getCidSaida().equals(v2.getCidEntrd()) && !voos1Final.contains(v1))
                            voos1Final.add(v1);
                    }
                }

            }

            double menorPreco = 0;
            List<Double> compPrecos = new ArrayList<>();

            // Se existir "voos1Final", entao serão necessarias 2 ligações (vôos)
            if (voos1Final != null)
            {
                voo2 = new Voo();

                //      Cria mais 2 listas, agora para filtrar
                //  somente aos voos que se ligam entre as cidades
                List<Voo> compVoos1 = new ArrayList<>(),
                          compVoos2 = new ArrayList<>();

                List<List<Voo>> compVoosResu = new ArrayList<>();

                for (Voo v2 : voos2)
                {
                    for (Voo v1 : voos1Final)
                    {
                        if ((menorPreco == 0 && v1.getCidSaida().equals(v2.getCidEntrd()))
                                || (v1.getPreco() <= menorPreco && v1.getCidSaida().equals(v2.getCidEntrd())))
                        {
                            menorPreco = v1.getPreco();

                            if (!compVoos1.contains(v1))
                                compVoos1.add(v1);
                        }
                    }

                    // Add v2 a cada leitura de todos os v1
                    if (!compVoos2.contains(v2))
                        compVoos2.add(v2);

                    menorPreco = 0;

                }


                /*       Tendo as duas listas de comparação de voos completas,
                    agora coleta os de menores preços e atribui a lista de listas de voos
                    (cada lista_elemento representando cada ligação de voos possivel    */

                for (Voo v1: compVoos1)
                {
                    for (Voo v2: compVoos2)
                    {
                        if (v1.getCidSaida().equals(v2.getCidEntrd()) )
                        {
                            List<Voo> vs = new ArrayList<>();
                            vs.add(v1);
                            vs.add(v2);
                            compVoosResu.add(vs);

                        }
                    }

                }


                //      Por fim, coleta os voos de menores preços:

                //  Cria lista para por precos
                List<Double> precos = new ArrayList<>();

                // Cria mapa cada preco, referente às listas de voos (dupla de voos)
                HashMap<Double, List<Voo>> mapVoo = new HashMap<>();

                //  Calcula preco total de cada dupla e add na listaPrecos
                for (List<Voo> vs: compVoosResu)
                {
                    double preco = 0;

                    for (Voo v: vs)
                        preco += v.getPreco();

                    mapVoo.put(preco, vs);
                    precos.add(preco);
                }

                if (!precos.isEmpty())
                {
                    //  Com listaPrecos pronta, coleta o menor preco
                    precos.sort(Comparator.naturalOrder());
                    menorPreco = precos.get(0);
                    double finalMenorPreco = menorPreco;


                /*          Cria lista de voosFinal.
                   Essa lista conterá a dupla de voos mais baratos   */

                    List<Voo> voosFinal = new ArrayList<>();
                    mapVoo.forEach((preco, voos) ->
                    {
                        if (preco == finalMenorPreco) {
                            for (Voo v : voos)
                                voosFinal.add(v);
                        }
                    });

                    //  Por fim, atribui cada voo das duplas em voo1 e voo2
                    voo1 = voosFinal.get(0);
                    voo2 = voosFinal.get(1);

                    voosFinal.add(voo1);
                    voosFinal.add(voo2);

                }

            }

            /*      Senão, então apenas um único vôo é necessario!
                Logo, "voos1" é usado, ao invés de "voos1Final"     */
            else
            {
                for (Voo v1 : voos1)
                {
                    if (   ( menorPreco == 0 && v1.getCidSaida().equals(cliente.getCidSaida()) )
                        || ( v1.getPreco() <= menorPreco && v1.getCidSaida().equals(cliente.getCidSaida()) ) )
                    {
                        menorPreco = v1.getPreco();
                        voo1 = v1;
                    }
                }
            }

            // Atualiza PrecoTotal de Voos, após cada dupla (ou voo unico) atribuido

            if (voo1 != null)
            {
                if (voo2 != null)
                    precoTotalVoo = voo1.getPreco() + voo2.getPreco();

                else
                    precoTotalVoo = voo1.getPreco();
            }

        }

    }

    public void displayOrcamento()
    {
        if ( (cliente != null || hotel != null) && (voo1 != null || voo2 != null) )
        {
            valido = true;

            System.out.println("\n-------------------------------------------------------------------------------------------------------");
            System.out.println("\n\n\n\t\t\t Orçamento criado:");

            System.out.println("\nCliente referente:");
            cliente.displayInfo();

            if (voo1 != null)
            {
                if (voo2 != null){
                    System.out.println("\n\n\n1° Vôo selecionado:");
                    voo1.displayInfo();
                }
                else {
                    System.out.println("\n\n\nVôo ÚNICO selecionado:");
                    voo1.displayInfo();
                }
            }

            if (voo2 != null)
            {
                System.out.println("\n\n2° Vôo selecionado:");
                voo2.displayInfo();
            }

            if (hotel != null)
            {
                System.out.println("\n\nHotel selecionado:");
                hotel.displayInfo();
            }
            else
                System.out.println("\n\nHotel NÃO disponível, conforme as requisições do cliente");


            System.out.println("\n\n\n-------------------------------------------------------------------------------------------------------");
        }

        else
        {

            if (cliente == null || voo1 == null  )
                valido = false;

            System.out.println("\n-------------------------------------------------------------------------------------------------------");

            if (cliente == null)
                System.out.println("\nCliente indisponível!");

            else
            {
                if (hotel == null)
                    System.out.println("\n\t\tHotel NÃO disponível, conforme as requisições do cliente");

                if (voo1 == null)
                    System.out.println("\n\t\tVôo NÃO disponível, conforme as requisições do cliente");

                if (voo2 == null)
                    System.out.println("\n\t\t2° Vôo NÃO disponível, conforme as requisições do cliente");

            }

            System.out.println("\n\n\n-------------------------------------------------------------------------------------------------------");
        }

    }


    public double getPrecoTotalVoo()
    {
        if (voo1 != null)
        {
            if (voo2 != null)
                precoTotalVoo = voo1.getPreco() + voo2.getPreco();
            else
                precoTotalVoo = voo1.getPreco();
        }

        return precoTotalVoo;
    }

    public double getPrecoTotalHotel()
    {
        if (hotel != null)
            precoTotalHotel = hotel.getPreco();

        return cliente.getDias()*precoTotalHotel;
    }

    public void setPrecoTotalOrcamento(){
        precoTotalOrcamento = precoTotalHotel + precoTotalVoo;
    }


    public double getPrecoTotalOrcamento(){
        return precoTotalOrcamento;

    }

}
