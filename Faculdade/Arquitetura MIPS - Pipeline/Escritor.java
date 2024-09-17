import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;


public class Escritor {
    
    
    public Escritor() {

    }

    public void salvarEmArquivo(List<List<String>> conteudoTrabalhado, String nomeDoArquivo) {

        String arquivoResultado = nomeDoArquivo.replace(".txt", "").concat("-RESULTADO.txt");

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter("Arquivos" + File.separator + arquivoResultado))) {
            for (List<String> instrucao : conteudoTrabalhado) {  
                // Junta as partes da instrução em uma única String, separando por espaço  
                String linha = String.join(", ", instrucao);  
                escritor.write(linha);  
                escritor.newLine(); // Adiciona uma nova linha após cada instrução  
            }  
            System.out.println("Instruções MIPS salvas com sucesso em " + arquivoResultado);
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }

        
    }
}
