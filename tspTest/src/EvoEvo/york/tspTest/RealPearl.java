package EvoEvo.york.tspTest;

import EvoEvo.york.machineMetaModel.Domain;
import EvoEvo.york.machineMetaModel.Pearl;

public class RealPearl extends Pearl{
    private double _value;

    public RealPearl(double value, Domain domain) {
        super(domain);
        _value = value;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        RealPearl result = (RealPearl) super.clone();
        result._value = _value;
        return result;
    }

    @Override
    /** Two cities are the same if their names are the same, nothing else matters. */
    public boolean equals(Object obj) {
        return this.getClass().isAssignableFrom(obj.getClass()) && _value == ((RealPearl)obj)._value;
    }

    public void setValue(double value) {
        this._value = value;
    }

    public double getValue() {
        return _value;
    }

    @Override
    public String toString() {
        return String.valueOf(_value);
    }
}
