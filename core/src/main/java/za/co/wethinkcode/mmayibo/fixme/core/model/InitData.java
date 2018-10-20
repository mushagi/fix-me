package za.co.wethinkcode.mmayibo.fixme.core.model;

import javax.xml.bind.annotation.*;
import java.util.Collection;
import java.util.List;

@XmlRootElement(name = "InitData")
@XmlAccessorType(XmlAccessType.FIELD)

public class InitData {

    @XmlElementWrapper(name="Markets")
    @XmlElement(name="Market")
    public Collection<MarketModel> markets;


    @XmlElementWrapper(name="Instruments")
    @XmlElement(name="Instrument")
    public List<InstrumentModel> instruments;
}
