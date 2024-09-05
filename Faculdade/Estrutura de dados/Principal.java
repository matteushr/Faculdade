public class Principal {
    public static void main(String[] args) {
        Celula celula1 = new Celula(10);
        Celula celula2 = new Celula(20);
        Celula celula3 = new Celula(30);
        Celula celula4 = new Celula(43);
        Celula celula5 = new Celula(22);
        Celula celula6 = new Celula(98);
        Celula celula7 = new Celula(36);
        Celula celula8 = new Celula(71);
        Celula celula9 = new Celula(8);
        Celula celula10 = new Celula(59);

        Exercicio4 lista = new Exercicio4();


        lista.inserirAoFinal(celula10);
        lista.inserirAoFinal(celula9);

        lista.imprimir();

        lista.troca(lista, celula10);

        lista.imprimir();

        

        


    }
}
