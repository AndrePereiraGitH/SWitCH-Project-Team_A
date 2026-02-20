package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

public class Library { //Define a classe Library (biblioteca de publicações, pertencente a um User

    private List<PublicationSample> myPublications; //private para encapsulamento, cria uma List<Publication> chamada myPublications
    private final String libraryName; //private final para ser privado e imutável, de um String intitulado libraryName
    private final User user; //private final para ser privado e imutável, do tipo User (classe User) chamada "user"

    //Constructor
    public Library(String libraryName, User user) { //Construtor de Library, como atributos tem um String (os acima indicados) e um User (o acima indicado)
        this(List.of(), libraryName, user); //Vai chamar o construtor principal com o "this"
    } //List.of() é um méto.do do Java para criar uma lista imutável vazia, resulta em List<Publications> myPublications = []
    //Resumindo, este construtor cria uma biblioteca nova com nome = libraryName, user = user, lista de publicações inicialmente vazia

    public Library(List<PublicationSample> myPublications, String libraryName, User user){ //Construtor completo, quando temos a lista inicial de publications

        if (libraryName == null || libraryName.isBlank()){ //Casos de libraryName ser null ou vazia
            throw new IllegalArgumentException("Library Name cannot be null or blank");
        }
        if (user == null){ //Casos de user ser null
            throw new IllegalArgumentException("User cannot be Null");
        }
        this.libraryName = libraryName;
        this.user = user;
        // ✅ garante sempre lista mutável
        this.myPublications = new ArrayList<>(myPublications == null ? List.of() : myPublications);
    } //Estes vão atribuir valores fornecidos aos atributos da classe.

    //Get publications in the Library
    public List<PublicationSample> getAllPublications(){ //Um getter
        return List.copyOf(myPublications); //Vai retornar uma cópia da lista myPublications, de modo a ser imutável, não se pode alterar (Read-Only)
    }

    public String getLibraryName(){ //Um getter
        return libraryName; //retorna o nome da library
    }

    public User getUser(){ //Um getter
        return user; //retorna o nome do user (dono da library)
    }

    //Add publication
    public boolean addPublication(PublicationSample publicationSample){ //Este é um méto.do, do tipo boolean, que vai verificar se a publicação já existe na library e se não existir, adiciona e retorna True
        if (!myPublications.contains(publicationSample)){ //Se na myPublications NÃO estiver a "publication" fornecida, irá executar a linha 47
            myPublications.add(publicationSample); //dá add à publication à myPublications
            return true; //Publication was added
        }
        return false; //Publication was not added (already exists)
    }



    // US0018
    public boolean containsPublication(PublicationSample publication) {
        return myPublications.contains(publication);
    }

    // Necessary methods to compare the objects
    // Allows Library objects to be compared by content instead of reference.
    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        if (!(obj instanceof Library)) return false;

        Library other = (Library) obj;

        return this.getAllPublications().equals(other.getAllPublications());
    }

    // Ensures consistency with equals().
    @Override
    public int hashCode() {
        return getAllPublications().hashCode();
    }


}
