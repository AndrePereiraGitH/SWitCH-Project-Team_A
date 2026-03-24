package TOPSECRET.ddd;

public interface DomainEntity <ID extends DomainID>  {

    ID id();

    boolean sameIdentityAs(Object object);
}
