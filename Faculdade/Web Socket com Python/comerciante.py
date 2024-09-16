import math
import mysql.connector
from socket import *
import time
import random

# Conectando ao banco de dados
serverPort = 12000
serverSocket = socket(AF_INET, SOCK_STREAM)
serverSocket.bind(('', serverPort))
serverSocket.listen(1)
print('O servidor ativo e aguardando conexões.')

# Conectando ao banco de dados e tratando possíveis excessões
connection = mysql.connector.connect(
    
    host='localhost',
    user='admin',
    password='root',
    database='seu_ze_store',
)
if connection.is_connected():
    print("Conexão com o banco de dados realizada.")
cursor = connection.cursor()
print('Este é o painel do comerciante.')


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



def classificacao_produto(valor):
    if valor > 1 and valor <= 200:
        return 'barato'
    elif 200 < valor <= 800:
        return 'medio'
    elif valor > 800:
        return 'caro'

def calcular_valor_minimo(valor):
        """Calcula o valor mínimo permitido na negociação."""
        classificacao_do_produto = classificacao_produto(valor)
        if classificacao_do_produto == 'barato':
            
            print(f'Este produto é classificado como {classificacao_produto(valor)}. Dessa forma, o desconto máximo possível é de 22%.\n')
            return valor * 0.78
        elif classificacao_do_produto == 'medio':
            print(f'Este produto é classificado como {classificacao_produto(valor)}. Dessa forma, o desconto máximo possível é de 14%.\n')
            return valor * 0.86
        elif classificacao_do_produto == 'caro':
            print(f'Este produto é classificado como {classificacao_produto(valor)}. Dessa forma, o desconto máximo possível é de 8%.\n')
            return valor * 0.92

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
    enviar_varias_mensagens('-----------------------------------\n')

    enviar_ultima_mensagem('Qual valor você sugere?')

    print(f'O cliente tem interesse de negociar {produto[0]}\n.')
    
    resposta = float(receber_mensagem())

    if not isinstance(resposta, (int, float)):
        enviar_varias_mensagens('Por favor, digite um valor numérico na proxima vez.\n')
        entrar_loja()
        return
        
    #Inicialmente, o valor mínimo do produto é ele mesmo.
    valor = produto[1]
    valor_minimo = valor
    lance = int(resposta)

    valor_minimo = calcular_valor_minimo(valor)
    
    # O valor será um número de ponto flutuante gerado aleatoriamente. Inicialmente, o valor ofertado pelo comerciante
    # é menor que o valor mais baixo que ele aceita. Isso porque será vantajoso se o cliente aceitar a primeira oferta
    # time.sleep(1.5)
    lance_comerciante = random.uniform(valor_minimo, valor)

    
    
    print(f'Valor minimo aceito por {produto[0]}: R${"{:,.2f}".format(valor_minimo)}\n')
    print(f'Lance que o comerciante tem: R${"{:,.2f}".format(lance_comerciante)}\n')
    print(f'Preço sugerido de compra pelo cliente: R${resposta}')

    processo_negociacao(produto_id, produto, lance_comerciante, resposta)
        
    return False



def processo_negociacao(produto_id, produto, lance_comerciante: float, resposta):
    
    lance = float(resposta)
    valor_minimo = calcular_valor_minimo(produto[1])
    
    if lance < valor_minimo / 2:
        print('O cliente decidiu dar uma oferta muito abaixo do mínimo!')
        
        enviar_varias_mensagens("Não é nem um pouco aceitável esse valor!")
        enviar_varias_mensagens("Meu lucro vai onde nessa brincadeira toda?")
        enviar_varias_mensagens(f"Sem negociação. Para você o preço é preço original de R${produto[1]}.")
        enviar_ultima_mensagem(f"Fechado? (aceitar ou recusar)")
        resposta = receber_mensagem()

        if processar_comandos_negociacao(resposta) == 'aceitar':
            
            novo_amount = produto[2] - 1
            cursor.execute(f"UPDATE inventory SET product_amount = {novo_amount} WHERE product_ID = {produto_id}")
            connection.commit()
            enviar_ultima_mensagem(f"Trato feito. Você adquiriu {produto[0]} por R${produto[1]}")
            
            
        elif processar_comandos_negociacao(resposta) == 'recusar':
            enviar_ultima_mensagem(f"Negociação encerrada. Sem acordo. Até mais!")
            
            
    elif lance < valor_minimo:
        enviar_varias_mensagens(
            f'Por esse preço fica difícil. R${lance} para um produto de R${produto[1]}? ')
        enviar_varias_mensagens(f'É muito baixo.')
        enviar_ultima_mensagem(f'Eu acredito que R${"{:,.2f}".format(lance_comerciante)} é justo. Te agrada? (aceitar/recusar)')

        resposta = receber_mensagem()

        if processar_comandos_negociacao(resposta) == 'aceitar': 
            concluir_negociacao(produto, produto[0], produto_id)
            
        elif processar_comandos_negociacao(resposta) == 'recusar':
            
            lance_comerciante_novo = random.uniform(valor_minimo, math.floor(lance_comerciante))

            lance_comerciante_novo = "{:,.2f}".format(lance_comerciante_novo)
            enviar_varias_mensagens(f"Minha última oferta é R${lance_comerciante_novo}. Mais que isso eu levo prejuízo.")
            enviar_ultima_mensagem("Qual sua decisão? (aceitar ou recusar)")
            resposta = receber_mensagem()

            if processar_comandos_negociacao(resposta) == 'aceitar':
                concluir_negociacao(produto, lance_comerciante_novo, produto_id)
                
            elif processar_comandos_negociacao(resposta) == 'recusar':
                enviar_varias_mensagens(f"Sem acordo então. Até mais.")
    
    elif valor_minimo < lance < produto[1]:
        
        concluir_negociacao(produto, lance, produto_id)
    
    elif lance == produto[1]:
        
         comprar_produto(produto_id) 
         
    elif lance > produto[1]:
        gorjeta = lance - produto[1]
        enviar_varias_mensagens(f'Olha só... Vou aceitar a sua gorjeta de R${"{:,.2f}".format(gorjeta)}, obrigado!')
        concluir_negociacao(produto, lance, produto_id)
    entrar_loja()

