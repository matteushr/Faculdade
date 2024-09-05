

public class Celula {
    int valor; //variável
    Celula prox; // referência


    public Celula(int valor) {
        this.valor = valor;
        prox = null;
    }


    public int getValue() {
        return valor;
    }
    public void setValue(int valor) {
        this.valor = valor;
    }

    public Celula getNextCell() {
        return prox;
    }

    public void setNextCell(Celula c) {
        prox = c;
    }
}