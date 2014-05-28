package devmop.music.catalogue;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.impl.SequenceImpl;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.getDataType;
import static org.jooq.impl.DSL.table;
/**
 * @author ( michael )
 */
public class DatabaseBackedRecordingRepository implements RecordingRepository
{
  private DSLContext dsl;
  private Table<Record> RECORDINGS = table("recordings");
  private Field<Long> ID = field("id", Long.class);
  private Field<String> TITLE = field("title", String.class);

  DatabaseBackedRecordingRepository(DSLContext dsl)
  {
    this.dsl = dsl;
  }

  @Override
  public ID create(final Recording recording)
  {
    long id = dsl.fetchOne("select nextval('recording_ids') as id").getValue(0, Long.class);
    dsl.insertInto(RECORDINGS).set(ID, id).set(TITLE, recording.getTitle()).execute();
    return new ID(id);
  }

  @Override
  public Recording find(final ID id)
  {
    checkKeyExists(id);
    Record record = dsl.select(TITLE).from(RECORDINGS).where(ID.equal(id.getId())).fetchOne();
    return new Recording(record.getValue(TITLE));
  }

  @Override
  public void update(final ID id, final Recording recording)
  {
    checkKeyExists(id);
    dsl.update(RECORDINGS).set(TITLE, recording.getTitle()).where(ID.equal(id.getId())).execute();
  }

  @Override
  public void delete(final ID id)
  {
    checkKeyExists(id);
    dsl.delete(RECORDINGS).where(ID.equal(id.getId())).execute();
  }

  @Override
  public void deleteAll()
  {
    dsl.truncate(RECORDINGS).execute();
  }

  public void checkKeyExists(ID id)
  {
    int count = dsl.select(ID).from(RECORDINGS).where(ID.equal(id.getId())).fetchCount();
    if (count != 1)
    {
      throw new NotFoundException();
    }
  }
}
