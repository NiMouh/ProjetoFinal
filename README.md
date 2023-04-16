# Apontamentos para o projeto final de curso

## Introdução

### Âmbito, Enquadramento e Motivação

Este documento funciona como relatório do projeto final de curso de Engenharia Informática. O projeto visa aplicar e consolidar os conhecimentos
adquiridos ao longo do percurso académico, através da criação de uma aplicação que facilita o processo de anonimização de dados, usando a ferramenta
ARX. Todo o desenvolvimento da mesma é feita na linguagem Java. De modo complementar, é esperado que este projeto aguçe a capacidade de análise de
problemas na resolução de problemas de segurança e preservação de dados.

### Problema e Objetivos do Trabalho

O principal objetivo do trabalho é, conforme o enunciado disponibilizado pela professora responsável, *criação de uma ‘interface’ que permita
automatizar a anonimização e análise de vários conjuntos de dados fazendo variar os parâmetros do modelo de privacidade usado*.

### Abordagem

De modo a conseguir desenvolver o projeto o mais tranquilamente possível, são necessárias algumas bases sobre o funcioncamento da ferramenta ARX, bem
como conhecimentos sólidos da linguagem Java. Para tal, foi necessário a leitura e análise de documentação disponibilizada pela ferramenta,
entendimento do significado dos valores estatísticos obtidos, bem como o desenvolvimento de uma versão do projeto em terminal, para fossar possível a
sua compreensão e posterior desenvolvimento. E desta junção de conhecimentos teóricos das ciências matemáticas com os conhecimentos práticos de
programação que resulta o desenvolvimento deste trabalho.

## Estado-da-arte

### Introdução

A anonimização de dados é um tema relevante na atualidade, especialmente para empresas e organizações que precisam lidar
com abundantes de informações sensíveis. Automatizar esse processo pode ser uma solução para facilitar a aplicação
de técnicas de anonimização em larga escala, selecionando as melhores abordagens e avaliando a qualidade dos dados
anonimizados.

Além disso, a automatização da anonimização de dados pode ajudar as empresas a cumprir as leis de proteção de dados,
como o Regulamento Geral sobre a Proteção de Dados (RGPD). Essas leis exigem que as empresas protejam os dados pessoais
dos indivíduos e adotem medidas adequadas de segurança e anonimização.

Neste capítulo, vai ser falado sobre a ferramenta que nos vai auxiliar na automatização do processo de anonimização de
dados, nas técnicas de anonimização e privacidade e nos modelos de privacidade.

### Objetivo

**Automatizar** processos de **anonimização** de dados usando a ferramenta ARX. Vão ser recebidos um conjunto de dados
de
entrada num formato de ficheiro CSV/Excel e o utilizador vai escolher o modelo de anonimização e privacidade que quer
aplicar. O programa vai gerar um ficheiro de saída com os dados anonimizados e um relatório com as estatísticas do
processo de anonimização. Resumindo:

1. **Aplicar** o modelo de privacidade ao conjunto de dados de entrada num processo automático.
2. **Quantificar** o risco da divulgação de informação sensível ou reidentificação.
3. **Avaliar** a utilidade dos dados.

### Motivação

A anonimização de dados é importante salvaguardar a privacidade dos indivíduos, impossibilitando a identificação direta
ou indireta de indivíduos a partir desses dados. Empresas e governos devem usar esses dados seguramente e responsável, e
há vários exemplos de vazamentos de dados que ilustram a importância da anonimização. Os casos mais conhecidos incluem a
*Equifax*, que sofreu um vazamento de dados em 2017, e o *Facebook*, em 2018. A falta de anonimização de dados pode
levar a problemas de privacidade e segurança, e as empresas devem implementar medidas adequadas de anonimização para
garantir a proteção dos dados sensíveis.

### ARX - A Tool for Data Anonymization

