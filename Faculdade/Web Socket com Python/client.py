from socket import *
import sys

def conectar_servidor():
    serverName = 'localhost'
    serverPort = 12000
    client_socket = socket(AF_INET, SOCK_STREAM)
    try:
        client_socket.connect((serverName, serverPort))
        print('Conexão estabelecida.')
    except ConnectionRefusedError:
        print("O servidor está inativo, não foi possível estabelecer a conexão.")
        sys.exit()
    
    
    return client_socket

def receber_mensagem(client_socket):
    """Recebe mensagens do servidor."""
    return client_socket.recv(1024).decode()


def enviar_mensagem(mensagem, client_socket):
    client_socket.send(mensagem.encode())


    
def fechar_conexao(client_socket):
        """Finaliza a comunicação com o servidor corretamente."""
        client_socket.shutdown(SHUT_RDWR)
        client_socket.close()
        print("Conexão com o servidor finalizada.")
        sys.exit()
            

def conversar_com_servidor():
    client_socket = conectar_servidor()
    while True:
        mensagem = receber_mensagem(client_socket)

        # if mensagem == "FIN":
        #     fechar_conexao()
        #     break

        # Como o modo de conexão é enviar e receber uma mensagem por vez, ajustei a lógica para que o servidor
        # quantas mensagens desejar. Quando a palavra "END" é recebida, entende-se que o servidor enviou todas as mensagens
        counter = 0
        
        
        
        while mensagem != "END":
            
            counter += 1
            if counter == 30:
                break
            
            if mensagem == 'fin':
                fechar_conexao(client_socket)
            
            print('Seu zé:', mensagem)
            mensagem = receber_mensagem(client_socket)

        # Solicita a próxima mensagem do cliente
        print(' ')
        resposta = input('Cliente: ')
            
        enviar_mensagem(resposta, client_socket)

        # Checa se a mensagem contém "sair" ou "n" e espera a resposta final do servidor
       


conversar_com_servidor()

