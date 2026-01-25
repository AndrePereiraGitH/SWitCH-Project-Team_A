package TOPSECRET.controller; //Define o "package" onde a classe está localizada

import TOPSECRET.domain.Country;
import TOPSECRET.repository.CountryRepository; //import de classes para o Java saber que existem

public class US007ToRegisterCountryController { //Define uma classe pública

    private final CountryRepository _countryRepository; //Um atributo da classe (final - não pode ser alterado)

    public US007ToRegisterCountryController(CountryRepository countryRepository) { // Construtor da classe. Recebe o repositório, não o cria
        this._countryRepository = countryRepository; // this é referência ao atributo desta classe
    }

    //Being called by the UI - "registerNewCountry (countryName)"

    public Country registerNewCountry (String _countryName) { //Méto.do público que recebe uma String e retorna um objeto tipo Country

        if (_countryName == null || _countryName.isBlank()){ //Vai validar, com o if, se o _countryName for null ou estiver em branco
            throw new IllegalArgumentException("Country name cannot be null or blank"); //caso o if seja true, "throw new ..." é lançar uma exceção caso o nome seja inválido. Protege o domínio
        }
        return _countryRepository.createAndAddNewCountry(_countryName); //caso o if seja false, o _countryName não é null nem está em branco, o controller chama o repositório para criar um objeto "Country" e adicionar à lista/base de dados. Objeto Country é retornado
    }
}
