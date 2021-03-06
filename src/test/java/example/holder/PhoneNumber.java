package example.holder;

import org.seasar.doma.Holder;

@Holder(valueType = String.class)
public class PhoneNumber {

  private final String value;

  public PhoneNumber(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public int hashCode() {
    final var prime = 31;
    var result = 1;
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    var other = (PhoneNumber) obj;
    if (value == null) {
      if (other.value != null) return false;
    } else if (!value.equals(other.value)) return false;
    return true;
  }
}
