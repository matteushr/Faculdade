import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * solução com inserção de bolhas/NOP.
 * solução com inserção de adiantamentos.
 * solução com reordenamento de instruções.
 */

public class Solucoes {

    private Opcodes verificarOpcodes;
    private List<List<String>> resultado;

    public Solucoes() {
        verificarOpcodes = new Opcodes();
        resultado = new ArrayList<>();

    }

    public void exibirInformacoes(List<String> instrucao) {

    }

    public void resolverComInsercaoDeBolha(List<List<String>> instructions) {
        List<List<String>> solucaoFinal = new ArrayList<>();

        for (int i = 0; i < instructions.size(); i++) {
            List<String> instrucao = instructions.get(i);
            solucaoFinal.add(instrucao);

            String registradorAtual = "";
            if (verificarOpcodes.contemOpcodeHazard(instrucao)) {
                // O primeiro registrador é analisado, pois é ele que precisa ser carregado pela
                // memoria
                registradorAtual = verificarOpcodes.getRegistrador(instrucao);
            }

            // Flag para controlar a inserção de NOPs
            boolean hazardResolvido = false;

            // Verificamos a próxima instrução
            if (i + 1 < instructions.size()) {
                verInstrucao(instrucao);
                List<String> proximaInstrucao = instructions.get(i + 1);
                String registradorProximoIgual = "";
                boolean isHazardOpcode = false;
                // Verifica se a instrução possui registradores e verifica se o algum dos
                // registradores corresponde ao da instrução sendo analisada
                if (!verificarOpcodes.contemRegistrador(proximaInstrucao)) {
                    isHazardOpcode = true;
                }

                if (proximaInstrucao.contains(registradorAtual)) {
                    registradorProximoIgual = registradorAtual;
                }

                // Verifica se há hazard que requer dois NOPs
                if (registradorAtual.equals(registradorProximoIgual)) {
                    // Escreve NOP duas vezes apenas se ainda não foi resolvido
                    if ( !(isHazardOpcode)) {
                        solucaoFinal.add(Arrays.asList(escreverNOP()));
                        solucaoFinal.add(Arrays.asList(escreverNOP()));
                        hazardResolvido = true; // Marca como resolvido
                    }
                }
            }

            // Verificamos a segunda instrução após a atual
            if (i + 2 < instructions.size()) {
                List<String> segundaInstrucao = instructions.get(i + 2);
                String registradorSegundoIgual = "";

                if (verificarOpcodes.contemOpcodeHazard(segundaInstrucao)) {
                    registradorSegundoIgual = verificarOpcodes.getRegistrador(segundaInstrucao);
                }

                if (segundaInstrucao.contains(registradorAtual)) {
                    registradorSegundoIgual = registradorAtual;
                }

                // Verifica se há hazard que requer um NOP
                if (registradorAtual.equals(registradorSegundoIgual)) {
                    // Escreve apenas um NOP se ainda não foi resolvido
                    if (!hazardResolvido) {
                        solucaoFinal.add(Arrays.asList(escreverNOP()));
                        hazardResolvido = true; // Marca como resolvido
                    }
                }
            }
        }

        resultado = solucaoFinal;
    }

    public String escreverNOP() {
        return "NOP";
    }

    public void verInstrucao(List<String> instrucao) {

        System.out.println("\n----------------\n");
        System.out.print("Instrucao ");
        for (String instruc : instrucao) {
            System.out.print(instruc + " ");

        }
        System.out.println("\n----------------\n");
    }

    public void printarResultado() {
        int index = 1; // Para numeração das listas internas
        System.out.println("\nEsta é a lista resultante da tecnica de bolha: \n");
        for (List<String> lista : resultado) {
            System.out.print("Lista " + index + ": ");
            for (String elemento : lista) {
                System.out.print(elemento + ",");
            }
            System.out.println(); // Nova linha após cada lista interna
            index++;
        }
    }

    public void resolverComReordenamento(List<List<String>> instructions) {

    }
}
