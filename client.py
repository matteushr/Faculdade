from socket import *
import sys

serverName = 'localhost'
serverPort = 12000
cliente_socket = socket(AF_INET, SOCK_STREAM)

try:
    cliente_socket.connect((serverName, serverPort))
    print('Conexão estabelecida.')
except ConnectionRefusedError:
    print("O servidor está inativo, não foi possível estabelecer a conexão.")
    sys.exit()


def enviar_mensagem(mensagem):
    cliente_socket.send(mensagem.encode())


def conversar_com_servidor():
    while True:
        resposta = cliente_socket.recv(1024).decode()

        # Como o modo de conexão é enviar e receber uma mensagem por vez, ajustei a lógica para que o servidor
        # quantas mensagens desejar. Quando a palavra "END" é recebida, entende-se que o servidor enviou todas as mensagens
        counter = 0
        while resposta != "END":
            counter += 1
            if counter == 10:
                break
            print('Seu zé:', resposta)
            resposta = cliente_socket.recv(1024).decode()

        # Solicita a próxima mensagem do cliente
        mensagem = input('Cliente: ')
        enviar_mensagem(mensagem)

        # Checa se a mensagem contém "sair" ou "n" e espera a resposta final do servidor
        if mensagem == "sair" or mensagem == "n":
            resposta = cliente_socket.recv(1024).decode()
            count = 0
            while resposta != "END":
                count += 1
                if count == 10:
                    break
                print('Servidor:', resposta)
                resposta = cliente_socket.recv(1024).decode()
            break  # Sai do loop após receber a resposta final


conversar_com_servidor()
cliente_socket.close()
