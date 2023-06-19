# This is a sample Python script.
import matplotlib.pyplot as plt
import pandas as pd


def make_graph():
    df = pd.read_csv('diferencial_epsilon.csv', sep=';', index_col=False)

    quality_values = ['gen. intensity', 'missings', 'entropy', 'squared error']
    attributes = ['_gen', '_race', '_reg', '_years2finish', '_years_leave']

    for value in quality_values:
        plt.rcParams["figure.figsize"] = (20, 10)
        plt.rcParams.update({'font.size': 20})
        df.plot.bar(x='epsilon', y=[value + attribute for attribute in attributes], width=0.9)
        plt.xlabel('Valor do Epsilon')
        plt.ylabel('Valor em Percentagem')
        plt.title("Estatísticas da Diferencial da " + value + " com um valor de Delta de 0.01")
        plt.rcParams.update({'font.size': 15})
        plt.legend(loc='upper right')
        plt.savefig('diferencial_epsilon_statistics_' + value + '.png', bbox_inches='tight')
        plt.clf()


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    make_graph()
