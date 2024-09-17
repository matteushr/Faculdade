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
     * Lê cada linha de instrução do arquivo e formata para conter apenas espaços
     * simples, oque
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


        System.out.println("\nLENDO ARQUIVO " + arquivo.getName() + ". ELE POSSUI O SEGUINTE CONTEÚDO:");
        BufferedReader leitorDeArquivo = new BufferedReader(new FileReader(arquivo));

    
        String linhaDaInstrucao;
        while ((linhaDaInstrucao = leitorDeArquivo.readLine()) != null) {

            if (!linhaDaInstrucao.trim().isEmpty()) {

                String linhaFormatada = linhaDaInstrucao.trim().replaceAll("\\s+", " ");
                instrucoes.add(linhaFormatada);
            }
        }
        
        leitorDeArquivo.close();

    }

    public List<String> getInstrucoes() {
        return instrucoes;
    }
    

}
