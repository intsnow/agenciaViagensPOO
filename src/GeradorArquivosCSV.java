import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GeradorArquivosCSV {

    public static void main(String[] args) {
        gerarClientesCSV();
        gerarHoteisCSV();
        gerarVoosCSV();
    }

    public static void gerarClientesCSV() {
        try {
            FileWriter writer = new FileWriter("clientes_1000.csv");
            Random random = new Random();
            String[] cidades = {"RIO", "SAO", "FLN", "CNF", "PMW", "REC", "FOR", "POA", "SSA", "BEL"};
            for (int i = 1; i <= 1000; i++) {
                String cidadeOrigem = cidades[random.nextInt(cidades.length)];
                String cidadeDestino = cidadeOrigem;
                while (cidadeDestino.equals(cidadeOrigem)) {
                    cidadeDestino = cidades[random.nextInt(cidades.length)];
                }

                int randId = random.nextInt(100);

                String cliente = "cliente " + randId + ";" + cidadeOrigem + ";" + cidadeDestino + ";" + (random.nextInt(5) + 1) + " dias;" + (random.nextInt(5) + 1) + " estrelas;R$ " + (random.nextInt(5000) + 1000) + "\n";
                writer.write(cliente);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void gerarHoteisCSV() {
        try {
            FileWriter writer = new FileWriter("hoteis.csv");
            Random random = new Random();
            String[] cidades = {"RIO", "SAO", "FLN", "CNF", "PMW", "REC", "FOR", "POA", "SSA", "BEL"};
            for (int i = 1; i <= 60; i++) {
                String cidade = cidades[random.nextInt(cidades.length)];
                String hotel = cidade + ";Hotel " + i + ";" + (random.nextInt(50) + 10) + " vagas;R$ " + (random.nextInt(1000) + 200) + ";" + (random.nextInt(5) + 1) + " estrelas\n";
                writer.write(hotel);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void gerarVoosCSV() {
        try {
            FileWriter writer = new FileWriter("voos.csv");
            Random random = new Random();
            String[] cidades = {"RIO", "SAO", "FLN", "CNF", "PMW", "REC", "FOR", "POA", "SSA", "BEL"};
            for (int i = 1; i <= 80; i++) {
                String cidadeOrigem = cidades[random.nextInt(cidades.length)];
                String cidadeDestino = cidadeOrigem;
                while (cidadeDestino.equals(cidadeOrigem)) {
                    cidadeDestino = cidades[random.nextInt(cidades.length)];
                }
                int assentos = (random.nextInt(400) + 100) / 10 * 10; // Números realistas de assentos
                String hora = String.format("%02d:%02d", random.nextInt(24), random.nextInt(60)); // Formatação do horário
                String data = String.format("%02d/%02d/2024", random.nextInt(30) + 1, random.nextInt(12) + 1); // Geração de datas diferentes
                String voo = cidadeOrigem + ";" + cidadeDestino + ";" + data + ";" + hora + ";" + assentos + " assentos;R$ " + (random.nextInt(800) + 200) + "\n";
                writer.write(voo);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}