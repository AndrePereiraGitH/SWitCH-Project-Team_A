package TOPSECRET.repository;

import TOPSECRET.domain.Country;

import java.util.ArrayList;
import java.util.List;


public class CountryRepository {

    //Guardar os países registados numa "List"
    private final List<Country> registeredCountries = new ArrayList<>(); //registeredCountries é privado, só o repositório pode modificar

    //Criar um country e adicionar à list
    public void createAndAddNewCountry(String _countryName) { //chama o construtor do domain
        Country country = new Country(_countryName); //vai verificar no enum
        registeredCountries.add(country);
    }

    //Return de uma cópia da list (para prevenir modificações externas)
    public List<Country> getAllCountries() { //method para ler, seguro para usar em testes
        return List.copyOf(registeredCountries);
    }
}
