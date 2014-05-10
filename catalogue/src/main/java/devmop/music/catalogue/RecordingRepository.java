package devmop.music.catalogue;

/**
 * @author (michael)
 */
public interface RecordingRepository
{
  public ID create(Recording recording);
  public Recording find(ID id);
  public void update(final ID id, final Recording recording);
  public void delete(final ID id);
  public void deleteAll();
}
