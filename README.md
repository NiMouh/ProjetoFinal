# Apontamentos para o projeto final de curso

## Manual de Instalação (No intellij)

Estes são os passos a seguir para criar o executável do projeto:

1. Abrir o projeto no intellij;
2. Ir a File -> Project Structure;
3. Na janela que aparece, ir a Artifacts;
4. Clicar no botão + e escolher JAR -> From modules with dependencies;
5. Escolher a classe principal (SetupApplication) e clicar em OK;
6. Clicar em OK na janela que aparece;
7. Ir a Build -> Build Artifacts;
8. Clicar em Build;
9. O executável vai ser criado na pasta out/artifacts/Setup_jar;
10. Para executar o programa, basta correr o ficheiro Setup.jar;

## Manual de Utilização

Estes são os passos a seguir para utilizar o programa:

1. Abrir o programa;
2. Inicialmente irá aparecer uma janela para escolher o 'dataset' que quer anonimizar, e como esse ficheiro está
   organizado (separado por vírgulas, pontos e vírgulas, etc);
3. A seguir, irá aparecer uma janela para definir os tipos de dados que o 'dataset' contém e os tipos de atributos que o
   mesmo tem (sensível, identificador, etc);
4. Após isso irá ser redirecionado para o menu principal, ele é exibido o 'dataset' e uma barra de navegação lateral no
   lado esquerdo;
5. As hiperligações para cada secção estão separadas por modelo de privacidade, e nelas tem a possível anonimização
   junto das estatísticas e riscos de cada processo feito (se ainda não foi feito nenhum irá ser exibido uma mensagem de
   erro a dizer que não foi feita nenhuma anonimização);
6. Para fazer um processo de anonimização basta clicar na hiperligação correspondente ao modelo de privacidade que quer
   utilizar, dar o valor minímo, máximo e o 'step' e clicar em 'Anonimize';
7. Irá ser pedido para escolher onde guardar estes processos (para não haver nenhum tipo de incomptabilidade, é
   recomendado que não seja alterado o nome ou localização do ficheiro);

## Modificações feitas na Biblioteca ARX

Durante o desenvolvimento de um projeto de ‘software’, é comum ocorrer conflitos no código que podem levar a erros e
problemas de funcionamento. Um dos problemas que pode surgir é a leitura duplicada de determinadas *'packages'* do
projeto, o que pode causar conflitos no módulo e afetar o desempenho da biblioteca.

Neste projeto, foi identificado que os *'packages'* *'java.util'* e a *'javax'* eram lidas duas vezes, o que
causava conflitos no módulo e afetando o funcionamento da biblioteca. Isso afetava a capacidade da biblioteca de
lidar com determinados dados.

Para resolver esse problema, tivemos que realizar uma alteração na biblioteca, removendo os mesmos. Essa mudança foi
necessária para garantir que a biblioteca conseguisse funcionar corretamente, sem causar erros ou problemas.

## Referências

Estas são as referências usadas para a realização deste projeto:

* [Facebook Data breach](https://www.cnbc.com/2018/10/12/facebook-security-breach-details.html)
* [Equifax Data breach](https://www.equifaxsecurity2017.com/)
* [Utility-driven assessment of anonymized data via clustering](https://www.nature.com/articles/s41597-022-01561-6)
* [Sobre a ferramenta](https://onlinelibrary.wiley.com/doi/10.1002/spe.2812)
* [Sobre a API](https://arx.deidentifier.org/development/api/)
* [Introdução da Ferramenta ARX](https://www.youtube.com/watch?v=N8I-sxmMfqQ&t=1s)
* [Documentação da API](https://arx.deidentifier.org/development/api/)