ARX é uma ferramenta, *open source* que permite a anonimização de dados sensíveis. Contém uma grande variedade de
algoritmos de anonimização, privacidade, transformação de dados e análise estatística. Contém também ferramentas para
gráficos, visualização de dados e exportação de dados. Neste projeto, vamos usar a API do ARX que entrega capacidades de
anonimização de dados para aplicação Java. Para a sua compreensão, vai ser usado a documentação disponibilizada pelo
ARX.

### Modelos de privacidade

#### Algoritmos de Agrupamento/ Clustering Algorithms

Os algoritmos de **agrupamento**, assim como o nome o diz, agrupam os dados para que os dados sensíveis não sejam
divulgados. Os algoritmos de agrupamento são baseados em 3 princípios:

##### *K-Anonymity*

No modelo *K-Anonymity*, os dados são **agrupados** para cada grupo ter pelo menos K elementos. O K é um valor definido
pelo utilizador. O *K-Anonymity* é um modelo de privacidade que garante que os dados sensíveis não são divulgados. No
entanto, o *K-Anonymity* não garante que os dados não sejam **reidentificados**.

##### *L-Diversity*

Este modelo de privacidade é uma **extensão** do *K-Anonymity*. No modelo *L-Diversity*, os dados são agrupados para
cada grupo ter pelo menos K elementos e que cada grupo tenha **pelo menos** L valores **distintos** para o atributo
sensível. O L é um valor definido pelo utilizador. O *L-Diversity* é um modelo de privacidade que garante que os dados
sensíveis não são divulgados. No entanto, o *L-Diversity* não garante que os dados não sejam **reidentificados**.

##### *T-Closeness*

Este modelo de privacidade é uma **extensão** do *L-Diversity*. No modelo *T-Closeness*, os dados são agrupados para
cada grupo ter pelo menos K elementos e que cada grupo tenha pelo menos L valores distintos para o atributo sensível.
Além disso, os dados são agrupados para que a diferença entre o valor mais frequente e o valor menos frequente para o
atributo sensível seja **menor ou igual a T**. O T é uma variável definida pelo utilizador. O *T-Closeness* é um modelo
de privacidade que garante que os dados sensíveis não são divulgados. No entanto, o *T-Closeness* não garante que os
dados não sejam **reidentificados**.

#### Privacidade Diferencial (*Differential Privacy*)

A privacidade diferencial é um modelo de privacidade que garante que os dados sensíveis não são divulgados e que os
dados não são reidentificados. O modelo de privacidade diferencial é baseado em 2 princípios:

##### Diferencial de Laplace

O modelo de privacidade diferencial de Laplace é um modelo de privacidade que garante que os dados sensíveis não são
divulgados e que os dados não são reidentificados. O modelo de privacidade diferencial de Laplace é baseado no princípio
de, que os dados sensíveis não são divulgados (reidentificados).

##### Diferencial de Gauss

O modelo de privacidade diferencial de Gauss é um modelo de privacidade que garante que os dados sensíveis não são
divulgados e que os dados não são reidentificados. O modelo de privacidade diferencial de Gauss é baseado no princípio
de, que os dados sensíveis não são divulgados (reidentificados).

### Conclusão

Em suma, a automatização da anonimização de dados é uma solução relevante para empresas e organizações que precisam
lidar com grandes volumes de informações sensíveis e precisam cumprir as leis de proteção de dados, como o RGPD. A
ferramenta ARX é uma opção eficiente para implementar esse processo, permitindo a aplicação de modelos de privacidade,
como o *K-Anonymity*, *L-Diversity* e *T-Closeness*, para garantir a proteção dos dados e evitar a divulgação de
informações sensíveis. A anonimização de dados é uma questão importante para garantir a privacidade e segurança dos
indivíduos, e a automatização desse processo pode ajudar empresas e organizações a cumprir com as suas obrigações legais
e éticas.

## Engenharia de Software

### Requisítos funcionais

Estes são os requisítos funcionais que o sistema deve ter:

