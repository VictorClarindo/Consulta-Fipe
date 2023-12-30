package br.com.alura.consultafipe.principal;

import br.com.alura.consultafipe.model.Dados;
import br.com.alura.consultafipe.model.Modelos;
import br.com.alura.consultafipe.model.Veiculos;
import br.com.alura.consultafipe.service.ConsumoApi;
import br.com.alura.consultafipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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

        if (opcao.toLowerCase().contains("car")) {
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

        System.out.println("\nDigite um trecho do carro que gostaria de pesquisar: ");
        var veiculoABuscar = scanner.nextLine();

        var modeloBuscado = modelosLista.modelos().stream()
                .filter(dados -> dados.nome().toLowerCase().contains(veiculoABuscar.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\n Modelos filtrados:");
        modeloBuscado.forEach(System.out::println);

        System.out.println("Agora digite o codigo do modelo que quer saber o valor: ");
        var codigoModelo = scanner.nextLine();

        endereco += codigoModelo + "/anos/";

        json = consumoApi.obterDadosApi(endereco);

        var anos = conversor.obterLista(json, Dados.class);
        List<Veiculos> veiculosLista = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAno = endereco + anos.get(i).codigo();
            json = consumoApi.obterDadosApi(enderecoAno);
            Veiculos veiculo = conversor.obterDados(json, Veiculos.class);
            veiculosLista.add(veiculo);
        }
        veiculosLista.forEach(System.out::println);
    }
}

