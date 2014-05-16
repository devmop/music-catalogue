package devmop.music.catalogue
import groovy.transform.CompileStatic
import org.jooq.DSLContext
import org.jooq.Field
import org.jooq.Record
import org.jooq.Sequence
import org.jooq.Table
import org.jooq.impl.SequenceImpl

import static org.jooq.impl.DSL.field
import static org.jooq.impl.DSL.getDataType
import static org.jooq.impl.DSL.table
/**
 * @author ( michael )
 */
@CompileStatic
class DatabaseBackedRecordingRepository implements RecordingRepository
{
  private DSLContext dsl
  private Table<Record> RECORDINGS = table('recordings')
  private Field<Long> ID = field("id", Long.class)
  private Field<String> TITLE = field("title", String.class)
  private Sequence<Long> ID_SEQUENCE = new SequenceImpl<Long>('recording_ids', null, getDataType(Long))


  DatabaseBackedRecordingRepository(DSLContext dsl)
  {
    this.dsl = dsl
  }

  @Override
  ID create(final Recording recording)
  {
    long id = dsl.fetchOne('select nextval(\'recording_ids\') as id').getValue(0, Long)
    dsl.insertInto(RECORDINGS).set(ID, id).set(TITLE, recording.title).execute();
    return new ID(id);
  }

  @Override
  Recording find(final ID id)
  {
    checkKeyExists(id)
    def record = dsl.select(TITLE).from(RECORDINGS).where(ID.equal(id.id)).fetchOne()
    return new Recording(record.getValue(TITLE))
  }

  @Override
  void update(final ID id, final Recording recording)
  {
    checkKeyExists(id)
    dsl.update(RECORDINGS).set(TITLE, recording.title).where(ID.equal(id.id)).execute()
  }

  @Override
  void delete(final ID id)
  {
    checkKeyExists(id)
    dsl.delete(RECORDINGS).where(ID.equal(id.id)).execute()
  }

  @Override
  void deleteAll()
  {
    dsl.truncate(RECORDINGS).execute()
  }

  void checkKeyExists(ID id)
  {
    def count = dsl.select(ID).from(RECORDINGS).where(ID.equal(id.id)).fetchCount()
    if (count != 1)
    {
      throw new NotFoundException()
    }
  }
}
