package kalchenko.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DataController {

    @Autowired
    DataRepository dataRepository;

}
