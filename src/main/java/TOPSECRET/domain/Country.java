package TOPSECRET.domain;

import java.util.Objects; // importante para podermos fazer o Objects.equals() e Objects.hash()

public class Country { //define a classe Country, entidade de domínio

    private String _countryName; // é um atributo da classe, privado (encapsulamento), guarda o estado interno do objeto

    //Construtor
    public Country(String _countryName) { //Construtor principal da classe, vai servir para garantir que os "Country" são válidos

        //Country name is null
        if (_countryName == null) { //Entramos neste "if" se o String fornecido (nome do país) for null
            throw new IllegalArgumentException("Country name is null"); //lança a exceção se o nome for null
        }

        //Check if country is real (exists in the "Countries" ENUM)
        try { //início de um bloco "try-catch", estes servem para, durante a execução, gerir exceções para tratar das falhas de forma controlada em vez de interromper a execução
            TOPSECRET.domain.Enum.Countries.valueOf(_countryName.toUpperCase()); //esta linha converte a String fornecida para UpperCase e tenta encontrar o resultado disso dentro do ENUM "Countries"
        } catch (IllegalArgumentException e) { //Se o valor anterior não existir no ENUM, entra-se nesta parte do bloco que faz a linha 21
            throw new IllegalArgumentException("Country name is not valid");
        }

        //Country is valid (is in ENUM "Countries" and isn't null)
        this._countryName = _countryName; //Se não for null e existir no ENUM, o objeto a criar é válido
    }

    //Empty Constructor - for testing purposes
    public Country() { //construtor não está a validar em nada, existe para caso seja necessário em testes (mais usado no início do desenvolvimento)
    }

    //Getter for countryName
    public String getCountryName() { //Este é um getter, utilizado para pedir uma leitura
        return _countryName; // retorna o nome do país
    }

    @Override //Vai sobrescrever o méto.do que o Java tem com o nome "equals" de modo a fazer o que nós lhe dissermos para fazer
    public boolean equals(Object o) {
        if (this == o) return true; //Se o objeto em memória for igual a "o", retorna true (é um boolean)
        if (o == null || getClass() != o.getClass()) return false; //Se "o" for null ou for de outra classe, retorna false. "getClass()" retorna uma instância de java.lang.Class que descreve o tipo de objeto
        Country country = (Country) o; //Faz "cast" do objeto genérico para "Country"
        return Objects.equals(_countryName, country._countryName); //Retorna, segundo o méto.do "equals" da classe Objects, uma comparação entre os nomes dos países (usado para detetar duplicados)
    }

    @Override
    public int hashCode() { //É um méto.do obrigatório para quando se sobrescreve "equals" como fizemos em cima
        return Objects.hash(_countryName); //Vai retornar/gerar o "hash" baseado no nome (é um tipo de identificador, #...)
    }
}
