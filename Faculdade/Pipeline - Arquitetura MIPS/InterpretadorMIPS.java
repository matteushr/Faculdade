import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InterpretadorMIPS {

    public List<String> instrucoes;

    public InterpretadorMIPS() throws IOException {

        instrucoes = new ArrayList<String>();
    }

    /**
     * Lê cada linha de instrução do arquivo e formata para conter apenas espaços simples, oque
     * formaliza o formato e torna a interpretação das instruções mais simples.
     * 
     * Ao formatar, envia esta linha para uma lista, que conterá todas as instruções
     * 
     *
     * @param arquivo Arquivo para leitura e interpretação
     * @throws IOException
     * 
     */

    public void lerArquivo(File arquivo) throws IOException {

        // String ArquivoNovoGerado = arquivoParaModificar.getName().replace(".txt", "")
        // + "-RESULTADO.txt";
        System.out.println("\nLENDO ARQUIVO "+arquivo.getName()+". ELE POSSUI O SEGUINTE CONTEÚDO:");
        BufferedReader leitorDeArquivo = new BufferedReader(new FileReader(arquivo));

        // O parâmetro true indica que eu tenho a intenção de escrever, em vez de
        // sobescrever o arquivo.
        // BufferedWriter escritor = new BufferedWriter(new
        // FileWriter(ArquivoNovoGerado, true));

        String linhaDaInstrucao;
        while ( (linhaDaInstrucao = leitorDeArquivo.readLine()) != null) {

            String linhaFormatada = linhaDaInstrucao.trim().replaceAll("\\s+", " ");
            instrucoes.add(linhaFormatada);
        }
        // escritor.close();
        leitorDeArquivo.close();

    }

    public <T> void imprimirLista(List<T> lista) {
        System.out.println("\nElementos de instrução mips na lista:");
        for (T elemento : lista) {
            System.out.print(elemento + ", ");
        }
    }

    public List<String> getInstrucoes() {
        return instrucoes;
    }

}
