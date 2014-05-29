package devmop.music.catalogue.client;

import devmop.music.catalogue.ID;
import devmop.music.catalogue.Recording;
import devmop.music.catalogue.RecordingRepository;

/**
 * @author (michael)
 */
public class RemoteRecordingRepository implements RecordingRepository
{
  @Override
  public ID create(final Recording recording)
  {
    return null;
  }

  @Override
  public Recording find(final ID id)
  {
    return null;
  }

  @Override
  public void update(final ID id, final Recording recording)
  {

  }

  @Override
  public void delete(final ID id)
  {

  }

  @Override
  public void deleteAll()
  {

  }
}
