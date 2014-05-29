package devmop.music.catalogue.api;

import devmop.music.catalogue.ID;
import devmop.music.catalogue.Recording;
import devmop.music.catalogue.RecordingRepository;

import javax.ws.rs.*;

/**
 * @author (michael)
 */
@Path("/recordings")
@Consumes("application/json")
@Produces("application/json")
public class RecordingAPI implements RecordingRepository
{
  private final RecordingRepository recordingRepository_;

  public RecordingAPI(final RecordingRepository recordingRepository)
  {
    recordingRepository_ = recordingRepository;
  }

  @Override
  @POST
  public ID create(final Recording recording)
  {
    return recordingRepository_.create(recording);
  }

  @Override
  @Path("/{id}")
  @GET
  public Recording find(@PathParam("id") final ID id)
  {
    return recordingRepository_.find(id);
  }

  @Override
  @Path("/{id}")
  @PUT
  public void update(@PathParam("id") final ID id, final Recording recording)
  {
    recordingRepository_.update(id, recording);
  }

  @Override
  @Path("/{id}")
  @DELETE
  public void delete(final ID id)
  {
    recordingRepository_.delete(id);
  }

  @Override
  @DELETE
  public void deleteAll()
  {
   recordingRepository_.deleteAll();
  }
}
