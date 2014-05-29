package devmop.music.catalogue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author (michael)
 */
@RunWith(Parameterized.class)
public class EnumCoverageTest
{
  @SuppressWarnings("unchecked")
  private static List<Class<? extends Enum>> enumClasses() {
    return (List) Arrays.asList(RepositoryFactory.Implementation.class);
  }

  @Parameterized.Parameters(name = "{1}")
  public static Iterable<Object[]>data()
  {
    LinkedList<Object[]> list = new LinkedList<Object[]>();
    for (Class clazz : enumClasses())
    {
      list.add(new Object[] {clazz, clazz.getSimpleName()});
    }
    return list;
  }

  private final Class<? extends Enum> enumClass_;

  public EnumCoverageTest(final Class<? extends Enum> enumClass, String name)
  {
    enumClass_ = enumClass;
  }

  @Test
  @SuppressWarnings("unchecked")
  public void exerciseValueOf() throws Exception
  {
    String name = enumClass_.getEnumConstants()[0].name();
    Method valueOf = enumClass_.getMethod("valueOf", String.class);
    valueOf.invoke(null, name);
  }
}
