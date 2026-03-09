package TOPSECRET.repository;

import TOPSECRET.domain.Country;
import TOPSECRET.domain.CountryFactory;

import java.util.ArrayList;
import java.util.List;


public class CountryRepository { //Define a classe - responsável por armazenar os objetos "Country" e controlar o acesso a dados

    //Guardar os países registados numa "List"
    private final List<Country> registeredCountries = new ArrayList<>(); //registeredCountries é privado, só o repositório pode modificar
    //private garante encapsulamento, final faz com que a referência à lista não possa ser trocada, List<Country> diz que registeredCountries é do tipo List (uma lista) de objetos Country;
    //new ArrayLists<>() é uma implementação concreta, cria uma nova lista Arrays. É um "resizable array", pode ir aumentando e diminuindo
    //linha funciona como "base de dados" em memória
    private final CountryFactory countryFactory;

    public CountryRepository(CountryFactory countryFactory){
        this.countryFactory = countryFactory;
    }

    //Prevenir duplicados
    private boolean isANewCountry(Country country){ //méto.do privado auxiliar, só usado nesta classe (por causa de ser privado)
        for (Country newCountry : this.registeredCountries){ //loop pelos paises registados ("Para cada país registado, ver este "newCountry"
            if (country.equals(newCountry)) //Compara os países guardados com o dado;
                return false; //return false se o país nao for "novo", já está registado
        }
        return true; //o país é novo, pode ser registado
    }

    //Criar um country e adicionar à list
    public Country createAndAddNewCountry(String countryName) { //méto.do público, para criar o objeto do tipo Country e adicionar à lista (repositório)
        Country country = countryFactory.create(countryName); //cria um novo objeto do domínio e chama o construtor da classe Country (a validação interna acontece lá)
        if (!isANewCountry(country)) { //prevenir duplicados, o "isANewCountry" é "true" e com o ! fica false, ou seja, if "!" isANewCountry significa se for falso
            //chama o méto.do auxiliar de cima, se não for um país novo, entra no "if"
            throw new IllegalArgumentException("Country is already registered"); //quando entrar no "if", vai dar "throw new" a uma exceção e usa essa mensagem
            //acontece quando se tenta registar o mesmo país mais do que uma vez
        }
        registeredCountries.add(country); //só entra aqui se não entrar no "if" - não há duplicado
        return country; //retorna o "country", para permitir acesso do Controller e do UI (não relevante por agora)
        }

    //Return de uma cópia da list (para prevenir modificações externas)
    public List<Country> getAllCountries() { //méto.do para ler, seguro para usar em testes. Serve para listar países e para os testes unitários
        return List.copyOf(registeredCountries); //importante ter o copy.Of porque assim retorna uma cópia imutável da lista original (dos países - tipo List<Country>)
    }
}