* [ ] O utilizador deve poder abrir e guardar ficheiros de dados.
* [ ] O utilizador deve poder escolher o formato de dados e o tipo de atributos.
* [ ] O utilizador deve poder escolher o(s) modelo(s) de privacidade e os seus parâmetros.
* [ ] O utilizador deve poder ter uma visualização dos dados.
* [ ] O utilizador deve ter uma análise estatítisca da segurança dos dados.
* [ ] O utilizador deve poder exportar os dados anonimizados e a análise estatística.
* [ ] O sistema deve descardar os dados considerados 'identifacadores'.
* [ ] O sistema tem que ser intuítivo e fácil de usar.
* [ ] O sistema tem que ser rápido e eficiente.

### Requisítos não funcionais

Estes são os requisítos não funcionais que o sistema deve ter:

* [ ] O sistema tem que ser seguro, e escalável.
* [ ] O sistema tem que usar a API do ARX.
* [ ] O sistema será feito em Java.
* [ ] O utilizador deve ser responsável por qualquer uso indevido dos dados.
* [ ] O sistema deve permitir o uso da anonimidade K, caso K seja maior que 2.
* [ ] O sistema deve permitir ao utilizador escolher qual das transformações de dados deve ser aplicada.
* [ ] O sistema deve execução estas operações num tempo aceitável, caso contrário, o sistema deve informar o utilizador
  que o tempo de execução é demasiado elevado.

### Interface

A ‘interface’ do sistema deve permitir que os utilizadores façam as seguintes tarefas:

* [ ] **Seleção de fonte de dados:** permitir que os utilizadores selecionem a fonte dos dados a serem anonimizados e
  analisados, podendo ser uma folha do Excel, um banco de dados, um arquivo CSV, ou outro formato compatível com o ARX.
* [ ] **Seleção de modelo de privacidade:** permitir que os utilizadores escolham o modelo de privacidade a ser aplicado
  na anonimização, como k-anonimato, l-diversidade, t-closeness, ou outro modelo disponível na API do ARX.
* [ ] **Configuração de parâmetros:** permitir que os utilizadores definam os parâmetros específicos do modelo de
  privacidade, como tamanho do grupo k, sensibilidade l, ou outros parâmetros relevantes para o modelo escolhido.
* [ ] **Visualização dos dados:** permitir que os utilizadores visualizem os dados a serem anonimizados e analisados,
  podendo ser exibidos em forma de tabela, gráficos ou outro modo de visualização apropriada.
* [ ] **Anonimização:** permitir que os utilizadores iniciem o processo de anonimização dos dados, usando o modelo de
  privacidade escolhido e os parâmetros configurados.
* [ ] **Análise exploratória:** permitir que os utilizadores realizem análises exploratórias nos dados anonimizados,
  como agrupamento, análise de tendências, entre outras técnicas disponíveis no ARX.
* [ ] **Exportação de dados:** permitir que os utilizadores exportem os dados anonimizados e as análises realizadas para
  outros formatos, como folhas do Excel, arquivos CSV ou outros formatos suportados pelo ARX.
* [ ] **Registo de atividades:** permitir que a ‘interface’ registe as atividades realizadas pelos utilizadores durante
  a anonimização e análise dos dados, para fins de rastreabilidade e auditoria.

## Primeiro Uso da API (Projeto em Terminal)

O primeiro uso da API do ARX será feito em terminal, para possibilitar a testagem da API e perceber como a mesma
funciona.
Estes são os passos que o utilizador deve seguir para anonimizar os dados, nesta primeira versão:

* [ ] Ler ficheiro de entrada
* [ ] Definir o tipo de metadados e o tipo de atributos
    * [ ] Caso seja um tipo 'sensivel', definir algoritmo de generalização/supressão (criar/exportar hierarquia)
    * [ ] Caso seja um tipo 'indentificador', suprimir o atributo
    * [ ] Caso seja um tipo 'quase identificador', definir algoritmo de generalização/supressão (criar/exportar
      hierarquia)
    * [ ] Caso seja um tipo 'insensivel', não fazer nada
