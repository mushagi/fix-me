package za.co.wethinkcode.mmayibo.fixme.market;

import za.co.wethinkcode.mmayibo.fixme.core.persistence.IRepository;
import za.co.wethinkcode.mmayibo.fixme.core.persistence.RepositoryImp;

public class Global {
    static public IRepository repository = new RepositoryImp();
}