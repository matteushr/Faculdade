import java.util.*;

public class Opcodes {

    private List<String> hazardOpcodes;
    private List<String> registradores;

    public Opcodes() {
        hazardOpcodes = new ArrayList<String>();
        registradores = new ArrayList<>();
        listarOpcodes();
        listarRegistradores();
    }

    public void listarOpcodes() {
        // Adiciona apenas os opcodes que podem causar RAW hazards
        hazardOpcodes.add("lw"); // Load word
        hazardOpcodes.add("add"); // Addition
        hazardOpcodes.add("sub"); // Subtraction
        hazardOpcodes.add("addi"); // Addition immediate
        hazardOpcodes.add("addu"); // Addition unsigned
        hazardOpcodes.add("subu"); // Subtraction unsigned
        hazardOpcodes.add("and"); // Bitwise AND
        hazardOpcodes.add("or"); // Bitwise OR
        hazardOpcodes.add("xor"); // Bitwise XOR
        hazardOpcodes.add("nor"); // Bitwise NOR
        hazardOpcodes.add("slt"); // Set on less than
        hazardOpcodes.add("sltu"); // Set on less than unsigned
        hazardOpcodes.add("addiu"); // Addition immediate unsigned
        hazardOpcodes.add("slti"); // Set on less than immediate
        hazardOpcodes.add("sltiu"); // Set on less than immediate unsigned
        hazardOpcodes.add("lb"); // Load byte
        hazardOpcodes.add("lh"); // Load halfword
        hazardOpcodes.add("lwl"); // Load word left
        hazardOpcodes.add("lbu"); // Load byte unsigned
        hazardOpcodes.add("lhu"); // Load halfword unsigned
        hazardOpcodes.add("lwr"); // Load word right
        hazardOpcodes.add("sb"); // Store byte
        hazardOpcodes.add("sh"); // Store halfword
        hazardOpcodes.add("swl"); // Store word left
        hazardOpcodes.add("sw"); // Store word
        hazardOpcodes.add("swr"); // Store word right
        hazardOpcodes.add("andi"); // Bitwise AND immediate
        hazardOpcodes.add("ori"); // Bitwise OR immediate
        hazardOpcodes.add("xori"); // Bitwise XOR immediate
        hazardOpcodes.add("lui"); // Load upper immediate
        hazardOpcodes.add("sll"); // Shift left logical
        hazardOpcodes.add("srl"); // Shift right logical
        hazardOpcodes.add("sra"); // Shift right arithmetic
        hazardOpcodes.add("sllv"); // Shift left logical variable
        hazardOpcodes.add("srlv"); // Shift right logical variable
        hazardOpcodes.add("srav"); // Shift right arithmetic variable
        hazardOpcodes.add("mfhi"); // Move from HI
        hazardOpcodes.add("mthi"); // Move to HI
        hazardOpcodes.add("mflo"); // Move from LO
        hazardOpcodes.add("mtlo"); // Move to LO
        hazardOpcodes.add("mult"); // Multiply
        hazardOpcodes.add("multu"); // Multiply unsigned
        hazardOpcodes.add("div"); // Divide
        hazardOpcodes.add("divu"); // Divide unsigned
    }

    public void listarRegistradores() {
        // Registradores $s (salvos)
        registradores.add("$zero"); // Registrador zero (sempre contém o valor 0)
        registradores.add("$at"); // Registrador assembler temporary
        registradores.add("$v0"); // Registrador para valores de retorno
        registradores.add("$v1"); // Registrador para valores de retorno
        registradores.add("$a0"); // Registrador para argumentos
        registradores.add("$a1");
        registradores.add("$a2");
        registradores.add("$a3");
        registradores.add("$a4"); // Registrador adicional de argumento
        registradores.add("$a5"); // Registrador adicional de argumento
        registradores.add("$a6"); // Registrador adicional de argumento
        registradores.add("$a7"); // Registrador adicional de argumento
        registradores.add("$t0"); // Registradores temporários
        registradores.add("$t1");
        registradores.add("$t2");
        registradores.add("$t3");
        registradores.add("$t4");
        registradores.add("$t5");
        registradores.add("$t6");
        registradores.add("$t7");
        registradores.add("$t8");
        registradores.add("$t9");
        registradores.add("$t10"); // Registradores temporários adicionais
        registradores.add("$t11");
        registradores.add("$t12");
        registradores.add("$t13");
        registradores.add("$t14");
        registradores.add("$t15");
        registradores.add("$s0"); // Registradores de salvamento
        registradores.add("$s1");
        registradores.add("$s2");
        registradores.add("$s3");
        registradores.add("$s4");
        registradores.add("$s5");
        registradores.add("$s6");
        registradores.add("$s7");
        registradores.add("$k0"); // Registrador reservado para o kernel
        registradores.add("$k1"); // Registrador reservado para o kernel
        registradores.add("$gp"); // Registrador ponteiro global
        registradores.add("$sp"); // Registrador ponteiro de pilha
        registradores.add("$fp"); // Registrador de quadro de pilha (frame pointer)
        registradores.add("$ra"); // Registrador de retorno de endereço
        
    }

    public boolean contemOpcodeHazard(List<String> instrucao) {
        return (hazardOpcodes.contains(instrucao.get(0)));
    }

    public String getRegistrador(List<String> instrucao) {
        // Itera sobre os tokens da instrução e retorna o primeiro registrador encontrado
        for (String token : instrucao) {
            if (registradores.contains(token)) {
                return token;  // Retorna o registrador encontrado
            }
        }
        return " ";
        // Se o método for chamado, sempre terá um registrador, então não precisa de um caso alternativo.
        //throw new IllegalArgumentException("Nenhum registrador encontrado, isso não deveria acontecer. Instrucao defeituosa: ");
    }
    
    public boolean contemRegistrador(List<String> instrucao) {
        // Verifica se a lista de instruções contém um registrador
        for (String token : instrucao) {
            if (registradores.contains(token)) {
                return true;  // Retorna true se houver um registrador
            }
        }
        return false;  // Retorna false se nenhum registrador for encontrado
    }
   
}
