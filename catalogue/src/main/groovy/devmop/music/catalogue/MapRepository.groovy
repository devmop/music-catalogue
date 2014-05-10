package devmop.music.catalogue

import groovy.transform.CompileStatic

/**
 * @author ( michael )
 */
@CompileStatic
class MapRepository implements Repository
{
  def Map<ID, Recording> recordings = [:]

  @Override
  ID save(final Recording recording)
  {
    def id = new ID();
    recordings.put(id, recording);
    return id;
  }

  @Override
  Recording find(final ID id)
  {
    return recordings.get(id);
  }
}