def concluir_negociacao(produto, valor, produto_id):
    
    enviar_varias_mensagens(f"Perfeito! Você leva pra casa {produto[0]} por R${valor}.")
    novo_amount = produto[2] - 1
    cursor.execute(f"UPDATE inventory SET product_amount = {novo_amount} WHERE product_ID = {produto_id}")
    connection.commit()
    enviar_varias_mensagens("Foi bom fazer negócios com você. ")
def entrar_loja():
    enviar_varias_mensagens('Seus comandos são: comprar, negociar e sair.')
    enviar_varias_mensagens('Produtos disponíveis:')
    if is_table_empty():
        enviar_varias_mensagens('A loja por enquanto está vazia, aguarde que vou adicionar produtos!')
    else:
        print_items()
        enviar_ultima_mensagem('O que deseja fazer?')


def comprar_produto(produto_id):
    cursor.execute(f"SELECT product_name, product_price, product_amount FROM inventory WHERE product_ID = {produto_id}")
    produto = cursor.fetchone()
    print(produto)
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
    connection_socket.send(mensagem.encode())
    connection_socket.send('END'.encode())
    time.sleep(0.9)


def enviar_varias_mensagens(mensagem):
    connection_socket.send(mensagem.encode())
    time.sleep(0.5)


def receber_mensagem():
    return connection_socket.recv(1024).decode()


def encerrar_conexao(): 
          
    try:
        # Sinaliza o desejo de encerrar a conexão do cliente.
        enviar_ultima_mensagem('fin')
        print("Desejo de encerrar a conexão detectado.")
        
        # Finaliza a conexão de ambos os lados.
        connection_socket.shutdown(SHUT_RDWR)
        connection_socket.close()
        print("O socket do cliente foi desligado. Comunicação encerrada.")
    except Exception as e:
        print(f"Erro ao encerrar a conexão: {e}")



def mensagem_boas_vindas():
        """Envia a mensagem de boas-vindas para o cliente."""
        enviar_varias_mensagens('Bem-vindo(a) à minha loja!')
        enviar_ultima_mensagem('Deseja ver o que tenho na loja? (sim/nao)')



def conversar_com_cliente():
        """Gerencia a comunicação com o cliente."""
        mensagem_boas_vindas()

        try:
            # Loop para processar mensagens do cliente
            while True:
                sentence = receber_mensagem().lower()

                # Processa o comando recebido
                if processar_comando(sentence):
                    break  # Se o comando processado pedir para encerrar, sair do loop

        except ConnectionResetError:
            print("Conexão resetada pelo cliente.")
            # Sempre certifique-se de fechar a conexão ao sair do loop
            encerrar_conexao()

def processar_comando(comando):
    comandos = ['sim', 'nao','comprar', 'negociar', 'recusar', 'aceitar', 'sair']
    
    partes_comando = comando.split()
    token_do_comando = partes_comando[0]
    
    if not token_do_comando in comandos:
        enviar_ultima_mensagem("Comando inválido.")
        return
    
    #Primeiro, todos os comandos simples (com apenas 1 token) são interpretados.
    if token_do_comando == 'nao' or token_do_comando == 'sair':
        enviar_varias_mensagens('Obrigado, volte sempre.')
        encerrar_conexao()
        return True
    
    elif token_do_comando == 'sim':
        return entrar_loja()
        
    #Quando qualquer um desses tokens forem identificados, significa que está no contexto de negociação.
    elif comando == 'recusar' or comando == 'aceitar':
        return processar_comandos_negociacao(comando);
    
    #Após todos esses comandos serem analisados, resta apenas os comandos com mais de um token.
    #Portanto, utilizamos split() para interpretar os tokens individualmente
        
    #partes_comando = comando.split()
    
    # O primeiro condicional faz uma validação de que o comando está seguindo o formato: <comprar/negociar> <ID do produto>

    if len(partes_comando) == 2 and partes_comando[1].isdigit():

        token_do_comando = partes_comando[0]
        produto_id = int(partes_comando[1])
        
        if token_do_comando == 'comprar':
            print(partes_comando)
            comprar_produto(produto_id)
            
        elif token_do_comando == 'negociar':
            negociar(produto_id)             
    else:
        enviar_ultima_mensagem("Comando inválido. Para comprar ou negociar use: <comprar/negociar> <ID do produto>.")
        return

def processar_comandos_negociacao(comando):
    lista_comandos = ['aceitar', 'recusar']
    
    if comando in lista_comandos:
        
        if comando == 'aceitar':
            return 'aceitar'       
        elif comando == 'recusar':
            return 'recusar'
    else:
        enviar_ultima_mensagem('Por favor, digite aceitar ou recusar para responder a pergunta.')
        return

#Todo
while True:
        connection_socket, addr = serverSocket.accept()
        if connection_socket and addr:
            print(f'Cliente {addr} está na loja nesse momento!')
            
            conversar_com_cliente()
            
