
/**
 * Esta classe representa um Leitor de arquivos. Ele tem capacidade de ler todos os arquivos no formato .txt em um diretório
 * e em seguida, enviar cada arquivo separado por um 
 *
 * @author Matheus Henrique Araujo
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;
import java.util.regex.Pattern;

public class Leitor {

    public Leitor() {

    }

    /**
     * 
     * Este método procura por arquivos com a extensão .txt no diretório definido
     * pela constante DIRECTORY. Para cada arquivo encontrado, chama o método
     * readFile para processá-lo.
     * 
     * @param diretorio O caminho completo do diretório a ser pesquisado.
     * @throws IOException Se ocorrer um erro durante a operação de leitura dos arquivos.
     *
     * 
     */
    public boolean localizouArquivos(String diretorio) throws IOException {
        File directory = new File(diretorio);

        if (!directory.isDirectory()) {
            System.out.println("O caminho fornecido não é um diretório.");
            return false; // Diretório inválido
        }

        // Verifica se existem arquivos .txt no diretório
        File[] arquivosTxt = directory.listFiles((dir, name) -> name.endsWith(".txt"));
        if (arquivosTxt == null || arquivosTxt.length == 0) {
            System.out.println("Nenhum arquivo .txt encontrado no diretório.");
            return false; // Nenhum arquivo .txt encontrado
        }

        return true; // Diretório e arquivos .txt encontrados
    }

    // Retorna os arquivos .txt do diretório
    public File[] receberArquivos(String diretorio) {
        File directory = new File(diretorio);
        
        File[] diretorios = directory.listFiles((dir, name) -> name.endsWith(".txt"));
        System.out.println("ARQUIVOS LOCALIZADOS. AQUIVOS:\n");
        for(File diretoriox: diretorios) {
            System.out.println(diretoriox.getName());
        }

        return diretorios;
    }


    


}
