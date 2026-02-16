package TOPSECRET.domain;

import TOPSECRET.domain.Enum.TypeOfBinding;

import java.util.Objects;

public class PublicationType {

    private TypeOfBinding _typeOfBinding;
    private String _details;

    public PublicationType (TypeOfBinding typeOfBinding, String details)
    {

        if (typeOfBinding == null){
            throw new IllegalArgumentException("Type of binding can´t be null");
        }

        if (details == null || details.isBlank()){
            throw new IllegalArgumentException("Details can´t be null or blank");
        }

        this._typeOfBinding = typeOfBinding;
        this._details = details;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        PublicationType that = (PublicationType) obj;
        return  _typeOfBinding == that._typeOfBinding && _details.equals(that._details);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(_typeOfBinding, _details);
    }

    public TypeOfBinding getTypeOfBinding() { return _typeOfBinding; }
    public String getDetails() { return _details; }
}
