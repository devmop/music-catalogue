package devmop.music.catalogue

import groovy.transform.CompileStatic

/**
 * @author ( michael )
 */
@CompileStatic
class MapBackedRecordingRepository implements RecordingRepository
{
  def Map<ID, Recording> recordings = [:]

  @Override
  ID create(final Recording recording)
  {
    def id = new ID(0L)
    recordings.put(id, recording)
    return id
  }

  @Override
  Recording find(final ID id)
  {
    checkKeyExists(id);
    return recordings.get(id)
  }

  @Override
  void update(final ID id, final Recording recording)
  {
    checkKeyExists(id)
    recordings.put(id, recording)
  }

  @Override
  void delete(final ID id)
  {
    checkKeyExists(id)
    recordings.remove(id)
  }

  @Override
  void deleteAll()
  {
    recordings = [:]
  }

  private void checkKeyExists(ID id)
  {
    if (!recordings.containsKey(id)) {
      throw new NotFoundException()
    }
  }
}