* [ ] Definir o modelo de privacidade (Ele tem de ter pelo menos um atribusto quase identificador e os atributos
  sensiveis têm de ter o algoritmo de generalização/supressão definida)
    * [ ] Definir o tamanho do grupo (k-anonimato)
    * [ ] Caso desejado definir a diversidade (l-diversidade) e a diferença (t-closeness)
    * [ ] Devolver a tabelo dos dados anonimizados
* [ ] Guardar os ficheiros anonimizados
* [ ] Exibir estatísticas sobre a segurança dos dados
    * [ ] Exibir a quantidade de dados restantes em relação ao original:
        * [ ] Dados restantes;
        * [ ] Dados suprimidos;
    * [ ] Exibir o risco estimado de reidentificação:
        * [ ] Prosecutor Risk: risco de ser acusado de um crime;
        * [ ] Journalist Risk: risco de ser exposto por um jornalista;
        * [ ] Marketer Risk: risco de ser exposto por um marketing;
    * [ ] Exibir nível de qualidade do conjunto de dados anonimizados:
        * [ ] Taxa de Generalização (Generalization intensity);
        * [ ] Taxa de Detalhe (Granularity);
        * [ ] Taxa de Identificabilidade (Discernability);
        * [ ] ...

## Estatísticas

### Introdução

As estatísticas são uma parte fundamental de qualquer aplicação de automatização de processos de anonimização. Permitem
aos utilizadores avaliar o nível de segurança dos dados anonimizados e a qualidade do conjunto de dados resultante. Além
disso, as estatísticas ajudam a atenuar o risco de reidentificação, permitindo que os utilizadores avaliem o risco de
serem expostos por jornalistas, marketers e outros agentes. Neste capítulo, vamos explorar a importância das
estatísticas numa aplicação de automatização de processos de anonimização, discutindo as principais métricas e como elas
podem ser usadas para avaliar a eficácia da anonimização.

### Quantidade de Dados Restantes

A quantidade de dados restantes é uma métrica que permite aos utilizadores avaliar o nível de perda de informação que
ocorreu durante o processo de anonimização. Esta métrica é calculada dividindo o número de registros anonimizados pelo
número de registros originais. Quanto maior for o valor desta métrica, maior será a quantidade de dados restantes. O
nosso objetivo é manter o maior número de dados possível, para que a anonimização não prejudique a qualidade dos dados.

### Nível de Risco

A avaliação do nível de risco é uma das estatísticas-chave numa aplicação de automatização de processos de anonimização.
O risco de reidentificação pode ser medido via diferentes métricas, que consideram os diferentes tipos de ameaças e os
seus respetivos impactos. Nesta subsecção, vamos explorar os possíveis riscos, como eles são medidos e como
essa avaliação pode ser usada para tomar decisões informadas sobre a anonimização dos dados. Além disso, discutiremos
como as estatísticas de risco podem ser usadas para comunicar o nível de segurança dos dados anonimizados aos
'*stakeholders*' envolvidos.

#### Risco de Ser Acusado de um Crime (*Prosecutor Risk*)

