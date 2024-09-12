import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InterpretadorMIPS {

    private File[] arquivos;
    public List<String> instructions;
    private ListaDE parsedInstructions;
    private Leitor leitor;

    public InterpretadorMIPS() throws IOException {
        leitor = new Leitor();
        arquivos = leitor.localizarArquivos("Testes");
        instructions = new ArrayList<String>();
    }

    /**
     * 
     *
     * @param arquivoParaModificar Arquivo para leitura e interpretação
     * @throws IOException 
     * 
     */

     public void lerTodosArquivos() throws IOException {
        
            readFile(arquivos[1]);
        
     }

    public void readFile(File arquivo) throws IOException {

        // String ArquivoNovoGerado = arquivoParaModificar.getName().replace(".txt", "")
        // + "-RESULTADO.txt";

       

            BufferedReader leitorDeArquivo = new BufferedReader(new FileReader(arquivo));

            // O parâmetro true indica que eu tenho a intenção de escrever, em vez de
            // sobescrever o arquivo.
            // BufferedWriter escritor = new BufferedWriter(new
            // FileWriter(ArquivoNovoGerado, true));

            String line;
            while ((line = leitorDeArquivo.readLine()) != null) {
                String linhaFormatada = line.trim().replaceAll("\\s+", " ");
                instructions.add(linhaFormatada);
            }
            // escritor.close();
            leitorDeArquivo.close();

    }

    
        public <T> void imprimirLista(List<T> lista) {
            System.out.println("Elementos de instrução mips na lista:");
            for (T elemento : lista) {
                System.out.println(elemento);
            }
    }
    


public static void main(String[] args) throws IOException {
    InterpretadorMIPS i = new InterpretadorMIPS();

    i.lerTodosArquivos();
    i.imprimirLista(i.instructions);

}

}
