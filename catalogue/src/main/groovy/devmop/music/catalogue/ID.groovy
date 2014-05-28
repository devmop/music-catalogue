package devmop.music.catalogue

import groovy.transform.CompileStatic

/**
 * @author ( michael )
 */
@CompileStatic
class ID
{
  private final long id

  public ID(final long id)
  {
    this.id = id
  }

  public long getId()
  {
    return id
  }
}
