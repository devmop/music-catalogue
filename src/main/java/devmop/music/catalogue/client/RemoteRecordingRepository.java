package devmop.music.catalogue.client;

import javax.inject.Named;

import devmop.music.catalogue.ID;
import devmop.music.catalogue.Recording;
import devmop.music.catalogue.RecordingRepository;
import feign.Feign;
import feign.RequestLine;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

/**
 * @author (michael)
 */
public class RemoteRecordingRepository implements RecordingRepository
{
  private final Client client_;

  public RemoteRecordingRepository(String url) {
    client_ = Feign.builder().decoder(new JacksonDecoder())
                   .encoder(new JacksonEncoder()).retryer(new Retryer.Default(100, 1000, 1))
                   .target(Client.class, url);
  }

  @Override
  public ID create(final Recording recording)
  {
    return client_.create(recording);
  }

  @Override
  public Recording find(final ID id)
  {
    return client_.find(id);
  }

  @Override
  public void update(final ID id, final Recording recording)
  {
    client_.update(id, recording);
  }

  @Override
  public void delete(final ID id)
  {
    client_.delete(id);
  }

  @Override
  public void deleteAll()
  {
    client_.deleteAll();
  }

  private interface Client {

    @RequestLine("POST /recordings")
    public ID create(Recording recording);

    @RequestLine("GET /recordings?id={id}")
    public Recording find(@Named("id") ID id);

    @RequestLine("PUT /recordings/{id}")
    public void update(@Named("id") ID id, Recording recording);

    @RequestLine("DELETE /recordings/{id}")
    public void delete(@Named("id") ID id);

    @RequestLine("DELETE /recordings")
    public void deleteAll();
  }
}
