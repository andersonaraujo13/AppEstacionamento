package managedbean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import controller.service.SpotService;
import model.Spot;

@ManagedBean(name = "spotView")
@ViewScoped
public class HistoricoMB {
	private List<Spot> spot;

	public List<Spot> getSpot() {
		return spot;
	}

	@PostConstruct
	public void init() {

		SpotService service = new SpotService();
		this.spot = service.getSpots();

	}
}
