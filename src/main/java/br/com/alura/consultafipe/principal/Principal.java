package br.com.alura.consultafipe.principal;

import br.com.alura.consultafipe.model.Dados;
import br.com.alura.consultafipe.model.Modelos;
import br.com.alura.consultafipe.service.ConsumoApi;
import br.com.alura.consultafipe.service.ConverteDados;

import java.util.Comparator;
import java.util.Scanner;

public class Principal {

    Scanner scanner = new Scanner(System.in);
    ConsumoApi consumoApi = new ConsumoApi();
    ConverteDados conversor = new ConverteDados();

    public void opcoesMenu() {
        String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
        String endereco;

        System.out.println("" +
                "**** OPÇÕES ****\n" +
                "\n" +
                "Carros\n" +
                "\n" +
                "Motos\n" +
                "\n" +
                "Caminhões\n" +
                "\n" +
                "Digite uma das opções para consultar valores: ");

        String opcao = scanner.nextLine();

        if (opcao.toLowerCase().contains("carr")) {
            endereco = URL_BASE + "carros/marcas/";
        } else if (opcao.toLowerCase().contains("mot")) {
            endereco = URL_BASE + "motos/marcas/";
        } else {
            endereco = URL_BASE + "caminhoes/marcas/";
        }

        var json = consumoApi.obterDadosApi(endereco);
        var marcas = conversor.obterLista(json, Dados.class);
        marcas.stream()
                .sorted(Comparator.comparing(Dados::nome))
                .forEach(System.out::println);

        System.out.println("Agora informe o código da marca para consulta: ");
        var codigoMarca = scanner.nextLine();
        endereco += codigoMarca + "/modelos/";

        json = consumoApi.obterDadosApi(endereco);

        System.out.println("\nModelos desta marca: ");

        var modelosLista = conversor.obterDados(json, Modelos.class);
        modelosLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);


    }
}
