import mysql.connector
from socket import *
import time
import random

# Conectando ao banco de dados
connection = mysql.connector.connect(
    host='localhost',
    user='admin',
    password='root',
    database='seu_ze_store',
)

cursor = connection.cursor()


# Funções para manipulação do banco de dados (CRUD)
def add_or_update_product(product_name, product_amount, product_price):
    command = f"SELECT product_amount FROM inventory WHERE product_name = '{product_name}'"
    cursor.execute(command)
    result = cursor.fetchone()

    if result:
        new_amount = result[0] + product_amount
        command = f"UPDATE inventory SET product_amount = {new_amount} WHERE product_name = '{product_name}'"
        cursor.execute(command)
        print(f"Produto '{product_name}' atualizado. Novo total: {new_amount}.")
    else:
        command = f"INSERT INTO inventory (product_name, product_amount, product_price) VALUES ('{product_name}', {product_amount}, {product_price})"
        cursor.execute(command)
        print(f"'{product_amount} unidades do(a) {product_name}' foram adicionados. Preço: R${product_price}.")
    connection.commit()


def is_table_empty():
    cursor.execute(f"SELECT COUNT(*) FROM inventory")
    count = cursor.fetchone()[0]
    return count == 0


def print_items():
    cursor = connection.cursor(dictionary=True)
    cursor.execute(f"SELECT product_ID, product_name, product_price, product_amount FROM inventory")
    items = cursor.fetchall()

    enviar_varias_mensagens(f"{'ID':<10}{'Nome':<30}{'Preço':<18}{'Quantidade':<10}")
    enviar_varias_mensagens("-" * 80)
    for item in items:
        enviar_varias_mensagens(
            f"{item['product_ID']:<10}{item['product_name']:<30}{item['product_price']:<18}{item['product_amount']:<10}")


if connection.is_connected():
    print("Conexão com o banco de dados realizada.")
else:
    print("Falha na conexão ao banco de dados")

serverPort = 12000
serverSocket = socket(AF_INET, SOCK_STREAM)
serverSocket.bind(('', serverPort))
serverSocket.listen(1)
print('O servidor está pronto esperando mensagens')


def classificacao_produto(valor):
    if valor > 1 and valor <= 200:
        return 'barato'
    elif 200 < valor <= 800:
        return 'medio'
    elif valor > 800:
        return 'caro'


def negociar(produto_id):
    cursor.execute(f"SELECT product_name, product_price, product_amount FROM inventory WHERE product_ID = {produto_id}")
    produto = cursor.fetchone()

    if not produto:
        enviar_ultima_mensagem("ID do produto inválido.")
        return False
    if produto[2] == 0:
        enviar_ultima_mensagem("Não temos estoque no momento desde produto")
        return False

    enviar_varias_mensagens('-----------------------------------')
    enviar_varias_mensagens(f'Negociando o produto: {produto[0]}')
    enviar_varias_mensagens(f'Valor: R${produto[1]}')
    enviar_ultima_mensagem('Qual valor você pensa?')

    resposta = receber_mensagem()
    enviar_varias_mensagens('deu bom 1')

    if resposta.isdigit():

        enviar_varias_mensagens('deu bom 2')
        valor_minimo = produto[1]
        valor = produto[1]
        lance = int(resposta)

        cassificacao_produto = classificacao_produto(produto[1])
        if classificacao_produto == 'barato':
            # Caso seja barato, a redução máxima é 18%
            valor_minimo = produto[1] * 0.82
        elif classificacao_produto == 'medio':
            # Caso seja medio, a reducao máxima é 12%
            valor_minimo = produto[1] * 0.88
        else:
            # Caso seja caro, a redução máxima é de 7%
            valor_minimo = produto[1] * 0.93
        enviar_varias_mensagens('deu bom 3')
        # O valor será um número de ponto flutuante gerado aleatoriamente. Inicialmente, o valor ofertado pelo comerciante
        # é menor que o valor mais baixo que ele aceita. Isso porque será vantajoso se o cliente aceitar a primeira oferta
        lance_comerciante = random.uniform(valor_minimo + valor_minimo * 0.5, valor)

        if lance < valor_minimo / 2:
            enviar_varias_mensagens("Não é nem um pouco aceitável esse valor!")
            enviar_varias_mensagens("Meu lucro vai onde nessa brincadeira toda?")
            enviar_varias_mensagens(
                f"Parece até piada essa negociação. Para você o preço é preço original de R${produto[1]}.")
            enviar_ultima_mensagem(f"Fechado? (s/n)")
            resposta = receber_mensagem()

            if resposta == 's':
                novo_amount = produto[2] - 1
                cursor.execute(f"UPDATE inventory SET product_amount = {novo_amount} WHERE product_ID = {produto_id}")
                connection.commit()
                enviar_ultima_mensagem(f"Trato feito. Você adquiriu {produto[0]} por R${produto[1]}")
                processar_comando(resposta)
            elif resposta == 'n':
                enviar_ultima_mensagem(f"Negociação encerrada. Sem acordo. Voltando para loja..")

        elif lance < valor_minimo:
            enviar_varias_mensagens(
                f'Este valor não posso aceitar! Você está pedindo R${lance} e meu produto vale R${produto[1]}. ')
            enviar_varias_mensagens(f'É muito pouco!')
            lance_comerciante = "{:,.2f}".format(lance_comerciante)
            enviar_ultima_mensagem(f'Podemos fechar por R${lance_comerciante}. Te agrada? (s/n')

            resposta = receber_mensagem()

            if resposta == 's':
                enviar_varias_mensagens(f"Perfeito meu caro! Você leva pra casa {produto[0]} por R${produto[1]}.")
                enviar_varias_mensagens("Foi bom fazer negócios com você. ")
                enviar_ultima_mensagem("Te redirecionando para a loja...")
                entrar_loja()
            elif resposta == 'n':
                lance_comerciante_novo = random.uniform(valor_minimo, valor)

                while lance_comerciante_novo > float(lance_comerciante):
                    lance_comerciante_novo = random.uniform(valor_minimo + valor_minimo * 0.5, valor)

                lance_comerciante_novo = "{:,.2f}".format(lance_comerciante_novo)
                enviar_varias_mensagens(
                    f"Olha só, o melhor que posso fazer sem comprometer o meu negócio é R${lance_comerciante_novo}")

                resposta = receber_mensagem()

                if resposta == 's':
                    enviar_varias_mensagens(f"Perfeito meu caro! Você leva pra casa {produto[0]} por R${produto[1]}.")
                    novo_amount = produto[2] - 1
                    cursor.execute(
                        f"UPDATE inventory SET product_amount = {novo_amount} WHERE product_ID = {produto_id}")
                    connection.commit()
                    enviar_varias_mensagens("Foi bom fazer negócios com você. ")
                    enviar_ultima_mensagem("Te redirecionando para a loja...")
                    entrar_loja()
                elif resposta == 'n':
                    enviar_ultima_mensagem(f"Negociação encerrada. Sem acordo. Voltando para loja..")
                    entrar_loja()
            else:
                enviar_ultima_mensagem("Voce chegou la no final hahahaa")
    return False