O risco de ser acusado de um crime é uma das principais ameaças à privacidade dos dados pessoais. Esta ameaça pode
ser causada por um ataque de [*'data mining'*](https://en.wikipedia.org/wiki/Data_mining) ou por um [*'data
leak'*](https://en.wikipedia.org/wiki/Data_leak).

#### Risco de Ser Exposto por um Jornalista (*Journalist Risk*)

O risco de ser exposto por um jornalista é uma ameaça à privacidade dos dados pessoais que pode ser causada por um
[*'data leak'*](https://en.wikipedia.org/wiki/Data_leak).

#### Risco de Ser Exposto por um Marketer (*Marketer Risk*)

O risco de ser exposto por um marketer é uma ameaça à privacidade dos dados pessoais que pode ser causada por um
[*'data leak'*](https://en.wikipedia.org/wiki/Data_leak).

### Nível de Qualidade do Conjunto de Dados Anonimizados

A avaliação da qualidade do conjunto de dados anonimizados é outra estatística crítica numa aplicação de automatização
de processos de anonimização. A qualidade do conjunto de dados anonimizados é uma medida da eficácia da anonimização em
preservar a privacidade dos indivíduos enquanto mantém a utilidade dos dados. Nesta subsecção, vamos explorar as
principais métricas utilizadas para avaliar a qualidade do conjunto de dados anonimizados, incluindo a taxa de
generalização, a taxa de detalhe e a taxa de identificabilidade. Discutiremos como essas métricas podem ser usadas para
encontrar um equilíbrio entre privacidade e utilidade dos dados, e como a avaliação da qualidade do conjunto de dados
anonimizados pode ser usada para tomar decisões informadas sobre a seleção de técnicas de anonimização e a configuração
de parâmetros.

#### Taxa de Generalização (*Generalization Intensity*)

A taxa de generalização é uma métrica que mede o nível de generalização aplicado aos dados originais para obter os dados
anonimizados.

Uma taxa de generalização alta significa que os dados foram fortemente generalizados, resultando numa perda
significativa de detalhes relativamente aos dados originais. Uma taxa de generalização baixa significa que os dados
foram menos generalizados, mantendo mais precisão em relação aos dados originais.

#### Taxa de Detalhe (*Granularity*)

A taxa de detalhe é uma métrica que mede o grau de precisão dos dados anonimizados relativamente aos dados originais.

Uma alta taxa de detalhe significa que os dados anonimizados mantêm muitos detalhes relativamente aos dados originais,
enquanto uma taxa de detalhe baixa significa que os dados anonimizados são mais generalizados com menos detalhes
relativamente aos dados originais.

#### Taxa de Identificabilidade (*Discernability*)

A taxa de identificabilidade é uma métrica que mede a facilidade com que os indivíduos podem ser identificados a partir
dos dados anonimizados.

Uma alta taxa de identificabilidade significa haver um alto risco de reidentificação dos indivíduos, ou seja, facilmente
é possível identificar as informações pessoais de uma pessoa a partir dos dados anonimizados. Por outro lado, uma taxa
de identificabilidade baixa significa ser improvável que as informações pessoais dos indivíduos possam ser identificadas
a partir dos dados anonimizados.

## Modificações feitas na Biblioteca ARX

Durante o desenvolvimento de um projeto de ‘software’, é comum ocorrer conflitos no código que podem levar a erros e
problemas de funcionamento. Um dos problemas que pode surgir é a leitura duplicada de determinadas *'packages'* do
projeto, o que pode causar conflitos no módulo e afetar o desempenho da biblioteca.

Neste projeto, foi identificado que os *'packages'* ‘*java.util*’ e a ‘*javax*’ eram lidas duas vezes, o que
causava conflitos no módulo e afetando o funcionamento da biblioteca. Isso afetava a capacidade da biblioteca de
lidar com determinados dados.

Para resolver esse problema, tivemos que realizar uma alteração na biblioteca, removendo os mesmos. Essa mudança foi
necessária para garantir que a biblioteca conseguisse funcionar corretamente, sem causar erros ou problemas.

## Auxílios

Estas são as referências usadas para a realização deste projeto:

* [Facebook Data breach](https://www.cnbc.com/2018/10/12/facebook-security-breach-details.html)
* [Equifax Data breach](https://www.equifaxsecurity2017.com/)
* [Utility-driven assessment of anonymized data via clustering](https://www.nature.com/articles/s41597-022-01561-6)
* [Sobre a ferramenta](https://onlinelibrary.wiley.com/doi/10.1002/spe.2812)
* [Sobre a API](https://arx.deidentifier.org/development/api/)
* [Introdução da Ferramenta ARX](https://www.youtube.com/watch?v=N8I-sxmMfqQ&t=1s)
* [Documentação da API](https://arx.deidentifier.org/development/api/)