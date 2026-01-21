package TOPSECRET.repository;

import TOPSECRET.domain.Country;

import java.util.ArrayList;
import java.util.List;


public class CountryRepository {

    //Guardar os países registados numa "List"
    private final List<Country> registeredCountries = new ArrayList<>(); //registeredCountries é privado, só o repositório pode modificar

    //Prevenir duplicados
    private boolean isANewCountry(Country country){
        for (Country newCountry : this.registeredCountries){ //loop pelos paises registados
            if (country.equals(newCountry)) //Compara os paises guardados com o dado; equalsIgnoreCase ignora ser minuscula ou maiuscula.
                return false; //return false se o país nao for "novo", já está registado
        }
        return true; //o país é novo, pode ser registado
    }

    //Criar um country e adicionar à list
    public Country createAndAddNewCountry(String countryName) { //chama o construtor do domain
        Country country = new Country(countryName); //vai verificar no enum e se é null
        if (!isANewCountry(country)) { //prevenir duplicados, o "isANewCountry" é "true" e com o ! fica false, ou seja, if "!" isANewCountry significa se for falso
            throw new IllegalArgumentException("Country is already registered");
        }
        registeredCountries.add(country);
        return country;
        }

    //Return de uma cópia da list (para prevenir modificações externas)
    public List<Country> getAllCountries() { //method para ler, seguro para usar em testes
        return List.copyOf(registeredCountries);
    }
}