def entrar_loja():
    enviar_varias_mensagens('Seus comandos são: comprar, negociar e sair.')
    enviar_varias_mensagens('Produtos disponíveis:')
    if is_table_empty():
        enviar_varias_mensagens('A loja por enquanto está vazia, aguarde que vou adicionar produtos!')
    else:
        print_items()
        enviar_ultima_mensagem('O que deseja fazer?')


def processar_comando(comando):
    partes = comando.split()

    if partes[0] == 'comprar':
        if len(partes) == 2 and partes[1].isdigit():
            produto_id = int(partes[1])
            return comprar_produto(produto_id)
        else:
            enviar_ultima_mensagem("Comando inválido. Use 'comprar <ID do produto>'.")
            return False
    elif partes[0] == 'negociar':
        if len(partes) == 2 and partes[1].isdigit():
            return negociar(int(partes[1]))
        else:
            enviar_ultima_mensagem("Comando inválido. Use 'negociar <ID do produto>'.")
            return False
    elif comando == 'n':
        response = 'Nem me deu a oportunidade de ver meus produtos... :-('
        enviar_ultima_mensagem(response)
        print("Conexão finalizada pelo usuário.")
        return True
    elif comando == 's':
        return False


    elif comando == 'sair':
        response = 'Obrigado, volte sempre.'
        enviar_ultima_mensagem(response)
        print("Conexão finalizada pelo usuário.")
        return True

    else:
        enviar_ultima_mensagem("Comando desconhecido.")
        return False


def comprar_produto(produto_id):
    cursor.execute(f"SELECT product_name, product_price, product_amount FROM inventory WHERE product_ID = {produto_id}")
    produto = cursor.fetchone()

    # Verifica o nome e se existe produto no estoque
    if produto and produto[2] > 0:
        novo_amount = produto[2] - 1
        cursor.execute(f"UPDATE inventory SET product_amount = {novo_amount} WHERE product_ID = {produto_id}")
        connection.commit()
        enviar_ultima_mensagem(f"Você comprou {produto[0]} por R${produto[1]}.")
        return False
    elif produto and produto[2] <= 0:
        enviar_ultima_mensagem(f"Desculpe, o produto {produto[0]} está fora de estoque.")
        return False
    else:
        enviar_ultima_mensagem("ID do produto inválido.")
        return False


def enviar_ultima_mensagem(mensagem):
    connectionSocket.send(mensagem.encode())
    connectionSocket.send('END'.encode())


def enviar_varias_mensagens(mensagem):
    connectionSocket.send(mensagem.encode())
    time.sleep(0.1)


def receber_mensagem():
    return connectionSocket.recv(1024).decode()


def boas_vindas():
    enviar_varias_mensagens('Bem-vindo(a) à minha loja!')
    enviar_ultima_mensagem('Deseja ver o que tenho na loja? (S/N)')


def conversar_com_cliente():
    boas_vindas()
    while True:
        sentence = receber_mensagem().lower()

        # Verifica o comando e decide se a conexão deve ser finalizada
        if processar_comando(sentence):
            break  # Sai do loop se a conexão deve ser finalizada

        if sentence == 's':
            enviar_varias_mensagens('Certo! Venha então')
            entrar_loja()


while True:
    connectionSocket, _ = serverSocket.accept()
    conversar_com_cliente()
    connectionSocket.close()
