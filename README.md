# Genetic Magic Square Finder

Trabalho Prático de Inteligência Artificial - UNISC 2016/1

Alunos: Gabriel Bittencourt, Guilherme Sehn, Mateus Leonhardt

Este aplicativo encontra [quadrados mágicos](https://pt.wikipedia.org/wiki/Quadrado_m%C3%A1gico) através de algoritmo genético. Um quadrado mágico é uma tabela quadrada de números em progressão aritmética em que a soma de cada coluna, de cada linha e das duas diagonais são iguais.

![Quadrado mágico](https://wikimedia.org/api/rest_v1/media/math/render/svg/3bc23e727d4029de3d46c2b70b8eafd4fa718b70)

## Opções do programa
![User interface](https://cloud.githubusercontent.com/assets/830208/16548042/c9d52c22-4157-11e6-8f2f-a925643891bb.png)

### População

As opções disponíveis são:

1. **Tamanho da matriz:** tamanho do lado do quadrado mágico a ser encontrado.

2. **Tamanho da população:** quantidade de indivíduos na população. Este número é fixo para todas as gerações.

3. **Permitir indivíduos idênticos:** o algoritmo tende a gerar indivíduos idênticos enquanto avança, dificultando a convergência. Se esta opção é marcada, o algoritmo não irá permitir indivíduos duplicados na população. Quando isso ocorrer, se um cruzamento resultar em um indivíduo que já está na próxima geração, este será descartado e outros dois pais serão selecionados dentro da *mating pool* para gerar um novo indivíduo. É recomendado manter esta opção ativa para convergência mais rápida.

### Elitismo

1. **Tamanho da elite:** a quantidade de indivíduos com maior aptidão entre a população que serão transferidos para a próxima geração automaticamente. Deve ser menor que o tamanho da população, caso contrário o algoritmo não irá gerar novos indivíduos. Caso seja definido como 0, não haverá elitismo na execução do algoritmo genético. Nos testes realizados, foi percebido que uma elite grande em relação à população acelera a convergência.

2. **Período de morte da elite**: valor que indica a quantidade máxima de gerações em que a elite pode sobreviver sem que o algoritmo encontre novos quadrados mágicos. Se o valor 0 for informado, esta opção é desabilitada. Se um número positivo *N* for informado, após *N* gerações do algoritmo genético sem resultar em novos quadrados mágicos, a elite será desconsiderada da *mating pool* e não será transferida para a próxima geração. Após isso, uma nova elite será formada através do cruzamento dos indivíduos restantes na população. Manter esta opção ativada geralmente melhora a performance do algoritmo genético para este problema específico.

### Cruzamento

1. **Ponto mínimo e ponto máximo:** o cruzamento implementado no programa é de um ponto apenas. Este ponto é gerado aleatoriamente, e o usuário pode informar nestes campos os pontos mínimos e máximo aceitos, que podem ir de `0` até `N-1`, onde `N` é o tamanho do vetor do quadrado mágico (9 para um quadrado 3x3). Mais informações podem ser vistas na seção sobre [função de cruzamento](#função-de-cruzamento).

2. **Chance de mutação:** porcentagem dos novos indivíduos gerados por cruzamento que sofrerão [mutação](#funcionamento-da-mutação).

### Saída

1. **Exibir histórico completo:** Se marcado, o sistema irá exibir informações completas sobre a origem dos indivíduos na seção de histórico (primeiro pai, segundo pai, se faz parte da elite, e pontos de mutação). Para melhor performance, recomenda-se deixar desativado.

2. **Gravar histórico completo em arquivo:** Se marcado, irá criar um novo arquivo na pasta `output` no mesmo diretório onde o programa se encontra com todo o histórico de gerações. Recomenda-se deixar desmarcado para melhor performance e evitar uso de espaço em disco. **Importante: os arquivos gerados podem se tornar grandes rapidamente devido a quantidade de dados gerados. Uma execução para quadrados 4x4 pode ocupar mais de 1 GB de dados de histórico.**

3. **Limpar histórico periodicamente:** Caso esteja desmarcado, o aplicativo tentará mostrar o histórico completo de gerações do algoritmo genético na tela. **Importante: É recomendado manter ativado, caso contrário haverá estouro de memória após um determinado tempo de execução.**

## Técnicas utilizadas

### Representação do indivíduo

Cada indivíduo é representado na população através de um vetor plano com as linhas da matriz em sequência. Sendo assim, um vetor `[2, 7, 6, 9, 5, 1, 4, 3, 8]` representa o quadrado:

```
2 7 6
9 5 1
4 3 8
```

### Função de aptidão

1. Calcula-se o número mágico, que é o resultado esperado para a soma de todas as linhas, colunas e diagonais, através da fórmula `(L+(L^3))/2`, onde `L` é o tamanho do lado da matriz. Chamaremos o valor calculado de `M` daqui em diante.

2. Para cada linha, coluna e diagonal, é feita a soma dos números (denominada `S`), e calcula-se `N = |M-S|`

3. Todos os `N`s gerados são somados, formando a função de aptidão que é melhor quanto mais próxima de 0. Um quadrado mágico terá aptidão 0, e a aptidão aumenta conforme a "distância" necessária para se tornar um quadrado mágico.

#### Exemplo

```
Tamanho: 3x3

Quadrado:
1 2 3
4 5 6
7 8 9

L = 3
M = (3+(3^3))/2 = 15

Linha 1:
S = 1+2+3 = 6
N = |M-S| = |15-6| = 9

Linha 2:
S = 4+5+6 = 15
N = |M-S| = |15-15| = 0

Linha 3:
S = 7+8+9 = 24
N = |M-S| = |15-24| = 9

Coluna 1:
S = 1+4+7 = 12
N = |M-S| = |15-12| = 3

Coluna 2:
S = 2+5+8 = 15
N = |M-S| = |15-15| = 0

Coluna 3:
S = 3+6+9 = 18
N = |M-S| = |15-18| = 3

Diagonal 1:
S = 1+5+9 = 15
N = |M-S| = |15-15| = 0

Diagonal 2:
S = 3+5+7 = 15
N = |M-S| = |15-15| = 0

Aptidão = soma de todos N = 9+0+9+3+0+3+0+0 = 24
```

### Seleção para cruzamento
Os indivíduos são selecionados para cruzamento através da [técnica de torneio](https://en.wikipedia.org/wiki/Tournament_selection). O tamanho da *mating pool* é sempre igual à metade do tamanho da população.

Sendo o tamanho da população 200, teremos um `mating pool` do tamanho 100. A cada geração, instancia-se uma *mating pool* vazia. Selecionam-se dois indivíduos aleatórios na população, e o que tiver maior cálculo de aptidão entre eles é inserido na *mating pool*. Caso ocorra empate, um dos indivíduos é selecionado para a *mating pool*. Repete-se o processo até que a lista chegue no tamanho esperado.

### Função de cruzamento
Uma das características de quadrados mágicos é não poder repetir elementos, ou seja, não é permitido ter genes repetidos no vetor de representaçào do indivíduo. Os métodos de cruzamento mais básicos são inviáveis pois geram indivíduos inválidos com probabilidade muito alta. Por conta disso, torna-se necessário buscar um método de cruzamento mais sofisticado.

Foi utilizada a função de cruzamento proposta no artigo [*Genetic Algorithm Solution of the TSP Avoiding Special Crossover and Mutation*](http://www.ceng.metu.edu.tr/~ucoluk/research/publications/tspnew.pdf) pelo autor Göktürk Üçoluk. O artigo propõe um método de cruzamento para o [*TSP - Travelling Salesman Problem*](https://en.wikipedia.org/wiki/Travelling_salesman_problem) (problema do caixeiro-viajante), mas que também pode ser utilizado para o problema do quadrado mágico, visto que ambos utilizam codificação por permutação, na qual os genes não podem se repetir.

Cada cruzamento utiliza dois indivíduos como parentes. Para cada parente é calculado um vetor denominado sequência de inversão, que é uma representação alternativa e reversível do mesmo indivíduo. Por reversível entende-se que, através da sequência de inversão, é possível recalcular a representação original do indivíduo.

#### Cálculo da sequência de inversão

Inicia-se o cálculo com um vetor vazio `inv` com o mesmo tamanho do vetor que representa o indivíduo. Para facilitar a explicação desta etapa, será considerado que o vetor inicia no índice 1.

Para cada posição do vetor (seja `i` a posição do vetor), procura-se a localização do número `i` no vetor de representação original do quadrado mágico, e conta-se quantos indivíduos à esquerda de `i` possuem valor maior que ele. O resultado desta conta é o valor na posição `i` da sequência de inversão.

##### Exemplo

Para o vetor de representação normal `[2, 7, 6, 9, 5, 1, 4, 3, 8]`, iniciamos um novo vetor de mesmo tamanho que irá representar a sua sequência de inversão. Executa-se, iniciando de 1, os seguintes cálculos:

1. O valor 1 está localizado na sexta posição do vetor original. À esquerda, possuimos cinco números maiores: 2, 7, 6, 9 e 5. Portanto, o valor na posição 1 da sequência de inversão será 5. Nesse passo, `inv = [5, _, _, _, _, _, _, _, _]`.

2. O valor 2 está localizado na primeira posição do vetor original. À esquerda, não possuimos nenhum número, portanto o valor na posição 2 da sequência de inversão será 0. Nesse passo, `inv = [5, 0, _, _, _, _, _, _, _]`.

3. O valor 3 está localizado na oitava posição do vetor original. À esquerda, possuimos cinco números maiores: 7, 6, 9, 5 e 4. Portanto, o valor na posição 3 da sequência de inversão será 5. Nesse passo, `inv = [5, 0, 5, _, _, _, _, _, _]`.

4. O valor 4 está localizado na sétima posição do vetor original. À esquerda, possuimos quatro números maiores: 7, 6, 9 e 5. Portanto, o valor na posição 4 da sequência de inversão será 4. Nesse passo, `inv = [5, 0, 5, 4, _, _, _, _, _]`.

Repetem-se estes passos para as demais posições, e ao final encontra-se a seguinte sequência de inversão calculada para este indivíduo:

`[5, 0, 5, 4, 3, 1, 0, 1, 0]`

Esta representação é adequada para o cruzamento pois nela é permitido repetir genes mantendo ainda assim uma representação válida de quadrado mágico. Ao final, reverte-se os filhos do cruzamento para a representação original do vetor tendo como resultado um quadrado mágico válido, sem genes de valores repetidos.

#### Geração dos filhos

Tendo a sequência de inversão calculada para os dois parentes, é utilizado o cruzamento simples de um ponto para gerar dois filhos. O ponto de cruzamento é gerado aleatoriamente entre os limites `A` e `B` definidos na interface do aplicativo. O ponto pode ser de `0` a `N-1`, sendo N o tamanho do vetor (N é 9 para quadrados 3x3). Para a descrição desta etapa, será considerado que o vetor se inicia no índice 0.

##### Exemplo

Tendo duas sequências de inversão `[5, 0, 5, 4, 3, 1, 0, 1, 0]` (parente 1) e `[1, 6, 6, 0, 0, 0, 2, 1, 0]` (parente 2), e ponto de cruzamento 5, são gerados dois filhos.

O primeiro possui uma cópia até a posição 5 (inclusive) do parente 1 e o restante do parente 2, e o segundo possui uma cópia até a posição 5 (inclusive) do parente 2 e o restante do parente 1. Portanto:

- Filho 1 = `[5, 0, 5, 4, 3, 1, 2, 1, 0]`
- Filho 2 = `[1, 6, 6, 0, 0, 0, 0, 1, 0]`

Os filhos, assim como os pais, estão representados como sequências de inversão. Agora é necessário transformá-los de volta para a representação original do problema (por permutação).

#### Reversão da sequência de inversão

Para transformar um vetor na sequência de inversão para a representação original do quadrado mágico (permutação), é necessário primeiro  compor um vetor intermediário.

##### Composição do vetor intermediário

O vetor intermediário será chamado de `pos`, e o vetor de sequência de inversão como `inv`.

O algoritmo é descrito abaixo:

1. Iniciar o vetor `pos` vazio com a mesma quantidade de posições que `inv`.
2. Iniciar iteração (`i`) de `N-1` (última posição do vetor) até `0` (primeira posição do vetor), Dentro dessa iteração:
  1. Copiar `inv[i]` para `pos[i]`
  2. Comparar todos os valores à direita do índice `i` em `pos` com `inv`. Para cada item à direita do índice `i` em `pos` maior que `inv[i]`, incrementar o índice em `pos`.
  
Tendo o vetor intermediário (`pos`) formado, é possível utilizá-lo para montar o vetor na representação original.

##### Transformação para representação original

O quadrado final, em representação por permutação, será chaamado de `square`.

Para cada `i` (posição, iniciando de `0`) em `pos`, será definido o índice `pos[i]` de `square` para `i+1`.

###### Exemplo

Calculando o vetor intermediário:

```
Início do algoritmo:
inv = [5, 0, 5, 4, 3, 1, 2, 1, 0]
pos = [_, _, _, _, _, _, _, _, _] // todas as posições vazias no início

Primeira iteração (i=8):
pos = [_, _, _, _, _, _, _, _, 0] // copia-se inv[8] para pos[8]
Não temos nenhum item à direita de pos[8], então a iteração finaliza assim.

Seguda iteração (i=7):
pos = [_, _, _, _, _, _, _, 1, 0] // copia-se inv[7] para pos[7]
Não temos nenhum item à direita de pos[7] que seja maior ou igual a inv[7], então não efetua mais nenhuma alteração em "pos".

Terceira iteração (i=6):
pos = [_, _, _, _, _, _, 2, 1, 0] // copia-se inv[6] para pos[6]
Não temos nenhum item à direita de pos[6] que seja maior ou igual a inv[6], então não efetua mais nenhuma alteração em "pos".

Quarta iteração (i=5):
pos = [_, _, _, _, _, 1, 2, 1, 0] // copia-se inv[5] para pos[5]
Temos dois itens à direita do índice 5 em "pos" maiores ou iguais a inv[5] (2 e 1 são >= 1), então iremos incrementá-los. Logo:
pos = [_, _, _, _, _, 1, 3, 2, 0]

Quinta iteração (i=4):
pos = [_, _, _, _, 3, 1, 3, 2, 0] // copia-se inv[4] para pos[4]
Temos um item à direita do índice 4 em "pos" maior ou igual a inv[4] (3 >= 3), então iremos incrementá-lo. Logo:
pos = [_, _, _, _, 3, 1, 4, 2, 0]

Sexta iteração (i=3):
pos = [_, _, _, 4, 3, 1, 4, 2, 0] // copia-se inv[3] para pos[3]
Temos um item à direita do índice 3 em "pos" maior ou igual a inv[3] (4 >= 4), então iremos incrementá-lo. Logo:
pos = [_, _, _, 4, 3, 1, 5, 2, 0]

Sétima iteração (i=2):
pos = [_, _, 5, 4, 3, 1, 5, 2, 0] // copia-se inv[2] para pos[2]
Temos um item à direita do índice 2 em "pos" maior ou igual a inv[2] (5 >= 5), então iremos incrementá-lo. Logo:
pos = [_, _, 5, 4, 3, 1, 6, 2, 0]

Oitava iteração (i=1):
pos = [_, 0, 5, 4, 3, 1, 6, 2, 0] // copia-se inv[1] para pos[1]
Todos os itens à direita do índice 1 em "pos" são maiores ou iguais a inv[1], logo iremos incrementar todos eles:
pos = [_, 0, 6, 5, 4, 2, 7, 3, 1]

Nona iteração (i=0):
pos = [5, 0, 6, 5, 4, 2, 7, 3, 1] // copia-se inv[0] para pos[0]
Temos três itens à direita do índice 0 em "pos" que são maiores ou iguais a inv[0] (5, 6 e 7 são >= 5), então iremos incrementá-los. Logo:
pos = [5, 0, 7, 6, 4, 2, 8, 3, 1]

Vetor "pos" está finalizado.

Montagem do quadrado final:

pos = [5, 0, 7, 6, 4, 2, 8, 3, 1]
square = [_, _, _, _, _, _, _, _, _] // todas as posições vazias no início

Iteração 1: pos[0] é 5, logo square[5] é 1 -- square = [_, _, _, _, _, 1, _, _, _]
Iteração 2: pos[1] é 0, logo square[0] é 2 -- square = [2, _, _, _, _, 1, _, _, _]
Iteração 3: pos[2] é 7, logo square[7] é 3 -- square = [2, _, _, _, _, 1, _, 3, _]
Iteração 4: pos[3] é 6, logo square[6] é 4 -- square = [2, _, _, _, _, 1, 4, 3, _]
Iteração 5: pos[4] é 4, logo square[4] é 5 -- square = [2, _, _, _, 5, 1, 4, 3, _]
Iteração 6: pos[5] é 2, logo square[2] é 6 -- square = [2, _, 6, _, 5, 1, 4, 3, _]
Iteração 7: pos[6] é 8, logo square[8] é 7 -- square = [2, _, 6, _, 5, 1, 4, 3, 7]
Iteração 8: pos[7] é 3, logo square[3] é 8 -- square = [2, _, 6, 8, 5, 1, 4, 3, 7]
Iteração 9: pos[8] é 1, logo square[1] é 9 -- square = [2, 9, 6, 8, 5, 1, 4, 3, 7]
```

Ao final, `square` é `[2, 9, 6, 8, 5, 1, 4, 3, 7]`.

### Funcionamento da mutação
A mutação pode ocorrer ou não após um cruzamento, ocorrendo aleatoriamente. Quando uma mutação ocorre, dois genes são trocados de posição aleatoriamente. Exemplo:

- Indivíduo original: `[1, 2, 3, 4, 5, 6, 7, 8, 9]`
- Posições de mutação (aleatórias, índice começa em 0): `[3, 6]`
- Indivíduo após mutação: `[1, 2, 3, 7, 5, 6, 4, 8, 9]`

# Resultados

Alguns logs de execução contendo quadrados mágicos encontrados e o tempo necessário para faze-los podem ser encontrados na pasta [misc/execution-times](https://github.com/guisehn/genetic-magic-square-finder/tree/master/misc/execution-times) deste repositório.

## Comparação com força bruta

Uma versão deste aplicativo utilizando apenas força bruta através de permutação foi desenvolvida e pode ser encontrada em:
https://github.com/guisehn/magic-square-finder

Ele demora diversas horas para encontrar quadrados mágicos acima do tamanho 3x3. Mais detalhes podem ser vistos na página do repositório.
