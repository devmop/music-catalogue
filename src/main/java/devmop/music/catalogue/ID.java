package devmop.music.catalogue;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author ( michael )
 */
public class ID
{
  private final long id;

  @JsonCreator
  public ID(final long id)
  {
    this.id = id;
  }

  @JsonValue
  public long getId()
  {
    return id;
  }
}
