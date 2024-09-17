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

    public List<List<String>> resolverComBolhas(List<List<String>> instructions) {  
    List<List<String>> solucaoFinal = new ArrayList<>();  

    for (int i = 0; i < instructions.size(); i++) {  
        List<String> instrucaoAtual = instructions.get(i);  
        solucaoFinal.add(instrucaoAtual);  // Add current instruction to the final solution  

        String registradorAtual = "";  
        if (verificarOpcodes.contemOpcodeHazard(instrucaoAtual)) {  
            registradorAtual = verificarOpcodes.getRegistrador(instrucaoAtual);  
        }  

        // Check if next two instructions depend on current register (for RAW hazard)  
        for (int j = 1; j <= 2 && (i + j) < instructions.size(); j++) {  
            List<String> instrucaoProxima = instructions.get(i + j);  

            if (instrucaoProxima.contains(registradorAtual)) {  
                // Insert NOP to resolve hazard  
                solucaoFinal.add(Arrays.asList(escreverNOP()));  
                System.out.println("Inserting NOP to resolve hazard with instruction: " + instrucaoProxima);  
                
                // Check if the current instruction is an 'add' and the next is an 'sw'  
                if (j == 1 && instrucaoAtual.get(0).equals("add") && instrucaoProxima.get(0).equals("sw")) {  
                    // Insert an additional NOP after the first NOP  
                    solucaoFinal.add(Arrays.asList(escreverNOP()));  
                    System.out.println("Inserting additional NOP after add to avoid data hazard with sw: " + instrucaoProxima);  
                }  
            }  
        }  

    }  

    resultado = solucaoFinal;  // Update final result  
    return resultado;
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

    public void printar(List<List<String>> listas) {
        int index = 1; // Para numeração das listas internas
        System.out.println("\nEsta é a lista resultante da tecnica de bolha: \n");
        for (List<String> lista : listas) {
            System.out.print("Lista " + index + ": ");
            for (String elemento : lista) {
                System.out.print(elemento + ",");
            }
            System.out.println(); // Nova linha após cada lista interna
            index++;
        }
    }


    public List<List<String>> resolverComReordenamentoComBolha(List<List<String>> instructions) {
        List<List<String>> solucaoFinal = new ArrayList<>();
        List<List<String>> dependentes = new ArrayList<>();
        List<List<String>> independentes = new ArrayList<>();
    
        // Separate independent and dependent instructions
        for (int i = 0; i < instructions.size(); i++) {
            List<String> instrucaoAtual = instructions.get(i);
            boolean temDependencia = false;
            String registradorEscrito = "";
    
            if (verificarOpcodes.contemOpcodeHazard(instrucaoAtual)) {
                registradorEscrito = verificarOpcodes.getRegistrador(instrucaoAtual);
                
                // Check upcoming instructions for RAW hazards
                for (int j = i + 1; j < instructions.size(); j++) {
                    List<String> instrucaoSeguinte = instructions.get(j);
                    if (instrucaoSeguinte.contains(registradorEscrito)) {
                        temDependencia = true;
                        break;
                    }
                }
            }
    
            // If the instruction has a dependency, it must be processed later
            if (temDependencia) {
                dependentes.add(instrucaoAtual);
            } else {
                independentes.add(instrucaoAtual);
            }
        }
    
        // Add independent instructions first to avoid stalling the pipeline
        solucaoFinal.addAll(independentes);
    
        // Add dependent instructions, potentially inserting NOPs if needed
        for (List<String> instrucaoDependente : dependentes) {
            solucaoFinal.add(instrucaoDependente);
            // Check if NOPs are needed after reordering
            if (solucaoFinal.size() > 1) {
                List<String> ultimaInstrucao = solucaoFinal.get(solucaoFinal.size() - 2);
                if (ultimaInstrucao.contains(verificarOpcodes.getRegistrador(instrucaoDependente))) {
                    solucaoFinal.add(Arrays.asList(escreverNOP()));  // Insert NOP after dependency
                }
            }
        }
    
        resultado = solucaoFinal;  // Update final reordered result
        return resultado;
    }
    public List<List<String>> resolverComAdiantamentoEReordenamentoComBolha(List<List<String>> instructions) {
        List<List<String>> solucaoFinal = new ArrayList<>();
        List<List<String>> dependentes = new ArrayList<>();
        List<List<String>> independentes = new ArrayList<>();
        
        // Para rastrear os registradores na fase EX e MEM
        String registradorEx = "";  // Registro cujo resultado está na fase EX
        String registradorMem = "";  // Registro cujo resultado está na fase MEM
        
        // Separar instruções independentes e dependentes
        for (int i = 0; i < instructions.size(); i++) {
            List<String> instrucaoAtual = instructions.get(i);
            boolean temDependencia = false;
            String registradorEscrito = "";
        
            if (verificarOpcodes.contemOpcodeHazard(instrucaoAtual)) {
                registradorEscrito = verificarOpcodes.getRegistrador(instrucaoAtual);
                
                // Verificar dependências para as instruções seguintes
                for (int j = i + 1; j < instructions.size(); j++) {
                    List<String> instrucaoSeguinte = instructions.get(j);
                    if (instrucaoSeguinte.contains(registradorEscrito)) {
                        temDependencia = true;
                        break;
                    }
                }
            }
        
            // Se a instrução tem dependência, deve ser processada mais tarde
            if (temDependencia) {
                dependentes.add(instrucaoAtual);
            } else {
                independentes.add(instrucaoAtual);
            }
        }
        
        // Adicionar instruções independentes primeiro para evitar stalls no pipeline
        solucaoFinal.addAll(independentes);
        
        // Processar instruções dependentes
        for (List<String> instrucaoDependente : dependentes) {
            // Verificar se o adiantamento é possível
            String registradorAtual = "";
            if (verificarOpcodes.contemOpcodeHazard(instrucaoDependente)) {
                registradorAtual = verificarOpcodes.getRegistrador(instrucaoDependente);
            }
            
            // Verificar se o adiantamento pode resolver o hazard
            boolean podeFazerAdiantamento = false;
            if (registradorAtual.equals(registradorEx) || registradorAtual.equals(registradorMem)) {
                podeFazerAdiantamento = true;  // Dados disponíveis para adiantamento
            }
            
            if (podeFazerAdiantamento) {
                solucaoFinal.add(instrucaoDependente);  // Adicionar a instrução se adiantamento for possível
            } else {
                // Se o adiantamento não for possível, adicionar a instrução e uma bolha (NOP)
                solucaoFinal.add(instrucaoDependente);
                solucaoFinal.add(Arrays.asList(escreverNOP()));  // Inserir NOP após a dependência
            }
            
            // Atualizar registradores na fase EX e MEM
            registradorMem = registradorEx;  // Mover EX para MEM
            registradorEx = registradorAtual;  // Atualizar EX com o registrador atual
        }
        
        return solucaoFinal;
    }
    
    

    public List<List<String>> resolverComAdiantamentoEBolha(List<List<String>> instructions) {
        List<List<String>> solucaoFinal = new ArrayList<>();
        
        // To track the registers being written in EX and MEM stages
        String registradorEx = "";  // Register whose result is in EX stage
        String registradorMem = "";  // Register whose result is in MEM stage
    
        for (int i = 0; i < instructions.size(); i++) {
            List<String> instrucaoAtual = instructions.get(i);
            solucaoFinal.add(instrucaoAtual);  // Add current instruction to final solution
    
            String registradorAtual = "";
            if (verificarOpcodes.contemOpcodeHazard(instrucaoAtual)) {
                registradorAtual = verificarOpcodes.getRegistrador(instrucaoAtual);  // Get the register written by current instruction
            }
    
            // Check if the next instruction depends on the current one
            if (i + 1 < instructions.size()) {
                List<String> proximaInstrucao = instructions.get(i + 1);
    
                // Check if the next instruction uses the same register as the current one (RAW hazard)
                if (proximaInstrucao.contains(registradorAtual)) {
                    // Can forwarding resolve the hazard?
                    boolean podeFazerAdiantamento = false;
    
                    // Forwarding is possible if the register is in EX or MEM stage
                    if (registradorAtual.equals(registradorEx) || registradorAtual.equals(registradorMem)) {
                        podeFazerAdiantamento = true;  // Data is available for forwarding
                    }
    
                    if (!podeFazerAdiantamento) {
                        // If forwarding isn't possible, insert one NOP to resolve the hazard
                        solucaoFinal.add(Arrays.asList(escreverNOP()));
                        System.out.println("Hazard detected: inserting NOP after instruction " + instrucaoAtual);
                    }
                }
            }
    
            // Track which register is in EX and MEM stages (for forwarding purposes)
            registradorMem = registradorEx;  // Move EX stage register to MEM stage
            registradorEx = registradorAtual;  // Current instruction result is now in EX stage
        }
    
        resultado = solucaoFinal;  // Update the result with the final solution
        return resultado;
    }

    
    
}
