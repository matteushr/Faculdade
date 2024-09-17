import java.io.File;
import java.io.IOException;
import java.util.*;

class MIPSProcessor {
    private Leitor leitorDeArquivos;
    private InterpretadorMIPS interpretador;
    private List<List<String>> instrucoesDivididasEmPartes;
    private Solucoes tecnicasDeResolucao;
    private Escritor escritor;
    private File[] arquivosDasInstrucoes;

    // Construtor inicializa as classes e variáveis
    public MIPSProcessor() throws IOException {
        leitorDeArquivos = new Leitor();
        interpretador = new InterpretadorMIPS();
        instrucoesDivididasEmPartes = new ArrayList<>();
        tecnicasDeResolucao = new Solucoes();
        escritor = new Escritor();
    }

    // Métodos de resolução para diferentes técnicas
    public void resolverComBolha(String nomeDoArquivo) {
        List<List<String>> resultado = tecnicasDeResolucao.resolverComBolhas(instrucoesDivididasEmPartes);
        escritor.salvarEmArquivo(resultado, nomeDoArquivo);
    }

    public void resolverComReordenamentoComBolha(String nomeDoArquivo) {
        List<List<String>> resultado = tecnicasDeResolucao.resolverComReordenamentoComBolha(instrucoesDivididasEmPartes);
        escritor.salvarEmArquivo(resultado, nomeDoArquivo);
    }

    public void resolverComAdiantamentoEBolha(String nomeDoArquivo) {
        List<List<String>> resultado = tecnicasDeResolucao.resolverComAdiantamentoEBolha(instrucoesDivididasEmPartes);
        escritor.salvarEmArquivo(resultado, nomeDoArquivo);
    }

    public void adiantamentoEReordenamentoBolha(String nomeDoArquivo) {
        List<List<String>> resultado = tecnicasDeResolucao.resolverComAdiantamentoEReordenamentoComBolha(instrucoesDivididasEmPartes);
        escritor.salvarEmArquivo(resultado, nomeDoArquivo);
    }

    // Executa o processamento dos arquivos
    public void executar() throws IOException {
        if (!leitorDeArquivos.localizouArquivos(leitorDeArquivos.getDiretorio())) {
            return;
        }

        arquivosDasInstrucoes = leitorDeArquivos.receberArquivos();

        for (int i = 0; i < arquivosDasInstrucoes.length; i++) {
            processarInstrucoesESalvar(arquivosDasInstrucoes[i]);
            String nomeDoArquivo = arquivosDasInstrucoes[i].getName();

            if (i <= 1) {
                resolverComBolha(nomeDoArquivo);
            } else if (i > 1 && i <= 3) {
                resolverComAdiantamentoEBolha(nomeDoArquivo);
            } else if (i > 3 && i <= 6) {
                resolverComReordenamentoComBolha(nomeDoArquivo);
            } else if (i > 6 && i <= 9) {
                adiantamentoEReordenamentoBolha(nomeDoArquivo);
            }

            instrucoesDivididasEmPartes.clear();
        }
    }

    // Processa instruções e as salva em uma lista dividida em partes
    private void processarInstrucoesESalvar(File arquivo) throws IOException {
        interpretador.lerArquivo(arquivo);
        List<String> instrucoes = interpretador.getInstrucoes();

        for (String instrucao : instrucoes) {
            if (!instrucao.equals("nop")) {
                instrucoesDivididasEmPartes.add(dividirInstrucao(instrucao));
            }
        }
    }

    // Divide uma instrução em partes menores e retorna uma lista
    private List<String> dividirInstrucao(String instrucao) {
        List<String> instrucaoDividida = new ArrayList<>();
        String[] partes = instrucao.replace(",", "").replaceAll("\\s+", " ").split(" ");

        Collections.addAll(instrucaoDividida, partes);
        return instrucaoDividida;
    }
}
