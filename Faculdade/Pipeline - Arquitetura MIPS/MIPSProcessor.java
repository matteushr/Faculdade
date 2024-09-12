import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.List;

class MIPSProcessor {
    private Leitor leitorDeArquivos;
    private InterpretadorMIPS interpretador;

    private final String DIRETORIO = "Testes";
    File[] arquivos;

    public MIPSProcessor() throws IOException {
        leitorDeArquivos = new Leitor();
        arquivos = leitorDeArquivos.localizarArquivos(DIRETORIO);
    }

    // Processa o arquivo e faz o parsing
    public void process() {
        fileReader.readFile();
        this.mipsParser = new MIPSParser(fileReader.getInstructions());
        mipsParser.parseAll();
    }

    // Exibe as instruções parseadas
    public void showParsedInstructions() {
        List<List<String>> parsedInstructions = mipsParser.getParsedInstructions();
        for (List<String> instr : parsedInstructions) {
            System.out.println(instr);
        }
    }
}