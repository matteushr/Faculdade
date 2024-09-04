import random

def definir_porcentagem(num, porcentagem):
    return (num * porcentagem) / 100

def classificacao_produto(valor):
    if valor > 1 and valor <= 200:
        return 'barato'
    elif valor > 200 and valor <= 800:
        return 'medio'
    elif valor > 800:
        return 'caro'

valor = 10500
lance = 8760
def definir_negociacao(string):
    classificacao = classificacao_produto(valor)
    #if classificacao == 'barato':




valor_minimo = valor * 0.75
valor_negociacao = random.uniform(valor_minimo ,valor)
porcentagem_comerciante = 100 - int(valor_negociacao/valor * 100)
comerciante_puto = False

desconto = 100 - int(lance/valor * 100)
if lance < valor_minimo:
    print(f'Esquece meu chapa, tá de brincadeira com esse valor? {desconto}% de desconto aqui não. ')
    comerciante_puto = True


if comerciante_puto:
    valor_minimo_puto = valor * 0.88
    valor_negociacao_puto = random.uniform(valor_minimo,valor)
    porcentagem_comerciante_puto = 100 - int(valor_negociacao_puto/valor * 100)
    valor_negociacao_puto = "{:,.2f}".format(valor_negociacao_puto)

    print(f'Oque tenho para te oferecer é {valor_negociacao_puto}')


valor = "{:,.2f}".format(valor)
valor_minimo = "{:,.2f}".format(valor_minimo)
valor_negociacao = "{:,.2f}".format(valor_negociacao)
lance = "{:,.2f}".format(lance)
if comerciante_puto:
    valor_minimo_puto = "{:,.2f}".format(valor_minimo_puto)


print('\n\n')
print("########################################################")
print(f'Valor do Produto: R${valor}')
print(f'Valor minimo: R${valor_minimo}')
print(f'Valor negociado 1: R${valor_negociacao}')
print(f'Valor do comerciante em porcentagem: {porcentagem_comerciante}%')
print('')
if comerciante_puto:

    print(f'valor minimo (puto): R${valor_minimo_puto}')
    print(f'valor negociado final (puto): R${valor_negociacao_puto}')
    print(f'Desconto de porcentagem do comerciante puto: {porcentagem_comerciante_puto}%')


print(f'Desconto pedido: {desconto}%')
print(f'Lance: R${lance}')
sentence = ' '

partido = sentence.split()

print (partido[0])

