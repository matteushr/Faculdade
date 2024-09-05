public class ListaSEL {
    Celula primeiro;

    public ListaSEL() {
        primeiro = null;
    }

    Celula getPrimeiro() {
        return primeiro;
    }

    void setPrimeiro(Celula n) {
        primeiro = n;
    }

    boolean vazia() {
        return (primeiro == null);
    }

    void inserirOrdenadamente(Celula c) {

        // vazia
        if (vazia()) {
            primeiro = c;
            return;
        }

        // Um elemento apenas
        if (primeiro.getValue() >= c.getValue()) {
            c.setNextCelula(primeiro);
            primeiro = c;
            return;
        }

        // Dois elementos ou mais
        Celula atual = primeiro;
        Celula proximo = primeiro.getNextCelula();

        while (proximo != null && proximo.getValue() < c.getValue()) {
            atual = atual.getNextCelula();
            proximo = atual.getNextCelula();
        }

        c.setNextCelula(proximo);
        atual.setNextCelula(c);

    }

    void inserirAoFinal(Celula c) {
        if (vazia()) {
            primeiro = c;
            System.out.println("Célula de valor " + c.getValue() + " adicionada ao final da fila.");
            imprimir();
            return;
        }

        Celula aux = primeiro;
        while (aux.getNextCelula() != null) {
            aux = aux.getNextCelula();
        }
        System.out.println("Célula de valor " + c.getValue() + " adicionada ao final da fila.");
        aux.setNextCelula(c);
        imprimir();
    }

    void inserirNoInicio(Celula c) {
        if (vazia()) {
            System.out.println("Célula vazia, primeiro elemento inserido");
            primeiro = c;
            return; // Retorna imediatamente após adicionar o primeiro elemento
        }

        c.setNextCelula(primeiro);
        primeiro = c;
        System.out.println("Elemento inserido no início");
        imprimir();
    }

    Celula pesquisar(int v) {
        if (vazia()) {
            System.out.println("Lista vazia!");
            return null;
        }

        Celula aux = primeiro;

        while (aux != null && aux.getValue() != v) {
            aux = aux.getNextCelula();
        }
        System.out.println("Verificação completa da pesquisa, verifique o resultado: ");
        if (aux != null && aux.getValue() == v) {
            System.out.println("Achou valor!");
            return aux;
        }
        System.out.println("Nao achou nada");
        return null; // Valor não encontrado, retorna null
    }

    boolean removerInicio() {
        if (vazia()) {
            return false;
        }

        if (primeiro.getNextCelula() == null) {
            primeiro = null;
            return true;
        }

        primeiro = primeiro.getNextCelula();
        System.out.println("Elemento removido com sucesso do inicio");
        imprimir();
        return true;
    }

    boolean removerFinal() {
        if (vazia()) {
            return false;
        }

        if (primeiro.getNextCelula() == null) {
            System.out.println("Apenas um elemento na lista, ele foi removido");
            primeiro = null;
            imprimir();
            return true;
        }

        Celula atual = primeiro;
        Celula proxima = primeiro.getNextCelula();

        while (proxima.getNextCelula() != null) {
            atual = proxima;
            proxima = proxima.getNextCelula();
        }

        atual.setNextCelula(null);
        System.out.println("Último elemento na lista, que continha mais de um elemento, removido.");
        return true;
    }

    void imprimir() {
        Celula aux = primeiro;
        System.out.print("Elementos: ");
        while (aux != null) {
            System.out.print(aux.getValue());
            aux = aux.getNextCelula();
            if (aux != null) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    void imprimirVetor(int[] vetor) {
        System.out.print("Elementos: ");
        for (int elemento : vetor) {
            System.out.print(elemento + ", ");
        }
    }

    ListaSEL unirVetoresEmLista(int[] v1, int[] v2) {

        ListaSEL lista = new ListaSEL();

        int lengthV1 = v1.length;
        int lengthV2 = v2.length;

        int auxv1 = 0;
        int auxv2 = 0;

        int[] newVector = new int[lengthV1 + lengthV2];

        for (int v = 0; v < newVector.length; v++) {

            if (v < lengthV1) {
                newVector[v] = v1[auxv1];
                auxv1++;
            } else if (v >= lengthV1) {
                newVector[v] = v2[auxv2];
                auxv2++;
            }

        }

        // Utilizando Bubble Sort para ordenar
        for (int i = 0; i < newVector.length - 1; i++) {
            for (int j = 0; j < newVector.length - i - 1; j++) {
                if (newVector[j] > newVector[j + 1]) {
                    // Swap the elements
                    int auxiliar = newVector[j];
                    newVector[j] = newVector[j + 1];
                    newVector[j + 1] = auxiliar;
                }
            }
        }

        for (int i = 0; i < newVector.length; i++) {

            Celula c = new Celula(newVector[i]);

            if (i == 0) {
                lista.setPrimeiro(c);
            } else {

                lista.inserirAoFinal(c);

            }

        }

        return lista;
    }

    int getTamanhoLista(Celula c) {

        if (vazia()) {
            return 0;
        } else if (c.getNextCelula() != null) {
            return 1 + getTamanhoLista(c.getNextCelula());
        } else {
            return 1;
        }
    }

    boolean isEven(int c) {
        if (c % 2 == 0) {
            return true;
        }

        return false;
    }

    void removerElementosParesV2() {

        if (vazia()) {
            return;
        }

        Celula aux = getPrimeiro();
        Celula primeiroElemento = getPrimeiro();

        while (aux != null) {

            if (isEven(primeiroElemento.getValue())) {

                // Apenas um elemento
                if (primeiroElemento.getNextCelula() == null) {

                    setPrimeiro(null);
                    return;

                } else {
                    // Pelomenos dois elementos
                    setPrimeiro(primeiroElemento.getNextCelula());
                    primeiroElemento = getPrimeiro();
                    aux = primeiroElemento;
                    continue;
                }

            }
            // Verifica se o valor do próximo elemento é par

            if (aux.getNextCelula() != null) {
                if (isEven(aux.getNextCelula().getValue())) {
                    // Verifica se existe algum elemento duas casas depois do aux
                    if (aux.getNextCelula().getNextCelula() != null) {
                        // Pula uma casa e ignora o elemento par
                        aux.setNextCelula(aux.getNextCelula().getNextCelula());
                    } else {
                        // É o penúltimo elemento, portanto o último elemento é par, logo é ignorado.
                        aux.setNextCelula(null);
                    }
                } else {
                    // Caso seja ímpar
                    aux = aux.getNextCelula();

                }

            } else {
                if (isEven(aux.getValue())) {
                    // Se o último elemento for par, remove-o
                    aux = null; // Isto não altera a lista, pois já é o último
                } else {
                    // Se o último elemento for ímpar, simplesmente termina o loop
                    break;
                }

            }

        }
    }
}
