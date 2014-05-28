package devmop.music.catalogue;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author ( michael )
 */
public class MapBackedRecordingRepository implements RecordingRepository
{
  private Map<ID, Recording> recordings = new LinkedHashMap<ID, Recording>();

  @Override
  public ID create(final Recording recording)
  {
    ID id = new ID(0L);
    recordings.put(id, recording);
    return id;
  }

  @Override
  public Recording find(final ID id)
  {
    checkKeyExists(id);
    return recordings.get(id);
  }

  @Override
  public void update(final ID id, final Recording recording)
  {
    checkKeyExists(id);
    recordings.put(id, recording);
  }

  @Override
  public void delete(final ID id)
  {
    checkKeyExists(id);
    recordings.remove(id);
  }

  @Override
  public void deleteAll()
  {
    recordings = new LinkedHashMap<ID, Recording>();
  }

  private void checkKeyExists(ID id)
  {
    if (!recordings.containsKey(id)) {
      throw new NotFoundException();
    }
  }
}
