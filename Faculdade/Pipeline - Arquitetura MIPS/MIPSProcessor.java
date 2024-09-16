import java.io.File;
import java.io.IOException;
import java.util.*;

class MIPSProcessor {
    private Leitor leitorDeArquivos;
    private InterpretadorMIPS interpretador;
    private List<List<String>> instrucoesDivididasEmPartes;
    private Solucoes tecnicasDeResolucao;

    private final String DIRETORIO = "Testes";
    File[] arquivosDasInstrucoes;

    public MIPSProcessor() throws IOException {
        leitorDeArquivos = new Leitor();
        interpretador = new InterpretadorMIPS();
        instrucoesDivididasEmPartes = new ArrayList<>();
        tecnicasDeResolucao = new Solucoes();
    }

    public void resolverComBolha() {
        tecnicasDeResolucao.resolverComInsercaoDeBolha(instrucoesDivididasEmPartes);
        tecnicasDeResolucao.printarResultado();
    }

    public void executar() throws IOException {
        
        if (!leitorDeArquivos.localizouArquivos(DIRETORIO)) {
           return;
        }

        arquivosDasInstrucoes = leitorDeArquivos.receberArquivos(DIRETORIO);
        processarInstrucaoESeparar();

    }

    public void imprimirListaFormatada() {
        int index = 1; // Para numeração das listas internas
        System.out.println("\nEsta é a lista do "+arquivosDasInstrucoes[1].getName()+ ": \n");
        for (List<String> lista : instrucoesDivididasEmPartes) {
            System.out.print("Lista " + index + ": ");
            for (String elemento : lista) {
                System.out.print(elemento + ",");
            }
            System.out.println(); // Nova linha após cada lista interna
            index++;
        }
    }

    /**
     *  Recebe como parâmetro uma Lista que contém todas as instruções de um arquivo separadas por linha
     *  que foram recebidas 
     * @throws IOException
    */
    public void processarInstrucaoESeparar() throws IOException {

        //Realiza a leitura de todas as linhas do arquivo
        interpretador.lerArquivo(arquivosDasInstrucoes[1]);

        List<String> instrucoes = interpretador.getInstrucoes();

        for(String instrucao : instrucoes) {
           instrucoesDivididasEmPartes.add(dividirInstrucao(instrucao));
        }



    }

    public List<String> dividirInstrucao(String instrucao) {

        List<String> instrucaoDividida = new ArrayList<String>();
        String[] instrucoesEmPartesMenores;

        instrucoesEmPartesMenores = instrucao.replace(",", "").replaceAll("\\s+", " ").split(" ");

        for (String parteDaInstrucao : instrucoesEmPartesMenores) {
            instrucaoDividida.add(parteDaInstrucao);
        }

        //interpretador.imprimirLista(instrucaoDividida);

        return instrucaoDividida;

    }

    public static void main(String[] args) throws IOException {
        MIPSProcessor m = new MIPSProcessor();
        

        m.executar();
        m.imprimirListaFormatada();
        m.resolverComBolha();
        


    }
}
