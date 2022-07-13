package kalchenko.data;

import org.springframework.data.repository.CrudRepository;

public interface DataCRUD extends CrudRepository<Data, Long>, DataRepository{

}
