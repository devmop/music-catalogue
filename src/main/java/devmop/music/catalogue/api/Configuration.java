package devmop.music.catalogue.api;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import devmop.music.catalogue.RepositoryFactory;

/**
 * @author (michael)
 */
public class Configuration extends io.dropwizard.Configuration
{
  private RepositoryFactory repositoryFactory_ = new RepositoryFactory();

  @NotNull
  public RepositoryFactory getRepositoryFactory()
  {
    return repositoryFactory_;
  }

  @JsonProperty("repository")
  public void setRepositoryFactory(final RepositoryFactory repositoryFactory)
  {
    repositoryFactory_ = repositoryFactory;
  }
}
