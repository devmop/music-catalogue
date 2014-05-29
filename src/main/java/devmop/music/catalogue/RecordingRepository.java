package devmop.music.catalogue;

/**
 * @author (michael)
 */
public interface RecordingRepository
{
  ID create(Recording recording);
  Recording find(ID id);
  void update(final ID id, final Recording recording);
  void delete(final ID id);
  void deleteAll();

}
